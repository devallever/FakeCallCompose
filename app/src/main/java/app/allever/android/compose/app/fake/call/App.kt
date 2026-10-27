package app.allever.android.compose.app.fake.call

import android.app.Application

class App: Application() {
    companion object {
        //context
        lateinit var context: Application
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}