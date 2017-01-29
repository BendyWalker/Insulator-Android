package com.bendywalker.insulator

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar

/**
 * Created by Ben David Walker (bendywalker) on 09/01/2017.
 */

class DashboardActivity : BaseActivity() {
    val toolbar by lazy { findViewById(R.id.dashboard_toolbar) as Toolbar }
    val viewPager by lazy { findViewById(R.id.dashboard_viewPager) as ViewPager }
    val tabLayout by lazy { findViewById(R.id.dashboard_tabLayout) as TabLayout }

    val persistedValues by lazy { PersistedValues(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)

            if (persistedValues.versionCode < packageInfo.versionCode) {
                persistedValues.versionCode = packageInfo.versionCode
            }
        } catch (exception: PackageManager.NameNotFoundException) {
            exception.printStackTrace()
        }

        if (persistedValues.firstRun) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        } else {
            setContentView(R.layout.activity_dashboard)
            toolbar.inflateMenu(R.menu.dashboard)
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_settings -> true //TODO: Open Settings activity here
                    else -> false
                }
            }

            val pagerAdapter = PagerAdapter(supportFragmentManager, this)
            viewPager.adapter = pagerAdapter
            viewPager.offscreenPageLimit = 1
            tabLayout.setupWithViewPager(viewPager)
        }
    }

    class PagerAdapter(fragmentManager: FragmentManager, val context: Context) : FragmentPagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment {
            return Page.tabForPosition(position)!!.fragment
        }

        override fun getCount(): Int {
            return Page.values().size
        }

        override fun getPageTitle(position: Int): CharSequence {
            val string = context.resources.getString(Page.tabForPosition(position)!!.titleStringRes)
            return string
        }

        enum class Page(val titleStringRes: Int) {

            VARIABLES(R.string.title_variables),
            CONSTANTS(R.string.title_constants);

            val fragment: Fragment
                get() {
                    when (this) {
                        VARIABLES -> return VariableDataFragment.newInstance()
                        CONSTANTS -> return ConstantDataFragment.newInstance()
                    }
                }

            companion object {
                fun tabForPosition(position: Int): Page? {
                    when (position) {
                        0 -> return VARIABLES
                        1 -> return CONSTANTS
                    }

                    return null
                }
            }
        }
    }
}
