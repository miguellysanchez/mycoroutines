package com.sample.mycoroutines.p6_channels

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sample.mycoroutines.R
import kotlinx.android.synthetic.main.activity_channels.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.Random

class ChannelsActivity : AppCompatActivity() {

    val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channels)
        initializeViews()
    }

    private fun initializeViews() {
        channels_button_run_simple_channels.setOnClickListener {
            simpleChannels()
        }
    }

    private fun simpleChannels() {
        val channel = Channel<Int>()
        channels_button_run_simple_channels.visibility = View.GONE
        launch(CommonPool) {
            while (!channel.isClosedForSend) {
                val x = random.nextInt(1000)
                println("Sending value [$x]>>>")
                channel.send(x)
                delay(1100)
            }
            println("Channel Closed for sending ")
        }
        launch(UI) {
            var numExpected = 10
            while (numExpected > 0) {
                numExpected--
                println("-->Receiving")
                channels_textview_indicator.text = ">>>Received value: ${channel.receive()}"
                println("-->Received")

                delay(500)
            }
            println("Done receiving!")
            channel.close()
            channels_button_run_simple_channels.visibility = View.VISIBLE
        }

    }
}