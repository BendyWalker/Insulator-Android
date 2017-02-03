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
    private val bloodGlucoseUnitPreference by lazy { findViewById(R.id.bloodGlucoseUnitPreference_welcome) as BloodGlucoseUnitPreference }
    private val carbohydrateAccuracyPreference by lazy { findViewById(R.id.carbohydrateAccuracyPreference_welcome) as CarbohydrateAccuracyPreference }
    private val saveSuggestionOnExitPreference by lazy { findViewById(R.id.saveSuggestionOnExitPreference_welcome) as SaveSuggestionOnExitPreference }
    private val desiredBloodGlucoseLevelCardBody by lazy { findViewById(R.id.cardBody_welcome_desiredBloodGlucoseLevel) as CardBody }
    private val carbohydrateFactorCardBody by lazy { findViewById(R.id.cardBody_welcome_carbohydrateFactor) as CardBody }
    private val correctiveFactorCardBody by lazy { findViewById(R.id.cardBody_welcome_correctiveFactor) as CardBody }
    private val readyToUseCardView by lazy { findViewById(R.id.cardView_welcome_readyToUse) as CardView }
    private val useInsulatorButton by lazy { findViewById(R.id.button_welcome_useInsulator) as Button }

    private val persistedValues by lazy { PersistedValues(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        bloodGlucoseUnitPreference.onChangeListener = this

        carbohydrateAccuracyPreference.onChangeListener = this

        desiredBloodGlucoseLevelCardBody.onTextChangeListener = this
        desiredBloodGlucoseLevelCardBody.onFocusChangeListener = this

        carbohydrateFactorCardBody.onTextChangeListener = this
        carbohydrateFactorCardBody.onFocusChangeListener = this

        correctiveFactorCardBody.onTextChangeListener = this
        correctiveFactorCardBody.onFocusChangeListener = this

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
    }

    override fun onCardBodyFocusChange(id: Int, hasFocus: Boolean) {
        when (id) {
            R.id.cardBody_welcome_desiredBloodGlucoseLevel -> if (!hasFocus) persistedValues.desiredBloodGlucose = desiredBloodGlucoseLevelCardBody.value
            R.id.cardBody_welcome_carbohydrateFactor -> if (!hasFocus) persistedValues.carbohydrateFactor = carbohydrateFactorCardBody.value
            R.id.cardBody_welcome_correctiveFactor -> if (!hasFocus) persistedValues.correctiveFactor = correctiveFactorCardBody.value
        }
    }
}
