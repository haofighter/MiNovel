//
// Created by mac on 2020/11/10.
//

#include <jni.h>
#include "client/linux/handler/minidump_descriptor.h"
#include "client/linux/handler/exception_handler.h"
#include <android/log.h>

#define TAG    "crash"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型

bool DumpCallback(const google_breakpad::MinidumpDescriptor &descriptor,
                  void *context,
                  bool succeeded) {
    printf("Dump path: %s\n", descriptor.path());
    return false;//true 表示异常被处理，不需要再交给其他程序处理  false 表示异常未被处理
}


extern "C"
JNIEXPORT void JNICALL
Java_com_hao_minovel_jni_DragCrash_initNativeCrash(JNIEnv *env, jclass clazz, jstring path) {

    const char *path_ = env->GetStringUTFChars(path, 0);
    google_breakpad::MinidumpDescriptor descriptor(path_);
    static google_breakpad::ExceptionHandler eh(descriptor, NULL, DumpCallback,
                                                NULL, true, -1);
    env->ReleaseStringUTFChars(path, path_);
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_hao_minovel_jni_DragCrash_getString(JNIEnv *env, jclass clazz) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


jstring fun1(JNIEnv *env, jobject jobj) {
    std::string hello = "动态注册";
    return env->NewStringUTF(hello.c_str());
}

jstring fun2(JNIEnv *env, jobject jobj) {
    std::string hello = "动态注册";
    LOGD("%s",hello.c_str());
    return env->NewStringUTF(hello.c_str());
}

static const char *mClassName = "com/hao/minovel/jni/DragCrash";
static const JNINativeMethod mMethods[] = {
        //参数一：java方法名
        //参数二：()中为调用时需要的传残参类型后面为返回类型
        //参数三：jni中对应的方法
        {"getString2", "()Ljava/lang/String;",                           (jstring *) fun1},
        {"initDate",   "(Lcom/hao/minovel/jni/Test;)Ljava/lang/String;", (void *) fun2},
};


//在调用java的load的时候 会调用此方法
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *res) {
    JNIEnv *env = NULL; //获得 JniEnv
    int r = vm->GetEnv((void **) &env, JNI_VERSION_1_4);
    if (r != JNI_OK) { return -1; }
    jclass mainActivityCls = env->FindClass(mClassName); // 注册 如果小于0则注册失败
    r = env->RegisterNatives(mainActivityCls, mMethods, 2);//第三个参数为你需要加载的mMethods中的数量
    if (r != JNI_OK) { return -1; }
    return JNI_VERSION_1_4;
}