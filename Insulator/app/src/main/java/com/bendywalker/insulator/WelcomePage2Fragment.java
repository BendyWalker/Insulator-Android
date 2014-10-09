package com.bendywalker.insulator;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;


public class WelcomePage2Fragment extends Fragment
{
    RelativeLayout halfUnitsCard, bloodGlucoseUnitsCard, continueCard;
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
        View view = inflater.inflate(R.layout.fragment_welcome_page_2, container, false);

        continueButton = (Button) view.findViewById(R.id.button_continue);
        halfUnitsCard = (RelativeLayout) view.findViewById(R.id.card_half_units);
        bloodGlucoseUnitsCard = (RelativeLayout) view
                .findViewById(R.id.card_blood_glucose_measurement);
        continueCard = (RelativeLayout) view.findViewById(R.id.card_continue);
        continueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        halfUnitsCard
                .startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up_left));
        bloodGlucoseUnitsCard
                .startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up_right));
        continueCard
                .startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up_left));

        return view;
    }
}