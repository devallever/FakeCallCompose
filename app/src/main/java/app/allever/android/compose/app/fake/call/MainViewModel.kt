package app.allever.android.compose.app.fake.call

import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.allever.android.compose.app.fake.call.data.ContactItem
import app.allever.android.compose.app.fake.call.data.RecentItem
import app.allever.android.compose.app.fake.call.util.log
import app.allever.android.compose.app.fake.call.util.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    val vibrationState = mutableStateOf(true)
    val flashState = mutableStateOf(true)
    var pageTitleState = mutableStateOf("Fake Call")
    var pageDescState = mutableStateOf("Realistic prank calls")
    var pageIconState = mutableStateOf(R.drawable.fc_ic_phone)

    var pageState = mutableIntStateOf(0)
    var phoneNumberState = mutableStateOf("12345678901")
    var nameState = mutableStateOf("Cristy")
    var contactState = mutableStateListOf<ContactItem>().apply {
        addAll(FaceCall.internalContacts)
    }
    var showScheduleState = mutableStateOf(false)
    var timeState = mutableIntStateOf(1)
    val scheduleTitleState = mutableStateOf(FaceCall.scheduleList.first().title)
    var avatarState = mutableStateOf(Any()).apply {
        value = R.drawable.fc_ic_contact
    }
    var contactSelectIndex = mutableIntStateOf(-1)


    val recentState = mutableStateListOf<RecentItem>().apply {
        addAll(FaceCall.recentList)
    }

    init {
        viewModelScope.launch {

        }
    }


}