package com.bendywalker.insulator;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PersistentDataFragment extends Fragment {
    Card desiredBloodGlucoseLevelCard, carbohydrateFactorCard, correctiveFactorCard;
    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_persistent, container, false);

        desiredBloodGlucoseLevelCard = (Card) view.findViewById(R.id.card_desired_blood_glucose_level);
        carbohydrateFactorCard = (Card) view.findViewById(R.id.card_carbohydrate_factor);
        correctiveFactorCard = (Card) view.findViewById(R.id.card_corrective_factor);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        String desiredBloodGlucoseLevelKey = getActivity().getString(R.string.preference_desired_blood_glucose_level);
        String carbohydrateFactorKey = getActivity().getString(R.string.preference_carbohydrate_factor);
        String correctiveFactorKey = getActivity().getString(R.string.preference_corrective_factor);

        float desiredBloodGlucoseLevelFloat = Float.valueOf(desiredBloodGlucoseLevelCard.getStringFromEntry());
        float carbohydrateFactorFloat = Float.valueOf(carbohydrateFactorCard.getStringFromEntry());
        float correctiveFactorFloat = Float.valueOf(correctiveFactorCard.getStringFromEntry());

        preferences.edit().putFloat(desiredBloodGlucoseLevelKey, desiredBloodGlucoseLevelFloat);
        preferences.edit().putFloat(carbohydrateFactorKey, carbohydrateFactorFloat);
        preferences.edit().putFloat(correctiveFactorKey, correctiveFactorFloat);

        preferences.edit().commit();
    }
}
