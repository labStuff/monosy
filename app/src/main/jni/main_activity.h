//
// Created by Administrator on 2016/4/12.
//

#ifndef AIDLAPPLICATION_SAVE_PARAMS_H
#define AIDLAPPLICATION_SAVE_PARAMS_H

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/ipc.h>
#include <string.h>
#include <sys/types.h>
#include <assert.h>
#include <math.h>
#include <sys/time.h>
#include <signal.h>
#include <termio.h>
#include <pthread.h>
#include <android/log.h>


#define ECHO_POS              3915
#define NOISE_POS             4960
#define FBUF_LEN			  6      //滤波过程缓存数据长度
#define FILTER_LEN			  7      //滤波器组系数长度
#define SPLBUF_LEN            5      //声压级缓存数据长度
#define CHANNEL_NUM           16     //通道数
#define PART_NUM              8      //I/O曲线分段数
#define pi                    3.1415926

#define LOG_TAG "monosy"
#define  LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)


// 函数声明
void cmd_parser(int argc, char *argv[]);
void usage(void);
void load_par(void);


#endif //AIDLAPPLICATION_SAVE_PARAMS_H
