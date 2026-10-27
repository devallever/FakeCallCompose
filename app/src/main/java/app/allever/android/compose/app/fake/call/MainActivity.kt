package app.allever.android.compose.app.fake.call

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.allever.android.compose.app.fake.call.ui.theme.FakeCallComposeTheme
import app.allever.android.compose.app.fake.call.ui.theme.GreenEnd
import app.allever.android.compose.app.fake.call.ui.theme.PurpleEnd
import app.allever.android.compose.app.fake.call.util.toast

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val rememberIndex = remember { mutableIntStateOf(0) }

            FakeCallComposeTheme(true) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //整个页面
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,  // 三段分布：上、中、下
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(Color(0xff111111))
                    ) {
                        //顶部
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
                                        .background(GreenEnd),
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.fc_ic_phone),
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
                                        text = "Fake Call",
                                        fontSize = 16.sp,
                                        lineHeight = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                    )
                                    //desc
                                    Text(
                                        text = "realistic prank call",
                                        fontSize = 14.sp,
                                        lineHeight = 14.sp,
                                        color = Color.Gray,
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .clip(RoundedCornerShape(45.dp))
                                    .border(1.dp, Color(0xff8298E7), RoundedCornerShape(45.dp))
                                    .clickable {
                                        toast("选择时间")
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
                                        text = "Schedule",
                                        fontSize = 12.sp,
                                        lineHeight = 12.sp,
                                        color = Color.White
                                    )
                                    //now
                                    Text(
                                        text = "Now",
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
                        //中心内容区
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
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
                        //底部导航栏
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xff242731)),
                            contentAlignment = Alignment.Center //内容居中
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            rememberIndex.intValue = 0
                                        }, horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    NavigationItem(
                                        R.drawable.fc_ic_nav_home_filled,
                                        "Keypad",
                                        rememberIndex.intValue == 0
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            rememberIndex.intValue = 1
                                        }, horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    NavigationItem(
                                        R.drawable.fc_ic_phone,
                                        "Recent",
                                        rememberIndex.intValue == 1
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            rememberIndex.intValue = 2
                                        }, horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    NavigationItem(
                                        R.drawable.fc_ic_settings,
                                        "Settings",
                                        rememberIndex.intValue == 2
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun NavigationItem(icon: Int, text: String, selected: Boolean) {
        val iconColor = if (selected) Color.White else Color.Gray
        val textColor = if (selected) GreenEnd else Color.Gray
        val bgColor = if (selected) GreenEnd else Color(0xff2C2C2E)
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
                    .background(GreenEnd)
            )
        }

    }

    @Preview
    @Composable
    fun MainPage() {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(300.dp)
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
                        Image(
                            painter = painterResource(R.drawable.fc_avatar_01),
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
                                text = "John Doe",
                                fontSize = 16.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            //number
                            Text(
                                text = "+1 234 567 890",
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
                                    toast("选择通讯录联系人")
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
                                        toast("选择图片")
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
                        item {
                            Box(
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(84.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        toast("清除头像")
                                    }
                                    .border(0.5.dp, Color.Gray, RoundedCornerShape(12.dp))

                            ) {
                                Image(
                                    painter = painterResource(R.drawable.fc_ic_no_avatar),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .align(Alignment.Center),
                                    colorFilter = ColorFilter.tint(Color.DarkGray)
                                )
                            }
                        }

                        FaceCall.internalContacts.forEach { contact ->
                            item {
                                Box(
                                    modifier = Modifier
                                        .padding(end = 10.dp)
                                        .size(84.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            toast(contact.name)
                                        }
                                        .border(0.5.dp, Color.Gray, RoundedCornerShape(12.dp))

                                ) {
                                    if (contact.isInternal) {
                                        Image(
                                            painter = painterResource(contact.icon!!),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .align(Alignment.Center)
                                        )
                                    } else {
                                        Image(
                                            painter = painterResource(R.drawable.fc_ic_no_avatar),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .align(Alignment.Center),
                                        )
                                    }

                                }
                            }
                        }
                    }
                }
            }

            //按钮
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(GreenEnd)
                    .align(Alignment.BottomCenter)
            )

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
}