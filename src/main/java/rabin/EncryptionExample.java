package rabin;

import rabin.encryption.Cipher;
import rabin.encryption.RabinCipher;
import rabin.encryption.RabinCipherParams;
import rabin.key.RabinKeyPair;
import rabin.key.RabinKeyStore;
import rabin.utils.PrintUtils;

import java.io.IOException;

public class EncryptionExample {
    public static void main(String[] args) throws IOException {
        RabinKeyPair keyPair = RabinKeyStore.getOrGenerateAndSave(257);
        Cipher cipher = new Cipher(new RabinCipher());

        byte[] block = {
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
                17, 18, 19, 20, 21, 22, 23, 24/*, 25, 26, 27, 28, 29, 30/*, 31, 32/*,
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16/*,
                17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32*/
        };

        PrintUtils.printArray(block);
        System.out.println();


        cipher.init(true, new RabinCipherParams(keyPair.getPublicKey()));

        byte[] e1 = cipher.update(block);
        byte[] e2 = cipher.doFinal();

        PrintUtils.printArray(e1);
        PrintUtils.printArray(e2);
        System.out.println();


        cipher.init(false, new RabinCipherParams(keyPair.getPrivateKey()));

        byte[] d1 = cipher.update(e1);
        byte[] d2 = cipher.update(e2);
        byte[] d3 = cipher.doFinal();

        PrintUtils.printArray(d1);
        PrintUtils.printArray(d2);
        PrintUtils.printArray(d3);
    }
}
