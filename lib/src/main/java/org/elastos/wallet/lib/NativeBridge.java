package org.elastos.wallet.lib;

public class NativeBridge {
    static {
        System.loadLibrary("Elastos.ORG.Wallet.Lib");
    }
}
