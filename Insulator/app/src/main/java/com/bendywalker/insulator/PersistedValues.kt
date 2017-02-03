package com.bendywalker.insulator

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by Ben David Walker (bendywalker) on 09/01/2017.
 */

/**
 * Wrapper around [PreferenceManager] that simplifies interfacing with [SharedPreferences].
 *
 * @param context The context of the preferences whose values are wanted.
 */
class PersistedValues(context: Context) {
    private val versionCodeKey = context.getString(R.string.key_versionCode)
    private val firstRunKey = context.getString(R.string.key_isFirstRun)
    private val bloodGlucoseUnitKey = context.getString(R.string.key_bloodGlucoseUnit)
    private val allowFloatingPointCarbohydratesKey = context.getString(R.string.key_allowFloatingPointCarbohydrates)
    private val saveSuggestionOnExitKey = context.getString(R.string.key_saveSuggestionOnExit)
    private val carbohydrateFactorKey = context.getString(R.string.key_carbohydrateFactor)
    private val correctiveFactorKey = context.getString(R.string.key_correctiveFactor)
    private val desiredBloodGlucoseKey = context.getString(R.string.key_desiredBloodGlucose)

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    var versionCode: Int
        get() = preferences.getInt(versionCodeKey, 0)
        set(value) { preferences.edit().putInt(versionCodeKey, value).apply() }

    var firstRun: Boolean
        get() = preferences.getBoolean(firstRunKey, true)
        set(value) { preferences.edit().putBoolean(firstRunKey, value).apply() }

    var bloodGlucoseUnit: BloodGlucoseUnit
        get() = BloodGlucoseUnit.valueOf(preferences.getString(bloodGlucoseUnitKey, "MMOL"))
        set(value) { preferences.edit().putString(bloodGlucoseUnitKey, value.toString()).apply() }

    var allowFloatingPointCarbohydrates: Boolean
        get() = preferences.getBoolean(allowFloatingPointCarbohydratesKey, false)
        set(value) { preferences.edit().putBoolean(allowFloatingPointCarbohydratesKey, value).apply() }

    var saveSuggestionOnExit: Boolean
        get() = preferences.getBoolean(saveSuggestionOnExitKey, false)
        set(value) { preferences.edit().putBoolean(saveSuggestionOnExitKey, value).apply() }

    var carbohydrateFactor: Double
        get() = preferences.getString(carbohydrateFactorKey, "0.0").toDouble()
        set(value) { preferences.edit().putString(carbohydrateFactorKey, value.toString()).apply() }

    var correctiveFactor: Double
        get() = preferences.getString(correctiveFactorKey, "0.0").toDouble()
        set(value) { preferences.edit().putString(correctiveFactorKey, value.toString()).apply() }

    var desiredBloodGlucose: Double
        get() = preferences.getString(desiredBloodGlucoseKey, "0.0").toDouble()
        set(value) { preferences.edit().putString(desiredBloodGlucoseKey, value.toString()).apply() }
}