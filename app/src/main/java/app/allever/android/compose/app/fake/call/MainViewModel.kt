package app.allever.android.compose.app.fake.call

import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.allever.android.compose.app.fake.call.util.log
import app.allever.android.compose.app.fake.call.util.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    var pageState = mutableIntStateOf(0)
    var phoneNumberState = mutableStateOf("")
    var nameState = mutableStateOf("")
    var contactState = mutableStateOf(FaceCall.internalContacts)
    var showScheduleState = mutableStateOf(false)
    var timeState = mutableIntStateOf(1)
    val scheduleTitleState = mutableStateOf(FaceCall.scheduleList.first().title)



    init {
        viewModelScope.launch {
            delay(1000)
            nameState.value = "Cristy"
            phoneNumberState.value = "12345678901"
        }
    }


}