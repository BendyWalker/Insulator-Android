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
    val preferenceManager: PreferenceManager by lazy { PreferenceManager(this) }
    val currentBloodGlucoseEditText: EditText by lazy { findViewById(R.id.editText_dashboard_currentBloodGlucose) as EditText }
    val carbohydratesInMealEditText: EditText by lazy { findViewById(R.id.editText_dashboard_carbohydratesInMeal) as EditText }
    val totalDoseTextView: TextView by lazy { findViewById(R.id.textView_dashboard_totalDose) as TextView }
    val calculateButton: Button by lazy { findViewById(R.id.button_dashboard_calculate) as Button }

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

        preferenceManager.carbohydrateFactor = 10.0
        preferenceManager.correctiveFactor = 2.0
        preferenceManager.allowFloatingPointCarbohydrates = false
        preferenceManager.desiredBloodGlucose = 6.5
        preferenceManager.firstRun = false
        preferenceManager.bloodGlucoseUnit = BloodGlucoseUnit.MMOL

        if (preferenceManager.firstRun) {
            // TODO: Start welcome activity
            finish()
        } else {
            // TODO: Setup activity layout
            setContentView(R.layout.activity_dashboard)

            val calculator = Calculator(preferenceManager = preferenceManager)

            calculateButton.setOnClickListener {
                calculator.currentBloodGlucose = currentBloodGlucoseEditText.text.toString().toDouble()
                calculator.carbohydratesInMeal = carbohydratesInMealEditText.text.toString().toDouble()
                totalDoseTextView.text = calculator.totalDose.toString()
            }
        }
    }
}