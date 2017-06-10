package rabin.utils;

import java.math.BigInteger;

/**
 * BigInteger to/from byte array conversion utilities.
 */
public abstract class ConversionUtil {

    private ConversionUtil() {
    }

    public static byte[] packToByteArray(BigInteger first, int targetLength) {
        byte[] high = asUnsignedByteArray(first);

        byte[] result = new byte[targetLength];

        System.arraycopy(high, 0, result, targetLength - high.length, high.length);

        return result;
    }

    public static byte[] packToByteArray(BigInteger first, BigInteger second) {
        byte[] firstArray = asUnsignedByteArray(first);
        byte[] secondArray = asUnsignedByteArray(second);

        int maxLength = Integer.max(firstArray.length, secondArray.length);

        byte[] targetArray = new byte[maxLength * 2];
        System.arraycopy(firstArray, 0, targetArray, 0, firstArray.length);
        System.arraycopy(secondArray, 0, targetArray, maxLength, secondArray.length);

        return targetArray;
    }

    public static byte[] packToByteArray2(BigInteger first, BigInteger resolveInt, int targetLength) {
        byte[] high = packToByteArray(first, targetLength - 1);
        byte[] low = asUnsignedByteArray(resolveInt);

        byte[] result = new byte[targetLength];

        System.arraycopy(high, 0, result, targetLength - high.length, high.length);
        System.arraycopy(low, 0, result, 0, low.length);

        return result;
    }

    public static byte[] packToByteArray(BigInteger first, BigInteger second, int targetLength) {
        byte[] high = asUnsignedByteArray(first);
        byte[] low = asUnsignedByteArray(second);

        byte[] result = new byte[targetLength];

        System.arraycopy(high, 0, result, targetLength / 2 - high.length, high.length);
        System.arraycopy(low, 0, result, targetLength - low.length, low.length);

        return result;
    }


    public static byte[] packToByteArray(BigInteger first, byte resolveByte, int targetLength) {
        byte[] high = asUnsignedByteArray(first);
        byte[] low = new byte[]{resolveByte};

        byte[] result = new byte[targetLength];
        PrintUtils.printArray(result);
        System.arraycopy(high, 0, result, 0, high.length);
        PrintUtils.printArray(result);
        System.arraycopy(low, 0, result, targetLength - low.length, 1);
        PrintUtils.printArray(result);

        return result;
    }

    public static BigInteger[] unpackFromByteArray(byte[] array) {
        return unpackFromByteArray(array, 0, array.length);
    }

    public static BigInteger[] unpackFromByteArray(byte[] array, int offset, int length) {
        if ((length & 1) != 0) {
            throw new IllegalArgumentException("Byte sequence should be even length");
        }

        int halfLen = length / 2;

        byte[] firstArray = new byte[halfLen];
        byte[] secondArray = new byte[halfLen];

        System.arraycopy(array, offset, firstArray, 0, halfLen);
        System.arraycopy(array, offset + halfLen, secondArray, 0, halfLen);

        BigInteger first = fromUnsignedByteArray(firstArray);
        BigInteger second = fromUnsignedByteArray(secondArray);

        return new BigInteger[]{first, second};
    }

    public static BigInteger[] unpackFromByteArray2(byte[] array, int offset, int length) {
//        if ((length & 1) == 0) {
//            throw new IllegalArgumentException("Byte sequence should be even length");
//        }

        int firstSize = length - 1;

        byte[] firstArray = new byte[firstSize];
        byte[] secondArray = new byte[1];

        System.arraycopy(array, offset + 1, firstArray, 0, length - 1);
        System.arraycopy(array, offset, secondArray, 0, 1);

        BigInteger first = fromUnsignedByteArray(firstArray);
        BigInteger second = fromUnsignedByteArray(secondArray);
//        BigInteger second = new BigInteger(secondArray);

        return new BigInteger[]{first, second};
    }

    public static byte[] asUnsignedByteArray(
            BigInteger value) {
        byte[] bytes = value.toByteArray();

        if (bytes[0] == 0) {
            byte[] tmp = new byte[bytes.length - 1];

            System.arraycopy(bytes, 1, tmp, 0, tmp.length);

            return tmp;
        }

        return bytes;
    }

    public static BigInteger fromUnsignedByteArray(byte[] buf) {
        return new BigInteger(1, buf);
    }
}
