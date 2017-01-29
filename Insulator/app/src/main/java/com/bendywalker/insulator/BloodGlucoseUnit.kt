package com.bendywalker.insulator

/**
 * Created by Ben David Walker (bendywalker) on 06/01/2017.
 */

/**
 * Specifies the different units used to measure blood glucose.
 */
enum class BloodGlucoseUnit(val string: String) {
    MMOL("mmol/L"),
    MGDL("mg/dL");

    val displayString: String
        get() = string
}