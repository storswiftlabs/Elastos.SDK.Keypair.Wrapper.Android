package org.elastos.sdk.keypair;

public final class ElastosKeypairFileCoin extends NativeBridge {
    private ElastosKeypairFileCoin() {}

    public static native String GetSinglePublicKey(ElastosKeypair.Data seed, int seedLen);

    public static native String GetSinglePrivateKey(ElastosKeypair.Data seed, int seedLen);

    public static native String GetPublicKeyFromPrivateKey(String privateKey);

    public static native String GetAddress(String publicKey);

    public static native boolean IsAddressValid(String address);

    public static native int Sign(String privateKey, ElastosKeypair.Data data, int len,
                                  ElastosKeypair.Data signedData);

    public static native boolean Verify(String publicKey, ElastosKeypair.Data data, int len,
                                        ElastosKeypair.Data signedData, int signedLen);

    public static native String GenerateRawTransaction(String privateKey, String transaction);

    public static native String GetGenerateSubPrivateKey(ElastosKeypair.Data seed, int seedLen, int index);

    public static native String GetGenerateSubPublicKey(ElastosKeypair.Data seed, int seedLen, int index);
}
