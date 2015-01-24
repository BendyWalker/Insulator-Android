package com.bendywalker.insulator;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;


public class WelcomePage4Fragment extends Fragment
{
    CardView welcomeCard;
    Button useInsulatorButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_welcome_page_4, container, false);

        welcomeCard = (CardView) view.findViewById(R.id.card_ready_to_use);

        useInsulatorButton = (Button) view.findViewById(R.id.button_use_insulator);
        useInsulatorButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        preferences.edit()
                   .putBoolean(getString(R.string.preference_first_time_open), false)
                   .apply();

        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_up);
        animation.setStartOffset(getResources().getInteger(R.integer.animation_offset));
        welcomeCard.startAnimation(animation);

        return view;
    }
}
