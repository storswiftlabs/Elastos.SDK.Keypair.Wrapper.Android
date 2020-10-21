cmake_minimum_required(VERSION 3.4.1)

add_library( Elastos.SDK.Keypair SHARED
             Log.cpp
             JniUtils.cpp
             ElastosSdkKeypairJni.cpp
             ElastosSdkKeypairFileCoinJni.cpp )

file(GLOB Elastos.SDK.Keypair.C-Libraries "${CFG_ELASTOS_SDK_KEYPAIR_C_DIR}/jni/${ANDROID_ABI}/*")
file(GLOB Elastos.SDK.Keypair.C-HeaderDir "${CFG_ELASTOS_SDK_KEYPAIR_C_DIR}/jni/include")
target_include_directories( Elastos.SDK.Keypair PRIVATE "${Elastos.SDK.Keypair.C-HeaderDir}" )
target_link_libraries( Elastos.SDK.Keypair "${Elastos.SDK.Keypair.C-Libraries}" )

find_library( log-lib log )
target_link_libraries( Elastos.SDK.Keypair ${log-lib} )
