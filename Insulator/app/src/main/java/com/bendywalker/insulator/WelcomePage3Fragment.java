package com.bendywalker.insulator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


public class WelcomePage3Fragment extends Fragment implements Card.OnTextChangeListener {
    Card desiredBloodGlucoseLevelCard, carbohydrateFactorCard, correctiveFactorCard;
    CardView continueCard;
    Button continueButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_page_3, container, false);

        desiredBloodGlucoseLevelCard = (Card) view
                .findViewById(R.id.card_desired_blood_glucose_level);
        desiredBloodGlucoseLevelCard.setOnTextChangeListener(this);

        carbohydrateFactorCard = (Card) view.findViewById(R.id.card_carbohydrate_factor);
        carbohydrateFactorCard.setOnTextChangeListener(this);

        correctiveFactorCard = (Card) view.findViewById(R.id.card_corrective_factor);
        correctiveFactorCard.setOnTextChangeListener(this);

        continueCard = (CardView) view.findViewById(R.id.card_continue);

        continueButton = (Button) view.findViewById(R.id.button_continue);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
                        .getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(continueButton.getWindowToken(), 0);

                ((WelcomeActivity) getActivity()).goForwardToFragment(new WelcomePage4Fragment());
            }
        });

        // addAnimationsToCards();

        return view;
    }

    private void addAnimationsToCards() {
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

        for (Animation animation : animations) {
            animation.setStartOffset(offset * offsetMultiplier);
            offsetMultiplier++;
        }

        carbohydrateFactorCard.startAnimation(carbohydrateFactorAnimation);
        correctiveFactorCard.startAnimation(correctiveFactorAnimation);
        desiredBloodGlucoseLevelCard.startAnimation(desiredBloodGlucoseLevelAnimation);
        continueCard.startAnimation(continueAnimation);
    }

    @Override
    public void onTextChange() {
        continueButton.setEnabled(
                desiredBloodGlucoseLevelCard.isEntryFilled() && carbohydrateFactorCard
                        .isEntryFilled() && correctiveFactorCard.isEntryFilled());
    }
}