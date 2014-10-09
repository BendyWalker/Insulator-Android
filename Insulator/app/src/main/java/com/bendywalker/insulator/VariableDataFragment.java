package com.bendywalker.insulator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class VariableDataFragment extends Fragment {
    //implements Card.OnTextChangeListener

    Card currentBloodGlucoseLevelCard, carbohydratesInMealCard;
    TextView suggestedInsulinDoseTextView;
    Button calculateButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_variable, container, false);

        currentBloodGlucoseLevelCard = (Card) view.findViewById(R.id.card_current_blood_glucose_level);
        carbohydratesInMealCard = (Card) view.findViewById(R.id.card_carbohydrates_in_meal);
        suggestedInsulinDoseTextView = (TextView) view.findViewById(R.id.display_suggested_dose);
        calculateButton = (Button) view.findViewById(R.id.button_calculate);

        calculateButton.setOnClickListener(new MyOnClickListener());

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                resetCards();
        }
        return false;
    }

    private void resetCards() {
        currentBloodGlucoseLevelCard.resetEntry();
        carbohydratesInMealCard.resetEntry();
        suggestedInsulinDoseTextView.setText("0.0");
    }

//    @Override
//    public void onTextChange() {
//        Calculator calculator = new Calculator(currentBloodGlucoseLevelCard.getFloatFromEntry(), carbohydratesInMealCard.getFloatFromEntry(), getActivity());
//        suggestedInsulinDoseTextView.setText(String.valueOf(calculator.getCalculatedInsulinDose()));
//    }

    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Calculator calculator = new Calculator(currentBloodGlucoseLevelCard.getFloatFromEntry(), carbohydratesInMealCard.getFloatFromEntry(), getActivity());
            suggestedInsulinDoseTextView.setText(String.valueOf(calculator.getCalculatedInsulinDose()));
        }
    }
}
