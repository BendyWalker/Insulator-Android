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
    public void lengthOfTwo_isCorrect() {
        String number = "12";
        assertEquals("1.2", Card.Companion.addFloatingPoint(number));
    }

    @Test
    public void lengthOfTwo_withZeroes_isCorrect() {
        String number = "01";
        assertEquals("0.1", Card.Companion.addFloatingPoint(number));
    }

    @Test
    public void lengthOfThree_isCorrect() {
        String number = "123";
        assertEquals("12.3", Card.Companion.addFloatingPoint(number));
    }

    @Test
    public void lengthOfFour_isCorrect() {
        String number = "1234";
        assertEquals("123.4", Card.Companion.addFloatingPoint(number));
    }

    @Test
    public void lengthOfFour_withZeroes_isCorrect() {
        String number = "0001";
        assertEquals("0.1", Card.Companion.addFloatingPoint(number));
    }

    @Test
    public void lengthOfFive_withInterspersedZeroes_isCorrect() {
        String number = "00101";
        assertEquals("10.1", Card.Companion.addFloatingPoint(number));
    }
}