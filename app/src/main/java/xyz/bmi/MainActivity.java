package xyz.bmi;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import xyz.util.BMI;
import xyz.util.S;

public class MainActivity extends AppCompatActivity {


    private EditText mHeightEt;
    private EditText mWeightEt;
    private TextView mHeightTv;
    private TextView mWeightTv;
    private TextView mBmiValueTv;
    private TextView mBmiAdviceTv;
    private Button mCalcButton;
    private String[] mAdvices;
    private BMI mBmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setListener();
        Resources res = getResources();
        String bmiValue = String.format(res.getString(R.string.bmi_value), "");
        mBmiValueTv.setText(bmiValue);
        String bmiAdvice = String.format(res.getString(R.string.advice), "");
        mBmiAdviceTv.setText(bmiAdvice);
        mAdvices = res.getStringArray(R.array.advice);
        mBmi = new BMI(S.Unit.METRIC_SYSTEM);
    }

    private void setListener() {
        mCalcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String height = mHeightEt.getText().toString();
                String weight = mWeightEt.getText().toString();
                if (height.equals("") || weight.equals("")) {
                    return;
                }
                mBmi.setHeight(Integer.parseInt(height));
                mBmi.setWeight(Integer.parseInt(weight));
                mBmiValueTv.setText(getString(R.string.bmi_value, mBmi.getBmiOfFormat()));
                mBmiAdviceTv.setText(getString(R.string.advice, mAdvices[mBmi.getBmiAdvice()]));

            }
        });
    }

    private void findViews() {
        mHeightEt = (EditText) findViewById(R.id.et_height);
        mWeightEt = (EditText) findViewById(R.id.et_weight);
        mBmiValueTv = (TextView) findViewById(R.id.tv_bmi_value);
        mBmiAdviceTv = (TextView) findViewById(R.id.tv_bmi_advice);
        mCalcButton = (Button) findViewById(R.id.btn_calc);
        mHeightTv = (TextView) findViewById(R.id.tv_height);
        mWeightTv = (TextView) findViewById(R.id.tv_weight);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mBmi.getSystem() == S.Unit.METRIC_SYSTEM) {
            menu.findItem(R.id.menu_metric).setTitle(R.string.to_imperial);
        }
        if (mBmi.getSystem() == S.Unit.IMPERIAL_SYSTEM) {
            menu.findItem(R.id.menu_metric).setTitle(R.string.to_metric);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_metric) {

            switch (mBmi.getSystem()) {
                case S.Unit.METRIC_SYSTEM:
                    mBmi.setSystem(S.Unit.IMPERIAL_SYSTEM);
                    mHeightTv.setText(R.string.height_fin);
                    mWeightTv.setText(R.string.weight_flb);
                    break;
                case S.Unit.IMPERIAL_SYSTEM:
                    mBmi.setSystem(S.Unit.METRIC_SYSTEM);
                    mHeightTv.setText(R.string.height);
                    mWeightTv.setText(R.string.weight);
                    break;
                default:
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
