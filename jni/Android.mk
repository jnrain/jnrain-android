ROOT := $(call my-dir)


#include $(CLEAR_VARS)
#
#LOCAL_PATH      := $(ROOT)/didadi
#LOCAL_MODULE    := didadi
#LOCAL_CFLAGS    := -O2 -pipe -I$(LOCAL_PATH)/include
#LOCAL_SRC_FILES := init.c
#
#include $(BUILD_SHARED_LIBRARY)


include $(CLEAR_VARS)

LOCAL_PATH             := $(ROOT)/dddjni
LOCAL_MODULE           := jnrain-android
#LOCAL_SHARED_LIBRARIES := didadi
LOCAL_CFLAGS           := -O2 -pipe -I$(ROOT)/didadi/include
LOCAL_SRC_FILES        := \
	../didadi/init.c \
	jniwrappers.c

include $(BUILD_SHARED_LIBRARY)


# vim:set ai ts=8 sw=8 sts=8 fenc=utf-8:
