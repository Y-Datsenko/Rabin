package rabin.encryption;

import rabin.key.RabinKey;

public class RabinCipherParams implements CipherParameters {
    private final RabinKey key;

    public RabinCipherParams(RabinKey key) {
        this.key = key;
    }

    public RabinKey getKey() {
        return key;
    }
}
