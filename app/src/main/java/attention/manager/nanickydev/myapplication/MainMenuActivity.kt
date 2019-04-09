package attention.manager.nanickydev.myapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_menu.*


class MainMenuActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        setupViewPager()


        tablayout.setTabTextColors(resources.getColor(R.color.colorPrimary), resources.getColor(R.color.videoItemTextColor))
        tablayout.setupWithViewPager(viewpager)



    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(VideoFragment(), "Video")
        adapter.addFragment(MusicFragment(), "Music")
        viewpager.adapter = adapter
    }

}
