package com.bendywalker.insulator;

import java.text.DecimalFormat;

public class Calculator {
    private double carbohydrateFactor;
    private double correctiveFactor;
    private double desiredBloodGlucose;
    private double currentBloodGlucose;
    private double carbohydratesInMeal;
    private BloodGlucoseUnit bloodGlucoseUnit;

    public Calculator(double carbohydrateFactor, double correctiveFactor, double desiredBloodGlucose, double currentBloodGlucose, double carbohydratesInMeal, BloodGlucoseUnit bloodGlucoseUnit) {
        this.carbohydrateFactor = carbohydrateFactor;
        this.correctiveFactor = correctiveFactor;
        this.desiredBloodGlucose = desiredBloodGlucose;
        this.currentBloodGlucose = currentBloodGlucose;
        this.carbohydratesInMeal = carbohydratesInMeal;
        this.bloodGlucoseUnit = bloodGlucoseUnit;
    }

    private double convertBloodGlucose(double bloodGlucose) {
        switch (bloodGlucoseUnit) {
            case mmol:
                return bloodGlucose;
            case mgdl:
                return bloodGlucose / 18;
            default:
                return bloodGlucose;
        }
    }

    public double getCarbohydrateDose() {
        double carbohydrateDose = 0.0;

        if (carbohydrateFactor != 0) {
            carbohydrateDose = carbohydratesInMeal / carbohydrateFactor;
        }

        return carbohydrateDose;
    }

    public double getCorrectiveDose() {
        double correctiveDose = 0.0;

        if (currentBloodGlucose != 0) {
            correctiveDose = (convertBloodGlucose(currentBloodGlucose) - convertBloodGlucose(desiredBloodGlucose)) / correctiveFactor;
        }

        return correctiveDose;
    }

    public double getSuggestedDose() {
        double suggestedDose = getCarbohydrateDose() + getCorrectiveDose();

        if (suggestedDose < 0) {
            suggestedDose = 0.0;
        }

        return suggestedDose;
    }

    public static String getString(Double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.0");
        return decimalFormat.format(value);
    }
}
