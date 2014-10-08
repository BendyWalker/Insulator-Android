package com.bendywalker.insulator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

public class SettingsActivity extends Activity implements OnClickListener {

	// Declare variables
	public static final String PREFS_NAME = "savedPrefs";
	SharedPreferences savedValues;

	boolean ratioX2Cbool, mmolBool, halfUnitsBool, firstTimeOpen;

	RadioButton ratioI2X, ratioX2C, measureMMOL, measureMGDL;
	RadioGroup icrRadioGroup, measureRadioGroup;

	Button saveButton;

	Switch halfUnitsSwitch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		// Retrieve saved values
		savedValues = getSharedPreferences(PREFS_NAME, 0);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Initialise variables
		firstTimeOpen = savedValues.getBoolean("firstTimeOpen", true);

		/// Radio Buttons
		//// Blood Measurement
		measureMMOL = (RadioButton) findViewById(R.id.radio_button_mmol);
		onRadioButtonClicked(measureMMOL);

		measureMGDL = (RadioButton) findViewById(R.id.radio_button_mgdl);
		onRadioButtonClicked(measureMGDL);

		measureRadioGroup = (RadioGroup) findViewById(R.id.radio_group_bloodmeasurement);

		//// Preferred Ratio
		ratioI2X = (RadioButton) findViewById(R.id.radio_button_insulin2X);
		onRadioButtonClicked(ratioI2X);

		ratioX2C = (RadioButton) findViewById(R.id.radio_button_carbs2X);
		onRadioButtonClicked(ratioX2C);

		icrRadioGroup = (RadioGroup) findViewById(R.id.radio_group_icr);

		/// Half Units Switch
		halfUnitsSwitch = (Switch) findViewById(R.id.switch_halfunits);
		halfUnitsSwitch.setOnClickListener(this);

		checkSettings();

		/// Save Button
		saveButton = (Button) findViewById(R.id.button_save_settings);
		saveButton.setOnClickListener(this);

		// Open Welcome activity if firstTimeOpen is true
		if (firstTimeOpen) {
			getActionBar().setDisplayHomeAsUpEnabled(false);
			saveButton.setVisibility(View.VISIBLE);
			this.setTitle("Set-Up 1/4");
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		checkSettings();
	}

	public void onRadioButtonClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		// Retrieve saved values
		savedValues = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = savedValues.edit();

		// Check which radio button was clicked
		switch(view.getId()) {

		case R.id.radio_button_carbs2X:
			if (checked) {
				editor.putBoolean("ratioX2C", true);
				editor.commit();
			}
			break;

		case R.id.radio_button_insulin2X:
			if (checked) {
				editor.putBoolean("ratioX2C", false);
				editor.commit();
			}
			break;

		case R.id.radio_button_mmol:
			if (checked) {
				editor.putBoolean("mmol", true);
				editor.commit();
			}
			break;

		case R.id.radio_button_mgdl:
			if (checked) {
				editor.putBoolean("mmol", false);
				editor.commit();
			}
			break;
		}

	}

	@Override
	public void onClick(View v) {
		savedValues = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = savedValues.edit();

		switch (v.getId()) {

		case R.id.button_save_settings:
			//Intent i = new Intent(this, TDDActivity.class);
			//startActivity(i);

		case R.id.switch_halfunits:
			if (halfUnitsSwitch.isChecked()) {
				editor.putBoolean("halfunits", true);
				editor.commit();
			} else {
				editor.putBoolean("halfunits", false);
				editor.commit();
			}
		}	
	}

	/**
	 * Checks and unchecks the different settings options based on SharedPreferences.
	 */
	public void checkSettings() {
		savedValues = getSharedPreferences(PREFS_NAME, 0);

		mmolBool = savedValues.getBoolean("mmol", true);
		ratioX2Cbool = savedValues.getBoolean("ratioX2C", true);
		halfUnitsBool = savedValues.getBoolean("halfunits", false);

		if (mmolBool) {
			measureMMOL.setChecked(true);
		} else {
			measureMGDL.setChecked(true);
		}

		if (ratioX2Cbool) {
			ratioX2C.setChecked(true);
		} else {
			ratioI2X.setChecked(true);
		}

		halfUnitsSwitch.setChecked(halfUnitsBool);


		Log.w("halfunits after check settings", Boolean.toString(savedValues.getBoolean("halfunits", false)));
	}

}
