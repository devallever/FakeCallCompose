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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.allever.android.compose.app.fake.call.ui.theme.FakeCallComposeTheme
import app.allever.android.compose.app.fake.call.ui.theme.GreenEnd

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
                                    .background(Color(0xff242A3B))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
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
                                        color = Color(0xff565F81),
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
                            Text(text = "Center Content", modifier = Modifier.fillMaxSize())
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
}