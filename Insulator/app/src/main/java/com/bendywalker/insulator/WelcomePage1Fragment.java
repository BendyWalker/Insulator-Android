package com.bendywalker.insulator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;


public class WelcomePage1Fragment extends Fragment
{
    CardView welcomeCard;
    Button continueButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_welcome_page_1, container, false);

        continueButton = (Button) view.findViewById(R.id.button_continue);
        continueButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((WelcomeActivity) getActivity()).goForwardToFragment(new WelcomePage2Fragment());
            }
        });

        return view;
    }
}
