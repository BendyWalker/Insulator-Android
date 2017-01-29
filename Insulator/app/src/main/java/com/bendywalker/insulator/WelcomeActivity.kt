package com.bendywalker.insulator

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.View
import android.widget.Button

/**
 * Created by Ben David Walker (bendywalker) on 28/01/2017.
 */

class WelcomeActivity : BaseActivity(), BloodGlucoseUnitPreference.OnChangeListener, CarbohydrateAccuracyPreference.OnChangeListener, CardBody.OnTextChangeListener, CardBody.OnFocusChangeListener {
    val bloodGlucoseUnitPreferencePicker by lazy { findViewById(R.id.bloodGlucoseUnitPreference_welcome) as BloodGlucoseUnitPreference }
    val desiredBloodGlucoseLevelCardBody by lazy { findViewById(R.id.cardBody_welcome_desiredBloodGlucoseLevel) as CardBody }
    val carbohydrateFactorCardBody by lazy { findViewById(R.id.cardBody_welcome_carbohydrateFactor) as CardBody }
    val correctiveFactorCardBody by lazy { findViewById(R.id.cardBody_welcome_correctiveFactor) as CardBody }
    val readyToUseCardView by lazy { findViewById(R.id.cardView_welcome_readyToUse) as CardView }
    val useInsulatorButton by lazy { findViewById(R.id.button_welcome_useInsulator) as Button }

    val persistedValues by lazy { PersistedValues(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        bloodGlucoseUnitPreferencePicker.onChangeListener = this
        desiredBloodGlucoseLevelCardBody.onTextChangeListener = this
        carbohydrateFactorCardBody.onTextChangeListener = this
        correctiveFactorCardBody.onTextChangeListener = this

        useInsulatorButton.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            persistedValues.firstRun = false
            finish()
        }
    }

    override fun onBloodGlucoseUnitPreferenceChange(bloodGlucoseUnit: BloodGlucoseUnit) {
        desiredBloodGlucoseLevelCardBody.updateHint()
        correctiveFactorCardBody.updateHint()
        desiredBloodGlucoseLevelCardBody.reset()
        correctiveFactorCardBody.reset()
    }

    override fun onCarbohydrateAccuracyChanged(allowFloatingPointCarbohydrates: Boolean) {
        carbohydrateFactorCardBody.reset()
    }

    override fun onCardBodyTextChange(id: Int, string: String) {
        if (desiredBloodGlucoseLevelCardBody.value != 0.0 &&
                carbohydrateFactorCardBody.value != 0.0 &&
                correctiveFactorCardBody.value != 0.0) {
            readyToUseCardView.visibility = View.VISIBLE
        } else {
            readyToUseCardView.visibility = View.GONE
        }
    }

    override fun onCardBodyFocusChange(id: Int, hasFocus: Boolean) {
        when (id) {
            desiredBloodGlucoseLevelCardBody.id -> if (!hasFocus) persistedValues.desiredBloodGlucose = desiredBloodGlucoseLevelCardBody.value
            carbohydrateFactorCardBody.id -> if (!hasFocus) persistedValues.carbohydrateFactor = carbohydrateFactorCardBody.value
            correctiveFactorCardBody.id -> if (!hasFocus) persistedValues.correctiveFactor = correctiveFactorCardBody.value
        }
    }
}
