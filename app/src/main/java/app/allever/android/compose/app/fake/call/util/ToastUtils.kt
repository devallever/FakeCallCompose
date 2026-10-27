package app.allever.android.compose.app.fake.call.util

import android.widget.Toast
import app.allever.android.compose.app.fake.call.App

fun toast(msg: String) {
    Toast.makeText(App.context, msg, Toast.LENGTH_SHORT).show()
}