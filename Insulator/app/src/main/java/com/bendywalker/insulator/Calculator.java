package com.bendywalker.insulator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Ben on 06/10/2014.
 */
public class Calculator {
    private float carbohydrateFactor;
    private float correctiveFactor;
    private float desiredBloodGlucoseLevel;
    private float currentBloodGlucoseLevel;
    private float carbohydratesInMeal;

    SharedPreferences preferences;


    // Constructors
    public Calculator() {
        super();
    }

    public Calculator(float currentBloodGlucoseLevel, float carbohydratesInMeal, Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);

        this.currentBloodGlucoseLevel = checkBloodGlucoseMeasurement(currentBloodGlucoseLevel);
        this.carbohydratesInMeal = carbohydratesInMeal;
        this.carbohydrateFactor = preferences.getFloat(context.getString(R.string.preference_carbohydrate_factor), 0);
        this.correctiveFactor = preferences.getFloat(context.getString(R.string.preference_corrective_factor), 0);
        this.desiredBloodGlucoseLevel = checkBloodGlucoseMeasurement(preferences.getFloat(context.getString(R.string.preference_desired_blood_glucose_level), 0));
    }

    private float checkBloodGlucoseMeasurement(float bloodGlucoseInMgdl) {
        if (true) { // TODO: change to correct preference, mg/dl
            return bloodGlucoseInMgdl / 18;
        }

        return bloodGlucoseInMgdl;
    }

    public float calculateCarbohydrateDose() {
        float carbohydrateDose = 0;

        if (true) { // TODO: change to correct preference, kind of insulin-to-carb ratio
            carbohydrateDose = (carbohydrateFactor * carbohydratesInMeal) / 10;
        } else {
            carbohydrateDose = carbohydratesInMeal / carbohydrateFactor;
        }

        return carbohydrateDose;
    }


    public float calculateCorrectiveDose() {
        return (currentBloodGlucoseLevel - desiredBloodGlucoseLevel) / correctiveFactor;
    }

    public double getCalculatedInsulinDose() {
        double total = calculateCarbohydrateDose() + calculateCorrectiveDose();

        if (total < 0) {
            total = 0;
        } else {
            if (true) { // TODO: change to correct preference, half units
                total = (Math.round(total * 2)) * 0.5;
            } else {
                total = Math.round(total);
            }
        }

        return total;
    }
}