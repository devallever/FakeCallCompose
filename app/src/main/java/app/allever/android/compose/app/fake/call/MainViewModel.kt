package app.allever.android.compose.app.fake.call

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var pageState = mutableIntStateOf(0)
    var phoneNumberState = mutableStateOf("")
    var nameState = mutableStateOf("")
    var contactState = mutableStateOf(FaceCall.internalContacts)

    fun a() {
    }

}