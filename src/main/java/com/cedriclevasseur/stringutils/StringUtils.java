package com.cedriclevasseur.stringutils;

import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author cedric
 */
public class StringUtils {

    public static String truncateWhenUTF8fromInternet(String aString, int maxBytes) {
        int sizeOfCurrentStringInBytes = 0;
        for (int i = 0; i < aString.length(); i++) {
            char currentChar = aString.charAt(i);

            // ranges from http://en.wikipedia.org/wiki/UTF-8
            int skip = 0;
            int sizeOfCurrentCharInBytes;
            if (currentChar <= 0x007f) {
                sizeOfCurrentCharInBytes = 1;
            } else if (currentChar <= 0x07FF) {
                sizeOfCurrentCharInBytes = 2;
            } else if (currentChar <= 0xd7ff) {
                sizeOfCurrentCharInBytes = 3;
            } else if (currentChar <= 0xDFFF) {
                // surrogate area, consume next char as well
                sizeOfCurrentCharInBytes = 4;
                skip = 1;
            } else {
                sizeOfCurrentCharInBytes = 3;
            }

            if (sizeOfCurrentStringInBytes + sizeOfCurrentCharInBytes > maxBytes) {
                return aString.substring(0, i);
            }
            sizeOfCurrentStringInBytes += sizeOfCurrentCharInBytes;
            i += skip;
        }
        return aString;
    }

    public static String truncateWhenUTF8(String aString, int maxBytes) {

        if (aString.getBytes().length < maxBytes) {
            return aString;
        }

        // +1 because we always cut the last utf8-character, which is at least one byte.
        // We replace the last three byte by the ... character
        return StringUtils.truncateWhenUTF8(ArrayUtils.subarray(aString.getBytes(), 0, maxBytes - 2), maxBytes - 2).concat("\u2026");

    }

    public static String truncateWhenUTF8(byte[] arrayOfBytes, int maxBytes) {

        byte last = arrayOfBytes[maxBytes - 1];
        Byte lastB = last;
        char lastC = (char) last;

        //if (lastB.compareTo((byte) 0x0080) < 0) {
        if (lastC < 0x0080) {
            // last byte is a char one byte long
            return new String(arrayOfBytes);
        }

        //if ((lastB.compareTo((byte) 0x0080) >= 0) && (lastB.compareTo((byte) 0x00C0) < 0)) {
        if ((lastC > 0x0080 && lastC < 0x00C0)) {
            //last byte is the middle of a char multi-bytes long
            return StringUtils.truncateWhenUTF8(ArrayUtils.subarray(arrayOfBytes, 0, maxBytes - 1), maxBytes - 1);
        }

        //if (lastB.compareTo((byte) 0xC0) >= 0) {
        if (lastC >= 0xC0) {
            // last byte is a first byte of a char multi-bytes long.
            return new String(ArrayUtils.subarray(arrayOfBytes, 0, maxBytes - 1));
        }
        // unreachable code.
        return null;
    }

    public static String truncateWhenUTF8Strictly(String aString, int maxBytes) {
        if (aString.getBytes().length < maxBytes) {
            return aString;
        }

        // +1 because we always cut the last utf8-character, which is at least one byte.
        // We replace the last three byte by the ... charactere
        return StringUtils.truncateWhenUTF8Strictly(ArrayUtils.subarray(aString.getBytes(), 0, maxBytes));

    }

    public static String truncateWhenUTF8Strictly(byte[] arrayOfBytes) {

        int length = arrayOfBytes.length;

//        if(getFirstBytePosition(arrayOfBytes)==length-1){
//            return new String(ArrayUtils.subarray(arrayOfBytes, 0, length ));
//        }
        int firstBytePosition = getFirstBytePosition(arrayOfBytes);

        //int numberOfByteToTruncated = length - firstBytePosition;
        int lastCharLong = 0;

        Byte lastByte = arrayOfBytes[firstBytePosition];
        char lastC = (char) arrayOfBytes[firstBytePosition];

        //if (lastByte.compareTo((byte) 0x0080) <= 0) {
        //if (lastC < 0x0080) {
        if (ByteCompareTo(lastByte,(byte)0x80) < 0){
            lastCharLong = 1;
        } //else if (lastByte.compareTo((byte) 0x07FF) <=0 ){
        //else if ((lastC > 0x0080 && lastC < 0x00C0)) {
        else if ((ByteCompareTo(lastByte,(byte)0xC2) >= 0) && (ByteCompareTo(lastByte,(byte)0xDF) < 0)){    
            lastCharLong = 2;
        }
        //else if (lastByte.compareTo((byte) 0xd7ff) <=0 ){
        //if ((lastC > 0x0080 && lastC < 0x00C0)) {
        else if ((ByteCompareTo(lastByte,(byte)0xE0) >= 0) && (ByteCompareTo(lastByte,(byte)0xEF) < 0)){    
            lastCharLong = 3;
        }
        //else if (lastByte.compareTo((byte) 0xDFFF) <=0 ){
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

        //if (lastByte.compareTo((byte) 0x0080) < 0) {
        //if ((lastC & 0x0079) == 0x0079) {
        if (ByteCompareTo(lastByte,(byte)0x80) < 0){
            // last byte is a char one byte long
            return length - 1;
        }

        //if (lastByte.compareTo((byte) 0xC0) >= 0) {
        //if (lastC >= 0xC0){
        //if ((lastC & 0xB9) == 0xB9) {
        if (ByteCompareTo(lastByte,(byte)0xC0) >= 0){
            // last byte is a first byte of a char multi-bytes long.
            return length - 1;
        }

        return getFirstBytePosition(ArrayUtils.subarray(arrayOfBytes, 0, length - 1));

    }

    public static int ByteCompareTo(byte ba, byte bb) {
        char a = (char)ba;
        char b = (char)bb;
        return a-b;
    }

    public static String cut(String s, int charLimit) {
        byte[] utf8 = s.getBytes();
        if (utf8.length <= charLimit) {
            return s;
        }
        int n16 = 0;
        boolean extraLong = false;
        int i = 0;
        while (i < charLimit) {
            // Unicode characters above U+FFFF need 2 words in utf16
            extraLong = ((utf8[i] & 0xF0) == 0xF0);
            if ((utf8[i] & 0x80) == 0) {
                i += 1;
            } else {
                int b = utf8[i];
                while ((b & 0x80) > 0) {
                    ++i;
                    b = b << 1;
                }
            }
            if (i <= charLimit) {
                n16 += (extraLong) ? 2 : 1;
            }
        }
        return s.substring(0, n16);
    }
}
