cmake_minimum_required(VERSION 3.4.1)

add_library( Elastos.ORG.Wallet.Lib SHARED
             src/main/cpp/Log.cpp
             src/main/cpp/JniUtils.cpp
             src/main/cpp/ElastosWalletLibJni.cpp )

file(GLOB Elastos.ORG.Wallet.Lib.C-Libraries "${PROJECT_SOURCE_DIR}/build/tmp/Elastos.ORG.Wallet.Lib.C/jni/${ANDROID_ABI}/*")
file(GLOB Elastos.ORG.Wallet.Lib.C-HeaderDir "${PROJECT_SOURCE_DIR}/build/tmp/Elastos.ORG.Wallet.Lib.C/jni/include")
target_include_directories( Elastos.ORG.Wallet.Lib PRIVATE "${Elastos.ORG.Wallet.Lib.C-HeaderDir}" )
target_link_libraries( Elastos.ORG.Wallet.Lib "${Elastos.ORG.Wallet.Lib.C-Libraries}" )


find_library( log-lib log )
target_link_libraries( Elastos.ORG.Wallet.Lib ${log-lib} )
