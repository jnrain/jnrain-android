#include <stdint.h>
#include <jni.h>

#include "didadi.h"


jstring Java_org_jnrain_didadi_test_DDDTest_init(
        JNIEnv *env,
        jobject obj)
{
    char result_buf[128];

    didadi_init(result_buf, 128);

    return (*env)->NewStringUTF(env, result_buf);
}


/* vim:set ai et ts=4 sw=4 sts=4 fenc=utf-8: */
