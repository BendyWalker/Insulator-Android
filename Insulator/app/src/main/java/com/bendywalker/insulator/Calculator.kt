package com.bendywalker.insulator

import java.math.RoundingMode
import java.text.DecimalFormat
import com.bendywalker.insulator.BloodGlucoseUnit

/**
 * Created by Ben David Walker (bendywalker) on 06/01/2017.
 */

class Calculator(private val carbohydrateFactor: Double = 0.0,
                 private val correctiveFactor: Double = 0.0,
                 desiredBloodGlucose: Double = 0.0,
                 currentBloodGlucose: Double = 0.0,
                 private val carbohydratesInMeal: Double = 0.0,
                 private val totalDailyDose: Double = 0.0,
                 private val bloodGlucoseUnit: BloodGlucoseUnit = BloodGlucoseUnit.MMOL) {

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
                correctiveDose = (convertBloodGlucose(currentBloodGlucose) - convertBloodGlucose(desiredBloodGlucose)) / convertBloodGlucose(correctiveFactor)
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

    fun getCarbohydrateFactor(): Double {
        var carbohydrateFactor = 500 / totalDailyDose
        carbohydrateFactor = round(carbohydrateFactor)
        return carbohydrateFactor
    }

    fun getCorrectiveFactor(): Double {
        var correctiveFactor = 0.0

        when (bloodGlucoseUnit) {
            BloodGlucoseUnit.MMOL -> correctiveFactor = 100 / totalDailyDose
            BloodGlucoseUnit.MGDL -> correctiveFactor = 100 / totalDailyDose * MGDL_CONVERSION_VALUE
        }

        correctiveFactor = round(correctiveFactor)
        return correctiveFactor
    }

    companion object {
        val MGDL_CONVERSION_VALUE = 18

        fun round(value: Double?): Double {
            val decimalFormat = DecimalFormat("#0.0")
            decimalFormat.roundingMode = RoundingMode.HALF_UP
            return java.lang.Double.valueOf(decimalFormat.format(value))!!
        }
    }
}
