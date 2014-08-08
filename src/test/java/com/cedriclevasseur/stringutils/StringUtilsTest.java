package com.cedriclevasseur.stringutils;

import static com.cedriclevasseur.stringutils.StringUtils.ByteCompareTo;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author cedric
 */
public class StringUtilsTest {

    /**
     * Test of truncateWhenUTF8 method, of class StringUtils.
     */
    @org.junit.Test
    public void testTruncateWhenUTF8FromInternet() {
        System.out.println("truncateWhenUTF8");
        String s = "éééééééééé";
        int maxBytes = 10;
        String expResult = "ééééé";
        String result = StringUtils.truncateWhenUTF8fromInternet(s, maxBytes);
        assertEquals("ééééé", StringUtils.truncateWhenUTF8fromInternet("éééééééééé", 10));
        assertEquals("ééééé", StringUtils.truncateWhenUTF8fromInternet("ééééééééée", 10));
        assertEquals("éeeééé", StringUtils.truncateWhenUTF8fromInternet("éeeéééééée", 10));
        assertEquals("eeeeeeeeee", StringUtils.truncateWhenUTF8fromInternet("eeeeeeeeee", 10));
    }

//    @org.junit.Test
//    public void testTruncateWhenUTF8() {
//        System.out.println("truncateWhenUTF8");
//        // \u2026 is ... char, 3 bytes long.
//        assertEquals("ééé\u2026", StringUtils.truncateWhenUTF8("éééééééééé", 10));
//        assertEquals("ééé\u2026", StringUtils.truncateWhenUTF8("ééééééééée", 10));
//        assertEquals("éeeé\u2026", StringUtils.truncateWhenUTF8("éeeéééééée", 10));
//        assertEquals("eeeeeee\u2026", StringUtils.truncateWhenUTF8("eeeeeeeeeee", 10));
//        assertEquals("\u2025\u2025\u2026", StringUtils.truncateWhenUTF8("\u2025\u2025\u2025\u2025\u2025\u2025", 9));
//        assertEquals("\u2026\u2026\u2026", StringUtils.truncateWhenUTF8("\u2026\u2026\u2026\u2026\u2026\u2026", 10));
//
//    }

    @org.junit.Test
    public void testTruncateWhenUTF8Strictly() {
        System.out.println("truncateWhenUTF8Stricly");
        // \u2026 is ... char, 3 bytes long.
        assertEquals("eeeeeeeeee", StringUtils.truncateWhenUTF8Strictly("eeeeeeeeeee", 10));
        assertEquals("ééééé", StringUtils.truncateWhenUTF8Strictly("éééééééééé", 10));
        assertEquals("éeeééé", StringUtils.truncateWhenUTF8Strictly("éeeéééééée", 10));
        assertEquals("éeééé", StringUtils.truncateWhenUTF8Strictly("éeéééééée", 10));
        //assertEquals("\u2025\u2025\u2025", StringUtils.truncateWhenUTF8fromInternet("\u2025\u2025\u2025\u2025\u2025\u2025", 9));
        assertEquals("\u2025\u2025\u2025", StringUtils.truncateWhenUTF8Strictly("\u2025\u2025\u2025\u2025\u2025\u2025", 9));
        assertEquals("\u2026\u2026\u2026", StringUtils.truncateWhenUTF8Strictly("\u2026\u2026\u2026\u2026\u2026\u2026", 10));
        
    }


    
    @org.junit.Test
    public void testCut() {
        System.out.println("truncateWhenUTF8Stricly");

        assertEquals("A", StringUtils.cut("A", 1));
        assertEquals("A", StringUtils.cut("A", 2));
        assertEquals("A", StringUtils.cut("A", 3));
        assertEquals("A", StringUtils.cut("A", 4));
        assertEquals("é", StringUtils.cut("é", 2));
        assertEquals("", StringUtils.cut("é", 1));
        assertEquals("\u2026", StringUtils.cut("\u2026", 3));
        assertEquals("ééééé", StringUtils.cut("éééééééééé", 10));
        assertEquals("ééééé", StringUtils.cut("ééééééééée", 10));
        assertEquals("éeeééé", StringUtils.cut("éeeéééééée", 10));
        assertEquals("eeeeeeeeee", StringUtils.cut("eeeeeeeeee", 10));
        assertEquals("\u2026\u2026\u2026", StringUtils.cut("\u2026\u2026\u2026\u2026\u2026\u2026", 9));
        assertEquals("\u2026\u2026\u2026", StringUtils.cut("\u2026\u2026\u2026\u2026\u2026\u2026", 10));
    }

    @org.junit.Test
    public void testGetFirstBytePosition() {
        assertEquals(-1, StringUtils.getFirstBytePosition(null));
        byte[] emptyArray = new byte[0];
        assertEquals(0, StringUtils.getFirstBytePosition(emptyArray));
        assertEquals(0, StringUtils.getFirstBytePosition("a".getBytes()));
        assertEquals(1, StringUtils.getFirstBytePosition("ab".getBytes()));
        assertEquals(0, StringUtils.getFirstBytePosition("é".getBytes()));
        assertEquals(1, StringUtils.getFirstBytePosition("aé".getBytes()));
        assertEquals(5, StringUtils.getFirstBytePosition("aééé".getBytes()));
    }
    
    @org.junit.Test
    public void testByteComparison() {
        
        assertTrue( ByteCompareTo((byte)0xDD, (byte)0xAA) >0 );
        assertTrue( ByteCompareTo((byte)0xAA,  (byte)0xDD ) <0 );
        assertTrue( ByteCompareTo((byte)0xDD,  (byte)0xDD ) ==0 );
        
    }
}
