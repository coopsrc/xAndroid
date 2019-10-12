package com.coopsrc.xandroid.utils;

import com.coopsrc.xandroid.utils.MemoryUnit;

import org.junit.Test;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-29 16:40
 */
public class MemoryUnitTest {
    @Test
    public void testFormat() {
        long b = 1L;
        long kb = b << 10;
        System.out.println("toKiloByte " + MemoryUnit.MEGA_BYTE.toKiloByte(101));
        System.out.println("toGigaByte " + MemoryUnit.MEGA_BYTE.toGigaByte(103424));
        System.out.println("toKiloByte " + (100 << 10));
        System.out.println("toKiloByte " + (101 << 10));

        System.out.println("kb: " + kb);
    }
}
