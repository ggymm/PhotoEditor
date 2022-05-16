#include <jni.h>
#include <string>

#include "inpainting.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_ninelock_editor_photo_jni_IiauApp_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    IIAUAPP::INPAINTING test;
    std::string out = test.inpainting("aa.png", "mask.png");
    return env->NewStringUTF(out.c_str());
}