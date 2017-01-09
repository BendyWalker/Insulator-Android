package com.bendywalker.insulator;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CalculatorUnitTest {

    private static Calculator mmolCalculator;
    private static Calculator mgdlCalculator;

    @BeforeClass
    public static void setup() {
        mmolCalculator = new Calculator(9.3, 2.3, 6.5, 12.4, 50.0, BloodGlucoseUnit.MMOL);
        mgdlCalculator = new Calculator(9.3, 41.4, 117.0, 223.2, 50.0, BloodGlucoseUnit.MMOL);
    }

    private static double EXPECTED_CARBOHYDRATE_DOSE = 5.4;

    @Test
    public void mmol_carbohydrateDose_isCorrect() throws Exception {
        assertEquals(EXPECTED_CARBOHYDRATE_DOSE, mmolCalculator.getCarbohydrateDose(), 0);
    }

    @Test
    public void mgdl_carbohydrateDose_isCorrect() throws Exception {
        assertEquals(EXPECTED_CARBOHYDRATE_DOSE, mgdlCalculator.getCarbohydrateDose(), 0);
    }

    private static double EXPECTED_CORRECTIVE_DOSE = 2.6;

    @Test
    public void mmol_correctiveDose_isCorrect() throws Exception {
        assertEquals(EXPECTED_CORRECTIVE_DOSE, mmolCalculator.getCorrectiveDose(), 0);
    }

    @Test
    public void mgdl_correctiveDose_isCorrect() throws Exception {
        assertEquals(EXPECTED_CORRECTIVE_DOSE, mgdlCalculator.getCorrectiveDose(), 0);
    }

    private static double EXPECTED_TOTAL_DOSE = EXPECTED_CARBOHYDRATE_DOSE + EXPECTED_CORRECTIVE_DOSE;

    @Test
    public void mmol_totalDose_isCorrect() throws Exception {
        assertEquals(EXPECTED_TOTAL_DOSE, mmolCalculator.getTotalDose(), 0);
    }

    @Test
    public void mgdl_totalDose_isCorrect() throws Exception {
        assertEquals(EXPECTED_TOTAL_DOSE, mgdlCalculator.getTotalDose(), 0);
    }

    private static double TOTAL_DAILY_DOSE = 50.0;

    @Test
    public void carbohydrateFactor_isCorrect() throws Exception {
         double expectedCarbohydrateFactor = 10;
        assertEquals(expectedCarbohydrateFactor, Calculator.Companion.getCarbohydrateFactor(TOTAL_DAILY_DOSE), 0);
    }

    @Test
    public void mmol_correctiveFactor_isCorrect() throws Exception {
        double expectedMmolCorrectiveFactor = 2;
        assertEquals(expectedMmolCorrectiveFactor, Calculator.Companion.getCorrectiveFactor(TOTAL_DAILY_DOSE, BloodGlucoseUnit.MMOL), 0);
    }

    @Test
    public void mgdl_correctiveFactor_isCorrect() throws Exception {
        double expectedMgdlCorrectiveFactor = 36;
        assertEquals(expectedMgdlCorrectiveFactor, Calculator.Companion.getCorrectiveFactor(TOTAL_DAILY_DOSE, BloodGlucoseUnit.MGDL), 0);
    }
}