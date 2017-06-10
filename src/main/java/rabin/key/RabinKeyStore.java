package rabin.key;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class RabinKeyStore {

    private static final Path keyPath = Paths.get("/", "home", "yevhenii", "rabin", "final", "key.bin");

    public static RabinKeyPair getOrGenerateAndSave(int bitLength) throws IOException {
        if (!Files.exists(keyPath)) {
            return generateAndSave(bitLength);
        }

        RabinKeyPair keyPair = getKeyPair();
        if (keyPair.getPublicKey().getN().bitLength() == bitLength) {
            return keyPair;
        }

        return generateAndSave(bitLength);
    }

    public static RabinKeyPair generateAndSave(int bitLength) throws IOException {
        Random random = new Random();

        RabinKeyGenerator keyGenerator = new RabinKeyGenerator(random);
        RabinKeyPair keyPair = keyGenerator.generateKeyPair(bitLength);

        serialize(keyPair);

        return keyPair;
    }

    private static void serialize(RabinKeyPair keyPair) throws IOException {
        try (OutputStream out = Files.newOutputStream(keyPath);
             ObjectOutputStream oos = new ObjectOutputStream(out)) {

            oos.writeObject(keyPair);
        }
    }

    public static RabinKeyPair getKeyPair() throws IOException {
        try (InputStream in = Files.newInputStream(keyPath);
             ObjectInputStream ois = new ObjectInputStream(in)) {

            return (RabinKeyPair) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
