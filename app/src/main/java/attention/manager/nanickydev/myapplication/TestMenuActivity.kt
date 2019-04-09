package attention.manager.nanickydev.myapplication

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity


class TestMenuActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_menu)

        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)


        val viewPager = findViewById(R.id.viewpager) as ViewPager
        setupViewPager(viewPager)

        val tabLayout = findViewById(R.id.tablayout) as TabLayout
//        tabLayout.setTabTextColors(resources.getColor(R.color.colorGreyText), resources.getColor(R.color.colorPrimary))
        tabLayout.setupWithViewPager(viewPager)



    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(VideoFragment(), "Video")
        adapter.addFragment(MusicFragment(), "Music")
        viewPager.adapter = adapter
    }

}
