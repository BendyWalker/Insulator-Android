package com.bendywalker.insulator;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class TDDActivity extends Activity implements OnClickListener, TextWatcher {

	// Declare variables
	public static final String PREFS_NAME = "savedPrefs";
	SharedPreferences savedValues;

	double TDD, breakfastDose, lunchDose, dinnerDose, supperDose, longDose;

	boolean firstTimeOpen;

	Button saveTDDButton, saveBreakfastButton, saveLunchButton, saveDinnerButton, saveSupperButton, saveLongButton, saveCalculationButton;

	EditText TDDEditText, breakfast1EditText, breakfast2EditText, breakfast3EditText, breakfast4EditText, breakfast5EditText, 
	breakfast6EditText, breakfast7EditText, lunch1EditText, lunch2EditText, lunch3EditText, lunch4EditText, lunch5EditText, 
	lunch6EditText, lunch7EditText, dinner1EditText, dinner2EditText, dinner3EditText, dinner4EditText, dinner5EditText, 
	dinner6EditText, dinner7EditText, supper1EditText, supper2EditText, supper3EditText, supper4EditText, supper5EditText, 
	supper6EditText, supper7EditText, longEditText;

	LinearLayout TDDSection, breakfastSection, lunchSection, dinnerSection, supperSection, longSection, calculationSection;

	TextView TDDNumber;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tdd);

		// Prevents keyboard from being displayed when activity is opened
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// Retrieve saved values
		savedValues = getSharedPreferences(PREFS_NAME, 0);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Initalise variables
		firstTimeOpen = savedValues.getBoolean("firstTimeOpen", true);

		/// Buttons
		saveTDDButton = (Button) findViewById(R.id.button_save_tdd_entry);
		saveTDDButton.setEnabled(false);
		saveTDDButton.setOnClickListener(this);

		saveBreakfastButton = (Button) findViewById(R.id.button_save_tdd_breakfast);
		saveBreakfastButton.setEnabled(false);
		saveBreakfastButton.setOnClickListener(this);

		saveLunchButton = (Button) findViewById(R.id.button_save_tdd_lunch);
		saveLunchButton.setEnabled(false);
		saveLunchButton.setOnClickListener(this);

		saveDinnerButton = (Button) findViewById(R.id.button_save_tdd_dinner);
		saveDinnerButton.setEnabled(false);
		saveDinnerButton.setOnClickListener(this);

		saveSupperButton = (Button) findViewById(R.id.button_save_tdd_supper);
		saveSupperButton.setEnabled(false);
		saveSupperButton.setOnClickListener(this);

		saveLongButton = (Button) findViewById(R.id.button_save_tdd_long);
		saveLongButton.setEnabled(false);
		saveLongButton.setOnClickListener(this);

		saveCalculationButton = (Button) findViewById(R.id.button_save_tdd_calculation);
		saveCalculationButton.setOnClickListener(this);

		/// EditText Fields
		TDDEditText = (EditText) findViewById(R.id.edittext_tdd_entry);
		TDDEditText.addTextChangedListener(this);

		breakfast1EditText = (EditText) findViewById(R.id.edittext_tdd_breakfast_1);
		breakfast1EditText.addTextChangedListener(this);

		breakfast2EditText = (EditText) findViewById(R.id.edittext_tdd_breakfast_2);
		breakfast2EditText.addTextChangedListener(this);

		breakfast3EditText = (EditText) findViewById(R.id.edittext_tdd_breakfast_3);
		breakfast3EditText.addTextChangedListener(this);

		breakfast4EditText = (EditText) findViewById(R.id.edittext_tdd_breakfast_4);
		breakfast4EditText.addTextChangedListener(this);

		breakfast5EditText = (EditText) findViewById(R.id.edittext_tdd_breakfast_5);
		breakfast5EditText.addTextChangedListener(this);

		breakfast6EditText = (EditText) findViewById(R.id.edittext_tdd_breakfast_6);
		breakfast6EditText.addTextChangedListener(this);

		breakfast7EditText = (EditText) findViewById(R.id.edittext_tdd_breakfast_7);
		breakfast7EditText.addTextChangedListener(this);

		lunch1EditText = (EditText) findViewById(R.id.edittext_tdd_lunch_1);
		lunch1EditText.addTextChangedListener(this);

		lunch2EditText = (EditText) findViewById(R.id.edittext_tdd_lunch_2);
		lunch2EditText.addTextChangedListener(this);

		lunch3EditText = (EditText) findViewById(R.id.edittext_tdd_lunch_3);
		lunch3EditText.addTextChangedListener(this);

		lunch4EditText = (EditText) findViewById(R.id.edittext_tdd_lunch_4);
		lunch4EditText.addTextChangedListener(this);

		lunch5EditText = (EditText) findViewById(R.id.edittext_tdd_lunch_5);
		lunch5EditText.addTextChangedListener(this);

		lunch6EditText = (EditText) findViewById(R.id.edittext_tdd_lunch_6);
		lunch6EditText.addTextChangedListener(this);

		lunch7EditText = (EditText) findViewById(R.id.edittext_tdd_lunch_7);
		lunch7EditText.addTextChangedListener(this);

		dinner1EditText = (EditText) findViewById(R.id.edittext_tdd_dinner_1);
		dinner1EditText.addTextChangedListener(this);

		dinner2EditText = (EditText) findViewById(R.id.edittext_tdd_dinner_2);
		dinner2EditText.addTextChangedListener(this);

		dinner3EditText = (EditText) findViewById(R.id.edittext_tdd_dinner_3);
		dinner3EditText.addTextChangedListener(this);

		dinner4EditText = (EditText) findViewById(R.id.edittext_tdd_dinner_4);
		dinner4EditText.addTextChangedListener(this);

		dinner5EditText = (EditText) findViewById(R.id.edittext_tdd_dinner_5);
		dinner5EditText.addTextChangedListener(this);

		dinner6EditText = (EditText) findViewById(R.id.edittext_tdd_dinner_6);
		dinner6EditText.addTextChangedListener(this);

		dinner7EditText = (EditText) findViewById(R.id.edittext_tdd_dinner_7);
		dinner7EditText.addTextChangedListener(this);

		supper1EditText = (EditText) findViewById(R.id.edittext_tdd_supper_1);
		supper1EditText.addTextChangedListener(this);

		supper2EditText = (EditText) findViewById(R.id.edittext_tdd_supper_2);
		supper2EditText.addTextChangedListener(this);

		supper3EditText = (EditText) findViewById(R.id.edittext_tdd_supper_3);
		supper3EditText.addTextChangedListener(this);

		supper4EditText = (EditText) findViewById(R.id.edittext_tdd_supper_4);
		supper4EditText.addTextChangedListener(this);

		supper5EditText = (EditText) findViewById(R.id.edittext_tdd_supper_5);
		supper5EditText.addTextChangedListener(this);

		supper6EditText = (EditText) findViewById(R.id.edittext_tdd_supper_6);
		supper6EditText.addTextChangedListener(this);

		supper7EditText = (EditText) findViewById(R.id.edittext_tdd_supper_7);
		supper7EditText.addTextChangedListener(this);

		longEditText = (EditText) findViewById(R.id.edittext_tdd_long);
		longEditText.addTextChangedListener(this);

		/// TextViews
		TDDNumber = (TextView) findViewById(R.id.textview_display_tdd_number);

		/// LinearLayouts
		breakfastSection = (LinearLayout) findViewById(R.id.section_tdd_breakfast);
		lunchSection = (LinearLayout) findViewById(R.id.section_tdd_lunch);
		dinnerSection = (LinearLayout) findViewById(R.id.section_tdd_dinner);
		supperSection = (LinearLayout) findViewById(R.id.section_tdd_supper);
		longSection = (LinearLayout) findViewById(R.id.section_tdd_long);
		calculationSection = (LinearLayout) findViewById(R.id.section_calculate_tdd);
		TDDSection = (LinearLayout) findViewById(R.id.section_tdd_tdd);

		// What to do if first time application opened
		if (firstTimeOpen) {
			TDDSection.setVisibility(View.VISIBLE);
			saveTDDButton.setText(R.string.button_save_continue);
			saveCalculationButton.setText(R.string.button_save_continue);
			getActionBar().setDisplayHomeAsUpEnabled(false); // Disable Up navigation
			this.setTitle("Set-Up 2/4");
		}
	}

	@Override
	public void onClick(View v) {
		// Retrieve saved values
		savedValues = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = savedValues.edit();

		switch (v.getId()) {

		case R.id.button_save_tdd_entry: /** Saves manually entered TDD to SharedPrefs */
			editor.putString("savedTDD", TDDEditText.getText().toString());
			editor.commit();
			if (firstTimeOpen) {
				Intent i = new Intent(this, ICRActivity.class);
				startActivity(i);
			} else {
				finish();
			}
			break;

		case R.id.button_save_tdd_breakfast: /** Calcuates breakfast dose average, then moves onto lunch entry */
			breakfastDose = (Double.parseDouble(breakfast1EditText.getText().toString()) + (Double.parseDouble(breakfast2EditText.getText().toString()) +
					(Double.parseDouble(breakfast3EditText.getText().toString()) + (Double.parseDouble(breakfast4EditText.getText().toString()) +
							(Double.parseDouble(breakfast5EditText.getText().toString()) + (Double.parseDouble(breakfast6EditText.getText().toString()) + 
									(Double.parseDouble(breakfast7EditText.getText().toString()) / 7)))))));
			breakfastDose = breakfastDose / 7;
			breakfastSection.setVisibility(View.GONE);
			lunchSection.setVisibility(View.VISIBLE);
			break;

		case R.id.button_save_tdd_lunch: /** Calcuates lunch dose average, then moves onto dinner entry */
			lunchDose = (Double.parseDouble(lunch1EditText.getText().toString()) + (Double.parseDouble(lunch2EditText.getText().toString()) +
					(Double.parseDouble(lunch3EditText.getText().toString()) + (Double.parseDouble(lunch4EditText.getText().toString()) +
							(Double.parseDouble(lunch5EditText.getText().toString()) + (Double.parseDouble(lunch6EditText.getText().toString()) + 
									(Double.parseDouble(lunch7EditText.getText().toString()) / 7)))))));
			lunchDose = lunchDose / 7;
			lunchSection.setVisibility(View.GONE);
			dinnerSection.setVisibility(View.VISIBLE);
			break;

		case R.id.button_save_tdd_dinner: /** Calcuates dinner dose average, then moves onto supper entry */
			dinnerDose = (Double.parseDouble(dinner1EditText.getText().toString()) + (Double.parseDouble(dinner2EditText.getText().toString()) +
					(Double.parseDouble(dinner3EditText.getText().toString()) + (Double.parseDouble(dinner4EditText.getText().toString()) +
							(Double.parseDouble(dinner5EditText.getText().toString()) + (Double.parseDouble(dinner6EditText.getText().toString()) + 
									(Double.parseDouble(dinner7EditText.getText().toString()))))))));
			dinnerDose = dinnerDose / 7;
			dinnerSection.setVisibility(View.GONE);
			supperSection.setVisibility(View.VISIBLE);
			break;

		case R.id.button_save_tdd_supper: /** Calcuates supper dose average, then moves onto long-acting dose entry */
			supperDose = (Double.parseDouble(supper1EditText.getText().toString()) + (Double.parseDouble(supper2EditText.getText().toString()) +
					(Double.parseDouble(supper3EditText.getText().toString()) + (Double.parseDouble(supper4EditText.getText().toString()) +
							(Double.parseDouble(supper5EditText.getText().toString()) + (Double.parseDouble(supper6EditText.getText().toString()) + 
									(Double.parseDouble(supper7EditText.getText().toString()) / 7)))))));
			supperDose = supperDose / 7;
			supperSection.setVisibility(View.GONE);
			longSection.setVisibility(View.VISIBLE);
			break;

		case R.id.button_save_tdd_long: /** Adds all mealtime dose averages together, then adds long-acting dose to it to formulate TDD */
			longDose = Double.parseDouble(longEditText.getText().toString());
			Log.w("doses", Double.toString(breakfastDose) + "," + Double.toString(lunchDose) + "," + Double.toString(dinnerDose) + "," + Double.toString(supperDose) + "," + Double.toString(longDose));
			TDD = breakfastDose + lunchDose + dinnerDose + supperDose + longDose;
			Log.w("TDD", Double.toString(TDD));
			TDDNumber.setText(Long.toString(Math.round(TDD)));
			longSection.setVisibility(View.GONE);
			calculationSection.setVisibility(View.VISIBLE);
			break;

		case R.id.button_save_tdd_calculation: /** Saves calculated TDD to SharedPrefs, moves onto next activity */
			Log.w("calc pressed", "pressed");
			editor.putString("savedTDD", Long.toString(Math.round(TDD)));
			editor.commit();
			Intent j = new Intent(this, ICRActivity.class);
			startActivity(j);
			break;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		enableButtons();

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		enableButtons();

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		enableButtons();

	}

	/**
	 * Enables and disables buttons based on whether relevant fields are filled
	 */
	public void enableButtons() {
		if (isBreakfastFilled()) {
			saveBreakfastButton.setEnabled(true);
		} else {
			saveBreakfastButton.setEnabled(false);
		}
		if (isLunchFilled()) {
			saveLunchButton.setEnabled(true);
		} else {
			saveLunchButton.setEnabled(false);
		}
		if (isDinnerFilled()) {
			saveDinnerButton.setEnabled(true);
		} else {
			saveDinnerButton.setEnabled(false);
		}
		if (isSupperFilled()) {
			saveSupperButton.setEnabled(true);
		} else {
			saveSupperButton.setEnabled(false);
		}
		if (isLongFilled()) {
			saveLongButton.setEnabled(true);
		} else {
			saveLongButton.setEnabled(false);
		}
		if (isTDDFilled()) {
			saveTDDButton.setEnabled(true);
		} else {
			saveTDDButton.setEnabled(false);
		}

	}

	/**
	 * Checks if all 7 breakfast dose fields have been filled.
	 * @return Returns True if all fields are filled, and False if not.
	 */
	public boolean isBreakfastFilled() {
		if(MainActivity.isReady(breakfast1EditText) && MainActivity.isReady(breakfast2EditText) 
				&& MainActivity.isReady(breakfast3EditText) && MainActivity.isReady(breakfast4EditText) && 
				MainActivity.isReady(breakfast5EditText) && MainActivity.isReady(breakfast6EditText) &&
				MainActivity.isReady(breakfast7EditText)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if all 7 lunch dose fields have been filled.
	 * @return Returns True if all fields are filled, and False if not.
	 */
	public boolean isLunchFilled() {
		if(MainActivity.isReady(lunch1EditText) && MainActivity.isReady(lunch2EditText) 
				&& MainActivity.isReady(lunch3EditText) && MainActivity.isReady(lunch4EditText) && 
				MainActivity.isReady(lunch5EditText) && MainActivity.isReady(lunch6EditText) &&
				MainActivity.isReady(lunch7EditText)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if all 7 dinner dose fields have been filled.
	 * @return Returns True if all fields are filled, and False if not.
	 */
	public boolean isDinnerFilled() {
		if(MainActivity.isReady(dinner1EditText) && MainActivity.isReady(dinner2EditText) 
				&& MainActivity.isReady(dinner3EditText) && MainActivity.isReady(dinner4EditText) && 
				MainActivity.isReady(dinner5EditText) && MainActivity.isReady(dinner6EditText) &&
				MainActivity.isReady(dinner7EditText)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if all 7 supper dose fields have been filled.
	 * @return Returns True if all fields are filled, and False if not.
	 */
	public boolean isSupperFilled() {
		if(MainActivity.isReady(supper1EditText) && MainActivity.isReady(supper2EditText) 
				&& MainActivity.isReady(supper3EditText) && MainActivity.isReady(supper4EditText) && 
				MainActivity.isReady(supper5EditText) && MainActivity.isReady(supper6EditText) &&
				MainActivity.isReady(supper7EditText)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if the long-acting dose field has been filled.
	 * @return Returns True if field has been filled, and False if not.
	 */
	public boolean isLongFilled() {
		if (MainActivity.isReady(longEditText)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isTDDFilled() {
		if (MainActivity.isReady(TDDEditText)) {
			return true;
		} else {
			return false;
		}
	}

}