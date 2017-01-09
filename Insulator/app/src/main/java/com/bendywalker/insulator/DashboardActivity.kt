package com.bendywalker.insulator

import android.content.pm.PackageManager
import android.os.Bundle

class DashboardActivity : BaseActivity() {
    val preferenceManager = PreferenceManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)

            if (preferenceManager.versionCode < packageInfo.versionCode) {
                preferenceManager.versionCode = packageInfo.versionCode
            }
        } catch (exception: PackageManager.NameNotFoundException) {
            exception.printStackTrace()
        }

        if (preferenceManager.firstRun) {
            // TODO: Start welcome activity
            finish()
        } else {
            // TODO: Setup activity layout
        }
    }
}
