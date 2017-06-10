package rabin.key;

import java.math.BigInteger;

public class RabinPublicKey extends RabinKey {
    private final BigInteger n;

    RabinPublicKey(BigInteger p, BigInteger q) {
        super(p, q);
        n = p.multiply(q);
    }

    public BigInteger getN() {
        return n;
    }

    @Override
    public String toString() {
        return super.toString() + ", n = " + n.toString(16);
    }
}
