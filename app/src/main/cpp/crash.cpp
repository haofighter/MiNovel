//
// Created by mac on 2020/11/10.
//

#include <jni.h>
#include "client/linux/handler/minidump_descriptor.h"
#include "client/linux/handler/exception_handler.h"
#include <android/log.h>

bool DumpCallback(const google_breakpad::MinidumpDescriptor& descriptor,
                  void* context,
                  bool succeeded) {
    printf("Dump path: %s\n", descriptor.path());
    return false;//true 表示异常被处理，不需要再交给其他程序处理  false 表示异常未被处理
}



extern "C"
JNIEXPORT void JNICALL
Java_com_hao_minovel_jni_DragCrash_initNativeCrash(JNIEnv *env, jclass clazz, jstring path) {

    const char* path_= env->GetStringUTFChars(path,0);
    google_breakpad::MinidumpDescriptor descriptor(path_);
    static google_breakpad::ExceptionHandler eh(descriptor, NULL, DumpCallback,
                                         NULL, true, -1);
    env->ReleaseStringUTFChars(path,path_);
}