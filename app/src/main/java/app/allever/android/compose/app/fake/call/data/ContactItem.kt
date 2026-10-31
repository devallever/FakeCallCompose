package app.allever.android.compose.app.fake.call.data

data class ContactItem(
    var name: String,
    var number: String,
    var resource: Any? = null,
    var isInternal: Boolean = false
)