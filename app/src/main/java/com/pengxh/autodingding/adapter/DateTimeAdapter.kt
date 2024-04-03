package com.pengxh.autodingding.adapter

import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.pengxh.autodingding.R
import com.pengxh.autodingding.bean.DateTimeBean
import com.pengxh.autodingding.extensions.diffCurrentMillis
import com.pengxh.autodingding.extensions.isEarlierThenCurrent
import java.text.SimpleDateFormat

class DateTimeAdapter(context: Context, private val dataBeans: MutableList<DateTimeBean>) :
    RecyclerView.Adapter<DateTimeAdapter.ItemViewHolder>() {

    private val kTag = "DateTimeAdapter"
    private var layoutInflater = LayoutInflater.from(context)
    private var countDownTimerMap:HashMap<Int,CountDownTimer>? = null

    init {
        countDownTimerMap = HashMap();
    }

    override fun getItemCount(): Int = dataBeans.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            layoutInflater.inflate(R.layout.item_timer_rv_l, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val timeBean = dataBeans[position]
        holder.dateView.text = timeBean.date
        holder.timeView.text = timeBean.autoRealTime
        holder.weekDayView.text = timeBean.weekDay

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(position)
        }

        // 长按监听
        holder.itemView.setOnLongClickListener {
            itemClickListener?.onItemLongClick(position)
            true
        }

        val time = "${timeBean.date} ${timeBean.autoRealTime}"
        if (time.isEarlierThenCurrent()) {
            holder.countDownTextView.text = "任务已过期"
            holder.countDownTextView.setTextColor(Color.RED)
        } else {
            val diffCurrentMillis = time.diffCurrentMillis()

            holder.countDownTextView.setTextColor(Color.BLUE)

            holder.countDownProgress.max = diffCurrentMillis.toInt()
            if(countDownTimerMap!![position] == null){
                var countDownTimer = object : CountDownTimer(diffCurrentMillis, 1) {
                    override fun onTick(millisUntilFinished: Long) {
                        holder.countDownProgress.progress =
                            (diffCurrentMillis - millisUntilFinished).toInt()
                        val hour = millisUntilFinished / 1000/60/60
                        var hourStr = "" + hour
                        if(hour < 10){
                            hourStr = "0"+hour
                        }
                        var minitue = (millisUntilFinished - hour * 60 * 60 * 1000) / 1000/60;
                        var minuteStr = "" + minitue
                        if(minitue < 10){
                            minuteStr = "0"+minitue
                        }
                        var second = (millisUntilFinished/1000) - hour * 60 * 60 - minitue * 60
                        var secondStr = "" + second
                        if(second < 10){
                            secondStr = "0"+second
                        }
                        holder.countDownTextView.text = "${hourStr}:${minuteStr}:${secondStr}"
                    }

                    override fun onFinish() {
                        itemClickListener?.onCountDownFinish(position)
                    }
                }.start()
                countDownTimerMap!![position] = countDownTimer
            }
        }
    }

    fun stopAllCountDownTimer(){
        countDownTimerMap!!.forEach {
            it.value.cancel()
        }
        countDownTimerMap!!.clear()
    }

    fun stopCountDownTimer(position: Int) {
        countDownTimerMap!![position]?.cancel()
        countDownTimerMap!!.remove(position)
        Log.d(kTag, "position countDownTimer => 已取消")
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)

        fun onItemLongClick(position: Int)

        fun onCountDownFinish(position:Int)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timeView: TextView = itemView.findViewById(R.id.timeView)
        var dateView: TextView = itemView.findViewById(R.id.dateView)
        var weekDayView: TextView = itemView.findViewById(R.id.weekDayView)
        var countDownTextView: TextView = itemView.findViewById(R.id.countDownTextView)
        var countDownProgress: LinearProgressIndicator = itemView.findViewById(
            R.id.countDownProgress
        )
    }
}