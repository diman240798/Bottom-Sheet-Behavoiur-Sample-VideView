package attention.manager.nanickydev.myapplication

import android.content.res.Resources
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.MediaController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val path = Environment.getExternalStorageDirectory().canonicalPath + "/Download/vid.mp4";
        video_view.setVideoPath(path)

        var mediaController = MediaController(this)
        mediaController.visibility = View.GONE

        video_view.setMediaController(mediaController)

        // настройка поведения нижнего экрана
        val bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)

        fab.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            video_view.start()
        }

        stop_button.setOnClickListener {
            if (video_view.isPlaying) {
                video_view.pause()
                stop_button.setImageResource(android.R.drawable.ic_media_play)
            } else {
                video_view.start()
                stop_button.setImageResource(android.R.drawable.ic_media_pause)
            }
        }



        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED


        val initialWidth = getResources().getDimension(R.dimen.video_menu_height).toInt();
        var widthDifMax = Resources.getSystem().getDisplayMetrics().widthPixels - initialWidth
        var heightDifMax = Resources.getSystem().getDisplayMetrics().widthPixels - initialWidth

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> mediaController.visibility = View.VISIBLE
                    else -> mediaController.visibility = View.GONE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("SlideOffset", slideOffset.toString())

                // animate fab
                fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start()


                val btwAnim = 0.1F;
                if (slideOffset <= btwAnim ){


                    val percent: Float = slideOffset / btwAnim;

                    var animWidth =  (initialWidth + widthDifMax * percent).toDouble()

                    val layoutParams = video_outer.getLayoutParams()
                    layoutParams.width = Math.ceil(animWidth).toInt()
                    video_outer.setLayoutParams(layoutParams)

                } else {
                    // ensure width set to max
                    val layoutParamsWidth = video_outer.getLayoutParams()
                    layoutParamsWidth.width = widthDifMax + initialWidth
                    video_outer.setLayoutParams(layoutParamsWidth)



                    val percent: Float = (slideOffset - btwAnim) /(1 - btwAnim);

                    var animWidth =  (initialWidth + heightDifMax * percent).toDouble()

                    // animate View
                    val layoutParamsView = video_outer.getLayoutParams()
                    layoutParamsView.height = Math.ceil(animWidth).toInt()
                    video_outer.setLayoutParams(layoutParamsView)

                    // animate Parent
                    val layoutParamsSurrender = video_surrounder.getLayoutParams()
                    layoutParamsSurrender.height = Math.ceil(animWidth).toInt()
                    video_surrounder.setLayoutParams(layoutParamsSurrender)


                }


            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (video_view.isPlaying) {
            stop_button.setImageResource(android.R.drawable.ic_media_pause)
        } else {
            stop_button.setImageResource(android.R.drawable.ic_media_play)
        }
    }
}






/*WORKS
*
*  bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED)
                    state = true
                else if (newState == BottomSheetBehavior.STATE_COLLAPSED)
                    state = false
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("SlideOffset", slideOffset.toString())
                fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start()


                var animWidth = ValueAnimator.ofInt(video_outer.width, (160 + (Resources.getSystem().getDisplayMetrics().widthPixels - 160) * slideOffset).toInt())
                animWidth.addUpdateListener { valueAnimator ->
                    val animVal = valueAnimator.animatedValue as Int

                    val layoutParams = video_outer.getLayoutParams()
                    layoutParams.width = animVal
                    layoutParams.height = (160 + (Resources.getSystem().getDisplayMetrics().heightPixels / 2  - 160) * slideOffset).toInt()
                    video_outer.setLayoutParams(layoutParams)
                }
                animWidth.duration = 0
                animWidth.start()

            }
        })
* */


/*
                /*var anim = ValueAnimator.ofInt(video_outer.getMeasuredHeight(), (160 + (Resources.getSystem().getDisplayMetrics().heightPixels / 2  - 160) * slideOffset).toInt())
                anim.addUpdateListener { valueAnimator ->
                    val `val` = valueAnimator.animatedValue as Int
                    val layoutParams = video_outer.getLayoutParams()
                    layoutParams.height = `val`
                    video_outer.setLayoutParams(layoutParams)
                }
                anim.duration = 0
                anim.start()*/*/

/*val left = video_view.getLeft()
                    val top = video_view.getTop()
                    val right = animVal
                    val bottom = animVal
                    video_view.layout(left, top, right, bottom)*/

/*if (!state) {
                    var anim = ValueAnimator.ofInt(upper_part.getMeasuredHeight(), 500)
                    anim.addUpdateListener { valueAnimator ->
                        val `val` = valueAnimator.animatedValue as Int
                        val layoutParams = upper_part.getLayoutParams()
                        layoutParams.height = `val`
                        upper_part.setLayoutParams(layoutParams)
                    }
                    anim.duration = 0
                    anim.start()
                } else if (state){
                    var anim = ValueAnimator.ofInt(upper_part.getMeasuredHeight(), 160)
                    anim.addUpdateListener { valueAnimator ->
                        val `val` = valueAnimator.animatedValue as Int
                        val layoutParams = upper_part.getLayoutParams()
                        layoutParams.height = `val`
                        upper_part.setLayoutParams(layoutParams)
                    }
                    anim.duration = 0
                    anim.start()
                }*/