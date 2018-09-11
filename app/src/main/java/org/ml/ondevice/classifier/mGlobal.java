package org.ml.ondevice.classifier;

import android.app.Application;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.BufferedWriter;
import java.text.SimpleDateFormat;

/**
 * Created by haijunz on 18-1-4.
 */
public class mGlobal extends Application {
    public static String media_path = Environment.getExternalStorageDirectory() + "/ondevice_training";
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSSSSSSS");
    public static BufferedWriter sensor_all_out = null;
    public static BufferedWriter gps_out = null;
    public static BufferedWriter cell_out = null;
    public static String selflabel = "unknow";
    public static BufferedWriter Acc_out = null;
    public static BufferedWriter Gyr_out = null;
    public static BufferedWriter Mag_out = null;
    public static BufferedWriter Index_out_mt = null;
    public static String Index_out_mt_file="unknow";
    public static BufferedWriter Index_out = null;
    public static BufferedWriter lable_out = null;
    public static String data_android_id = "unknow";
    public static String label_android_id = "unknow";
    public static String android_id = "unknow";
    public static String labeVideoFileName = "unknow";
    // public static String datafrom ="unknow";
    public static boolean deviceLableRole = true;
    public static int map_index = 1;
    /**
     * 10Hz
     */
    public static int SENSOR_RATE_SLOW = 100000;
    /**
     * 50Hz
     */
    public static int SENSOR_RATE_NORMAL = 20000;
    /**
     * 80Hz
     */
    public static int SENSOR_RATE_MIDDLE = 12500;
    /**
     * 100Hz
     */
    public static int SENSOR_RATE_FAST = 10000;
    public static int Audio_souce = MediaRecorder.AudioSource.MIC;
    public static int Audio_format = AudioFormat.ENCODING_PCM_16BIT;
    public static int Audio_numChannels = 1;
    public static int Audio_sampleRate = 16000;
    public static int Audio_channelConfig = AudioFormat.CHANNEL_IN_MONO;
    public static boolean status_record = false;
}

