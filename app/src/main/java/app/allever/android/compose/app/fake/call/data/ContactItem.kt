package app.allever.android.compose.app.fake.call.data

import android.net.Uri

data class ContactItem(
    var name: String,
    var number: String,
    var icon: Int? = null,
    var uri: Uri? = null,
    var selected: Boolean = false,
    var isInternal: Boolean = false
)