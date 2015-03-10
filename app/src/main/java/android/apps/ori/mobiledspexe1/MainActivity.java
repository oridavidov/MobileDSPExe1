package android.apps.ori.mobiledspexe1;


import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class MainActivity extends ActionBarActivity implements View.OnClickListener  {

    static double ver = 1.1;
    private GraphicalView mChart;
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private XYSeries mCurrentSeries;
    private XYSeriesRenderer mCurrentRenderer;

    // button
    private CheckBox firstbutton;
    private CheckBox secondbutton;
    private CheckBox thirdbutton;
    private CheckBox fourbutton;
    private CheckBox fifthbutton;
    private CheckBox sixbutton;
    private Button   playwave;
    private Spinner  freqchoose;

    double PI = Math.PI;

    double[] wave;
    double[] sumWave;

    private AudioTrack audioTrack;

    private Thread playthread;
    private int seconds = 1;
    private double frequency = 2000.0;
    int numofsamples;


    private void initChart() {
        mCurrentSeries = new XYSeries("Sample Data");
        mDataset.addSeries(mCurrentSeries);
        mCurrentRenderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(mCurrentRenderer);
    }

    private double[] cosWave(double amplitude, double frequency, int phase, int time)
    {
        double sample_rate = 10 * frequency;
        double sample_time = 1/sample_rate;
        numofsamples = (int)(time/sample_time);

        double t = 0;


        double temp[] = new double[numofsamples];

        for (int samples = 0; samples < numofsamples; samples++)
        {
            temp[samples] = amplitude * Math.cos( 2 * PI * frequency * t + phase);
            t += sample_time;

            Log.d("ori", "pos: "+ samples + " value: "+Double.toString(temp[samples]));
        }
        return temp;
    }

    private void showCosWave() {

        Log.d("ori","num of samples: "+numofsamples);
        sumWave = new double[numofsamples];

        if (firstbutton.isChecked())
        {
            wave = cosWave(1,1,0, 1);
            for (int t=0; t<numofsamples; t++)
            {
                sumWave[t] += wave[t];
            }
        }

        if (secondbutton.isChecked())
        {
            wave = cosWave(-1.0/3.0,3,0, 1);
            for (int t=0; t<numofsamples; t++)
            {
                sumWave[t] += wave[t];
            }
        }

        if (thirdbutton.isChecked())
        {
            wave = cosWave(1.0/5.0,5,0, 1);
            for (int t=0; t<numofsamples; t++)
            {
                sumWave[t] += wave[t];
            }
        }

        if (fourbutton.isChecked())
        {
            wave = cosWave(-1.0/7.0,7,0, 1);
            for (int t=0; t<numofsamples; t++)
            {
                sumWave[t] += wave[t];
            }
        }

        if (fifthbutton.isChecked())
        {
            wave = cosWave(1.0/9.0,9,0, 1);
            for (int t=0; t<numofsamples; t++)
            {
                sumWave[t] += wave[t];
            }
        }

        if (sixbutton.isChecked())
        {
            wave = cosWave(-1.0/11.0,11,0, 1);
            for (int t=0; t<numofsamples; t++)
            {
                sumWave[t] += wave[t];
            }
        }

        // show the sum wave
        mCurrentSeries.clear();
        for (int t=0; t<numofsamples; t++)
        {
            mCurrentSeries.add(t, sumWave[t]);
        }
        if (mChart != null)
            mChart.repaint();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstbutton = (CheckBox)findViewById(R.id.b1st);
        firstbutton.setOnClickListener(this);
        secondbutton = (CheckBox)findViewById(R.id.b2st);
        secondbutton.setOnClickListener(this);
        thirdbutton = (CheckBox)findViewById(R.id.b3st);
        thirdbutton.setOnClickListener(this);
        fourbutton = (CheckBox)findViewById(R.id.b4st);
        fourbutton.setOnClickListener(this);
        fifthbutton = (CheckBox)findViewById(R.id.b5st);
        fifthbutton.setOnClickListener(this);
        sixbutton = (CheckBox)findViewById(R.id.b6st);
        sixbutton.setOnClickListener(this);
        playwave = (Button)findViewById(R.id.bplaywave);
        playwave.setOnClickListener(this);
        freqchoose = (Spinner)findViewById(R.id.sfreqchoose);
        freqchoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    protected void onResume() {
        super.onResume();
        LinearLayout layout = (LinearLayout) findViewById(R.id.wave);
        if (mChart == null) {
            initChart();
            showCosWave();
            mChart = ChartFactory.getCubeLineChartView(this, mDataset, mRenderer, 0.3f);
            layout.addView(mChart);
        } else {
            mChart.repaint();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        if ( ((Button)v).getText().equals("Play Wave") ) {
            ((Button) v).setText("Stop Playing Wave");
/*
            short samples[] = new short[samplerate*seconds*3];

            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    samplerate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, samples.length,
                    AudioTrack.MODE_STATIC);

            double temp[] = cosWave(Short.MAX_VALUE,frequency,0, seconds);

            for (int i=0; i<numofsamples; i++)
            {
                samples[i] = (short)temp[i];
            }

            //audioTrack.setLoopPoints(0,samples.length,-1);
            audioTrack.write(samples, 0, samples.length ); // write data to audio hardware
            audioTrack.play();
*/
        }

        else if (((Button)v).getText().equals("Stop Playing Wave"))
        {
            ((Button) v).setText("Play Wave");

            if (audioTrack != null)
            {
                audioTrack.stop();
                audioTrack.release();
            }

        }
        else
        {
            showCosWave();
        }
    }
}
