package app.allever.android.compose.app.fake.call

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import app.allever.android.compose.app.fake.call.data.ContactItem
import app.allever.android.compose.app.fake.call.ui.theme.FakeCallComposeTheme
import app.allever.android.compose.app.fake.call.ui.theme.GreenEnd
import app.allever.android.compose.app.fake.call.ui.theme.PinkEnd
import app.allever.android.compose.app.fake.call.ui.theme.PurpleEnd
import app.allever.android.compose.app.fake.call.util.DisplayHelper
import app.allever.android.compose.app.fake.call.util.log
import app.allever.android.compose.app.fake.call.util.toast
import coil.compose.AsyncImage
import okhttp3.internal.notify
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    private val pickPhoneLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val uri = result.data?.data ?: return@registerForActivityResult
            // uri 形如 content://com.android.contacts/data/phones/123，已含临时授权
            contentResolver.query(uri, null, null, null, null)?.use { c ->
                if (c.moveToFirst()) {
                    val name = c.getString(
                        c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    )
                    val number = c.getString(
                        c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    )
                    log("Contact: name=$name, number=$number")
                    mViewModel.nameState.value = name
                    mViewModel.phoneNumberState.value = number
                }
            }
        }
    }

    /**
     * 系统相册选择器（Photo Picker，Android 13+）。
     * Android 12 及以下 ActivityResultContracts 自动回退到 ACTION_GET_CONTENT。
     *
     * 为什么不能用 by lazy？
     * registerForActivityResult 要求在 LifecycleOwner STARTED 之前完成注册。
     * 如果用 lazy 并在 onClick()（RESUMED 状态）才第一次访问，会抛 IllegalStateException。
     *
     * 所以这里用普通 val — Activity 构造时立即执行注册，保证时序安全。
     */
    private val pickAvatarLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri ?: return@registerForActivityResult
        val savedPath = copyImageToPrivateDir(this, uri)
        if (savedPath != null) {
//            SettingStore.setAvatarPath(savedPath)
//            loadAvatar()
            val contactItem =
                ContactItem(name = "", number = "", resource = savedPath, isInternal = false)

            mViewModel.contactState.add(1, contactItem)
            mViewModel.avatarState.value = savedPath
            mViewModel.contactSelectIndex.value = 1
        } else {
            toast("Avatar save failed")
        }
    }

    /**
     * 将系统 Content URI（来自 Photo Picker / ACTION_PICK / OpenDocument）的图片
     * 复制到应用私有存储目录 filesDir/avatars/，返回内部文件的绝对路径字符串。
     *
     * 为什么需要复制而不是直接保存 URI？
     * ─────────────────────────────────────────
     * 1. Android 10+ 分区存储（Scoped Storage）：外部 URI 多为临时授权，进程重启后失效
     * 2. Photo Picker 返回的 URI 是 Activity 级别的临时 grant，onDestroy 后自动撤销
     * 3. 直接保存 file:// 或 content:// 在不同厂商 ROM 上兼容性差
     *
     * 存储位置：{filesDir}/avatars/avatar_{timestamp}.jpg
     * 生命周期：随 App 安装存在，卸载时自动清除
     * 读取方式：Coil.load(filePath) 直接支持
     *
     * @param context 任意 Context（用于 ContentResolver 和 filesDir）
     * @param sourceUri 从 ActivityResultContracts 返回的图片 URI
     * @return 私有文件绝对路径字符串；复制失败返回 null
     */
    fun copyImageToPrivateDir(context: Context, sourceUri: Uri): String? {
        return try {
            val avatarsDir = File(context.filesDir, "avatars").apply {
                if (!exists()) mkdirs()
            }
            val destFile = File(avatarsDir, "avatar_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            } ?: return null
            destFile.absolutePath
        } catch (e: Exception) {
            Log.e("SystemUtils", "copyImageToPrivateDir failed: ${e.message}", e)
            null
        }
    }


    private val mViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FakeCallComposeTheme(true) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //整个页面
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,  // 三段分布：上、中、下
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        //顶部
                        TopContent()
                        //中心内容区
                        CenterContent(modifier = Modifier.weight(1f))
                        //底部导航栏
                        BottomNavigation()
                    }
                }
            }
        }
    }

    @Composable
    fun TopContent() {
        var showSchedule by mViewModel.showScheduleState
        var buttonBottomY by remember { mutableIntStateOf(0) }
        var pageIndex by mViewModel.pageState
        var pageTitle by mViewModel.pageTitleState
        var pageDesc by mViewModel.pageDescState
        var pageIcon by mViewModel.pageIconState
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                //logo
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(getPageThemeColor()),
                ) {
                    Image(
                        painter = painterResource(pageIcon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
                //text
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Text(
                        text = pageTitle,
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                    //desc
                    Text(
                        text = pageDesc,
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        color = Color.Gray,
                    )
                }
            }

            if (pageIndex == 0) {
                Row(modifier = Modifier
                    .onGloballyPositioned {
                        buttonBottomY = it.boundsInWindow().bottom.roundToInt()
                        log("buttonBottomY = $buttonBottomY")
                    }
                    .align(Alignment.CenterEnd)
                    .clip(RoundedCornerShape(45.dp))
                    .border(1.dp, Color(0xff8298E7), RoundedCornerShape(45.dp))
                    .clickable {
                        showSchedule = !showSchedule
                    }
                    .background(Color(0xff242A3B))
                    .padding(horizontal = 8.dp, vertical = 4.dp)) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(45.dp))
                            .background(Color(0xff565F81))
                            .align(Alignment.CenterVertically)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.fc_ic_time),
                            contentDescription = null,
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.Center),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }

                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = "Schedule", fontSize = 12.sp, lineHeight = 12.sp, color = Color.White
                        )
                        //now
                        Text(
                            text = mViewModel.scheduleTitleState.value,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            color = PurpleEnd,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Image(
                        painter = painterResource(R.drawable.fc_ic_down_arrow),
                        contentDescription = null,
                        contentScale = ContentScale.Inside,
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.CenterVertically),
                        colorFilter = ColorFilter.tint(Color(0xff565F81))
                    )

                }
            }

        }
    }

    @Composable
    fun CenterContent(modifier: Modifier) {
        val rememberIndex = mViewModel.pageState
        Box(
            modifier = modifier.fillMaxWidth()
        ) {
            when (rememberIndex.intValue) {
                0 -> {
                    MainPage()
                }

                1 -> {
                    RecentPage()
                }

                2 -> {
                    SettingsPage()
                }
            }
        }
    }


    @Preview
    @Composable
    fun BottomNavigation() {
        val rememberIndex = mViewModel.pageState
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xff242731)), contentAlignment = Alignment.Center //内容居中
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .clickable {
                            rememberIndex.intValue = 0
                            mViewModel.pageState.intValue = 0
                            mViewModel.pageTitleState.value = "Keypad"
                            mViewModel.pageDescState.value = "realistic prank call"
                            mViewModel.pageIconState.value = R.drawable.fc_ic_nav_home_filled
                        }, horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NavigationItem(
                        R.drawable.fc_ic_nav_home_filled, "Keypad", rememberIndex.intValue == 0
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .clickable {
                            rememberIndex.intValue = 1
                            mViewModel.pageState.intValue = 1
                            mViewModel.pageTitleState.value = "Recent"
                            mViewModel.pageDescState.value = "recent calls"
                            mViewModel.pageIconState.value = R.drawable.fc_ic_phone
                        }, horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NavigationItem(
                        R.drawable.fc_ic_phone, "Recent", rememberIndex.intValue == 1
                    )
                }
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .weight(1f)
                        .clickable {
                            rememberIndex.intValue = 2
                            mViewModel.pageState.intValue = 2
                            mViewModel.pageTitleState.value = "Settings"
                            mViewModel.pageDescState.value = "settings"
                            mViewModel.pageIconState.value = R.drawable.fc_ic_settings
                        }, horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NavigationItem(
                        R.drawable.fc_ic_settings, "Settings", rememberIndex.intValue == 2
                    )
                }
            }
        }
    }

    @Composable
    fun NavigationItem(icon: Int, text: String, selected: Boolean) {
        val iconColor = if (selected) Color.White else Color.Gray
        val textColor = if (selected) getPageThemeColor() else Color.Gray
        val bgColor = if (selected) getPageThemeColor() else Color(0xff2C2C2E)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(bgColor),
            contentAlignment = Alignment.Center //内容居中
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(iconColor)
            )
        }
        Text(
            text = text,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(top = 0.dp)
        )
        if (selected) {
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(2.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(getPageThemeColor())
            )
        }

    }

    @Preview
    @Composable
    fun MainPage() {
        val contactList = mViewModel.contactState
        var avatarSelectedIndex by mViewModel.contactSelectIndex
        val name = mViewModel.nameState
        val phoneNumber = mViewModel.phoneNumberState
        var nameFocus by rememberSaveable { mutableStateOf(false) }
        var phoneNumberFocus by rememberSaveable { mutableStateOf(false) }
        var showSchedule by mViewModel.showScheduleState
        var scheduleSelectIndex by rememberSaveable { mutableIntStateOf(-1) }
        val avatarStatus = mViewModel.avatarState
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                //Caller Info
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xff1A1A1C))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.fc_ic_caller),
                            colorFilter = ColorFilter.tint(PurpleEnd),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Caller Info",
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xff202020))
                            .padding(10.dp)
                    ) {
                        AsyncImage(
                            model = avatarStatus.value,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(45.dp))
                                .border(1.dp, Color.Gray, RoundedCornerShape(45.dp))
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .align(Alignment.CenterVertically)
                                .weight(1f)
                        ) {
                            //name
                            Text(
                                text = name.value,
                                fontSize = 16.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            //number
                            Text(
                                text = phoneNumber.value,
                                fontSize = 14.sp,
                                lineHeight = 14.sp,
                                color = Color.White
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xff2A2C36))
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    pickPhoneLauncher.launch(
                                        Intent(
                                            Intent.ACTION_PICK,
                                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                                        )
                                    )

                                }, contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.fc_ic_caller),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                colorFilter = ColorFilter.tint(PurpleEnd)
                            )
                        }
                    }

                    //choose photo
                    Text(
                        text = "Choose Photo",
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    //联系人列表
                    LazyRow(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                    ) {
                        item {
                            Box(
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(84.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        pickAvatarLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }
                                    .border(0.5.dp, Color.Gray, RoundedCornerShape(12.dp))

                            ) {
                                Image(
                                    painter = painterResource(R.drawable.fc_ic_add),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .align(Alignment.Center),
                                    colorFilter = ColorFilter.tint(GreenEnd)
                                )
                            }
                        }

                        itemsIndexed(contactList) { index, contact ->
                            val isSelected = avatarSelectedIndex == index
                            val borderWidth = if (isSelected) 3.dp else 0.5.dp
                            val borderColor = if (isSelected) PurpleEnd else Color.Gray

                            if (index == 0) {
                                Box(
                                    modifier = Modifier
                                        .padding(end = 10.dp)
                                        .size(84.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            avatarSelectedIndex = index
                                            avatarStatus.value = R.drawable.fc_ic_contact
                                        }
                                        .border(borderWidth, borderColor, RoundedCornerShape(12.dp))

                                ) {
                                    AsyncImage(
                                        model = R.drawable.fc_ic_no_avatar,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .align(Alignment.Center),
                                        colorFilter = ColorFilter.tint(Color.DarkGray)
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .padding(end = 10.dp)
                                        .size(84.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            avatarSelectedIndex = index
                                            if (contact.isInternal) {
                                                mViewModel.nameState.value = contact.name
                                                mViewModel.phoneNumberState.value = contact.number
                                            }
                                            avatarStatus.value = contact.resource!!
                                        }
                                        .border(borderWidth, borderColor, RoundedCornerShape(12.dp))

                                ) {
                                    AsyncImage(
                                        model = contact.resource,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .align(Alignment.Center)
                                    )

                                    if (!contact.isInternal) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(RoundedCornerShape(90.dp))
                                                .background(Color(0x80000000))
                                                .clickable {
                                                    mViewModel.contactState.removeAt(index)
                                                    mViewModel.avatarState.value =
                                                        R.drawable.fc_ic_contact
                                                    avatarSelectedIndex = 0
                                                }
                                                .align(Alignment.TopEnd),
                                            contentAlignment = Alignment.Center) {
                                            Image(
                                                painter = painterResource(R.drawable.fc_ic_delete),
                                                contentDescription = null,
                                                colorFilter = ColorFilter.tint(Color.White),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                    }
                                }
                            }

                        }
                    }

                    val nameBorderColor = if (nameFocus) PurpleEnd else Color.Gray
                    val phoneNumberBorderColor = if (phoneNumberFocus) PurpleEnd else Color.Gray
                    val nameBorderWidth = if (nameFocus) 2.dp else 0.5.dp
                    val phoneNumberBorderWidth = if (phoneNumberFocus) 2.dp else 0.5.dp
                    Row(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .height(48.dp)
                            .fillMaxWidth()
                    ) {
                        BasicTextField(
                            name.value,
                            textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                            singleLine = true,
                            onValueChange = {
                                mViewModel.nameState.value = it
                            },
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .fillMaxSize()
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .border(nameBorderWidth, nameBorderColor, RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp)
                                .onFocusChanged {
                                    nameFocus = it.isFocused
                                }) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                                if (name.value.isEmpty()) {
                                    Text(
                                        text = "Name",
                                        color = Color.Gray,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(0.dp)
                                    )
                                }
                                it()
                            }
                        }

                        BasicTextField(
                            phoneNumber.value,
                            textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                            singleLine = true,
                            onValueChange = {
                                mViewModel.phoneNumberState.value = it
                            },
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .fillMaxSize()
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    phoneNumberBorderWidth,
                                    phoneNumberBorderColor,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 10.dp)
                                .onFocusChanged {
                                    phoneNumberFocus = it.isFocused
                                }) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                                if (phoneNumber.value.isEmpty()) {
                                    Text(
                                        text = "Phone Number",
                                        color = Color.Gray,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(0.dp)
                                    )
                                }
                                it()
                            }
                        }
                    }
                }

                //Call Schedule
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xff202020))
                        .padding(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xff292C36)), contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.fc_ic_time),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(PurpleEnd),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        //Title
                        Text(
                            text = "Call Schedule - ${mViewModel.scheduleTitleState.value}",
                            color = Color.White,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                        )
                        //desc
                        Text(
                            text = "Change timing from Schedule in the top bar",
                            color = Color.Gray,
                            fontSize = 11.sp,
                            lineHeight = 11.sp,
                            modifier = Modifier
                        )
                    }

                }
            }

            //按钮
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(GreenEnd)
                    .clickable {
                        toast("start")
                    }
                    .align(Alignment.BottomCenter)) {
                Row {
                    Image(
                        painter = painterResource(R.drawable.fc_ic_phone),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        text = "Start Fake Call",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }

            if (showSchedule) {
                val popWidth = 280
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x00000000))
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { /*showSchedule = false*/ })
                        }) {
                    Popup(
                        offset = IntOffset(
                            DisplayHelper.getScreenWidth() - DisplayHelper.dip2px(
                                popWidth
                            ), 0
                        ), onDismissRequest = { showSchedule = false }) {

                        Column(
                            modifier = Modifier
                                .width(popWidth.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xff24252F))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { }
                                .padding(vertical = 16.dp)) {
                            //Call schedule
                            Text(
                                text = "Call Schedule",
                                color = Color.White,
                                fontSize = 14.sp,
                                lineHeight = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            //when should the fake call start?
                            Text(
                                text = "When should the fake call start?",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            //line
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                thickness = 1.dp,
                                color = Color.Gray
                            )

                            //schedule list
                            LazyColumn(
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                itemsIndexed(FaceCall.scheduleList) { index, schedule ->
                                    val selected = scheduleSelectIndex == index
                                    val selectedBgColor =
                                        if (selected) Color(0xff2D3243) else Color(0xff242A3B)
                                    //box
                                    Box(
                                        modifier = Modifier
                                            .padding(
                                                horizontal = 0.dp, vertical = 2.dp
                                            )
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(selectedBgColor)
                                            .clickable {
                                                scheduleSelectIndex = index
                                                mViewModel.scheduleTitleState.value = schedule.title
                                                showSchedule = false
                                            }
                                            .padding(10.dp)) {

                                        Row {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(Color(0xff2F2E30))
                                            ) {
                                                Image(
                                                    painter = painterResource(R.drawable.fc_ic_time),
                                                    contentDescription = null,
                                                    colorFilter = ColorFilter.tint(Color.White),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }

                                            //column weight 1
                                            Column(
                                                modifier = Modifier
                                                    .padding(horizontal = 10.dp)
                                                    .weight(1f)
                                                    .align(Alignment.CenterVertically)
                                            ) {
                                                Text(
                                                    text = schedule.title,
                                                    color = Color.White,
                                                    fontSize = 14.sp,
                                                    lineHeight = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = schedule.desc,
                                                    color = Color.Gray,
                                                    fontSize = 12.sp,
                                                    lineHeight = 10.sp
                                                )
                                            }

                                            if (selected) {
                                                Image(
                                                    painter = painterResource(R.drawable.fc_ic_down_arrow),
                                                    contentDescription = null,
                                                    colorFilter = ColorFilter.tint(Color.White),
                                                    modifier = Modifier
                                                        .size(20.dp)
                                                        .align(Alignment.CenterVertically)
                                                )
                                            }
                                        }


                                    }


                                }
                            }
                        }
                    }
                }

            }

        }

    }

    @Preview
    @Composable
    fun RecentPage() {
        Text(text = "Recent Page")
    }

    @Preview
    @Composable
    fun SettingsPage() {
        Text(text = "Settings Page")
    }


    fun getPageThemeColor(): Color {
        return when (mViewModel.pageState.intValue) {
            0 -> GreenEnd
            1 -> PurpleEnd
            2 -> PinkEnd
            else -> GreenEnd
        }
    }
    override fun onBackPressed() {
        if (mViewModel.showScheduleState.value) {
            mViewModel.showScheduleState.value = false
        } else {
            super.onBackPressed()
        }

    }
}