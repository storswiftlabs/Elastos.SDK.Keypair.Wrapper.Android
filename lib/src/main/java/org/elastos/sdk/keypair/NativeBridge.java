package org.elastos.sdk.keypair;

public class NativeBridge {
    static {
        System.loadLibrary("Elastos.SDK.Keypair");
    }
}
