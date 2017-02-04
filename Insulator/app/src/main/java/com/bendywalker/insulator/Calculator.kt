package com.bendywalker.insulator

import com.bendywalker.insulator.extension.round

/**
 * Created by Ben David Walker (bendywalker) on 06/01/2017.
 */

/**
 * Calculates different dosage values given a set of parameters.
 *
 * @param currentBloodGlucose The user's current blood glucose level.
 * @param carbohydratesInMeal Grams of carbohydrates in the user's meal.
 * @param carbohydrateFactor The user's carbohydrate factor.
 * @param correctiveFactor The user's corrective factor.
 * @param desiredBloodGlucose The blood glucose level the user wishes to attain.
 * @param bloodGlucoseUnit The user's preferred blood glucose unit (mmol/L or mg/dL).
 */
class Calculator(currentBloodGlucose: Double = 0.0, var carbohydratesInMeal: Double = 0.0,
                 private val carbohydrateFactor: Double, correctiveFactor: Double,
                 desiredBloodGlucose: Double, private val bloodGlucoseUnit: BloodGlucoseUnit) {

    /**
     * Constructs a [Calculator] used to calculate different dosage values. The majority of parameters required for
     * calculations are retrieved from [SharedPreferences] via [PersistedValues].
     *
     * @param currentBloodGlucose The user's current blood glucose level
     * @param carbohydratesInMeal Grams of carbohydrates in the user's meal
     * @param persistedValues An instance of [PersistedValues] used to read calculation parameters from [SharedPreferences]
     */
    constructor(currentBloodGlucose: Double = 0.0, carbohydratesInMeal: Double = 0.0, persistedValues: PersistedValues) :
            this(currentBloodGlucose, carbohydratesInMeal, persistedValues.carbohydrateFactor, persistedValues.correctiveFactor, persistedValues.desiredBloodGlucose, persistedValues.bloodGlucoseUnit)

    private val correctiveFactor = correctiveFactor
        get() = convertBloodGlucose(field)

    private val desiredBloodGlucose = desiredBloodGlucose
        get() = convertBloodGlucose(field)

    var currentBloodGlucose: Double = currentBloodGlucose
        get() = convertBloodGlucose(field)

    val carbohydrateDose: Double
        get() {
            if (carbohydrateFactor != 0.0) {
                val carbohydrateDose = carbohydratesInMeal / carbohydrateFactor
                return carbohydrateDose.round(roundingPrecision)
            } else return 0.0
        }

    val correctiveDose: Double
        get() {
            if (correctiveFactor != 0.0) {
                val correctiveDose = (currentBloodGlucose - desiredBloodGlucose) / correctiveFactor
                return correctiveDose.round(roundingPrecision)
            } else return 0.0
        }

    val totalDose: Double
        get() {
            val totalDose = carbohydrateDose + correctiveDose
            return if (totalDose > 0) totalDose.round(roundingPrecision) else 0.0
        }

    private fun convertBloodGlucose(bloodGlucose: Double): Double {
        when (bloodGlucoseUnit) {
            BloodGlucoseUnit.MMOL -> return bloodGlucose
            BloodGlucoseUnit.MGDL -> return (bloodGlucose / mgdlConversionValue).round(roundingPrecision)
            else -> return bloodGlucose
        }
    }

    companion object {
        private val mgdlConversionValue = 18
        private val roundingPrecision = 1

        /**
         * Given a *total daily dose*, calculates a user's carbohydrate factor.
         *
         * @param totalDailyDose The sum of all insulin taken daily by a user.
         * @return Calculated carbohydrate factor.
         */
        fun carbohydrateFactor(totalDailyDose: Double): Double {
            val carbohydrateFactor = 500 / totalDailyDose
            return carbohydrateFactor.round(roundingPrecision)
        }

        /**
         * Given a *total daily dose*, calculates a user's corrective factor.
         *
         * @param totalDailyDose The sum of all insulin taken daily by a user.
         * @param bloodGlucoseUnit A user's preferred blood glucose unit.
         * @return Calculated corrective factor.
         */
        fun correctiveFactor(totalDailyDose: Double, bloodGlucoseUnit: BloodGlucoseUnit): Double {
            val correctiveFactor: Double

            when (bloodGlucoseUnit) {
                BloodGlucoseUnit.MMOL -> correctiveFactor = 100 / totalDailyDose
                BloodGlucoseUnit.MGDL -> correctiveFactor = 100 / totalDailyDose * mgdlConversionValue
            }

            return correctiveFactor.round(roundingPrecision)
        }
    }
}
