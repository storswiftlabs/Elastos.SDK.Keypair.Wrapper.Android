package org.elastos.sdk.keypair;

public final class ElastosKeypairCrypto extends NativeBridge {
    private ElastosKeypairCrypto() {}

    public static native String eciesEncrypt(String publicKey, byte[] plainData);

    public static native byte[] eciesDecrypt(String privateKey, String cipherText);

}
