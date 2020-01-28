package com.mobbile.paul.shrine.activity;

import android.content.Intent;
import android.util.Log;

import com.daimajia.androidanimations.library.Techniques;
import com.mobbile.paul.codelab.R;
import com.mobbile.paul.shrine.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class SplashScreenActivity extends AwesomeSplash {
    @Override
    public void initSplash(ConfigSplash configSplash) {

        /* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorLightGrey); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP


        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.ic_logo_ace); //or any other drawable
        configSplash.setAnimLogoSplashDuration(3000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.DropOut); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Path
//        configSplash.setPathSplash(Constants.DROID_LOGO); //set path String
        configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(3000);
        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.colorDarkShade); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(3000);
        configSplash.setPathSplashFillColor(R.color.colorWarmGreenText); //path object filling color


        //Customize Title
        configSplash.setTitleSplash("ACE DIGITAL");
        configSplash.setTitleTextColor(R.color.colorDarkShade);
        configSplash.setTitleTextSize(24f); //float value
        configSplash.setAnimTitleDuration(4000);
        configSplash.setAnimTitleTechnique(Techniques.DropOut);

    }

    @Override
    public void animationsFinished() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d("SplashScreenActivity","HERERERERERER");
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Log.d("SplashScreenActivity","HERERERERERER");
            startActivity(new Intent(this, Onboarding.class));
        }

        this.finish();
    }


}
