package comnitt.boston.proximity2app;


import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    ImageView img;
    TextView text,time;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    Ringtone ring;
    ProgressBar progressBar;
    MyCountDownTimer count;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        text =  (TextView) findViewById(R.id.tv);
        time = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        img= (ImageView) findViewById(R.id.imageView);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.values[0] > -4 && event.values[0]<4)
        {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Alert();
                }
            },10000);

            count = new MyCountDownTimer(10000,1000);
            count.start();
            text.setText("!Near!");
            img.setImageResource(R.drawable.near2);
        }


        else
        {
            if (ring!=null) ring.stop();
            if (handler!=null) handler.removeCallbacksAndMessages(null);
            if (count!=null) count.cancel();
            progressBar.setProgress(0);
            text.setText("!Far!");
            img.setImageResource(R.drawable.hd1);


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {


            time.setText("Seconds remaining: " + millisUntilFinished / 1000);
            int progress = (int) (millisUntilFinished/1000);
            progressBar.setProgress(progressBar.getMax()-progress);
        }

        @Override
        public void onFinish()
        {
            progressBar.setProgress(0);
        }
    }

    public void Alert   ()
    {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ring = RingtoneManager.getRingtone(getApplicationContext(), notification);
        ring.play();
    }




}
