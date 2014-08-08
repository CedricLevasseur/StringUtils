package com.cedriclevasseur.stringutils;


import org.apache.commons.lang.ArrayUtils;

/**
 *
 * @author cedric
 */
public class StringUtils {

    public static String truncateWhenUTF8(String aString, int maxBytes) {

        if (aString.getBytes().length < maxBytes) {
            return aString;
        }
        // +1 because we always cut the last utf8-character, which is at least one byte.
        // We replace the last three byte by the ... character
        return StringUtils.truncateWhenUTF8Strictly(ArrayUtils.subarray(aString.getBytes(), 0, maxBytes - 2)).concat("\u2026");

    }

    public static String truncateWhenUTF8Strictly(String aString, int maxBytes) {
        if (aString.getBytes().length < maxBytes) {
            return aString;
        }
        return StringUtils.truncateWhenUTF8Strictly(ArrayUtils.subarray(aString.getBytes(), 0, maxBytes));

    }

    public static String truncateWhenUTF8Strictly(byte[] arrayOfBytes) {

        int length = arrayOfBytes.length;
        int firstBytePosition = getFirstBytePosition(arrayOfBytes);
        int lastCharLong;
        Byte lastByte = arrayOfBytes[firstBytePosition];
        if (ByteCompareTo(lastByte,(byte)0x80) < 0){
            lastCharLong = 1;
        }
        else if ((ByteCompareTo(lastByte,(byte)0xC2) >= 0) && (ByteCompareTo(lastByte,(byte)0xDF) < 0)){    
            lastCharLong = 2;
        }
        else if ((ByteCompareTo(lastByte,(byte)0xE0) >= 0) && (ByteCompareTo(lastByte,(byte)0xEF) < 0)){    
            lastCharLong = 3;
        }
        else if ((ByteCompareTo(lastByte,(byte)0xF0) >= 0) && (ByteCompareTo(lastByte,(byte)0xF4) < 0)){    
            lastCharLong = 4;
        } else {
            lastCharLong = 3;
        }
        if (firstBytePosition + lastCharLong <= length) {
            //exact match
            return new String(arrayOfBytes);
        }
        return new String(ArrayUtils.subarray(arrayOfBytes, 0, firstBytePosition));
    }

    /**
     * Search for the first byte of an utf8 character
     * @param arrayOfBytes
     * @return a int representing the position in the array
     */
    public static int getFirstBytePosition(byte[] arrayOfBytes) {

        if (arrayOfBytes == null) {
            return -1;
        }
        if (arrayOfBytes.length == 0) {
            return 0;
        }

        int length = arrayOfBytes.length;
        Byte lastByte = arrayOfBytes[length - 1];
        char lastC = (char) arrayOfBytes[length - 1];


        if (ByteCompareTo(lastByte,(byte)0x80) < 0){
            // last byte is a char one byte long
            return length - 1;
        }

        if (ByteCompareTo(lastByte,(byte)0xC0) >= 0){
            // last byte is a first byte of a char multi-bytes long.
            return length - 1;
        }

        return getFirstBytePosition(ArrayUtils.subarray(arrayOfBytes, 0, length - 1));

    }

    /**
     * Compare two unsigned bytes, as Byte.compareTo is unusable (signed comparison)
     * (cast the byte in char to perform comparison)
     * @param aAsByte
     * @param bAsByte
     * @return an int
     */
    public static int ByteCompareTo(byte aAsByte, byte bAsByte) {
        char aAsChar = (char)aAsByte;
        char bAsChar = (char)bAsByte;
        return aAsChar-bAsChar;
    }

}
