package org.ml.ondevice.classifier;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioRecord;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;
//public class RuntimeShowDataActivity extends AppCompatActivity implements SensorEventListener {

public class RuntimeshowActivity extends Activity implements SensorEventListener, LocationListener {
    private static final int SampleNum = 20;
    private static final float ALPHA = 0.8f;
    private static final int AccGraphDisplaySampleMax = 100;
    private static final int AccMaxValue = 10;
    private static final int AccMinValue = 0;
    private static final int GyrGraphDisplaySampleMax = 100;
    private static final int GyrMaxValue = 10;
    private static final int GyrMinValue = 0;
    private static final int MagGraphDisplaySampleMax = 100;
    private static final int MagMaxValue = 100;
    private static final int MagMinValue = 20;
    private static final int GpsGraphDisplaySampleMax = 100;
    private static final int GpsMaxValue = 10;
    private static final int GpsMinValue = 0;
    private static final int AudioGraphDisplaySampleMax = 100;
    private static final int AudioMaxValue = 5000;
    private static final int AudioMinValue = 0;
    private static final int AudioGraphDisplaySampleMax_Cross = 100;
    private static final int AudioMaxValue_Cross = 8000;
    private static final int AudioMinValue_Cross = 0;
    private static LocationManager mLocationManager = null;
    private final Handler mHandler = new Handler();
    Random mRand = new Random();
    private SensorManager mSensorManager = null;
    private Runnable mTimer1;
    private LineGraphSeries<DataPoint> mAccData;
    private double Accgraph2LastValue = 5d;
    private LineGraphSeries<DataPoint> mGyrData;
    private double Grygraph2LastValue = 5d;
    private LineGraphSeries<DataPoint> mMagData;
    private double Maggraph2LastValue = 5d;
    private LineGraphSeries<DataPoint> mGpsData;
    private double Gpsgraph2LastValue = 5d;
    private AudioRecord mAudioRecord;
    private Thread recordingThread;
    private LineGraphSeries<DataPoint> mAudioData;
    private double Audiograph2LastValue = 5d;
    private LineGraphSeries<DataPoint> mAudioData_Cross;
    private double Audiograph2LastValue_Cross = 5d;
    //private SensorManager mSensorManager = null;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private long startTime = SystemClock.uptimeMillis();

    /*double[] xArray = new double[SampleNum];
    double[] yArray = new double[SampleNum];
    double[] zArray = new double[SampleNum];
    double[] tsArray = new double[SampleNum];*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get an instance of the PowerManager
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        // Create a bright wake lock
        //   mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass().getName());
        // instantiate our simulation view and set it as the activity's content
        // Get an instance of the SensorManager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> mSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        Sensor sensor;
        for (int index = 0; index < mSensors.size(); ++index) {
            sensor = mSensors.get(index);
            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        /*
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        */
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        /*try {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        if (mLocationManager.getProvider("gps") != null) {
            mLocationManager.requestLocationUpdates("gps",
                    0, 0, this);
        }
        if (mLocationManager.getProvider("network") != null) {
            mLocationManager.requestLocationUpdates("network",
                    0, 0, this);
        }
        setContentView(R.layout.activity_runtime_show_data);
        GraphView graph1 = (GraphView) findViewById(R.id.graph1);
        mAccData = new LineGraphSeries<>();
        // legend
        mAccData.setTitle("Ready");
        graph1.getLegendRenderer().setVisible(true);
        //graph1.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph1.addSeries(mAccData);
        graph1.getViewport().setXAxisBoundsManual(true);
        graph1.getViewport().setYAxisBoundsManual(true);
        graph1.getViewport().setMinY(AccMinValue);
        graph1.getViewport().setMaxY(AccMaxValue);
        graph1.getViewport().setMinX(0);
        graph1.getViewport().setMaxX(AccGraphDisplaySampleMax);
        GraphView graph2 = (GraphView) findViewById(R.id.graph2);
        mGyrData = new LineGraphSeries<>();
        // legend
        mGyrData.setTitle("Ready");
        graph2.getLegendRenderer().setVisible(true);
        //graph2.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph2.addSeries(mGyrData);
        graph2.getViewport().setXAxisBoundsManual(true);
        graph2.getViewport().setYAxisBoundsManual(true);
        graph2.getViewport().setMinY(GyrMinValue);
        graph2.getViewport().setMaxY(GyrMaxValue);
        graph2.getViewport().setMinX(0);
        graph2.getViewport().setMaxX(GyrGraphDisplaySampleMax);
        GraphView graph3 = (GraphView) findViewById(R.id.graph3);
        mMagData = new LineGraphSeries<>();
        // legend
        mMagData.setTitle("Ready)");
        graph3.getLegendRenderer().setVisible(true);
        //graph2.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph3.addSeries(mMagData);
        graph3.getViewport().setXAxisBoundsManual(true);
        graph3.getViewport().setYAxisBoundsManual(true);
        graph3.getViewport().setMinY(MagMinValue);
        graph3.getViewport().setMaxY(MagMaxValue);
        graph3.getViewport().setMinX(0);
        graph3.getViewport().setMaxX(MagGraphDisplaySampleMax);
        GraphView graph4 = (GraphView) findViewById(R.id.graph4);
        mGpsData = new LineGraphSeries<>();
        // legend
        mGpsData.setTitle("Ready");
        graph4.getLegendRenderer().setVisible(true);
        //graph2.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph4.addSeries(mGpsData);
        graph4.getViewport().setXAxisBoundsManual(true);
        graph4.getViewport().setYAxisBoundsManual(true);
        graph4.getViewport().setMinY(GpsMinValue);
        graph4.getViewport().setMaxY(GpsMaxValue);
        graph4.getViewport().setMinX(0);
        graph4.getViewport().setMaxX(GpsGraphDisplaySampleMax);
        GraphView graph5 = (GraphView) findViewById(R.id.graph5);
        mAudioData = new LineGraphSeries<>();
        // legend
        mAudioData.setTitle("Ready");
        graph5.getLegendRenderer().setVisible(true);
        //graph2.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph5.addSeries(mAudioData);
        graph5.getViewport().setXAxisBoundsManual(true);
        graph5.getViewport().setYAxisBoundsManual(true);
        graph5.getViewport().setMinY(AudioMinValue);
        graph5.getViewport().setMaxY(AudioMaxValue);
        graph5.getViewport().setMinX(0);
        graph5.getViewport().setMaxX(AudioGraphDisplaySampleMax);
        GraphView graph6 = (GraphView) findViewById(R.id.graph6);
        mAudioData_Cross = new LineGraphSeries<>();
        // legend
        mAudioData_Cross.setTitle("Ready");
        graph6.getLegendRenderer().setVisible(true);
        //graph2.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph6.addSeries(mAudioData_Cross);
        graph6.getViewport().setXAxisBoundsManual(true);
        graph6.getViewport().setYAxisBoundsManual(true);
        graph6.getViewport().setMinY(AudioMinValue_Cross);
        graph6.getViewport().setMaxY(AudioMaxValue_Cross);
        graph6.getViewport().setMinX(0);
        graph6.getViewport().setMaxX(AudioGraphDisplaySampleMax_Cross);
        startRecording();



        /*
         mAccData = new LineGraphSeries<>(generateData());
        graph.addSeries(mAccData);
        mTimer1 = new Runnable() {
            @Override
            public void run() {
                mAccData.resetData(generateData());
                mHandler.postDelayed(this, 300);
            }
        };
         mHandler.postDelayed(mTimer1, 300);
     */
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
         * when the activity is resumed, we acquire a wake-lock so that the
         * screen stays on, since the user will likely not be fiddling with the
         * screen or buttons.
         */
        //     mWakeLock.acquire();
        // Start the simulation
        // mSimulationView.startSimulation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*
         * When the activity is paused, we make sure to stop the simulation,
         * release our sensor resources and wake locks
         */
        // Stop the simulation
        //  mSimulationView.stopSimulation();
        // and release our wake-lock
        mWakeLock.release();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        mLocationManager.removeUpdates(this);
        mGlobal.status_record = false;
    }

    public synchronized void startRecording() {
        if (recordingThread != null) {
            return;
        }
        mGlobal.status_record = true;
        recordingThread =
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                record();
                            }
                        });
        recordingThread.start();
    }

    private void record() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);
        int bufferSizeInShorts = 50 * AudioRecord.getMinBufferSize(mGlobal.Audio_sampleRate, mGlobal.Audio_channelConfig, mGlobal.Audio_format);
        Log.i("RecordAudioTask", "Input Buffer Size: " + bufferSizeInShorts);
        // create the transfer buffer
        int transferSizeInShorts = AudioRecord.getMinBufferSize(mGlobal.Audio_sampleRate, mGlobal.Audio_channelConfig, mGlobal.Audio_format);
        short[] buffer = new short[transferSizeInShorts];
        Log.i("RecordAudioTask", "transfer buffer Buffer Size: " + transferSizeInShorts);
        int bufferSize = mGlobal.Audio_sampleRate * 2;    // 1s output buffer
        mAudioRecord = new AudioRecord(mGlobal.Audio_souce, mGlobal.Audio_sampleRate, mGlobal.Audio_channelConfig, mGlobal.Audio_format, bufferSizeInShorts);
        if (mAudioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.e(TAG, "Audio Record can't initialize!");
            return;
        }
        mAudioRecord.startRecording();
        while (mGlobal.status_record) {
            int size = mAudioRecord.read(buffer, 0, transferSizeInShorts);
            if (size > 0) {
                short[] tmpBuf = new short[size];
                System.arraycopy(buffer, 0, tmpBuf, 0, size);
                try {
                    mAudioData.appendData(new DataPoint(Audiograph2LastValue, root_mean_square(tmpBuf)), true, AudioGraphDisplaySampleMax);
                    mAudioData_Cross.appendData(new DataPoint(Audiograph2LastValue_Cross, calculate_cross(mGlobal.Audio_sampleRate, tmpBuf)), true, AudioGraphDisplaySampleMax_Cross);
                    Audiograph2LastValue += 1d;
                    Audiograph2LastValue_Cross += 1d;
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        mAudioRecord.stop();
        mAudioRecord.release();
    }

    public double root_mean_square(short[] data) {
        double ms = 0;
        for (int i = 0; i < data.length; i++) {
            ms += data[i] * data[i];
        }
        ms /= data.length;
        double d = Math.sqrt(ms);
        Log.i("RecordAudioTask", "double: " + d);
        return d;
    }

    public double calculate_cross(int sampleRate, short[] audioData) {
        int numSamples = audioData.length;
        int numCrossing = 0;
        for (int p = 0; p < numSamples - 1; p++) {
            if ((audioData[p] > 0 && audioData[p + 1] <= 0) ||
                    (audioData[p] < 0 && audioData[p + 1] >= 0)) {
                numCrossing++;
            }
        }
        float numSecondsRecorded = (float) numSamples / (float) sampleRate;
        float numCycles = numCrossing / 2;
        float frequency = numCycles / numSecondsRecorded;
        return frequency;
    }

    private DataPoint[] generateData() {
        int count = 200;
        DataPoint[] values = new DataPoint[count];
        for (int i = 0; i < count; i++) {
            double x = i;
            double f = mRand.nextDouble() * 0.15 + 0.3;
            double y = Math.sin(i * f + 2) + mRand.nextDouble() * 0.3;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values.clone();
        // Pass values through high-pass filter if enabled
        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.


       /*
        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * event.values[0];
        gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * event.values[1];
        gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * event.values[2];
        // Remove the gravity contribution with the high-pass filter.
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];
         */
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            double sumOfSquares = (values[0] * values[0])
                    + (values[1] * values[1])
                    + (values[2] * values[2]);
            double msqrt = Math.sqrt(sumOfSquares);
            mAccData.appendData(new DataPoint(Accgraph2LastValue, msqrt), true, AccGraphDisplaySampleMax);
            Accgraph2LastValue += 1d;
            /*if (msqrt > 1) {
                Context context = getApplicationContext();
                CharSequence text = "Movement detected:acct  >1 ";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }*/
            ;
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            double sumOfSquares = (values[0] * values[0])
                    + (values[1] * values[1])
                    + (values[2] * values[2]);
            double msqrt = Math.sqrt(sumOfSquares);
            mGyrData.appendData(new DataPoint(Grygraph2LastValue, msqrt), true, GyrGraphDisplaySampleMax);
            Grygraph2LastValue += 1d;
            ;
        } else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            ;
        } else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            ;
        } else if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            ;
        } else if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            ;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            double sumOfSquares = (values[0] * values[0])
                    + (values[1] * values[1])
                    + (values[2] * values[2]);
            double msqrt = Math.sqrt(sumOfSquares);
            mMagData.appendData(new DataPoint(Maggraph2LastValue, msqrt), true, MagGraphDisplaySampleMax);
            Maggraph2LastValue += 1d;
            ;
        } else if (event.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
            ;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onLocationChanged(Location location) {
        double speed = location.getSpeed();
        mGpsData.appendData(new DataPoint(Gpsgraph2LastValue, speed), true, GpsGraphDisplaySampleMax);
        Gpsgraph2LastValue += 1d;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
