package com.bendywalker.insulator

import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Created by Ben David Walker (bendywalker) on 06/01/2017.
 */

class Calculator(currentBloodGlucose: Double = 0.0,
                 private var carbohydratesInMeal: Double = 0.0,
                 private val carbohydrateFactor: Double = 0.0,
                 correctiveFactor: Double = 0.0,
                 desiredBloodGlucose: Double = 0.0,
                 private val bloodGlucoseUnit: BloodGlucoseUnit = BloodGlucoseUnit.MMOL) {

    constructor(currentBloodGlucose: Double,
                carbohydratesInMeal: Double,
                preferenceManager: PreferenceManager) :
            this(preferenceManager.carbohydrateFactor,
                    preferenceManager.correctiveFactor,
                    preferenceManager.desiredBloodGlucose,
                    currentBloodGlucose, carbohydratesInMeal,
                    preferenceManager.bloodGlucoseUnit)

    private val correctiveFactor = convertBloodGlucose(correctiveFactor)
    private val desiredBloodGlucose = convertBloodGlucose(desiredBloodGlucose)
    private var currentBloodGlucose = convertBloodGlucose(currentBloodGlucose)
        set(value) {
            convertBloodGlucose(value)
        }

    private fun convertBloodGlucose(bloodGlucose: Double): Double {
        when (bloodGlucoseUnit) {
            BloodGlucoseUnit.MMOL -> return bloodGlucose
            BloodGlucoseUnit.MGDL -> return round(bloodGlucose / MGDL_CONVERSION_VALUE)
            else -> return bloodGlucose
        }
    }

    val carbohydrateDose: Double
        get() {
            var carbohydrateDose = 0.0

            if (carbohydrateFactor != 0.0) {
                carbohydrateDose = carbohydratesInMeal / carbohydrateFactor
            }
            carbohydrateDose = round(carbohydrateDose)
            return carbohydrateDose
        }

    val correctiveDose: Double
        get() {
            var correctiveDose = 0.0

            if (currentBloodGlucose != 0.0) {
                correctiveDose = (currentBloodGlucose - desiredBloodGlucose) / correctiveFactor
            }
            correctiveDose = round(correctiveDose)
            return correctiveDose
        }

    val totalDose: Double
        get() {
            var totalDose = carbohydrateDose + correctiveDose

            if (totalDose < 0) {
                totalDose = 0.0
            }

            return totalDose
        }

    companion object {
        val MGDL_CONVERSION_VALUE = 18

        fun round(value: Double?): Double {
            val decimalFormat = DecimalFormat("#0.0")
            decimalFormat.roundingMode = RoundingMode.HALF_UP
            return java.lang.Double.valueOf(decimalFormat.format(value))!!
        }

        fun getCarbohydrateFactor(totalDailyDose: Double): Double {
            val carbohydrateFactor = 500 / totalDailyDose
            return round(carbohydrateFactor)
        }

        fun getCorrectiveFactor(totalDailyDose: Double, bloodGlucoseUnit: BloodGlucoseUnit): Double {
            val correctiveFactor: Double

            when (bloodGlucoseUnit) {
                BloodGlucoseUnit.MMOL -> correctiveFactor = 100 / totalDailyDose
                BloodGlucoseUnit.MGDL -> correctiveFactor = 100 / totalDailyDose * MGDL_CONVERSION_VALUE
            }

            return round(correctiveFactor)
        }
    }
}
