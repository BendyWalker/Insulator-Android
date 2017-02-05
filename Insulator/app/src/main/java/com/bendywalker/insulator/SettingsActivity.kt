package com.bendywalker.insulator

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import com.bendywalker.insulator.extension.round

/**
 * Created by Ben David Walker (bendywalker) on 05/02/2017.
 */
class SettingsActivity: BaseActivity(), BloodGlucoseUnitPreference.OnChangeListener {
    val bloodGlucoseUnitPreference by lazy { findViewById(R.id.bloodGlucoseUnitPreference_settings) as BloodGlucoseUnitPreference }
    val googlePlayFeedbackView by lazy { findViewById(R.id.view_settings_feedback_googlePlay) as LinearLayout }
    val twitterFeedbackView by lazy { findViewById(R.id.view_settings_feedback_twitter) as LinearLayout }

    val persistedValues by lazy { PersistedValues(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        bloodGlucoseUnitPreference.onChangeListener = this
        googlePlayFeedbackView.setOnClickListener { openGooglePlay() }
        twitterFeedbackView.setOnClickListener { openTwitter() }
    }

    override fun onBloodGlucoseUnitPreferenceChange(bloodGlucoseUnit: BloodGlucoseUnit) {
        var desiredBloodGlucose = persistedValues.desiredBloodGlucose
        var correctiveFactor = persistedValues.correctiveFactor
        println("Desired Blood Glucose (Before): $desiredBloodGlucose")
        println("Corrective Factor (Before): $correctiveFactor")

        when (bloodGlucoseUnit) {
            BloodGlucoseUnit.MMOL -> {
                desiredBloodGlucose = (desiredBloodGlucose / Calculator.mgdlConversionValue).round(1)
                correctiveFactor = (correctiveFactor / Calculator.mgdlConversionValue).round(1)
            }

            BloodGlucoseUnit.MGDL -> {
                desiredBloodGlucose = (desiredBloodGlucose * Calculator.mgdlConversionValue).round()
                correctiveFactor = (correctiveFactor * Calculator.mgdlConversionValue).round()
            }
        }

        println("Desired Blood Glucose (After): $desiredBloodGlucose")
        println("Corrective Factor (After): $correctiveFactor")
        persistedValues.desiredBloodGlucose = desiredBloodGlucose
        persistedValues.correctiveFactor = correctiveFactor
    }

    fun openGooglePlay() {}

    fun openTwitter() {}
}