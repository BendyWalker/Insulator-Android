package com.bendywalker.insulator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DecimalFormat;


public class Calculator
{
    private float carbohydrateFactor;
    private float correctiveFactor;
    private float desiredBloodGlucoseLevel;
    private float currentBloodGlucoseLevel;
    private float carbohydratesInMeal;
    private boolean isMmolSelected;

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
    }

    /**
     * Calculator should only be initialised without any arguments if it's only purpose is to
     * access the getString() method
     */
    public Calculator()
    {

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

    public double getCarbohydrateDose()
    {
        double carbohydrateDose = 0.0;

        if (carbohydrateFactor != 0)
        {
            carbohydrateDose = carbohydratesInMeal / carbohydrateFactor;
        }

        return carbohydrateDose;
    }

    public double getCorrectiveDose()
    {
        double correctiveDose = 0.0;

        if (currentBloodGlucoseLevel != 0)
        {
            correctiveDose = (currentBloodGlucoseLevel - desiredBloodGlucoseLevel) / correctiveFactor;
        }

        return correctiveDose;
    }

    public double getSuggestedDose()
    {
        double suggestedDose = getCarbohydrateDose() + getCorrectiveDose();

        if (suggestedDose < 0)
        {
            suggestedDose = 0.0;
        }

        return suggestedDose;
    }

    public String getString(Double dose)
    {
        DecimalFormat decimalFormat = new DecimalFormat("#0.0");
        return decimalFormat.format(dose);
    }
}
