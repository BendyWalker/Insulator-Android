package com.bendywalker.insulator;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


public class WelcomePage3Fragment extends Fragment
{
    Card desiredBloodGlucoseLevelCard, carbohydrateFactorCard, correctiveFactorCard;
    RelativeLayout continueCard;
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
        View view = inflater.inflate(R.layout.fragment_welcome_page_3, container, false);

        desiredBloodGlucoseLevelCard = (Card) view
                .findViewById(R.id.card_desired_blood_glucose_level);
        carbohydrateFactorCard = (Card) view.findViewById(R.id.card_carbohydrate_factor);
        correctiveFactorCard = (Card) view.findViewById(R.id.card_corrective_factor);
        continueCard = (RelativeLayout) view.findViewById(R.id.card_continue);

        continueButton = (Button) view.findViewById(R.id.button_continue);
        continueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((WelcomeActivity) getActivity()).goForwardToFragment();
            }
        });

        addAnimationsToCards();

        return view;
    }

    private void addAnimationsToCards()
    {
        int offset = getResources().getInteger(R.integer.animation_offset);
        int offsetMultiplier = 1;

        List<Animation> animations = new ArrayList<Animation>();

        Animation carbohydrateFactorAnimation = AnimationUtils
                .loadAnimation(getActivity(), R.anim.slide_in_up);
        animations.add(carbohydrateFactorAnimation);

        Animation correctiveFactorAnimation = AnimationUtils
                .loadAnimation(getActivity(), R.anim.slide_in_up);
        animations.add(correctiveFactorAnimation);

        Animation desiredBloodGlucoseLevelAnimation = AnimationUtils
                .loadAnimation(getActivity(), R.anim.slide_in_up);
        animations.add(desiredBloodGlucoseLevelAnimation);

        Animation continueAnimation = AnimationUtils
                .loadAnimation(getActivity(), R.anim.slide_in_up);
        animations.add(continueAnimation);

        for (Animation animation : animations)
        {
            animation.setStartOffset(offset * offsetMultiplier);
            offsetMultiplier++;
        }

        carbohydrateFactorCard.startAnimation(carbohydrateFactorAnimation);
        correctiveFactorCard.startAnimation(correctiveFactorAnimation);
        desiredBloodGlucoseLevelCard.startAnimation(desiredBloodGlucoseLevelAnimation);
        continueCard.startAnimation(continueAnimation);
    }
}