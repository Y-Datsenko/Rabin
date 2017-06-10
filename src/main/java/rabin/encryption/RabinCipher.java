package rabin.encryption;

import rabin.key.RabinKey;
import rabin.key.RabinPrivateKey;
import rabin.key.RabinPublicKey;
import rabin.utils.ConversionUtil;
import rabin.utils.EncryptionUtils;
import rabin.utils.Jacobi;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class RabinCipher implements AsymmetricBlockCipher {

    private final Random random;

    private RabinKey key;
    private int bitSize;
    private boolean forEncryption;

    public RabinCipher() {
        this(new Random());
    }

    public RabinCipher(Random random) {
        this.random = random;
    }

    @Override
    public void init(boolean forEncryption, CipherParameters param) {
        RabinCipherParams egParams = (RabinCipherParams) param;

        this.key = egParams.getKey();
        this.bitSize = this.key.getN().bitLength();
        this.forEncryption = forEncryption;

        if (this.forEncryption && !(this.key instanceof RabinPublicKey)) {
            throw new IllegalArgumentException("Key type doesn't match working mode");
        }
    }

    @Override
    public byte[] processBlock(byte[] block, int offset, int length) {
        EncryptionUtils.checkInputLengthLTE(getInputBlockSize(), length);

        if (forEncryption) {
            return encrypt(block, offset, length);
        } else {
            return decrypt(block, offset, length);
        }
    }

    private byte[] encrypt(byte[] block, int offset, int length) {
        if (length > getInputBlockSize()) {
            throw new IllegalArgumentException("Input block too large for current Rabin parameters");
        }

        byte[] input = copyIfNecessary(block, offset, length);

        BigInteger m = ConversionUtil.fromUnsignedByteArray(input);
        System.out.println("1m = " + m);
        BigInteger cipherText = encrypt((RabinPublicKey) key, m);
        BigInteger resolveInt = BigInteger.valueOf(getFinalInt(getBs(m, key.getN())));

//        return asUnsignedByteArray(cipherText);
        return ConversionUtil.packToByteArray2(cipherText, resolveInt, getOutputBlockSize());
    }

    private BigInteger encrypt(RabinPublicKey key, BigInteger m) {

        BigInteger n = key.getN();

        // c = c^2 mod n
        BigInteger c = m.pow(2).mod(n);
        return c;
    }

    private byte[] decrypt(byte[] block, int offset, int length) {
        if (length > getInputBlockSize()) {
            throw new IllegalArgumentException("Input block too large for current Rabin parameters");
        }

        BigInteger[] c = ConversionUtil.unpackFromByteArray2(block, offset, length);
        BigInteger plainText = decrypt((RabinPrivateKey) key, c);

        return ConversionUtil.packToByteArray(plainText, getOutputBlockSize());
    }

    private BigInteger decrypt(RabinPrivateKey key, BigInteger[] arr) {
        BigInteger p = key.getP();
        BigInteger q = key.getQ();

        BigInteger c = arr[0];
        BigInteger bs = arr[1];

        BigInteger[] answers = decrypt(c, p, q);

        for (BigInteger temp : answers) {
            if (bs.intValue() == getFinalInt(getBs(temp, key.getN()))) {
                return temp;
            }
        }

        return answers[0];
    }

    public static BigInteger[] decrypt(BigInteger c, BigInteger p, BigInteger q) {
        BigInteger N = p.multiply(q);
        BigInteger m_p1 = c.modPow(p.add(BigInteger.ONE).divide(BigInteger.valueOf(4)), p);
        BigInteger m_p2 = p.subtract(m_p1);
        BigInteger m_q1 = c.modPow(q.add(BigInteger.ONE).divide(BigInteger.valueOf(4)), q);
        BigInteger m_q2 = q.subtract(m_q1);

        BigInteger[] ext = ext_gcd(p, q);
        BigInteger y_p = ext[1];
        BigInteger y_q = ext[2];

        //y_p*p*m_q + y_q*q*m_p (mod n)
        BigInteger d1 = y_p.multiply(p).multiply(m_q1).add(y_q.multiply(q).multiply(m_p1)).mod(N);
        BigInteger d2 = y_p.multiply(p).multiply(m_q2).add(y_q.multiply(q).multiply(m_p1)).mod(N);
        BigInteger d3 = y_p.multiply(p).multiply(m_q1).add(y_q.multiply(q).multiply(m_p2)).mod(N);
        BigInteger d4 = y_p.multiply(p).multiply(m_q2).add(y_q.multiply(q).multiply(m_p2)).mod(N);

        System.out.println("d1 = " + d1);
        System.out.println("d2 = " + d2);
        System.out.println("d3 = " + d3);
        System.out.println("d4 = " + d4);

        return new BigInteger[]{d1, d2, d3, d4};
    }

    public static BigInteger[] ext_gcd(BigInteger a, BigInteger b) {
        BigInteger s = BigInteger.ZERO;
        BigInteger old_s = BigInteger.ONE;
        BigInteger t = BigInteger.ONE;
        BigInteger old_t = BigInteger.ZERO;
        BigInteger r = b;
        BigInteger old_r = a;
        while (!r.equals(BigInteger.ZERO)) {
            BigInteger q = old_r.divide(r);
            BigInteger tr = r;
            r = old_r.subtract(q.multiply(r));
            old_r = tr;

            BigInteger ts = s;
            s = old_s.subtract(q.multiply(s));
            old_s = ts;

            BigInteger tt = t;
            t = old_t.subtract(q.multiply(t));
            old_t = tt;
        }
        //gcd, x,y
        //x,y such that ax+by=gcd(a,b)
        return new BigInteger[]{old_r, old_s, old_t};
    }

    /**
     * b1 - 0 bit
     * b2 - 1 bit
     *
     * @param bs
     * @return
     */
    public static int getFinalInt(int[] bs) {
        int result = (1 * bs[0] + 2 * bs[1]);
        switch (result) {
            case -2:
                return 1;
            case -1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
        }
        return 0;
    }

    public static int[] getBs(BigInteger m, BigInteger n) {
        int b1 = m.remainder(BigInteger.valueOf(2)).intValue();
        int b2 = Jacobi.jacobi(m, n);
        return new int[]{b1, b2};
    }

    @Override
    public int getInputBlockSize() {
        if (forEncryption) {
            return (bitSize - 1) / 8 -1;
//            return (bitSize - 1) / 8 - 2;
        }

//        return (bitSize - 1) / 8;
//        return ((bitSize + 7) / 8);
        return ((bitSize + 7) / 8);
    }

    @Override
    public int getOutputBlockSize() {
        if (forEncryption) {
//            return (bitSize - 1) / 8;
//            return ((bitSize + 7) / 8);
            return ((bitSize + 7) / 8);
        }

//        return (bitSize - 1) / 8 - 2;
        return (bitSize - 1) / 8 -1;
    }

    private static byte[] copyIfNecessary(byte[] block, int offset, int length) {
        if (offset == 0 && length == block.length) {
            return block;
        }

        return Arrays.copyOfRange(block, offset, offset + length);
    }
}
