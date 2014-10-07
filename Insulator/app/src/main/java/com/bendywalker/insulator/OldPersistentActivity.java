package com.bendywalker.insulator;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class OldPersistentActivity extends Activity implements OnClickListener, TextWatcher
{

	// Declare variables
	public static final String PREFS_NAME = "savedPrefs";
	SharedPreferences savedValues;

	String TDD, ratio, desired = "";
	boolean firstTimeOpen, boolTDD, boolRatio, boolDesired, textChangeRunning = false;

	Button saveButton;

	TextView ratioInfo, desiredInfo;

	EditText tddEditText, ratioEditText, desiredEditText;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_persistent_old);

		// Prevents keyboard from being displayed when activity is opened
		this.getWindow()
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// Retrieve saved values
		savedValues = getSharedPreferences(PREFS_NAME, 0);

		// Initialise variables
		firstTimeOpen = savedValues.getBoolean("firstTimeOpen", true);

		// / Save Button
		saveButton = (Button) findViewById(R.id.button_save_persistent);
		saveButton.setOnClickListener(this);
		saveButton.setEnabled(false);

		ratioInfo = (TextView) findViewById(R.id.info_ratio);
		desiredInfo = (TextView) findViewById(R.id.info_desired_persistent);

		// / EditText Fields with TextWatchers
		// / Save Button is activated only when all three fields are filled
		tddEditText = (EditText) findViewById(R.id.edittext_tdd);
		ratioEditText = (EditText) findViewById(R.id.edittext_ratio);
		desiredEditText = (EditText) findViewById(R.id.edittext_desired_persistent);

		// // tddEditText
		// /// Data is restored from savedValues SharedPreferences
		String restoredTDD = savedValues.getString("savedTDD", "");
		tddEditText.setText(restoredTDD);
		enableSaveButton();
		// /// TextWatcher added to text field
		tddEditText.addTextChangedListener(this);

		// // ratioEditText
		// /// Data is restored from savedValues SharedPreferences
		String restoredRatio = savedValues.getString("savedRatio", "");
		ratioEditText.setText(restoredRatio);
		enableSaveButton();
		// /// TextWatcher added to text field
		ratioEditText.addTextChangedListener(this);

		// // desiredEditText
		// /// Data is restored from savedValues SharedPreferences
		String restoredDesired = savedValues.getString("savedDesired", "");
		desiredEditText.setText(restoredDesired);
		enableSaveButton();
		// /// TextWatcher added to text field
		desiredEditText.addTextChangedListener(this);

		// / Set ratioInfo based on preferred ratio
		if (savedValues.getBoolean("ratioX2C", true))
		{
			ratioInfo.setText(R.string.info_ratiox2c);
			ratioEditText.setHint(R.string.hint_units);
		}
		else
		{
			ratioInfo.setText(R.string.info_ratioi2x);
			ratioEditText.setHint(R.string.hint_grams);
		}

		setMMOLorMGDL();

		// Based on state of firstTimeOpen, change text on saveButton
		if (firstTimeOpen)
		{
			saveButton.setText(R.string.button_save_continue);
			getActionBar().setDisplayHomeAsUpEnabled(false); // Disable Up navigation
			this.setTitle("Set-Up 4/4");
		}
		else
		{
			saveButton.setText(R.string.button_save_return);
			getActionBar().setDisplayHomeAsUpEnabled(true); // Enable Up navigation
		}

		enableSaveButton();
	}


	@Override
	public void onStart()
	{
		super.onStart();
		tddEditText.setText(savedValues.getString("savedTDD", "0"));
		setMMOLorMGDL();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		if (!firstTimeOpen)
		{
			getMenuInflater().inflate(R.menu.persistent, menu);
		}
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId())
		{
			case R.id.action_activity_tdd:
				/** Opens TDDActivity */
				Intent k = new Intent(this, TDDActivity.class);
				startActivity(k);
				return true;

			case R.id.action_activity_icr:
				/** Opens PersistentActivity */
				Intent i = new Intent(this, ICRActivity.class);
				startActivity(i);
				return true;

			case R.id.action_settings:
				/** Opens SettingsActivity */
				Intent j = new Intent(this, SettingsActivity.class);
				startActivity(j);
				return true;
		}
		return false;
	}


	@Override
	public void onClick(View v)
	{

		// Retrieve saved values and create an editor
		savedValues = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = savedValues.edit();

		// Create a switch to determine which button has been pressed
		switch (v.getId())
		{

			case R.id.button_save_persistent:
				/** Saves values entered into text fields to SharedPreferences */
				saveData();

				editor.putBoolean("firstTimeOpen", false);
				editor.commit();

				Intent i = new Intent(this, OldMainActivity.class);
				startActivity(i);
				break;
		}
	}


	@Override
	public void afterTextChanged(Editable s)
	{
		enableSaveButton();
	}


	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
		enableSaveButton();
	}


	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		enableSaveButton();

		if (desiredEditText.hasFocus())
		{
			if ((desiredEditText.length() > 0) && (savedValues.getBoolean("mmol", true)))
			{
				OldMainActivity.addDecimalPlace(s, desiredEditText);
			}
		}
		else if (ratioEditText.hasFocus())
		{
			if (ratioEditText.length() > 0)
			{
				OldMainActivity.addDecimalPlace(s, ratioEditText);
			}
		}
	}


	/**
	 * Checks if tddEditText has been filled.
	 * 
	 * @return True if tddEditText has been filled, and False if not.
	 */
	public boolean isTDDFilled()
	{
		if (OldMainActivity.isReady(tddEditText))
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	/**
	 * Checks if ratioEditText has been filled.
	 * 
	 * @return True if ratioEditText has been filled, and False if not.
	 */
	public boolean isRatioFilled()
	{
		if (OldMainActivity.isReady(ratioEditText))
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	/**
	 * Checks if desiredEditText has been filled.
	 * 
	 * @return True if desiredEditText has been filled, and False if not.
	 */
	public boolean isDesiredFilled()
	{
		if (OldMainActivity.isReady(desiredEditText))
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	/**
	 * Enables SaveButton if the three 'isFilled' methods return true.
	 */
	public void enableSaveButton()
	{
		if (isTDDFilled() && isRatioFilled() && isDesiredFilled())
		{
			saveButton.setEnabled(true);
		}
		else
		{
			saveButton.setEnabled(false);
		}
	}


	/**
	 * Saves data entered into text fields to SharedPreferences.
	 */
	public void saveData()
	{
		// Retrieve saved values and create an editor
		savedValues = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = savedValues.edit();

		// Parses data from text fields and saves it
		if (!isTDDFilled())
		{
			TDD = "0";
		}
		else
		{
			TDD = tddEditText.getText()
					.toString();
			editor.putString("savedTDD", TDD);
		}

		if (!isRatioFilled())
		{
			ratio = "0";
		}
		else
		{
			ratio = ratioEditText.getText()
					.toString();
			editor.putString("savedRatio", ratio);
		}

		if (!isDesiredFilled())
		{
			desired = "0";
		}
		else
		{
			desired = desiredEditText.getText()
					.toString();
			editor.putString("savedDesired", desired);
		}

		editor.commit();
	}


	/**
	 * Sets current and desired blood sugar fields hint and info to mmol or mgdl based on settings
	 * preference.
	 */
	public void setMMOLorMGDL()
	{
		// Retrieve saved values
		savedValues = getSharedPreferences(PREFS_NAME, 0);

		// Set mmol or mgdl based on blood measurement preference
		if (savedValues.getBoolean("mmol", true))
		{
			desiredEditText.setHint(R.string.hint_mmol);
			desiredInfo.setText(R.string.info_desired_mmol);
		}
		else
		{
			desiredEditText.setHint(R.string.hint_mgdl);
			desiredInfo.setText(R.string.info_desired_mgdl);
		}
	}


	/**
	 * Adds a point to one decimal place of a string of characters.
	 * 
	 * @param s
	 * @param et
	 */
	public void addDecimalPlace(CharSequence s, EditText et)
	{
		List<Integer> storedPoints = new ArrayList<Integer>();

		if (!textChangeRunning)
		{
			textChangeRunning = true;

			StringBuilder decimalPlaceAuto = new StringBuilder(s.toString());

			while (decimalPlaceAuto.length() > 2 && decimalPlaceAuto.charAt(0) == '0'
					|| decimalPlaceAuto.charAt(0) == '.')
			{
				decimalPlaceAuto.deleteCharAt(0);
			}

			for (int i = 0; i < decimalPlaceAuto.length(); i++)
			{
				if (decimalPlaceAuto.charAt(i) == '.')
				{
					storedPoints.add(i);
				}
			}

			for (int i = 0; i < storedPoints.size(); i++)
			{
				decimalPlaceAuto.deleteCharAt(storedPoints.get(i));
			}

			while (decimalPlaceAuto.length() < 2)
			{
				decimalPlaceAuto.insert(0, '0');
			}

			decimalPlaceAuto.insert(decimalPlaceAuto.length() - 1, '.');

			et.setText(decimalPlaceAuto.toString());

			textChangeRunning = false;

			Selection.setSelection(et.getText(), decimalPlaceAuto.toString()
					.length());

		}
	}
}