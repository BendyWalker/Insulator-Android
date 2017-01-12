package com.bendywalker.insulator

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toolbar

/**
 * Created by Ben David Walker (bendywalker) on 09/01/2017.
 */

class DashboardActivity : BaseActivity() {
    val toolbar by lazy { findViewById(R.id.dashboard_toolbar) as Toolbar }

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
            // TODO: Start welcome activity
            finish()
        } else {
            // TODO: Setup activity layout
            setContentView(R.layout.activity_dashboard)
            toolbar.inflateMenu(R.menu.dashboard)
            toolbar.setOnMenuItemClickListener { menuItem -> true } //TODO: Open Settings activity here
        }
    }
}
