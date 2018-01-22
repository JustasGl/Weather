package com.example.android.orelis;

import static com.example.android.orelis.R.id.miestas;

/**
 * Created by Justas on 1/4/2018.
 */

public class hourly {
    public final String mlaipsniai, mlaikas,murls;
    public hourly(String laipsniai, String laikas, String urls)
    {
        mlaikas=laikas;
        mlaipsniai=laipsniai;
        murls=urls;
    }

    public String getMlaikas() {
        return mlaikas;
    }

    public String getMlaipsniai() {
        return mlaipsniai;
    }

    public String getMurls() {
        return murls;
    }


}