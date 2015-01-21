package com.bendywalker.insulator;

import android.app.Fragment;
import android.os.Bundle;
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

        welcomeCard = (CardView) view.findViewById(R.id.card_welcome);

        continueButton = (Button) view.findViewById(R.id.button_continue);
        continueButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((WelcomeActivity) getActivity()).goForwardToFragment(new WelcomePage2Fragment());
            }
        });

        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_up);
        animation.setStartOffset(getResources().getInteger(R.integer.animation_offset));
        welcomeCard.startAnimation(animation);

        return view;
    }
}
