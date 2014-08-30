package com.bendywalker.insulator;

import java.text.DecimalFormat;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ICRActivity extends Activity implements OnClickListener {

	// Declare variables
	public static final String PREFS_NAME = "savedPrefs";
	SharedPreferences savedValues;

	String importedTDD, exportedTDD, ratioString = "";

	double calculatedRatio, doubleTDD = 0;

	boolean ratioX2CBool, firstTimeOpen;

	RadioButton ratioI2X, ratioX2C;
	RadioGroup icrRadioGroup;

	EditText tddEditText;

	Button calculateButton, saveButton;

	TextView icrRatio, ratioInfo, tddInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_icr);

		// Prevents keyboard from being displayed when activity is opened
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// Retrieve saved values
		savedValues = getSharedPreferences(PREFS_NAME, 0);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Initalise variables
		firstTimeOpen = savedValues.getBoolean("firstTimeOpen", true);

		/// Buttons
		calculateButton = (Button) findViewById(R.id.button_calculate_icr);
		calculateButton.setEnabled(false);
		calculateButton.setOnClickListener(this);

		saveButton = (Button) findViewById(R.id.button_save_ratio);
		saveButton.setOnClickListener(this);
		saveButton.setText("Save & Return");

		/// Text entry field, with TextWatcher
		tddEditText = (EditText) findViewById(R.id.edittext_icr_tdd);
		tddEditText.setText(savedValues.getString("savedTDD", "0"));
		tddEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				enableButtons();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				enableButtons();
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				enableButtons();
			}

		});

		icrRatio = (TextView) findViewById(R.id.textview_icr_number);
		ratioInfo = (TextView) findViewById(R.id.info_ratio_icr);
		tddInfo = (TextView) findViewById(R.id.info_tdd_icr);

		/// Set ratioInfo based on preferred ratio
		if (savedValues.getBoolean("ratioX2C", true)) {
			ratioInfo.setText(R.string.info_ratiox2c_icr);
		} else {
			ratioInfo.setText(R.string.info_ratioi2x_icr);
		}

		if (firstTimeOpen) {
			saveButton.setText(R.string.button_save_continue);
			tddInfo.setText(R.string.info_tdd);
			getActionBar().setDisplayHomeAsUpEnabled(false); // Disable Up navigation
			
			this.setTitle("Set-Up 3/4");
		}

	}
	
	@Override
	public void onStart() {
		super.onStart();
		tddEditText.setText(savedValues.getString("savedTDD", "0"));
	}

	@Override
	public void onClick(View v) {
		// Retrieve saved values
		savedValues = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = savedValues.edit();
		DecimalFormat decimalFormatter = new DecimalFormat("0.0");

		switch(v.getId()) {

		case R.id.button_calculate_icr: /** Calculated insulin-to-carb ratio */
			// Restore preferred ratio
			ratioX2CBool = savedValues.getBoolean("ratioX2C", true);

			// Parse double from text entry field
			doubleTDD = Double.parseDouble(tddEditText.getText().toString());

			if (ratioX2CBool) {
				calculatedRatio = (doubleTDD / 500) * 10;
			} else {
				calculatedRatio = 500 / doubleTDD;
			}

			// Set icrRatio to calculatedRatio
			icrRatio.setText(decimalFormatter.format(calculatedRatio));

			saveButton.setEnabled(true);
			break;

		case R.id.button_save_ratio: /** Saves calculated ratio and places it in SharedPrefs */
			if (calculatedRatio == 0) {
				editor.putString("savedRatio", "");
			} else {
				editor.putString("savedRatio", decimalFormatter.format(calculatedRatio));
			}

			editor.putString("savedTDD", tddEditText.getText().toString());
			editor.commit();

			Intent i = new Intent(this, PersistentActivity.class);
			startActivity(i);
			break;
		}
	}

	/**
	 * Checks if the TDD entry field has been filled.
	 */
	public boolean isTDDFilled() {
		if (MainActivity.isReady(tddEditText)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Tried to enable both the calcuate and import buttons.
	 */
	public void enableButtons() {
		savedValues = getSharedPreferences(PREFS_NAME, 0);

		if (isTDDFilled()) {
			calculateButton.setEnabled(true);
		} else {
			calculateButton.setEnabled(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (!firstTimeOpen) {
			getMenuInflater().inflate(R.menu.icr, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_settings: /** Opens SettingsActivity */
			Intent j = new Intent(this, SettingsActivity.class);
			startActivity(j);
			return true;
			
		case R.id.action_activity_tdd: /** Opens TDDActivity */
			Intent i = new Intent(this, TDDActivity.class);
			startActivity(i);
			return true;
		}
		return false;
	}
}
