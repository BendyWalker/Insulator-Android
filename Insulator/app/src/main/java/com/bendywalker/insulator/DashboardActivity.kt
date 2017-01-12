package com.bendywalker.insulator

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

/**
 * Created by Ben David Walker (bendywalker) on 09/01/2017.
 */

class DashboardActivity : BaseActivity() {
    val persistedValues: PersistedValues by lazy { PersistedValues(this) }

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
        }
    }
}
