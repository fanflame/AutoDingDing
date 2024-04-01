package com.pengxh.autodingding.extensions

import com.pengxh.autodingding.bean.MailInfo
import com.pengxh.autodingding.utils.Constant
import com.pengxh.kt.lite.extensions.timestampToDate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

fun String.convertToWeek(): String {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    val calendar = Calendar.getInstance()
    try {
        calendar.time = format.parse(this)!!
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return when (calendar.get(Calendar.DAY_OF_WEEK)) {
        1 -> return "周日"
        2 -> return "周一"
        3 -> return "周二"
        4 -> return "周三"
        5 -> return "周四"
        6 -> return "周五"
        7 -> return "周六"
        else -> "错误"
    }
}

fun String.isEarlierThenCurrent(): Boolean {
    try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
        val date = dateFormat.parse(this)!!
        val t1 = date.time
        val t2 = System.currentTimeMillis()
        return (t1 - t2) < 0
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return false
}

/**
 * 时间差-秒
 * */
fun String.diffCurrentMillis(): Long {
    if (this.isBlank()) {
        return 0
    }
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
    val date = simpleDateFormat.parse(this)!!
    return abs(System.currentTimeMillis() - date.time)
}

fun String.createMail(toAddress: String): MailInfo {
    val mailInfo = MailInfo()
    mailInfo.mailServerHost = "smtp.qq.com" //发送方邮箱服务器
    mailInfo.mailServerPort = "587" //发送方邮箱端口号
    mailInfo.isValidate = true
    mailInfo.userName = Constant.USER_MAIL_ACCOUNT
    mailInfo.password = Constant.PERMISSION_CODE
    mailInfo.toAddress = toAddress // 接收者邮箱
    mailInfo.fromAddress = Constant.MAIL_FROM_ADDRESS
    mailInfo.subject = "自动哈没哈没哈通知" // 邮件主题
    val content = if (this == "") {
        "未监听到哈没哈没哈成功的通知，请手动登录检查" + System.currentTimeMillis().timestampToDate()
    } else {
        this
    }
    // 邮件文本
    mailInfo.content = content
    return mailInfo
}