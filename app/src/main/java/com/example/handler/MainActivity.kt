package com.example.handler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.TextView
import com.example.handler.MyConstants.HANDLER_MESSAGE
import com.example.handler.MyConstants.MESSAGE_KEY
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var myHandler: Handler
    lateinit var myCallBack: Handler.Callback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.text)
        textView.text = getWeek("2022-01-01")
        myCallBack = MyCallBack(textView)
        myHandler = Handler(myCallBack)
        MyThread().putMsg(myHandler)
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("--------onRestart", "onRestart")
    }

    override fun onStart() {
        super.onStart()
        Log.e("---------onStart", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e("---------onResume", "onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.e("---------onPause", "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.e("---------onStop", "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        myHandler.removeMessages(HANDLER_MESSAGE)
        myHandler.removeCallbacksAndMessages(myCallBack)
        Log.e("---------onDestroy", "onDestroy()")
    }

    private fun getWeek(pTime: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd")
        val c = Calendar.getInstance()
        try {
            c.time = format.parse(pTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val week = when (c[Calendar.DAY_OF_WEEK]) {
            Calendar.SUNDAY -> "日"
            Calendar.MONDAY -> "一"
            Calendar.TUESDAY -> "二"
            Calendar.WEDNESDAY -> "三"
            Calendar.THURSDAY -> "四"
            Calendar.FRIDAY -> "五"
            Calendar.SATURDAY -> "六"
            else -> {""}
        }
        return week
    }

}

internal class MyCallBack(var textView: TextView) : Handler.Callback {
    override fun handleMessage(p0: Message): Boolean { // 接受message的信息
        if (p0.what == HANDLER_MESSAGE && p0.data.size() > 0) {
            textView.text = (p0.data.getSerializable(MESSAGE_KEY) as MyBean).book.component1()
        }
        return true
    }
}

internal class MyThread {
    fun putMsg(handler: Handler) {
        Thread {
            try {
                Thread.sleep(5000)
                val message = Message()
                message.what = HANDLER_MESSAGE //标志是哪个线程传数据
                val bundle = Bundle()
                bundle.putSerializable(MESSAGE_KEY, MyBean(Book("茹茹")))
                message.data = bundle //bundle传值，耗时，效率低
                handler.sendMessage(message) //发送message信息
                //message有四个传值方法，
                //两个传int整型数据的方法message.arg1，message.arg2
                //一个传对象数据的方法message.obj
                //一个bundle传值方法
            } catch (e: InterruptedException) {
                println(Arrays.toString(e.stackTrace))
            }
        }.start()
    }
}