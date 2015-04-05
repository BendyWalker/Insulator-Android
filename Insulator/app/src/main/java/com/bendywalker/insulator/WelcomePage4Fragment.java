package com.bendywalker.insulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class WelcomePage4Fragment extends Fragment {
    CardView welcomeCard;
    Button useInsulatorButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_page_4, container, false);

        welcomeCard = (CardView) view.findViewById(R.id.card_ready_to_use);

        useInsulatorButton = (Button) view.findViewById(R.id.button_use_insulator);
        useInsulatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });

        MyPreferenceManager preferenceManager = new MyPreferenceManager(getActivity());
        preferenceManager.setIsFirstTimeOpen(false);

//        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_up);
//        animation.setStartOffset(getResources().getInteger(R.integer.animation_offset));
//        welcomeCard.startAnimation(animation);

        return view;
    }
}
