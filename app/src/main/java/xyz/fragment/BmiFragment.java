package xyz.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import xyz.bmi.R;
import xyz.util.BMI;
import xyz.util.S;

/**
 * Created by xyz on 2018/10/6.
 * Project Name:BMI2018
 */

public class BmiFragment extends Fragment {

    private View mView;
    private EditText mHeightEt;
    private EditText mWeightEt;
    private TextView mHeightTv;
    private TextView mWeightTv;
    private TextView mBmiValueTv;
    private TextView mBmiAdviceTv;
    private Button mCalcButton;
    private String[] mAdvices;
    private BMI mBmi;

    public static Fragment NewInstance(int system) {
        Fragment fragment=new BmiFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("system",system);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_bmi, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews();
        setListener();
        Resources res = getResources();
        String bmiValue = String.format(res.getString(R.string.bmi_value), "");
        mBmiValueTv.setText(bmiValue);
        String bmiAdvice = String.format(res.getString(R.string.advice), "");
        mBmiAdviceTv.setText(bmiAdvice);
        mAdvices = res.getStringArray(R.array.advice);
        int system = getArguments().getInt("system");
        mBmi = new BMI(system);
        setSystem(system);
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
        mHeightEt = (EditText) mView.findViewById(R.id.et_height);
        mWeightEt = (EditText) mView.findViewById(R.id.et_weight);
        mBmiValueTv = (TextView) mView.findViewById(R.id.tv_bmi_value);
        mBmiAdviceTv = (TextView) mView.findViewById(R.id.tv_bmi_advice);
        mCalcButton = (Button) mView.findViewById(R.id.btn_calc);
        mHeightTv = (TextView) mView.findViewById(R.id.tv_height);
        mWeightTv = (TextView) mView.findViewById(R.id.tv_weight);
    }

    public void setSystem(int system) {
        mBmi.setSystem(system);
        switch (system) {
            case S.Unit.METRIC_SYSTEM:

                mHeightTv.setText(R.string.height);
                mWeightTv.setText(R.string.weight);
                break;
            case S.Unit.IMPERIAL_SYSTEM:

                mHeightTv.setText(R.string.height_fin);
                mWeightTv.setText(R.string.weight_flb);
                break;
            default:
        }
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.getString("bmiValue") != null)
                mBmiValueTv.setText(getString(R.string.bmi_value, savedInstanceState.getString("bmiValue")));
            if (savedInstanceState.getInt("advice") > 0)
                mBmiAdviceTv.setText(getString(R.string.advice, mAdvices[savedInstanceState.getInt("advice") - 1]));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
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


}
