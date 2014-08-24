package klusman.scaantirevents.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import klusman.scaantirevents.R;

public class SplashActivity extends Activity {


    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            proceed();
        }
    };

    private void proceed() {
        startActivity(new Intent(SplashActivity.this, MyActivity.class));
        finish();
    }

    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(0, 700);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadeout, R.anim.fadein);
        mHandler.removeMessages(0);
    }
}
