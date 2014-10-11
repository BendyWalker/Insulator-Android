package com.bendywalker.insulator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Calculator
{
    SharedPreferences preferences;
    Context context;
    private float carbohydrateFactor;
    private float correctiveFactor;
    private float desiredBloodGlucoseLevel;
    private float currentBloodGlucoseLevel;
    private float carbohydratesInMeal;

    // Constructors
    public Calculator()
    {
        super();
    }

    public Calculator(float currentBloodGlucoseLevel, float carbohydratesInMeal, Context context)
    {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;

        this.currentBloodGlucoseLevel = convertBloodGlucoseMeasurement(currentBloodGlucoseLevel);
        this.carbohydratesInMeal = carbohydratesInMeal;
        this.carbohydrateFactor = preferences
                .getFloat(context.getString(R.string.preference_carbohydrate_factor), 0);
        this.correctiveFactor = convertBloodGlucoseMeasurement(
                preferences.getFloat(context.getString(R.string.preference_corrective_factor), 0));
        this.desiredBloodGlucoseLevel = convertBloodGlucoseMeasurement(preferences.getFloat(
                context.getString(R.string.preference_desired_blood_glucose_level), 0));
    }

    private float convertBloodGlucoseMeasurement(float bloodGlucose)
    {
        String bloodGlucoseUnit = preferences
                .getString(context.getString(R.string.preference_blood_glucose_units), "mmol");

        if (bloodGlucoseUnit.equals("mgdl"))
        {
            return bloodGlucose / 18;
        }
        else
        {
            return bloodGlucose;
        }
    }

    public double getCalculatedCarbohydrateDose(boolean rounded)
    {
        double carbohydrateDose = 0;

//        if (true) { // TODO: change to correct preference, kind of insulin-to-carb ratio
//            carbohydrateDose = (carbohydrateFactor * carbohydratesInMeal) / 10;
//        } else {
        carbohydrateDose = carbohydratesInMeal / carbohydrateFactor;
//        }

        if (rounded)
        {
            carbohydrateDose = roundNumber(carbohydrateDose);
        }

        return carbohydrateDose;
    }


    public double getCalculatedCorrectiveDose(boolean rounded)
    {
        double correctiveDose = 0.0;

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

    public double getCalculatedInsulinDose(boolean rounded)
    {
        double total = getCalculatedCarbohydrateDose(false) + getCalculatedCorrectiveDose(false);

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

    private double roundNumber(Double number)
    {
        double output;

        if (preferences.getBoolean(context.getString(R.string.preference_half_units), false))
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