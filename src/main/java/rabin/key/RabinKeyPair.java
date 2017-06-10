package rabin.key;

import java.io.Serializable;

public class RabinKeyPair implements Serializable {

    private final RabinPublicKey publicKey;
    private final RabinPrivateKey privateKey;

    RabinKeyPair(RabinPublicKey publicKey, RabinPrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public RabinPublicKey getPublicKey() {
        return publicKey;
    }

    public RabinPrivateKey getPrivateKey() {
        return privateKey;
    }

    @Override
    public String toString() {
        return "Public: " + publicKey.toString() + ", Private: " + privateKey.toString();
    }
}
