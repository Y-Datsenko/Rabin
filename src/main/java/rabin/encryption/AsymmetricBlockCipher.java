package rabin.encryption;

public interface AsymmetricBlockCipher {
    void init(boolean forEncryption, CipherParameters param);

    byte[] processBlock(byte[] block, int offset, int length);

    int getInputBlockSize();

    int getOutputBlockSize();
}
