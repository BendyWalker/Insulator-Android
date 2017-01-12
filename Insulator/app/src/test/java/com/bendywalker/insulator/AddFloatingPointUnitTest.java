package com.bendywalker.insulator;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ben David Walker (bendywalker) on 12/01/2017.
 */

public class AddFloatingPointUnitTest
{
    @Test
    public void zeroes_isCorrect() {
        String number = "0000000000";
        assertEquals("0.0", Card.Companion.addFloatingPoint(number, 1));
    }

    @Test
    public void lengthOfTwo_precisionOfOne_isCorrect() {
        String number = "12";
        assertEquals("1.2", Card.Companion.addFloatingPoint(number, 1));
    }

    @Test
    public void lengthOfTwo_zeroes_precisionOfOne_isCorrect() {
        String number = "01";
        assertEquals("0.1", Card.Companion.addFloatingPoint(number, 1));
    }

    @Test
    public void lengthOfThree_precisionOfOne_isCorrect() {
        String number = "123";
        assertEquals("12.3", Card.Companion.addFloatingPoint(number, 1));
    }

    @Test
    public void lengthOfThree_floatingPoint_precisionOfOne_isCorrect() {
        String number = "12.34";
        assertEquals("123.4", Card.Companion.addFloatingPoint(number, 1));
    }

    @Test
    public void lengthOfFour_isCorrect() {
        String number = "1234";
        assertEquals("123.4", Card.Companion.addFloatingPoint(number, 1));
    }

    @Test
    public void lengthOfFour_zeroes_isCorrect() {
        String number = "0001";
        assertEquals("0.1", Card.Companion.addFloatingPoint(number, 1));
    }

    @Test
    public void lengthOfFour_precisionOfTwo_isCorrect() {
        String number = "1234";
        assertEquals("12.34", Card.Companion.addFloatingPoint(number, 2));
    }

    @Test
    public void lengthOfFive_interspersedZeroes_isCorrect() {
        String number = "00101";
        assertEquals("10.1", Card.Companion.addFloatingPoint(number, 1));
    }

    @Test
    public void lengthOfFive_interspersedZeroes_precisionOfTwo_isCorrect() {
        String number = "01010";
        assertEquals("10.10", Card.Companion.addFloatingPoint(number, 2));
    }

    @Test
    public void lengthOfSix_precisionOfThree_isCorrect() {
        String number = "012345";
        assertEquals("12.345", Card.Companion.addFloatingPoint(number, 3));
    }
}