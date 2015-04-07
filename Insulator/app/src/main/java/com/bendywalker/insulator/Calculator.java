package com.bendywalker.insulator;

import java.text.DecimalFormat;

public class Calculator {
    private static final int MGDL_CONVERSION_VALUE = 18;

    private double carbohydrateFactor;
    private double correctiveFactor;
    private double desiredBloodGlucose;
    private double currentBloodGlucose;
    private double carbohydratesInMeal;
    private double totalDailyDose;
    private BloodGlucoseUnit bloodGlucoseUnit;

    public Calculator(double carbohydrateFactor, double correctiveFactor, double desiredBloodGlucose, double currentBloodGlucose, double carbohydratesInMeal, BloodGlucoseUnit bloodGlucoseUnit) {
        this.carbohydrateFactor = carbohydrateFactor;
        this.correctiveFactor = correctiveFactor;
        this.desiredBloodGlucose = desiredBloodGlucose;
        this.currentBloodGlucose = currentBloodGlucose;
        this.carbohydratesInMeal = carbohydratesInMeal;
        this.bloodGlucoseUnit = bloodGlucoseUnit;
    }

    public Calculator(double totalDailyDose, BloodGlucoseUnit bloodGlucoseUnit) {
        this.totalDailyDose = totalDailyDose;
        this.bloodGlucoseUnit = bloodGlucoseUnit;
    }

    private double convertBloodGlucose(double bloodGlucose) {
        switch (bloodGlucoseUnit) {
            case mmol:
                return bloodGlucose;
            case mgdl:
                return bloodGlucose / MGDL_CONVERSION_VALUE;
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

    public double getTotalDose() {
        double totalDose = getCarbohydrateDose() + getCorrectiveDose();

        if (totalDose < 0) {
            totalDose = 0.0;
        }

        return totalDose;
    }

    public double getCarbohydrateFactor() {
        return 500 / totalDailyDose;
    }

    public double getCorrectiveFactor() {
        double correctiveFactor = 0.0;
        switch (bloodGlucoseUnit) {
            case mmol:
                correctiveFactor = 100 / totalDailyDose;
                break;
            case mgdl:
                correctiveFactor = (100 / totalDailyDose) * MGDL_CONVERSION_VALUE;
        }

        return correctiveFactor;
    }

    public static String getString(Double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.0");
        return decimalFormat.format(value);
    }
}
