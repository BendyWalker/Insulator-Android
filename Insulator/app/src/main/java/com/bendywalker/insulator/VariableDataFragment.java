package com.bendywalker.insulator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class VariableDataFragment extends Fragment implements Card.OnTextChangedListener {
    Card currentBloodGlucoseLevelCard, carbohydratesInMealCard;
    TextView suggestedInsulinDoseTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_variable, container, false);

        currentBloodGlucoseLevelCard = (Card) view.findViewById(R.id.card_current_blood_glucose_level);
        carbohydratesInMealCard = (Card) view.findViewById(R.id.card_carbohydrates_in_meal);
        suggestedInsulinDoseTextView = (TextView) view.findViewById(R.id.display_suggested_dose);

        return view;
    }

    @Override
    public void performCalculation() {
        float currentBloodGlucoseLevel = Float.valueOf(currentBloodGlucoseLevelCard.getStringFromEntry());
        float carbohydratesInMeal = Float.valueOf(carbohydratesInMealCard.getStringFromEntry());

        Calculator calculator = new Calculator(currentBloodGlucoseLevel, carbohydratesInMeal, getActivity());

        suggestedInsulinDoseTextView.setText(String.valueOf(calculator.getCalculatedInsulinDose()));
    }
}
