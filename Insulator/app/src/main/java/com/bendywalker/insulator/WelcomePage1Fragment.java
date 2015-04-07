package com.bendywalker.insulator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;


public class WelcomePage1Fragment extends Fragment implements Card.OnTextChangeListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    Card desiredBloodGlucoseCard, carbohydrateFactorCard, correctiveFactorCard;
    RadioGroup bloodGlucoseUnitsRadioGroup;
    SwitchCompat floatingPointCarbohydrateSwitch;
    MenuItem continueButton;
    MyPreferenceManager preferenceManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_page_1, container, false);

        preferenceManager = ((WelcomeActivity) getActivity()).getPreferenceManager();

        bloodGlucoseUnitsRadioGroup = (RadioGroup) view
                .findViewById(R.id.card_blood_glucose_measurement_radio_group);
        bloodGlucoseUnitsRadioGroup.check(R.id.card_blood_glucose_measurement_radio_button_mmol);
        bloodGlucoseUnitsRadioGroup.setOnCheckedChangeListener(this);

        floatingPointCarbohydrateSwitch = (SwitchCompat) view.findViewById(R.id.card_floating_point_carbohydrates_switch);
        floatingPointCarbohydrateSwitch.setOnClickListener(this);

        desiredBloodGlucoseCard = (Card) view
                .findViewById(R.id.card_desired_blood_glucose_level);
        desiredBloodGlucoseCard.setOnTextChangeListener(this);

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_continue:
                preferenceManager.setIsFirstTimeOpen(false);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(carbohydrateFactorCard.getWindowToken(), 0);
                ((WelcomeActivity) getActivity()).goForwardToFragment(new WelcomePage2Fragment());
                break;
        }

        return true;
    }

    @Override
    public void onTextChange() {
        continueButton.setEnabled(desiredBloodGlucoseCard.isEntryFieldFilled() && carbohydrateFactorCard
                .isEntryFieldFilled() && correctiveFactorCard.isEntryFieldFilled());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (bloodGlucoseUnitsRadioGroup.getCheckedRadioButtonId()) {
            case R.id.card_blood_glucose_measurement_radio_button_mmol:
                preferenceManager.setBloodGlucoseUnit(BloodGlucoseUnit.mmol);
                break;
            case R.id.card_blood_glucose_measurement_radio_button_mgdl:
                preferenceManager.setBloodGlucoseUnit(BloodGlucoseUnit.mgdl);
                break;
        }

        correctiveFactorCard.updateHint();
        desiredBloodGlucoseCard.updateHint();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.card_floating_point_carbohydrates_switch) {
            preferenceManager.setAllowFloatingPointCarbohydrates(floatingPointCarbohydrateSwitch.isChecked());
        }
    }
}
