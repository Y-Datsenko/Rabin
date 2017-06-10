package rabin.key;

import java.math.BigInteger;

public class RabinPrivateKey extends RabinKey {

    RabinPrivateKey(BigInteger p, BigInteger q) {
        super(p, q);
    }

    @Override
    public String toString() {
        return "Private key " + super.toString();
    }
}