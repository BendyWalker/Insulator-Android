package com.bendywalker.insulator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Calculator
{
    private float carbohydrateFactor;
    private float correctiveFactor;
    private float desiredBloodGlucoseLevel;
    private float currentBloodGlucoseLevel;
    private float carbohydratesInMeal;
    private boolean isMmolSelected;
    private boolean isHalfUnitsEnabled;

    public Calculator(float currentBloodGlucoseLevel, float carbohydratesInMeal, Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        this.currentBloodGlucoseLevel = convertBloodGlucoseMeasurement(currentBloodGlucoseLevel);
        this.carbohydratesInMeal = carbohydratesInMeal;
        this.carbohydrateFactor = preferences
                .getFloat(context.getString(R.string.preference_carbohydrate_factor), 0);
        this.correctiveFactor = convertBloodGlucoseMeasurement(
                preferences.getFloat(context.getString(R.string.preference_corrective_factor), 0));
        this.desiredBloodGlucoseLevel = convertBloodGlucoseMeasurement(preferences.getFloat(
                context.getString(R.string.preference_desired_blood_glucose_level), 0));
        this.isMmolSelected = (preferences
                .getString(context.getString(R.string.preference_blood_glucose_units), "mmol"))
                .equals("mmol");
        this.isHalfUnitsEnabled = preferences
                .getBoolean(context.getString(R.string.preference_half_units), false);
    }

    private float convertBloodGlucoseMeasurement(float bloodGlucose)
    {
        if (!isMmolSelected)
        {
            return bloodGlucose / 18;
        }
        else
        {
            return bloodGlucose;
        }
    }

    public double getCarbohydrateDose(boolean rounded)
    {
        double carbohydrateDose = carbohydratesInMeal / carbohydrateFactor;

        if (rounded)
        {
            carbohydrateDose = roundNumber(carbohydrateDose);
        }

        return carbohydrateDose;
    }

    public double getCorrectiveDose(boolean rounded)
    {
        double correctiveDose = 0;

        if (currentBloodGlucoseLevel != 0)
        {
            correctiveDose = (currentBloodGlucoseLevel - desiredBloodGlucoseLevel) / correctiveFactor;
        }

        if (rounded)
        {
            correctiveDose = roundNumber(correctiveDose);
        }

        return correctiveDose;
    }

    public double getSuggestedDose(boolean rounded)
    {
        double total = getCarbohydrateDose(false) + getCorrectiveDose(false);

        if (total < 0)
        {
            total = 0;
        }
        else
        {
            if (rounded)
            {
                total = roundNumber(total);
            }
        }

        return total;
    }

    public double roundNumber(Double number)
    {
        double output;

        if (isHalfUnitsEnabled)
        {
            output = (Math.round(number * 2)) * 0.5;
        }
        else
        {
            output = Math.round(number);
        }

        return output;
    }
}