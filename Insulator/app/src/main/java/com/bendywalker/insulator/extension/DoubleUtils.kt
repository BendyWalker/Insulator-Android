package com.bendywalker.insulator.extension

import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Created by Ben David Walker on 09/01/2017.
 */

/**
 * Rounds a [Double] to a given level of precision. For example, passed a *precision* of **1**, the function
 * would round **1.234** to **1.2**.
 *
 * @param precision The number of places to which you wish to round the double. Has a default value of **0**
 * @param roundingMode The rounding mode you wish to use. Has a default value of **HALF_UP**
 * @return A rounded double
 */
fun Double.round(precision: Int = 0, roundingMode: RoundingMode = RoundingMode.HALF_UP): Double {
    var string = "#0."
    for (index in 0 until precision) {
        string = string.plus("0")
    }
    val decimalFormat = DecimalFormat(string)
    decimalFormat.roundingMode = roundingMode
    return decimalFormat.format(this).toDouble()
}
