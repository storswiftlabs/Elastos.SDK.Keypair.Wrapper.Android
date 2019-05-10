package org.elastos.sdk.keypair;

public final class ElastosKeypairCrypto extends NativeBridge {
    private ElastosKeypairCrypto() {}

    public static native String eciesEncrypt(String publicKey, String plainText);

    public static native String eciesDecrypt(String privateKey, String cipherText);

}
