cmake_minimum_required(VERSION 3.4.1)

add_library( Elastos.SDK.Keypair SHARED
             src/main/cpp/Log.cpp
             src/main/cpp/JniUtils.cpp
             src/main/cpp/ElastosSdkKeypairJni.cpp )

file(GLOB Elastos.SDK.Keypair.C-Libraries "${PROJECT_SOURCE_DIR}/build/tmp/Elastos.SDK.Keypair.C/jni/${ANDROID_ABI}/*")
file(GLOB Elastos.SDK.Keypair.C-HeaderDir "${PROJECT_SOURCE_DIR}/build/tmp/Elastos.SDK.Keypair.C/jni/include")
target_include_directories( Elastos.SDK.Keypair PRIVATE "${Elastos.SDK.Keypair.C-HeaderDir}" )
target_link_libraries( Elastos.SDK.Keypair "${Elastos.SDK.Keypair.C-Libraries}" )


find_library( log-lib log )
target_link_libraries( Elastos.SDK.Keypair ${log-lib} )
