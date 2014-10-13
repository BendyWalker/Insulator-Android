package com.bendywalker.insulator;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;


/**
 * Created by Ben on 13/10/2014.
 */
public class WelcomePage4Fragment extends Fragment
{
    RelativeLayout welcomeCard;
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

        welcomeCard = (RelativeLayout) view.findViewById(R.id.card_welcome);

        useInsulatorButton = (Button) view.findViewById(R.id.button_use_insulator);
        useInsulatorButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                                 .putBoolean(getString(R.string.preference_first_time_open), false)
                                 .commit();
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });

        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_up);
        animation.setStartOffset(getResources().getInteger(R.integer.animation_offset));
        welcomeCard.startAnimation(animation);

        return view;
    }
}
