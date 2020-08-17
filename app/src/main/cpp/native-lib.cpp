#include <jni.h>
#include <string>


extern "C"
JNIEXPORT jstring JNICALL
Java_com_hao_minovel_jni_Test_stringFromJNI(JNIEnv *env, jclass thiz) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}