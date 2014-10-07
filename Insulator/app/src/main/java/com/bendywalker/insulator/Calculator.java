package com.bendywalker.insulator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Ben on 06/10/2014.
 */
public class Calculator {
    public static final String CARBOHYRATE_FACTOR = "CARBOHYDRATE_FACTOR";
    public static final String CORRECTIVE_FACTOR = "CORRECTIVE_FACTOR";
    public static final String DESIRED_BLOOD_GLUCOSE_LEVEL = "DESIRED_BLOOD_GLUCOSE_LEVEL";

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

        this.currentBloodGlucoseLevel = convertMgdlToMmol(currentBloodGlucoseLevel);
        this.carbohydratesInMeal = carbohydratesInMeal;
        this.carbohydrateFactor = preferences.getFloat(CARBOHYRATE_FACTOR, 0);
        this.correctiveFactor = preferences.getFloat(CORRECTIVE_FACTOR, 0);
        this.desiredBloodGlucoseLevel = convertMgdlToMmol(preferences.getFloat(DESIRED_BLOOD_GLUCOSE_LEVEL, 0));
    }

    private float convertMgdlToMmol(float bloodGlucoseInMgdl) {
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