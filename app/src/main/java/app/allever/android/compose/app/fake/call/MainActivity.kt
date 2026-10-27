package app.allever.android.compose.app.fake.call

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.allever.android.compose.app.fake.call.ui.theme.FakeCallComposeTheme
import app.allever.android.compose.app.fake.call.ui.theme.GreenEnd
import app.allever.android.compose.app.fake.call.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FakeCallComposeTheme(true) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //整个页面
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(Color(0xff111111))
                    ) {
                        //内容区,占满屏幕
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0x80ff0000))
                        )
                        //底部导航栏
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xff21232B))
                                .align(Alignment.BottomCenter),//控制在父容器中的位置,
                            contentAlignment = Alignment.Center //内容居中
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(RoundedCornerShape(18.dp))
                                            .background(GreenEnd),
                                        contentAlignment = Alignment.Center //内容居中
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.fc_ic_nav_home_filled),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp),
                                            colorFilter = ColorFilter.tint(Color.White)
                                        )
                                    }
                                    Text(
                                        text = "首页",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GreenEnd,
                                        modifier = Modifier.padding(top = 0.dp)
                                    )
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
                    }
                }
            }
        }
    }
}