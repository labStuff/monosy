//
// Created by Administrator on 2016/4/14.
//

#include "com_lixinyuyin_monosyllabicdetect_view_GraphData.h"
#include "main_activity.h"

int savefd;
char *savefile;       // 参数配置文件
//B与A分别为滤波器组系统函数的分子与分母，,需要从文件里面读取
double
        B[CHANNEL_NUM][FILTER_LEN] = {0},
        A[CHANNEL_NUM][FILTER_LEN] = {0},
//补偿I/O曲线,k与b需要从文件里面读取
        k[CHANNEL_NUM][PART_NUM] = {0},
        b[CHANNEL_NUM][PART_NUM] = {0},
//各重要输入声压级点，需要从文件里面读取
        tpi[PART_NUM + 1] = {0};
unsigned char maxout = 100;
unsigned char attacktime = 64;
unsigned char releasetime = 128;


/*
 * Class:     com_yyjn_nativeapplication_MainActivity
 * Method:    readParam
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL
Java_com_lixinyuyin_monosyllabicdetect_view_GraphData_readParam
(JNIEnv
*env,
jobject obj
) {
load_par();

}

/*
 * Class:     com_yyjn_nativeapplication_MainActivity
 * Method:    saveWdrcInit
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_com_lixinyuyin_monosyllabicdetect_view_GraphData_saveWdrcInit
(JNIEnv
*env,
jobject obj, jstring
jstr) {
jclass clsstring = env->FindClass("java/lang/String");
jstring strencode = env->NewStringUTF("utf-8");
jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
jbyteArray barr = (jbyteArray) env->CallObjectMethod(jstr, mid, strencode);
jsize alen = env->GetArrayLength(barr);
jbyte *ba = env->GetByteArrayElements(barr, JNI_FALSE);
if (alen > 0) {
savefile = (char *) malloc(alen + 1);

memcpy(savefile, ba, alen
);
savefile[alen] = 0;
}
env->
ReleaseByteArrayElements(barr, ba,
0);

savefd = open(savefile, O_RDWR);
if (savefd < 0) {
printf("open config file error");
return;
}
lseek(savefd,
0L, SEEK_SET);
write(savefd, &maxout,
1);
write(savefd, &attacktime,
1);
write(savefd, &releasetime,
1);
}

/*
 * Class:     com_yyjn_nativeapplication_MainActivity
 * Method:    saveIntVal
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_com_lixinyuyin_monosyllabicdetect_view_GraphData_saveIntVal
(JNIEnv
*env,
jobject obj, jint
value) {
int data = value;
write(savefd, &data,
4);
}

/*
 * Class:     com_yyjn_nativeapplication_MainActivity
 * Method:    saveDoubleVal
 * Signature: (D)V
 */
JNIEXPORT void JNICALL
Java_com_lixinyuyin_monosyllabicdetect_view_GraphData_saveDoubleVal
(JNIEnv
*env,
jobject obj, jdouble
value) {
double data = value;
write(savefd, &data,
8);
}


void load_par(void) {
//    savefd = open(savefile, O_RDWR);
    if (savefd < 0) {
        printf("open config file error");
        return;
    }
    lseek(savefd, 0L, SEEK_SET);
    read(savefd, &maxout, 1);
    read(savefd, &attacktime, 1);
    read(savefd, &releasetime, 1);
    read(savefd, &B, 896); // 16*7*8
    read(savefd, &A, 896); // 16*7*8
    read(savefd, &k, 1024);// 16*8*8
    read(savefd, &b, 1024);// 16*8*8
    read(savefd, &tpi, 72);// 9*8
    LOGD("filename:%s", savefile);
    LOGD("maxout:%c", maxout);
    LOGD("attacktime:%c", attacktime);
    LOGD("releasetime:%c", releasetime);
    for (int i = 0; i < CHANNEL_NUM; i++) {
        for (int j = 0; j < PART_NUM; ++j) {
            LOGD("b[][] = %f", b[i][j]);
        }
    }
}



