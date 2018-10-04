package xyz.util;

import java.text.DecimalFormat;

/**
 * Created by xyz on 2018/9/30.
 * @author xyz
 */

public class BMI {

    private double mHeight;
    private double mWeight;
    private int mSystem;

    public BMI(int system) {
        mSystem = system;
    }



    public void setHeight(double height) {
        mHeight = height;
    }



    public void setWeight(double weight) {
        mWeight = weight;
    }

    public int getSystem() {
        return mSystem;
    }

    public void setSystem(int system) {
        mSystem = system;
    }

    private double bmiOfMetric() {
        return mWeight / Math.pow(mHeight/100, 2);
    }

    private double bmiOfImperial() {
        return 703 * bmiOfMetric()/10000;
    }

    public double getBmi() {
        switch (mSystem) {
            case S.Unit.METRIC_SYSTEM:
                return bmiOfMetric();
            case S.Unit.IMPERIAL_SYSTEM:
                return bmiOfImperial();
            default:
                return 0d;
        }
    }

    public String getBmiOfFormat() {
        return new DecimalFormat("0.00").format(getBmi());
    }

    public int getBmiAdvice() {
        long int_bmi = Math.round(getBmi());
        if (int_bmi < 20) {
            return 2;
        } else if (int_bmi > 25) {
            return 1;
        } else {
            return 0;
        }
    }
}
