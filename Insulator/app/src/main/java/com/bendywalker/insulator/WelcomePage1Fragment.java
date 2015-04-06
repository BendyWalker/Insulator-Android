package com.bendywalker.insulator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;


public class WelcomePage1Fragment extends Fragment implements Card.OnTextChangeListener {
    CardView bloodGlucoseUnitsCard;
    Card desiredBloodGlucoseLevelCard, carbohydrateFactorCard, correctiveFactorCard;
    RadioGroup bloodGlucoseUnitsRadioGroup;
    MenuItem continueButton;

    boolean areEntryFieldsFilled, isBloodGlucoseUnitSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_page_1, container, false);

        bloodGlucoseUnitsCard = (CardView) view
                .findViewById(R.id.card_blood_glucose_measurement);
        bloodGlucoseUnitsRadioGroup = (RadioGroup) view
                .findViewById(R.id.card_blood_glucose_measurement_radio_group);

        bloodGlucoseUnitsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isBloodGlucoseUnitSelected = true;
                toggleButtonEnabledState();
            }
        });

                desiredBloodGlucoseLevelCard = (Card) view
                        .findViewById(R.id.card_desired_blood_glucose_level);
        desiredBloodGlucoseLevelCard.setOnTextChangeListener(this);

        carbohydrateFactorCard = (Card) view.findViewById(R.id.card_carbohydrate_factor);
        carbohydrateFactorCard.setOnTextChangeListener(this);

        correctiveFactorCard = (Card) view.findViewById(R.id.card_corrective_factor);
        correctiveFactorCard.setOnTextChangeListener(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.welcome_1, menu);
        continueButton = menu.findItem(R.id.action_continue);
        toggleButtonEnabledState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_continue:
                ((WelcomeActivity) getActivity()).goForwardToFragment(new WelcomePage2Fragment());
                break;
        }

        return true;
    }

    @Override
    public void onTextChange() {
        areEntryFieldsFilled = desiredBloodGlucoseLevelCard.isEntryFieldFilled() && carbohydrateFactorCard
                .isEntryFieldFilled() && correctiveFactorCard.isEntryFieldFilled();
        toggleButtonEnabledState();
    }

    private void toggleButtonEnabledState() {
        continueButton.setEnabled(isBloodGlucoseUnitSelected && areEntryFieldsFilled);
    }
}
