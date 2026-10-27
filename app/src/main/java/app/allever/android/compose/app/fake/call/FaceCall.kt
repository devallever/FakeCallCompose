package app.allever.android.compose.app.fake.call

import app.allever.android.compose.app.fake.call.data.ContactItem

object FaceCall {

    val internalContacts: List<ContactItem> = mutableListOf<ContactItem>().apply {
        add(
            ContactItem(
                "张三", "12345678901", R.drawable.fc_avatar_01, selected = false, isInternal = true
            )
        )
        add(
            ContactItem(
                "李四",
                "12345678902",
                R.drawable.fc_avatar_02,
                selected = false,
                isInternal = true
            )
        )
        add(
            ContactItem(
                "王五",
                "12345678903",
                R.drawable.fc_avatar_03,
                selected = false,
                isInternal = true
            )
        )
        add(
            ContactItem(
                "赵六",
                "12345678904",
                R.drawable.fc_avatar_04,
                selected = false,
                isInternal = true
            )
        )
        add(
            ContactItem(
                "孙七",
                "12345678905",
                R.drawable.fc_avatar_05,
                selected = false,
                isInternal = true
            )
        )
    }
}