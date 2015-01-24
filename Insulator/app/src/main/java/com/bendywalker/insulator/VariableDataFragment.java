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
import android.widget.LinearLayout;
import android.widget.TextView;


public class VariableDataFragment extends Fragment implements Card.OnTextChangeListener
{
    Card currentBloodGlucoseLevelCard, carbohydratesInMealCard;
    TextView suggestedInsulinDoseTextView, carbohydrateDoseTextView, correctiveDoseTextView;
    LinearLayout root;

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
        root = (LinearLayout) view.findViewById(R.id.root);

        currentBloodGlucoseLevelCard.setOnTextChangeListener(this);
        carbohydratesInMealCard.setOnTextChangeListener(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.variable, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_reset:
                resetCards();
                break;

            case R.id.action_factor_suggestion:
                getActivity()
                        .startActivity(new Intent(getActivity(), FactorSuggestionActivity.class));
                break;
        }

        return false;
    }

    @Override
    public void onResume()
    {
        resetCards();

        super.onResume();
    }

    private void resetCards()
    {
        currentBloodGlucoseLevelCard.resetEntry();
        carbohydratesInMealCard.resetEntry();
        suggestedInsulinDoseTextView.setText("0.0");
        carbohydrateDoseTextView.setText("0.0");
        correctiveDoseTextView.setText("0.0");
        root.requestFocus();
    }

    @Override
    public void onTextChange()
    {
        float currentBloodGlucoseLevel = currentBloodGlucoseLevelCard.getFloatFromEntry();
        float carbohydratesInMeal = carbohydratesInMealCard.getFloatFromEntry();

        Calculator calculator = new Calculator(currentBloodGlucoseLevel, carbohydratesInMeal,
                                               getActivity());

        suggestedInsulinDoseTextView
                .setText(String.valueOf(calculator.getSuggestedDose(true)));

        carbohydrateDoseTextView.setText(String.valueOf(calculator.getCarbohydrateDose(
                true)));

        correctiveDoseTextView
                .setText(String.valueOf(calculator.getCorrectiveDose(true)));
    }
}
