#include <jni.h>

static const char* JavaClassName_ElastosKeypair_Data = "org/elastos/sdk/keypair/ElastosKeypair$Data";

jobject NewData(JNIEnv *jEnv);

void SetDataBuffer(JNIEnv *jEnv, jobject jData, int8_t *buf, int size);

std::shared_ptr<int8_t> GetDataBuffer(JNIEnv *jEnv, jobject data);

