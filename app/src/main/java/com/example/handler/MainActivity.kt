package com.example.handler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
            Calendar.SUNDAY -> "???"
            Calendar.MONDAY -> "???"
            Calendar.TUESDAY -> "???"
            Calendar.WEDNESDAY -> "???"
            Calendar.THURSDAY -> "???"
            Calendar.FRIDAY -> "???"
            Calendar.SATURDAY -> "???"
            else -> {""}
        }
        return week
    }

}

internal class MyCallBack(var textView: TextView) : Handler.Callback {
    override fun handleMessage(p0: Message): Boolean { // ??????message?????????
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
                Looper.prepare()
                Thread.sleep(5000)
                val message = Message()
                message.what = HANDLER_MESSAGE //??????????????????????????????
                val bundle = Bundle()
                bundle.putSerializable(MESSAGE_KEY, MyBean(Book("??????")))
                message.data = bundle //bundle???????????????????????????
                handler.sendMessage(message) //??????message??????
                Looper.loop()
                //message????????????????????????
                //?????????int?????????????????????message.arg1???message.arg2
                //??????????????????????????????message.obj
                //??????bundle????????????
            } catch (e: InterruptedException) {
                println(Arrays.toString(e.stackTrace))
            }
        }.start()
    }
}