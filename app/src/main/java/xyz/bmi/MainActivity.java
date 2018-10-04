package xyz.bmi;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
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
    private int mSystemTemp;
    public static final String PREFERENCE_NAME = "Setting";


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

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        int system = sp.getInt("system", 0);
        if (system == 1) {
            mHeightTv.setText(R.string.height_fin);
            mWeightTv.setText(R.string.weight_flb);
            mBmi.setSystem(S.Unit.IMPERIAL_SYSTEM);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sp = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int system = mBmi.getSystem();
        editor.putInt("system", system);
        editor.apply();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String[] bmiValue = mBmiValueTv.getText().toString().split("：");
        if (bmiValue.length == 2) {
            outState.putString("bmiValue", bmiValue[1]);
        }

        String[] bmiAdvice = mBmiAdviceTv.getText().toString().split("：");
        if (bmiAdvice.length == 2) {
            for (int i = 0; i < mAdvices.length; i++) {
                if (bmiAdvice[1].equals(mAdvices[i])) {
                    outState.putInt("advice", i + 1);
                }
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.getString("bmiValue") != null)
                mBmiValueTv.setText(getString(R.string.bmi_value, savedInstanceState.getString("bmiValue")));
            if (savedInstanceState.getInt("advice") > 0)
                mBmiAdviceTv.setText(getString(R.string.advice, mAdvices[savedInstanceState.getInt("advice") - 1]));
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_metric) {
            new AlertDialog.Builder(MainActivity.this).setTitle(R.string.select_unit)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setSingleChoiceItems(new String[]{getString(R.string.metric_system), getString(R.string.imperial_system)}, 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mSystemTemp = which;
                                }
                            })
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mSystemTemp == mBmi.getSystem())
                                return;
                            switch (mSystemTemp) {
                                case S.Unit.METRIC_SYSTEM:
                                    mBmi.setSystem(S.Unit.METRIC_SYSTEM);
                                    mHeightTv.setText(R.string.height);
                                    mWeightTv.setText(R.string.weight);
                                    break;
                                case S.Unit.IMPERIAL_SYSTEM:
                                    mBmi.setSystem(S.Unit.IMPERIAL_SYSTEM);
                                    mHeightTv.setText(R.string.height_fin);
                                    mWeightTv.setText(R.string.weight_flb);
                                    break;
                                default:
                            }
                        }
                    })
                    .show();


        }
        if (item.getItemId() == R.id.menu_about) {
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle(R.string.title_about)
                    .setMessage(R.string.alert_dialog__msg)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage(R.string.confirm_exit)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                }).show();
    }
}
