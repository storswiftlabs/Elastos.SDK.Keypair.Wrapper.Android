#include <jni.h>
#include <string>

#include "Elastos.Wallet.Utility.h"
#include "FileCoin.hpp"

#include "ElastosSdkKeypairJni.hpp"
#include "JniUtils.hpp"
#include "Log.hpp"

void int2str(int n, char *str)
{
    char buf[10] = "";
    int i = 0;
    int len = 0;
    int temp = n < 0 ? -n: n;  // temp为n的绝对值

    if (str == NULL)
    {
        return;
    }

     if(n == 0){
        str[0]= '0';
        str[1]= 0;
        return;
    }

    while(temp)
    {
        buf[i++] = (temp % 10) + '0';  //把temp的每一位上的数存入buf
        temp = temp / 10;
    }

    len = n < 0 ? ++i: i;  //如果n是负数，则多需要一位来存储负号
    str[i] = 0;            //末尾是结束符0
    while(1)
    {
        i--;
        if (buf[len-i-1] ==0)
        {
            break;
        }
        str[i] = buf[len-i-1];  //把buf数组里的字符拷到字符串
    }
    if (i == 0 )
    {
        str[i] = '-';          //如果是负数，添加一个负号
    }
}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_elastos_sdk_keypair_ElastosKeypairFileCoin_GetSinglePrivateKey(JNIEnv *jEnv, jclass jType,
                                                                        jobject jSeed, jint jSeedLen)
{
    auto seed = GetDataBuffer(jEnv, jSeed);
    int seedLen = jSeedLen;

    char* privKey = FileCoin::GetSinglePrivateKey(seed.get(), seedLen);
    if(privKey == nullptr) {
        return nullptr;
    }

    auto jPrivateKey = JniUtils::GetStringSafely(jEnv, privKey, false);
    freeBuf(privKey);

    return jPrivateKey.get();
}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_elastos_sdk_keypair_ElastosKeypairFileCoin_GetSinglePublicKey(JNIEnv *jEnv, jclass jType,
                                                                       jobject jSeed, jint jSeedLen)
{
    auto seed = GetDataBuffer(jEnv, jSeed);
    int seedLen = jSeedLen;

    char* pubKey = FileCoin::GetSinglePublicKey(seed.get(), seedLen);
    if(pubKey == nullptr) {
        return nullptr;
    }

    auto jPublicKey = JniUtils::GetStringSafely(jEnv, pubKey, false);
    freeBuf(pubKey);

    return jPublicKey.get();
}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_elastos_sdk_keypair_ElastosKeypairFileCoin_GetPublicKeyFromPrivateKey(JNIEnv *jEnv, jclass jType,
                                                                               jstring jPrivateKey)
{
    auto privKey = JniUtils::GetStringSafely(jEnv, jPrivateKey);

    char* pubKey = FileCoin::GetPublicKeyFromPrivateKey(privKey.get());
    if(pubKey == nullptr) {
        return nullptr;
    }

    auto jPublicKey = JniUtils::GetStringSafely(jEnv, pubKey, false);
    freeBuf(pubKey);

    return jPublicKey.get();
}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_elastos_sdk_keypair_ElastosKeypairFileCoin_GetAddress(JNIEnv *jEnv, jclass jType,
                                                               jstring jPublicKey) {
    auto pubKey = JniUtils::GetStringSafely(jEnv, jPublicKey);

    char *address = FileCoin::GetAddress(pubKey.get());
    if(address == nullptr) {
        return nullptr;
    }

    auto jAddress = JniUtils::GetStringSafely(jEnv, address, false);
    freeBuf(address);

    return jAddress.get();
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_org_elastos_sdk_keypair_ElastosKeypairFileCoin_IsAddressValid(JNIEnv *jEnv, jclass jType,
                                                                   jstring jAddress)
{
    auto address = JniUtils::GetStringSafely(jEnv, jAddress);

    bool valid = FileCoin::IsAddressValid(address.get());

    return valid;
}

extern "C"
JNIEXPORT jint JNICALL
Java_org_elastos_sdk_keypair_ElastosKeypairFileCoin_Sign(JNIEnv *jEnv, jclass jType,
                                                         jstring jPrivateKey,
                                                         jobject jData, jint jLen,
                                                         jobject jSignedData)
{
    auto privateKey = JniUtils::GetStringSafely(jEnv, jPrivateKey);
    auto data = GetDataBuffer(jEnv, jData);
    int len = jLen;
    void *signedData = nullptr;

    int signedLen = FileCoin::Sign(privateKey.get(), data.get(), len, &signedData);
    if(signedLen <= 0 || signedData == nullptr) {
        return signedLen;
    }

    SetDataBuffer(jEnv, jSignedData, static_cast<int8_t *>(signedData), signedLen);
    freeBuf(signedData);

    return signedLen;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_org_elastos_sdk_keypair_ElastosKeypairFileCoin_Verify(JNIEnv *jEnv, jclass jType,
                                                           jstring jPublicKey,
                                                           jobject jData, jint jLen,
                                                           jobject jSignedData, jint jSignedLen)
{
    auto publicKey = JniUtils::GetStringSafely(jEnv, jPublicKey);
    auto data = GetDataBuffer(jEnv, jData);
    int len = jLen;
    auto signedData = GetDataBuffer(jEnv, jSignedData);
    int signedLen = jSignedLen;

    bool verified = FileCoin::Verify(publicKey.get(), data.get(), len, signedData.get(), signedLen);

    return verified;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_elastos_sdk_keypair_ElastosKeypairFileCoin_GenerateRawTransaction(JNIEnv *jEnv, jclass jType,
                                                                               jstring jPrivateKey,
                                                                               jstring jTransaction)
{
    auto privateKey = JniUtils::GetStringSafely(jEnv, jPrivateKey);
    auto transaction = JniUtils::GetStringSafely(jEnv, jTransaction);

    char* rawTx = FileCoin::GenerateRawTransaction(privateKey.get(), transaction.get());
    if(rawTx == nullptr) {
        return nullptr;
    }

    auto jRawTx = JniUtils::GetStringSafely(jEnv, rawTx, false);
    freeBuf(rawTx);

    return jRawTx.get();
}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_elastos_sdk_keypair_ElastosKeypairFileCoin_GetGenerateSubPrivateKey(JNIEnv *jEnv, jclass jType,
                                                                        jobject jSeed, jint jSeedLen, jint jIndex)
{
    auto seed = GetDataBuffer(jEnv, jSeed);
    int seedLen = jSeedLen;
    int index = jIndex;
    char address_index[64] = {0};
    int2str(index,address_index);

    char* privKey = FileCoin::GetGenerateSubPrivateKey(seed.get(), seedLen, address_index);
    if(privKey == nullptr) {
        return nullptr;
    }

    auto jPrivateKey = JniUtils::GetStringSafely(jEnv, privKey, false);
    freeBuf(privKey);

    return jPrivateKey.get();
}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_elastos_sdk_keypair_ElastosKeypairFileCoin_GetGenerateSubPublicKey(JNIEnv *jEnv, jclass jType,
                                                                       jobject jSeed, jint jSeedLen, jint jIndex)
{
    auto seed = GetDataBuffer(jEnv, jSeed);
    int seedLen = jSeedLen;
    int index = jIndex;
    char address_index[64] = {0};
    int2str(index,address_index);


    char* pubKey = FileCoin::GetGenerateSubPublicKey(seed.get(), seedLen, address_index);
    if(pubKey == nullptr) {
        return nullptr;
    }

    auto jPublicKey = JniUtils::GetStringSafely(jEnv, pubKey, false);
    freeBuf(pubKey);

    return jPublicKey.get();
}