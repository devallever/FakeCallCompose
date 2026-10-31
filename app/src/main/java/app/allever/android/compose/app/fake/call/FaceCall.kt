package app.allever.android.compose.app.fake.call

import app.allever.android.compose.app.fake.call.data.ContactItem
import app.allever.android.compose.app.fake.call.data.RecentItem
import app.allever.android.compose.app.fake.call.data.ScheduleItem

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

    val scheduleList = mutableListOf<ScheduleItem>().apply {
        add(ScheduleItem(1, "Now", "Start immediately"))
        add(ScheduleItem(5, "5 sec", "Short delay"))
        //10
        add(ScheduleItem(10, "10 sec", "Quick delay"))
        //30
        add(ScheduleItem(30, "30 sec", "Long delay"))
        //60
        add(ScheduleItem(60, "1 min", "One minute delay"))
        //120
        add(ScheduleItem(120, "2 min", "Two minute delay"))
        //300
        add(ScheduleItem(300, "5 min", "Five minute delay"))
    }

    val recentList = mutableListOf<RecentItem>().apply {
        add(RecentItem("张三", "12345678901", System.currentTimeMillis(), R.drawable.fc_avatar_01))
        add(RecentItem("李四", "12345678902", System.currentTimeMillis(), R.drawable.fc_avatar_02))
        add(RecentItem("王五", "12345678903", System.currentTimeMillis(), R.drawable.fc_avatar_03))
        add(RecentItem("赵六", "12345678904", System.currentTimeMillis(), R.drawable.fc_avatar_04))
        add(RecentItem("孙七", "12345678905", System.currentTimeMillis(), R.drawable.fc_avatar_05))
        add(RecentItem("周八", "12345678906", System.currentTimeMillis(), R.drawable.fc_avatar_06))
    }
}