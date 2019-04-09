package attention.manager.nanickydev.myapplication

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.MediaController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlin.collections.ArrayList
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.widget.LinearLayout
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var videoController: MediaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView.startNestedScroll(View.OVER_SCROLL_ALWAYS)

        setVideos()
        update.setOnClickListener { getNewVideos(Collections.emptyList(), Collections.emptyList()) }

        videoController = MediaController(this)
        videoController.visibility = View.GONE

        video_view.setMediaController(videoController)

        setUpFab()
        setUpBottomSheetView()
        setUpBottomSheetBottomView()
        setUpBottomSheetBehavior()
    }

    private fun setUpBottomSheetBottomView() {

    }

    private fun setUpBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                onBottomSheetStateChanged(newState)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                onBottomSheetSlide(slideOffset)
            }
        })
    }

    @SuppressLint("RestrictedApi")
    private fun onBottomSheetSlide(slideOffset: Float) {
        Log.d("SlideOffset", slideOffset.toString())

        val initialWidth = resources.getDimension(R.dimen.video_menu_height).toInt()
        val widthDifMax = Resources.getSystem().displayMetrics.widthPixels - initialWidth
        var heightDifMax = Resources.getSystem().displayMetrics.widthPixels - initialWidth


        val btwAnim = 0.2F
        if (slideOffset <= btwAnim) {
            val percent: Float = slideOffset / btwAnim

            // animate fab
            fab.animate().scaleX(1 - percent).scaleY(1 - percent).setDuration(0).start()


            val animWidth = (initialWidth + widthDifMax * percent).toDouble()

            val layoutParams = video_outer.layoutParams
            layoutParams.width = Math.ceil(animWidth).toInt()
            video_outer.layoutParams = layoutParams

        } else {
            // ensure fab is fully hidden
            fab.animate().scaleX(0F).scaleY(0F).setDuration(0).start()

            if (fab.visibility == View.GONE) {
                fab.visibility = View.VISIBLE
            }

            // ensure width set to max
            val layoutParamsWidth = video_outer.layoutParams
            layoutParamsWidth.width = widthDifMax + initialWidth
            video_outer.layoutParams = layoutParamsWidth
        }

        //val percent: Float = (slideOffset - btwAnim) / (1 - btwAnim);
        //var animWidth = (initialWidth + heightDifMax * percent).toDouble() // after

        val animWidth =
            (initialWidth + (Resources.getSystem().displayMetrics.heightPixels.toDouble() / 2 - 160) * slideOffset)

        // animate View
        val layoutParamsView = video_outer.layoutParams
        layoutParamsView.height = Math.ceil(animWidth).toInt()
        video_outer.layoutParams = layoutParamsView

        // animate Parent
        val layoutParamsSurrender = video_surrounder.layoutParams
        layoutParamsSurrender.height = Math.ceil(animWidth).toInt()
        video_surrounder.layoutParams = layoutParamsSurrender
    }

    private fun onBottomSheetStateChanged(newState: Int) {
        when (newState) {
            BottomSheetBehavior.STATE_COLLAPSED -> listView.isNestedScrollingEnabled = true

            BottomSheetBehavior.STATE_EXPANDED -> {
                videoController.visibility = View.VISIBLE
                videoController.hide()
                listView.isNestedScrollingEnabled = false
            }

            else -> videoController.visibility = View.GONE
        }
    }

    private fun setUpBottomSheetView() {
        stop_button.setOnClickListener {
            if (video_view.isPlaying) {
                video_view.pause()
                stop_button.setImageResource(android.R.drawable.ic_media_play)
            } else {
                video_view.start()
                stop_button.setImageResource(android.R.drawable.ic_media_pause)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setUpFab() {
        fab.visibility = View.GONE
        fab.setOnClickListener {
            startVideo()
        }
    }

    private fun setVideos() {

        val allMedia = SharedPreferenceUtil.getVideos(this)
        val allMediaThumbs = SharedPreferenceUtil.getVideosThumbs(this)

        getNewVideos(allMedia, allMediaThumbs)
    }

    private fun getNewVideos(allMedia: MutableList<String>, allMediaThumbs: MutableList<Bitmap>) {
        progressBar.visibility = View.VISIBLE
        var allMedia1 = ArrayList<String>(allMedia)
        val allMediaThumbs1 = ArrayList<Bitmap>(allMediaThumbs)
        Thread {
            val listVideos = ArrayList<VideoDetails>()
            if (allMedia1.isEmpty()) allMedia1 = getAllMedia(this)
            if (allMediaThumbs1.isEmpty()) {
                for (vid in allMedia1) {
                    Log.d("video", vid)
                    val thumb: Bitmap =
                        ThumbnailUtils.createVideoThumbnail(vid, MediaStore.Images.Thumbnails.MINI_KIND)
                    allMediaThumbs1.add(thumb)
                }
                SharedPreferenceUtil.setVideosThumbs(this, allMediaThumbs1)
                SharedPreferenceUtil.setVideos(this, allMedia1)
                SharedPreferenceUtil.setVideosSize(this, allMedia1.size)
            }
            for (i in 0..(allMedia1.size - 1)) {
                val thumb: Bitmap = allMediaThumbs1[i]
                val vid = allMedia1[i]
                Log.d("video SETTING", vid)
                listVideos.add(VideoDetails(thumb, vid))
            }

            val frontListBaseAdapter = VideoListBaseAdapter(this, listVideos)


            runOnUiThread {
                setVideos(frontListBaseAdapter, listVideos)
                progressBar.visibility = View.GONE
            }

        }.start()
    }

    @SuppressLint("RestrictedApi")
    private fun setVideos(frontListBaseAdapter: VideoListBaseAdapter, listVideos: ArrayList<VideoDetails>) {
        listView.adapter = frontListBaseAdapter
        // on Item Click
        listView.setOnItemClickListener { parent, view, position, id ->
            val allowed = (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED
                    || (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN && bottomSheetBehavior.isHideable))
            if (!allowed) return@setOnItemClickListener

            if (bottomSheetBehavior.isHideable) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                bottomSheetBehavior.isHideable = false
            }


            video_view.setVideoPath(listVideos[position].videoPath)
            startVideo()
        }
        // on Item Click
    }

    private fun startVideo() {
        stop_button.setImageResource(android.R.drawable.ic_media_pause)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        video_view.start()
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