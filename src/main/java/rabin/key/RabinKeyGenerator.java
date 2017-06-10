package rabin.key;

import java.math.BigInteger;
import java.util.Random;

public class RabinKeyGenerator {

    private static final BigInteger THREE = new BigInteger("3");
    private static final BigInteger FOUR = new BigInteger("4");

    private final Random random;

    public RabinKeyGenerator() {
        this(new Random());
    }

    public RabinKeyGenerator(Random random) {
        this.random = random;
    }

    public RabinKeyPair generateKeyPair(int bitLength) {
        BigInteger[] safePrimes = generateSafePrimes(bitLength, this.random);

        BigInteger p = safePrimes[0];
        BigInteger q = safePrimes[1];

        RabinPublicKey publicKey = new RabinPublicKey(p, q);
        RabinPrivateKey privateKey = new RabinPrivateKey(p, q);

        return new RabinKeyPair(publicKey, privateKey);
    }


    /*
     * Finds a pair of prime BigInteger's {p, q: p = q = 3 mod 4}
     */
    public static BigInteger[] generateSafePrimes(int size, Random random) {
        BigInteger p, q;

        do {
            int half = size / 2;
            p = blumPrime(half, random);
            q = blumPrime(size - half, random);
        } while (p.equals(q));

        return new BigInteger[]{p, q};
    }

    /**
     * Generate a random blum prime ( a prime such that p≡3 (mod 4) )
     *
     * @param bitLength number of bits in the prime
     * @return a random blum prime
     */
    public static BigInteger blumPrime(int bitLength, Random random) {
        BigInteger p;
        do {
            p = BigInteger.probablePrime(bitLength, random);
        }
        while (!p.mod(FOUR).equals(THREE));
        return p;
    }

}
