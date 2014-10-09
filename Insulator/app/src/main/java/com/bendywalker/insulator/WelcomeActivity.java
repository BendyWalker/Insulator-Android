package com.bendywalker.insulator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class WelcomeActivity extends Activity implements OnClickListener
{

    // Declare variables
    Button continueOn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Initialise variables
        continueOn = (Button) findViewById(R.id.button_continue);
        continueOn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {

            case R.id.button_continue:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;

            default:
                break;
        }


    }

}
