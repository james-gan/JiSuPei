package com.jisupei.vpnew.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jisupei.R;
import com.jisupei.utils.AppUtils;
import com.jisupei.vpnew.VpHomePageActivity;

/**
 * Created by xiayumo on 16/5/12.
 */
public class NewVpOrderSubmitSuceAcitivity extends Activity{

    ImageView back_bt;

    Button bt1,bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_submit);


        back_bt= (ImageView)findViewById(R.id.back_bt);
        AppUtils.expandViewTouchDelegate( back_bt, 30, 30, 50, 50);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt1= (Button) findViewById(R.id.bt1);
        bt2= (Button) findViewById(R.id.bt2);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   MainActivity11
             //   startActivity(new Intent(OrderSubmitAcitivity.this,MainActivityNew1.class));

                    startActivity(new Intent(NewVpOrderSubmitSuceAcitivity.this,VpHomePageActivity.class));
                    finish();

            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent     intent=   new Intent(NewVpOrderSubmitSuceAcitivity.this,VpNewAllOrderActicity.class);
                    startActivity(intent);
                    finish();

            }
        });

    }



}
