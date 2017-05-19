package com.bendywalker.insulator

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_welcome.*

/**
 * Created by Ben David Walker (bendywalker) on 28/01/2017.
 */

class WelcomeActivity : BaseActivity(), BloodGlucoseUnitPreference.OnChangeListener, CarbohydrateAccuracyPreference.OnChangeListener, CardBody.OnTextChangeListener {
    private val persistedValues by lazy { PersistedValues(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        bloodGlucoseUnitPreference.onChangeListener = this
        carbohydrateAccuracyPreference.onChangeListener = this
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
        desiredBloodGlucoseLevelCardBody.updateUi()
        correctiveFactorCardBody.updateUi()
    }

    override fun onCarbohydrateAccuracyChanged(allowFloatingPointCarbohydrates: Boolean) {
        carbohydrateFactorCardBody.updateUi()
    }

    override fun onCardBodyTextChange(id: Int, string: String) {
        if (desiredBloodGlucoseLevelCardBody.value != 0.0 &&
                carbohydrateFactorCardBody.value != 0.0 &&
                correctiveFactorCardBody.value != 0.0) {
            readyToUseCardView.visibility = View.VISIBLE
        } else {
            readyToUseCardView.visibility = View.GONE
        }

        when (id) {
            desiredBloodGlucoseLevelCardBody.id -> persistedValues.desiredBloodGlucose = desiredBloodGlucoseLevelCardBody.value
            carbohydrateFactorCardBody.id -> persistedValues.carbohydrateFactor = carbohydrateFactorCardBody.value
            correctiveFactorCardBody.id -> persistedValues.correctiveFactor = correctiveFactorCardBody.value
        }
    }
}
