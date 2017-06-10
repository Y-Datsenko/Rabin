package rabin.utils;

public class PrintUtils {
    private PrintUtils(){
    }

    public static void printArray(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(Byte.toUnsignedInt(bytes[i]));

            if (hex.length() < 2) {
                builder.append('0');
            }
            builder.append(hex);

            if (i != bytes.length - 1) {
                builder.append(' ');
            }
        }

        System.out.println(builder.length() == 0 ? "[empty]" : builder.toString());
    }
}
