package com.bendywalker.insulator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PersistentDataFragment extends Fragment {
    Card desiredBloodGlucoseLevelCard, carbohydrateFactorCard, correctiveFactorCard;
    SharedPreferences preferences;

    String desiredBloodGlucoseLevelKey, carbohydrateFactorKey, correctiveFactorKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        desiredBloodGlucoseLevelKey = getString(R.string.preference_desired_blood_glucose_level);
        carbohydrateFactorKey = getString(R.string.preference_carbohydrate_factor);
        correctiveFactorKey = getString(R.string.preference_corrective_factor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_persistent, container, false);

        desiredBloodGlucoseLevelCard = (Card) view.findViewById(R.id.card_desired_blood_glucose_level);
        carbohydrateFactorCard = (Card) view.findViewById(R.id.card_carbohydrate_factor);
        correctiveFactorCard = (Card) view.findViewById(R.id.card_corrective_factor);

        float savedDesiredBloodGlucoseLevelFloat = preferences.getFloat(desiredBloodGlucoseLevelKey, 0);
        float savedCarbohydrateFactorFloat = preferences.getFloat(carbohydrateFactorKey, 0);
        float savedCorrectiveFactorFloat = preferences.getFloat(correctiveFactorKey, 0);

        if (savedDesiredBloodGlucoseLevelFloat != 0) {
            desiredBloodGlucoseLevelCard.setEntry(savedDesiredBloodGlucoseLevelFloat);
        }

        if (savedCarbohydrateFactorFloat != 0) {
            carbohydrateFactorCard.setEntry(savedCarbohydrateFactorFloat);
        }

        if (savedCorrectiveFactorFloat != 0) {
            correctiveFactorCard.setEntry(savedCorrectiveFactorFloat);
        }

        return view;
    }

}
