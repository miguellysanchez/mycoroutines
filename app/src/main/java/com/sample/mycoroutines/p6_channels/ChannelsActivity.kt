package com.sample.mycoroutines.p6_channels

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sample.mycoroutines.R
import kotlinx.android.synthetic.main.activity_channels.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineName
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.Random
import kotlin.coroutines.experimental.coroutineContext

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
        channels_button_run_producer_consumer.setOnClickListener {
            producerConsumer()
        }
        channels_button_run_ticker.setOnClickListener {
            tickerChannelDemo()
        }
        channels_button_run_actor.setOnClickListener {
            doActorDemo()
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
                delay(10)
            }
            println("Channel Closed for sending ")
        }
        launch(UI + CoroutineName("ABC")) {
            var numExpected = 10
            while (numExpected > 0) {
                numExpected--
//                println("-->ABC Receiving")
                val received = channel.receive()
                channels_textview_indicator.text = ">>>Received value @${coroutineContext[CoroutineName]}: $received}"
                println("-->ABC Received: [$received]")

                delay(750)
            }
            println("ABC Done receiving!")
            channels_button_run_simple_channels.visibility = View.VISIBLE
        }

        launch(UI + CoroutineName("XYZ")) {
            var numExpected = 10
            while (numExpected > 0) {
                numExpected--
//                println("-->XYZ Receiving")
                val received = channel.receive()
                channels_textview_indicator.text = ">>>Received value @${coroutineContext[CoroutineName]}: $received"
                println("-->XYZ Received [$received]")
                delay(1200)
            }
            println("XYZ Done receiving, closing channel!")
            channel.close()
            channels_button_run_simple_channels.visibility = View.VISIBLE
        }
    }

    private fun producerConsumer() {
        val intProducer = produce<Int> {
            for (x in 1..5) {
                send(random.nextInt(9999))
            }
        }
        launch(UI) {
            intProducer.consumeEach {
                channels_textview_indicator.text = "Gaining $it HP"
                delay(1000)
            }
            println("Finished consumption")
        }
    }

    private fun tickerChannelDemo() {
        //creates a ticker coroutine that ticks per period
        val tickerChannel = ticker(delay = 1000L)
        launch(UI) {
            val startTime = System.currentTimeMillis()
            var i = 0
            while (i < 10) {
                i++
                tickerChannel.receive()
                val currTime = System.currentTimeMillis() - startTime
                channels_textview_indicator.text = "TICK [$i] at $currTime"
            }
        }
    }


    private fun doActorDemo() {
        val chuckNorris = actor<Int>(UI) {
            print(">>>Chuck Norris is doing ")
//            println("Received 1 ${channel.receive()}")
//            println("Received 2 ${channel.receive()}")
//            println("Received 3 ${channel.receive()}")
//            println("Received 4 ${channel.receive()}")
            for (action in channel) {
                val actionPerformed = when (action % 4) {
                    0 -> "a Backflip Kick,"
                    1 -> "a Roundhouse Kick,"
                    2 -> "a Double Roundhouse Kick,"
                    else -> "a Whirlwind Roundhouse Kick,"
                }
                print("$actionPerformed")
            }
            println("...and standing still, all at the same time!")
        }
        launch {
            chuckNorris.send(random.nextInt())
            chuckNorris.send(random.nextInt())
            chuckNorris.send(random.nextInt())
            chuckNorris.send(random.nextInt())
            chuckNorris.close()
        }
    }


}