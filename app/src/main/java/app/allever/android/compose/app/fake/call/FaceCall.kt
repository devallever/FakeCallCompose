package app.allever.android.compose.app.fake.call

import app.allever.android.compose.app.fake.call.data.ContactItem

object FaceCall {

    val internalContacts: List<ContactItem> = mutableListOf<ContactItem>().apply {
        add(
            ContactItem(
                "", "", R.drawable.fc_ic_no_avatar, isInternal = true
            )
        )
        add(
            ContactItem(
                "张三", "12345678901", R.drawable.fc_avatar_01, isInternal = true
            )
        )
        add(
            ContactItem(
                "李四",
                "12345678902",
                R.drawable.fc_avatar_02,
                isInternal = true
            )
        )
        add(
            ContactItem(
                "王五",
                "12345678903",
                R.drawable.fc_avatar_03,
                isInternal = true
            )
        )
        add(
            ContactItem(
                "赵六",
                "12345678904",
                R.drawable.fc_avatar_04,
                isInternal = true
            )
        )
        add(
            ContactItem(
                "孙七",
                "12345678905",
                R.drawable.fc_avatar_05,
                isInternal = true
            )
        )
        add(
            ContactItem(
                "周八",
                "12345678906",
                R.drawable.fc_avatar_06,
                isInternal = true
            )
        )
    }
}