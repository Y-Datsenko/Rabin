package rabin.key;

import java.io.Serializable;
import java.math.BigInteger;

public class RabinKey implements Serializable {

    private final BigInteger p;
    private final BigInteger q;
    private final BigInteger n;

    public RabinKey(BigInteger p, BigInteger q) {
        this.p = p;
        this.q = q;
        this.n = p.multiply(q);
    }

    public BigInteger getQ() {
        return q;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getN() {
        return n;
    }

    @Override
    public String toString() {
        return "p = " + p.toString(16) + ", q = " + q.toString(16);
    }
}
