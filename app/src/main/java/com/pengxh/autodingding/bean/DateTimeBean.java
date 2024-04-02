package com.pengxh.autodingding.bean;

import android.util.Log;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import com.pengxh.autodingding.extensions.StringKt;

import kotlin.random.Random;

@Entity
public class DateTimeBean {
    private static final int RANGE_RADOM = 60*5;//秒
    @Id(autoincrement = true)
    private Long id;//主键ID

    private String uuid;
    private String date;
    private String time;
    private String weekDay;
    private String autoRealTime;

    @Generated(hash = 551494236)
    public DateTimeBean(Long id, String uuid, String date, String time, String weekDay, String autoRealTime) {
        this.id = id;
        this.uuid = uuid;
        this.date = date;
        this.time = time;
        this.weekDay = weekDay;
        this.autoRealTime = autoRealTime;
    }

    @Generated(hash = 1790840121)
    public DateTimeBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeekDay() {
        return this.weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getAutoRealTime() {
        return autoRealTime;
    }

    public void setAutoRealTime(String autoRealTime) {
        this.autoRealTime = autoRealTime;
    }

    public void updateAutoRealTime(){
        int randValue = Random.Default.nextInt(-RANGE_RADOM, RANGE_RADOM);
        this.autoRealTime = StringKt.addSecond(this.time,randValue);
        Log.v("fq ","time:"+time);
        Log.v("fq ","randValue:"+randValue);
        Log.v("fq ","autoRealTime:"+autoRealTime);
    }
}
