package com.bendywalker.insulator

import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Created by Ben David Walker (bendywalker) on 06/01/2017.
 */

class Calculator(currentBloodGlucose: Double = 0.0, var carbohydratesInMeal: Double = 0.0,
                 private val carbohydrateFactor: Double = 0.0, correctiveFactor: Double = 0.0,
                 desiredBloodGlucose: Double = 0.0, private val bloodGlucoseUnit: BloodGlucoseUnit = BloodGlucoseUnit.MMOL) {

    constructor(currentBloodGlucose: Double = 0.0, carbohydratesInMeal: Double = 0.0, preferenceManager: PreferenceManager) :
            this(currentBloodGlucose, carbohydratesInMeal, preferenceManager.carbohydrateFactor, preferenceManager.correctiveFactor, preferenceManager.desiredBloodGlucose, preferenceManager.bloodGlucoseUnit)

    private val correctiveFactor = correctiveFactor
        get() = convertBloodGlucose(field)

    private val desiredBloodGlucose = desiredBloodGlucose
        get() = convertBloodGlucose(field)

    var currentBloodGlucose: Double = currentBloodGlucose
        get() = convertBloodGlucose(field)

    private fun convertBloodGlucose(bloodGlucose: Double): Double {
        when (bloodGlucoseUnit) {
            BloodGlucoseUnit.MMOL -> return bloodGlucose
            BloodGlucoseUnit.MGDL -> return round(bloodGlucose / MGDL_CONVERSION_VALUE)
            else -> return bloodGlucose
        }
    }

    val carbohydrateDose: Double
        get() {
            if (carbohydrateFactor != 0.0) {
                val carbohydrateDose = carbohydratesInMeal / carbohydrateFactor
                return round(carbohydrateDose)
            } else return 0.0
        }

    val correctiveDose: Double
        get() {
            if (correctiveFactor != 0.0) {
                val correctiveDose = (currentBloodGlucose - desiredBloodGlucose) / correctiveFactor
                return round(correctiveDose)
            } else return 0.0
        }

    val totalDose: Double
        get() {
            val totalDose = carbohydrateDose + correctiveDose
            return if (totalDose > 0) totalDose else 0.0
        }

    companion object {
        val MGDL_CONVERSION_VALUE = 18

        fun round(value: Double?): Double {
            val decimalFormat = DecimalFormat("#0.0")
            decimalFormat.roundingMode = RoundingMode.HALF_UP
            return java.lang.Double.valueOf(decimalFormat.format(value))!!
        }

        fun carbohydrateFactor(totalDailyDose: Double): Double {
            val carbohydrateFactor = 500 / totalDailyDose
            return round(carbohydrateFactor)
        }

        fun correctiveFactor(totalDailyDose: Double, bloodGlucoseUnit: BloodGlucoseUnit): Double {
            val correctiveFactor: Double

            when (bloodGlucoseUnit) {
                BloodGlucoseUnit.MMOL -> correctiveFactor = 100 / totalDailyDose
                BloodGlucoseUnit.MGDL -> correctiveFactor = 100 / totalDailyDose * MGDL_CONVERSION_VALUE
            }

            return round(correctiveFactor)
        }
    }
}
