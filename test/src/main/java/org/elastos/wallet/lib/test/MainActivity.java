package org.elastos.sdk.keypair.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.elastos.sdk.keypair.ElastosWallet;
import org.elastos.sdk.keypair.ElastosWalletDID;
import org.elastos.sdk.keypair.ElastosWalletHD;
import org.elastos.sdk.keypair.ElastosWalletSign;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        TextView txtMsg = findViewById(R.id.txt_message);

        findViewById(R.id.btn_test_mnemonic).setOnClickListener((view) -> {
            String message = testGenrateMnemonic();
            txtMsg.setText(message);
        });
        findViewById(R.id.btn_test_hdwallet).setOnClickListener((view) -> {
            String message = testHDWalletAddress();
            txtMsg.setText(message);
        });
        findViewById(R.id.btn_test_did).setOnClickListener((view) -> {
            String message = testDid();
            txtMsg.setText(message);
        });
        findViewById(R.id.btn_test_sign).setOnClickListener((view) -> {
            String message = testSignTxData();
            message += testCosignTxData();
            txtMsg.setText(message);
        });
    }

    private String testGenrateMnemonic() {
        String message = "";

        String language = "english";
        String words = "";

        String mnemonic = ElastosWallet.generateMnemonic(language, words);
        if (mnemonic == null) {
            String errmsg = "Failed to generate mnemonic.";
            Log.e(TAG, errmsg);
            message += errmsg;
            return message;
        }
        message += "mnemonic: " + mnemonic + "\n";

        mSeed = new ElastosWallet.Data();
        int ret = ElastosWallet.getSeedFromMnemonic(mSeed, mnemonic, language, words, "0");
        if (ret <= 0) {
            String errmsg = "Failed to get seed from mnemonic. ret=" + ret + "\n";
            Log.e(TAG, errmsg);
            message += errmsg;
            return message;
        }
        mSeedLen = ret;
        message += "seed: " + mSeed.buf + ", len: " + mSeedLen + "\n";

        String privateKey = ElastosWallet.getSinglePrivateKey(mSeed, mSeedLen);
        if (privateKey == null) {
            String errmsg = "Failed to generate privateKey.\n";
            Log.e(TAG, errmsg);
            message += errmsg;

            return message;
        }
        message += "privateKey: " + privateKey + "\n";

        String publicKey = ElastosWallet.getSinglePublicKey(mSeed, mSeedLen);
        if (publicKey == null) {
            String errmsg = "Failed to generate publicKey.\n";
            Log.e(TAG, errmsg);
            message += errmsg;

            return message;
        }
        message += "publicKey: " + publicKey + "\n";

        String address = ElastosWallet.getAddress(publicKey);
        if (address == null) {
            String errmsg = "Failed to get address.\n";
            Log.e(TAG, errmsg);
            message += errmsg;

            return message;
        }
        message += "address: " + address + "\n";


        ElastosWallet.Data data = new ElastosWallet.Data();
        data.buf = new byte[]{0, 1, 2, 3, 4, 5};
        ElastosWallet.Data signedData = new ElastosWallet.Data();
        int signedLen = ElastosWallet.sign(privateKey, data, data.buf.length, signedData);
        if (signedLen <= 0) {
            String errmsg = "Failed to sign data.\n";
            Log.e(TAG, errmsg);
            message += errmsg;

            return message;
        }

        boolean verified = ElastosWallet.verify(publicKey, data, data.buf.length, signedData, signedLen);
        message += "verified: " + verified + "\n";

        message += "================================================\n";
        return message;
    }

    private String testHDWalletAddress() {
        String message = "";

        ElastosWallet.Data masterPublicKey = ElastosWalletHD.getMasterPublicKey(mSeed, mSeedLen, ElastosWalletHD.COIN_TYPE_ELA);
        if(masterPublicKey == null) {
            String errmsg = "Failed to generate master publicKey.\n";
            Log.e(TAG, errmsg);
            message += errmsg;

            return message;
        }
        message += "masterPublicKey: " + masterPublicKey.buf + "\n";

        int count = 10;
        String[] privateKeys = new String[count];
        String[] publicKeys = new String[count];
        String[] addresses = new String[count];
        for (int idx = 0; idx < count; idx++) {
            privateKeys[idx] = ElastosWalletHD.generateSubPrivateKey(mSeed, mSeedLen, ElastosWalletHD.COIN_TYPE_ELA, ElastosWalletHD.INTERNAL_CHAIN, idx);
            publicKeys[idx] = ElastosWalletHD.generateSubPublicKey(masterPublicKey, ElastosWalletHD.INTERNAL_CHAIN, idx);
            addresses[idx] = ElastosWallet.getAddress(publicKeys[idx]);

            message += "addresses[" + idx + "]: " + addresses[idx] + "\n";
        }

        message += "================================================\n";
        return message;
    }

    private String testDid() {
        String message = "";

        ElastosWallet.Data idChainMasterPublicKey = ElastosWalletDID.getIdChainMasterPublicKey(mSeed, mSeedLen);
        if(idChainMasterPublicKey == null) {
            String errmsg = "Failed to generate id chain master publicKey.\n";
            Log.e(TAG, errmsg);
            message += errmsg;

            return message;
        }
        message += "idChainMasterPublicKey: " + idChainMasterPublicKey.buf + "\n";

        int count = 10;
        String[] privateKeys = new String[count];
        String[] publicKeys = new String[count];
        String[] dids = new String[count];
        for (int idx = 0; idx < count; idx++) {
            privateKeys[idx] = ElastosWalletDID.generateIdChainSubPrivateKey(mSeed, mSeedLen, 0, idx);
            publicKeys[idx] = ElastosWalletDID.generateIdChainSubPublicKey(idChainMasterPublicKey, 0, idx);
            dids[idx] = ElastosWalletDID.getDid(publicKeys[idx]);

            message += "dids[" + idx + "]: " + dids[idx] + "\n";
        }

        message += "================================================\n";
        return message;
    }

    private String testSignTxData() {
        String message = "";

        String transaction = "{\"Transactions\":[{\"UTXOInputs\":[{"
                + "\"txid\":\"f176d04e5980828770acadcfc3e2d471885ab7358cd7d03f4f61a9cd0c593d54\","
                + "\"privateKey\":\"b6f010250b6430b2dd0650c42f243d5445f2044a9c2b6975150d8b0608c33bae\","
                + "\"index\":0,\"address\":\"EeniFrrhuFgQXRrQXsiM1V4Amdsk4vfkVc\"}],"
                + "\"Outputs\":[{\"address\":\"EbxU18T3M9ufnrkRY7NLt6sKyckDW4VAsA\","
                + "\"amount\":2000000}]}]}";
        String signedData = ElastosWalletSign.generateRawTransaction(transaction);
        if(signedData == null) {
            String errmsg = "Failed to generate raw transaction.\n";
            Log.e(TAG, errmsg);
            message += errmsg;

            return message;
        }
        message += signedData + "\n";

        message += "================================================\n";
        return message;
    }

    private String testCosignTxData() {
        String message = "";

        String data = "{\"Transactions\":[{\"UTXOInputs\":[{"
                + "\"txid\":\"c20d577997a6036683e1a88925eaa4c2e4ca2f34db95a3fe85ad3787da017bec\","
                + "\"index\":0,\"address\":\"8NJ7dbKsG2NRiBqdhY6LyKMiWp166cFBiG\"}],"
                + "\"Outputs\":[{\"address\":\"EbxU18T3M9ufnrkRY7NLt6sKyckDW4VAsA\","
                + "\"amount\":2000000}]}]}";

        String[] publicKeys = new String[] {
                "02bc11aa5c35acda6f6f219b94742dd9a93c1d11c579f98f7e3da05ad910a48306",
                "031a9d45859da69dbc444723048932b8f56bb9937c5260238b4821a3b1ccfd78b6",
                "02746aa551414e16921a3249ddd5e49923299c97102c7e7c5b9c6e81dd3949556d"
        };

        String privKey1 = "543c241f89bebb660157bcd12d7ab67cf69f3158240a808b22eb98447bad205d";
        String privKey2 = "fe7bb62ad9bed0a572bd9428574eba8d038b68ea3004d37eb7bcf3f297a2c48f";
        String privKey3 = "404a282fec850e7b880ad65f40ffd0bdddc50d8cf3217ca65d30f5378d377991";

        String address = ElastosWalletSign.getMultiSignAddress(publicKeys, 3, 2);
        if(address == null) {
            String errmsg = "Failed to get multi sign address.\n";
            Log.e(TAG, errmsg);
            message += errmsg;

            return message;
        }
        message += "MultiSignAddress: " + address + "\n";

        String signedData1 = ElastosWalletSign.multiSignTransaction(privKey2, publicKeys, 3, 2, data);
        message += "signedData1: " + signedData1 + "\n";

        String signedData2 = ElastosWalletSign.multiSignTransaction(privKey3, publicKeys, 3, 2, signedData1);
        message += "signedData2: " + signedData2 + "\n";

        String serialize = ElastosWalletSign.serializeMultiSignTransaction(signedData2);
        message += "serialize: " + serialize + "\n";

        message += "================================================\n";
        return message;
    }


    private ElastosWallet.Data mSeed;
    private int mSeedLen;
}
