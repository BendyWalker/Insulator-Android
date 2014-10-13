package com.bendywalker.insulator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class VariableDataFragment extends Fragment implements Card.OnTextChangeListener
{

    Card currentBloodGlucoseLevelCard, carbohydratesInMealCard;
    TextView suggestedInsulinDoseTextView, carbohydrateDoseTextView, correctiveDoseTextView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_variable, container, false);

        currentBloodGlucoseLevelCard = (Card) view
                .findViewById(R.id.card_current_blood_glucose_level);
        carbohydratesInMealCard = (Card) view.findViewById(R.id.card_carbohydrates_in_meal);
        suggestedInsulinDoseTextView = (TextView) view.findViewById(R.id.suggested_dose);
        carbohydrateDoseTextView = (TextView) view.findViewById(R.id.carbohydrate_dose);
        correctiveDoseTextView = (TextView) view.findViewById(R.id.corrective_dose);

        currentBloodGlucoseLevelCard.setOnTextChangeListener(this);
        carbohydratesInMealCard.setOnTextChangeListener(this);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_reset:
                resetCards();
        }
        return false;
    }

    private void resetCards()
    {
        currentBloodGlucoseLevelCard.resetEntry();
        carbohydratesInMealCard.resetEntry();
        suggestedInsulinDoseTextView.setText("0.0");
        carbohydrateDoseTextView.setText("0.0");
        correctiveDoseTextView.setText("0.0");
    }

    @Override
    public void onTextChange()
    {
        float currentBloodGlucoseLevel = currentBloodGlucoseLevelCard.getFloatFromEntry();
        float carbohydratesInMeal = carbohydratesInMealCard.getFloatFromEntry();

        Calculator calculator = new Calculator(currentBloodGlucoseLevel, carbohydratesInMeal,
                                               getActivity());

        suggestedInsulinDoseTextView
                .setText(String.valueOf(calculator.getCalculatedInsulinDose(true)));
        carbohydrateDoseTextView.setText(String.valueOf(calculator.getCalculatedCarbohydrateDose(
                true)));
        correctiveDoseTextView
                .setText(String.valueOf(calculator.getCalculatedCorrectiveDose(true)));
    }
}
