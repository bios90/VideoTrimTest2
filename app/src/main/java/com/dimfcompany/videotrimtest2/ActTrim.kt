package com.dimfcompany.videotrimtest2

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.innovattic.rangeseekbar.RangeSeekBar
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.act_trim.*
import java.io.File
import java.util.concurrent.TimeUnit

class ActTrim : AppCompatActivity()
{
    val TAG: String = "ActTrim"

    lateinit var file: File
    lateinit var timeLineCreator: TimeLineCreator
    var isPlaying = false
    var duration: Int? = null

    val subjSeeker: PublishSubject<Int> = PublishSubject.create()

    var dragging = false

    var mp: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_trim)
        timeLineCreator = TimeLineCreator(this)

        loadTimLine()
        setListeners()
        setVideo()
        setSubjListener()
        setPlayLineObservable()
    }

    private fun setPlayLineObservable()
    {
        Observable.interval(200, TimeUnit.MILLISECONDS)
                .subscribe(
                    {

                        if(mp != null)
                        {
                            seekbar_for_play.progress = mp!!.currentPosition
                            if (mp!!.currentPosition >= seekbar.getMaxThumbValue())
                            {
                                mp!!.seekTo(seekbar.getMinThumbValue())
                            }
                        }

                    })
    }

    private fun setSubjListener()
    {
        subjSeeker.debounce(10, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribe(
                    {
                        Log.e("Got result", "$it ")
                        video_view.seekTo(it)
//                        if (isPlaying)
//                        {
//                            video_view.start()
//                        }
                    })
    }

    private fun setListeners()
    {
        img_play_pause.setOnClickListener(
            {
                if (isPlaying)
                {
                    img_play_pause.setImageResource(R.drawable.ic_play)
                    video_view.pause()
                }
                else
                {
                    Log.e(TAG, "Will scroll!!!: ")
                    img_play_pause.setImageResource(R.drawable.ic_pause)
                    video_view.seekTo(seekbar.getMinThumbValue())
                    video_view.start()
                    seekbar_for_play.progress = video_view.currentPosition
                }

                Log.e(TAG, "Video current position is ${video_view.currentPosition} ")
                Log.e(TAG, "Play seeker  max : ${seekbar_for_play.max} and current is : ${seekbar_for_play.progress}")
                Log.e(TAG, "Trim seekbar min ${seekbar.minRange} max : ${seekbar.max} currentLeft: ${seekbar.getMinThumbValue()} ")



                isPlaying = !isPlaying
            })

//        seekbar_for_play.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener
//        {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
//            {
//                if (seekbar_for_play.isDragging)
//                {
//                    video_view.seekTo((progress) * 1000)
//                }
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?)
//            {
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar?)
//            {
//            }
//        })
    }

    fun loadTimLine()
    {
        var file_name = intent.getStringExtra("file_name")!!
        file = FileManager.getFileFromTemp(file_name, null)!!

        timeLineCreator.create_line(file)
    }

    private fun setVideo()
    {
        val uri = FileManager.uriFromFile(file)
        video_view.setVideoURI(uri)

        video_view.setOnPreparedListener(
            {
                duration = it.duration
                mp = it

                mp!!.setOnSeekCompleteListener(
                    {
                        seekbar_for_play.progress = mp!!.currentPosition
                        if (mp!!.currentPosition >= seekbar.getMaxThumbValue())
                        {
                            mp!!.seekTo(seekbar.getMinThumbValue())
                        }
                    })

                tv_time_left.setText("00:00:00")
                tv_time_right.setText(getTimeFromSeconds(duration!!))
                seekbar.minRange = 0
                seekbar.max = duration!!

                seekbar.setMinThumbValue(duration!! / 2 - 15000)
                seekbar.setMaxThumbValue(duration!! / 2 + 15000)

                seekbar_for_play.max = duration!!

                seekbar_for_play.progress = seekbar.getMinThumbValue()
                video_view.seekTo(seekbar.getMinThumbValue())

                seekbar.seekBarChangeListener = object : RangeSeekBar.SeekBarChangeListener
                {
                    override fun onStartedSeeking()
                    {
//                        video_view.pause()
                    }

                    override fun onStoppedSeeking()
                    {
                        subjSeeker.onNext(seekbar.getMinThumbValue())
                    }

                    override fun onValueChanged(minThumbValue: Int, maxThumbValue: Int)
                    {
//                        subjSeeker.onNext(seekbar.getMinThumbValue())
                        tv_time_left.setText(getTimeFromSeconds(minThumbValue))
                        tv_time_right.setText(getTimeFromSeconds(maxThumbValue))

                        val length = maxThumbValue - minThumbValue
                        showDifference(length)
                    }
                }
            })
    }

    fun getTimeFromSeconds(seconds: Int): String
    {
        val seconds = seconds / 1000
        val hr = seconds / 3600
        val rem = seconds % 3600
        val min = rem / 60
        val sec = rem % 60

        return String.format("%02d", hr) + ":" + String.format(
            "%02d",
            min
        ) + ":" + String.format("%02d", sec)
    }

    fun showDifference(time: Int)
    {
        val time_str = getTimeFromSeconds(time)
        tv_length.setText(time_str)

        if (time / 1000 > 30)
        {
            tv_length.setTextColor(getColorMy(android.R.color.holo_red_dark))
        }
        else
        {
            tv_length.setTextColor(getColorMy(android.R.color.holo_green_dark))
        }
    }

}