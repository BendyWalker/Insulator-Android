package com.bendywalker.insulator

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log

/**
 * Created by Ben David Walker (bendywalker) on 09/01/2017.
 */

/**
 * Wrapper around [PreferenceManager] that simplifies interfacing with [SharedPreferences].
 *
 * @param context The context of the preferences whose values are wanted.
 */
class PersistedValues(context: Context) {
    private val versionCodeKey = context.getString(R.string.key_version_code)
    private val firstRunKey = context.getString(R.string.key_is_first_run)
    private val allowFloatingPointCarbohydratesKey = context.getString(R.string.key_allow_floating_point_carbohydrates)
    private val bloodGlucoseUnitKey = context.getString(R.string.key_blood_glucose_unit)
    private val carbohydrateFactorKey = context.getString(R.string.key_carbohydrate_factor)
    private val correctiveFactorKey = context.getString(R.string.key_corrective_factor)
    private val desiredBloodGlucoseKey = context.getString(R.string.key_desired_blood_glucose)

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    var versionCode: Int
        get() = preferences.getInt(versionCodeKey, 0)
        set(value) {
            preferences.edit().putInt(versionCodeKey, value).apply()
        }

    var firstRun: Boolean
        get() = preferences.getBoolean(firstRunKey, true)
        set(value) {
            preferences.edit().putBoolean(firstRunKey, value).apply()
        }

    var allowFloatingPointCarbohydrates: Boolean
        get() = preferences.getBoolean(allowFloatingPointCarbohydratesKey, false)
        set(value) {
            preferences.edit().putBoolean(allowFloatingPointCarbohydratesKey, value).apply()
        }

    var bloodGlucoseUnit: BloodGlucoseUnit
        get() = BloodGlucoseUnit.valueOf(preferences.getString(bloodGlucoseUnitKey, "MMOL"))
        set(value) {
            preferences.edit().putString(bloodGlucoseUnitKey, value.toString()).apply()
        }

    var carbohydrateFactor: Double
        get() = preferences.getString(carbohydrateFactorKey, "0.0").toDouble()
        set(value) {
            preferences.edit().putString(carbohydrateFactorKey, value.toString()).apply()
        }

    var correctiveFactor: Double
        get() = preferences.getString(correctiveFactorKey, "0.0").toDouble()
        set(value) {
            preferences.edit().putString(correctiveFactorKey, value.toString()).apply()
        }

    var desiredBloodGlucose: Double
        get() = preferences.getString(desiredBloodGlucoseKey, "0.0").toDouble()
        set(value) {
            preferences.edit().putString(desiredBloodGlucoseKey, value.toString()).apply()
        }

    fun setValueForKey(value: Double, key: String?) {
        when(key) {
            carbohydrateFactorKey -> carbohydrateFactor = value
            correctiveFactorKey -> correctiveFactor = value
            desiredBloodGlucoseKey -> desiredBloodGlucose = value
            else -> if (key != null) Log.w(javaClass.simpleName, "Key $key passed to setValueForKey() function is invalid")
        }
    }
}