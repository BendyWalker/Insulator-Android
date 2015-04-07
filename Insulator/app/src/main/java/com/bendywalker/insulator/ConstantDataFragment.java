package com.bendywalker.insulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class ConstantDataFragment extends Fragment {
    Card desiredBloodGlucoseLevelCard, carbohydrateFactorCard, correctiveFactorCard;
    MyPreferenceManager preferenceManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        preferenceManager = new MyPreferenceManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_constant, container, false);

        desiredBloodGlucoseLevelCard = (Card) view
                .findViewById(R.id.card_desired_blood_glucose_level);
        carbohydrateFactorCard = (Card) view.findViewById(R.id.card_carbohydrate_factor);
        correctiveFactorCard = (Card) view.findViewById(R.id.card_corrective_factor);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.constant, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_factor_suggestion:
                getActivity()
                        .startActivity(new Intent(getActivity(), FactorSuggestionActivity.class));
                break;
        }
        return false;
    }

    @Override
    public void onResume() {
        restoreValuesToCards();
        super.onResume();
    }

    private void restoreValuesToCards() {
        double savedDesiredBloodGlucose = preferenceManager.getDesiredBloodGlucose();
        double savedCarbohydrateFactor = preferenceManager.getCarbohydrateFactor();
        double savedCorrectiveFactor = preferenceManager.getCorrectiveFactor();

        if (savedDesiredBloodGlucose != 0) {
            desiredBloodGlucoseLevelCard.setEntryFieldFromValue(savedDesiredBloodGlucose);
        }

        if (savedCarbohydrateFactor != 0) {
            carbohydrateFactorCard.setEntryFieldFromValue(savedCarbohydrateFactor);
        }

        if (savedCorrectiveFactor != 0) {
            correctiveFactorCard.setEntryFieldFromValue(savedCorrectiveFactor);
        }
    }
}
