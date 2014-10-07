package com.bendywalker.insulator;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class OldMainActivity extends Activity implements OnClickListener {

	// Declare variables
	public static final String PREFS_NAME = "savedPrefs";
	SharedPreferences savedValues;

	double carbs, ratio, TDD, current, desired, ratioCalculation, correctionCalculation,
	previousCalculation, bolusOnBoard, previousBolusOnBoard, bolusAgainstCarbs,
	total, totalRounded, totalCarbs = 0;

	long dateTimeDiffMillis, dateTimeDiffMins = 0;

	boolean firstTimeOpen, firstTimeCal, calculated, ratioX2C, halfunits;
	static boolean textChangeRunning;
	boolean carbTally = false;

	List<Double> carbArray = new ArrayList<Double>();
	List<Long> timeArray = new ArrayList<Long>();

	Button calculateButton, resetButton;

	EditText desiredEditText, currentEditText, carbsEditText;

	TextView insulinAmount, breakdownLabel, carbDose, correctionDose, bolusDose, carbsOnBoard, calculationTime, totalNumber, desiredInfo, currentInfo, carbsInfo;

	LinearLayout breakdownSection, insulinDoseSection, totalBreakdownSection;

	Calendar currentDateTimeCal, previousDateTimeCal, bloodTimeCal;

	SimpleDateFormat dateFormatter = new SimpleDateFormat("h:mm a");

	DecimalFormat decimalFormatter = new DecimalFormat("0.0");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_old);

		// Prevents keyboard from being displayed when activity is opened
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// Retrieve saved values
		savedValues = getSharedPreferences(PREFS_NAME, 0);

		// Initialise variables
		firstTimeOpen = savedValues.getBoolean("firstTimeOpen", true);
		firstTimeCal = savedValues.getBoolean("firstTimeCal", true);
		halfunits = savedValues.getBoolean("halfunits", false);

		/// Buttons
		calculateButton = (Button) findViewById(R.id.button_calculate_main);
		calculateButton.setOnClickListener(this);
		calculateButton.setEnabled(false);

		resetButton = (Button) findViewById(R.id.button_reset);
		resetButton.setOnClickListener(this);
		resetButton.setEnabled(false);

		/// EditText fields with TextWatchers
		/// Calculate & Reset Buttons are activated only when all three fields are filled
		desiredEditText = (EditText) findViewById(R.id.edittext_desired_main);
		currentEditText = (EditText) findViewById(R.id.edittext_current);
		carbsEditText = (EditText) findViewById(R.id.edittext_carbs);

		//// desiredEditText
		///// Data is restored from savedValues SharedPreferences
		String restoredDesired = savedValues.getString("savedDesired", "");
		desiredEditText.setText(restoredDesired);

		///// Add TextWatcher
		desiredEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				enableCalculateButton();
				enableResetButton();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				enableCalculateButton();
				enableResetButton();

			}

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				enableCalculateButton();
				enableResetButton();
				if ((desiredEditText.length() > 0) && (savedValues.getBoolean("mmol", true))) {
					addDecimalPlace(s, desiredEditText);
				}
			}
		});

		//// currentEditText
		///// Add TextWatcher
		currentEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				enableCalculateButton();
				enableResetButton();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				enableCalculateButton();
				enableResetButton();

			}

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				enableCalculateButton();
				enableResetButton();
				if ((currentEditText.length() > 0) && (savedValues.getBoolean("mmol", true))) {
						addDecimalPlace(s, currentEditText);
					}
				}
		});

		//// carbsEditText
		///// Add TextWatcher
		carbsEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				enableCalculateButton();
				enableResetButton();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				enableCalculateButton();
				enableResetButton();

			}

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				enableCalculateButton();
				enableResetButton();
			}

		});

		/// LinearLayouts
		breakdownSection = (LinearLayout) findViewById(R.id.section_breakdown);
		insulinDoseSection = (LinearLayout) findViewById(R.id.section_calculation_insulin);
		totalBreakdownSection = (LinearLayout) findViewById(R.id.section_breakdown_total);

		/// TextViews
		desiredInfo = (TextView) findViewById(R.id.info_desired_main);
		currentInfo = (TextView) findViewById(R.id.info_current);

		insulinAmount = (TextView) findViewById(R.id.textview_amount);

		breakdownLabel = (TextView) findViewById(R.id.textview_breakdown);

		carbDose = (TextView) findViewById(R.id.textview_dose_carbs_number);
		carbDose.setText(decimalFormatter.format(Double.parseDouble(savedValues.getString("savedCarbDose", "0"))));

		correctionDose = (TextView) findViewById(R.id.textview_dose_corrective_number);
		correctionDose.setText(decimalFormatter.format(Double.parseDouble(savedValues.getString("savedCorrectionDose", "0"))));

		bolusDose = (TextView) findViewById(R.id.textview_onboard_bolus_number);
		bolusDose.setText("-" + decimalFormatter.format(Double.parseDouble(savedValues.getString("savedBolusOnBoard", "0"))));

		carbsOnBoard = (TextView) findViewById(R.id.textview_onboard_carbs_number);
		carbsOnBoard.setText(Long.toString(Math.round(Double.parseDouble(savedValues.getString("savedCarbsOnBoard", "0")))));

		totalNumber = (TextView) findViewById(R.id.textview_total_number);

		if (halfunits) {
			totalNumber.setText(Double.toString((Math.round(Double.parseDouble(savedValues.getString("previousCalcValue", "0")) * 2))  * 0.5));
		} else {
			totalNumber.setText(Long.toString(Math.round(Double.parseDouble(savedValues.getString("previousCalcValue", "0")))));
		}

		calculationTime = (TextView) findViewById(R.id.textview_time_number);
		calculationTime.setText(dateFormatter.format(savedValues.getLong("previousDateTime", 0)));

		/// Calendars
		currentDateTimeCal = Calendar.getInstance();
		previousDateTimeCal = Calendar.getInstance();
		bloodTimeCal = Calendar.getInstance();

		setMMOLorMGDL();

		// Open Welcome activity if firstTimeOpen is true
		if (firstTimeOpen) {
			Intent i = new Intent(this, WelcomeActivity.class);
			startActivity(i);
		}

		// Hide Breakdown of Calculation if firstTimeCal is true, else show it
		if (firstTimeCal) {
			hideBreakdown();
			hideInsulinDoseArea();
		} else {
			showBreakdown();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		setMMOLorMGDL();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_activity_persistent: /** Opens PersistentActivity */
			Intent i = new Intent(this, OldPersistentActivity.class);
			startActivity(i);
			return true;

		case R.id.action_settings: /** Opens SettingsActivity */
			Intent j = new Intent(this, SettingsActivity.class);
			startActivity(j);
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.button_calculate_main: /** Calculates amount of insulin to inject */
			calculateInsulin();
			break;

		case R.id.button_reset: /** Resets variables */
			resetVariables();
			break;
		}

	}

	/**
	 * Checks if the EditText argument has a length not equal to zero.
	 * @param et EditText field
	 * @return True if EditText field has length not equal to zero, and False if not.
	 */
	public static boolean isReady(EditText et) {
		if (et.getText().toString().length() != 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if tddEditText has been filled.
	 * @return True if tddEditText has been filled, and False if not.
	 */
	public boolean isCurrentFilled() {
		if (isReady(currentEditText)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if ratioEditText has been filled.
	 * @return True if ratioEditText has been filled, and False if not.
	 */
	public boolean isCarbsFilled() {
		if (isReady(carbsEditText)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if desiredEditText has been filled.
	 * @return True if desiredEditText has been filled, and False if not.
	 */
	public boolean isDesiredFilled() {
		if (isReady(desiredEditText)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if there is a value stored in the 'savedRatio' SharedPreference.
	 * @return True if a value is stored, False if not.
	 */
	public boolean isRatioFilled() {
		savedValues = getSharedPreferences(PREFS_NAME, 0);
		if (savedValues.getString("savedRatio", "").equals("")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Checks if there is a value stored in the 'savedTDD' SharedPreference.
	 * @return True if a value is stored, False if not.
	 */
	public boolean isTDDFilled() {
		savedValues = getSharedPreferences(PREFS_NAME, 0);
		if (savedValues.getString("savedTDD", "").equals("")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Enables CalculateButton if the three 'isFilled' methods return true.
	 */
	public void enableCalculateButton() {
		if (isDesiredFilled() && isCurrentFilled() && isCarbsFilled() && isRatioFilled() && isTDDFilled()) {
			calculateButton.setEnabled(true);
		} else {
			calculateButton.setEnabled(false);
		}
	}

	/**
	 * Enables ResetButton if any one of the parameters are true.
	 */
	public void enableResetButton() {
		if (isCurrentFilled() || isCarbsFilled() || calculated) {
			resetButton.setEnabled(true);
		} else {
			resetButton.setEnabled(false);
		}
	}

	/**
	 * Takes data from entry fields and processes them to suggest insulin intake.
	 */
	public void calculateInsulin() {
		// Retrieve saved values
		savedValues = getSharedPreferences(PREFS_NAME, 0);

		// Restore values to variables from SharedPreferences
		ratioX2C = savedValues.getBoolean("ratioX2C", true);
		halfunits = savedValues.getBoolean("halfunits", false);
		previousCalculation = Double.parseDouble(savedValues.getString("previousCalcValue", "0"));
		previousBolusOnBoard = Double.parseDouble(savedValues.getString("savedBolusOnBoard", "0"));

		// Takes numbers from each EditText field and parses them into Double values
		TDD = Double.parseDouble(savedValues.getString("savedTDD", "0"));
		ratio = Double.parseDouble(savedValues.getString("savedRatio", "0"));
		desired = Double.parseDouble(desiredEditText.getText().toString());
		current = Double.parseDouble(currentEditText.getText().toString());
		carbs = Double.parseDouble(carbsEditText.getText().toString());

		convertMGDLtoMMOL();

		// Set current and previous DateTimeCal
		currentDateTimeCal = Calendar.getInstance();
		setPreviousDateTime();

		// Works out how much time has passed since last calculation
		calculateTimeDifference();

		// Calculations are performed to determine correct dose of insulin
		calculateRatio();

		calculateCorrective();

		calculateBolusOnBoard(dateTimeDiffMins);

		/// Checks carbs saved in array to see which need removing
		checkCarbsAgainstTimes();
		carbTally = true;

		calculateNegatedBolus();

		addToCarbandTimeArrays();

		checkCarbsAgainstTimes();

		isCorrectiveNeeded();

		/// Calculates total insulin required
		total = (ratioCalculation + correctionCalculation) - bolusAgainstCarbs;

		/// Sets insulin amount and breakdown total values
		if (total < 0) {
			insulinAmount.setText("0");
			totalNumber.setText("0");
			total = 0;
		} else {
			// If half unit calcs enabled, rounds to .5
			if (halfunits) {
				totalRounded = (Math.round(total * 2)) * 0.5;

				insulinAmount.setText(Double.toString(totalRounded));
				totalNumber.setText(Double.toString(totalRounded));
			} else {
				totalRounded = Math.round(total);

				insulinAmount.setText(Long.toString((long) totalRounded));
				totalNumber.setText(Long.toString((long) totalRounded));
			}
		}

		/// Sets breakdown values
		carbDose.setText(decimalFormatter.format(ratioCalculation));
		correctionDose.setText(decimalFormatter.format(correctionCalculation));
		bolusDose.setText("-" + decimalFormatter.format(bolusOnBoard));
		carbsOnBoard.setText(Double.toString(totalCarbs - carbArray.get(carbArray.size() - 1)));

		/// Save all necessary values to SharedPreferences
		SharedPreferences.Editor editor = savedValues.edit();
		editor.putBoolean("firstTimeCal", false);
		editor.putLong("previousDateTime", currentDateTimeCal.getTimeInMillis());
		editor.putLong("currentTimestamp", currentDateTimeCal.getTimeInMillis());
		editor.putString("previousCalcValue", Double.toString(total));
		editor.putString("savedDesired", Double.toString(desired));
		editor.putString("savedBolusOnBoard", Double.toString(bolusOnBoard));
		editor.putString("savedCarbsOnBoard", Double.toString(totalCarbs - carbArray.get(carbArray.size() - 1)));
		editor.putString("savedCarbDose", decimalFormatter.format(ratioCalculation));
		editor.putString("savedCorrectionDose", decimalFormatter.format(correctionCalculation));
		editor.putString("savedCarbArray", TextUtils.join(",", carbArray));
		editor.putString("savedTimeArray", TextUtils.join(",", timeArray));

		/// Display breakdown section of activity, and show insulin dose area
		showBreakdown();
		showInsulinDoseArea();
		calculated = true;
		totalCarbs = 0;
		carbTally = false;

		editor.commit();
	}

	/**
	 * Reset variable values and hide insulin dose area
	 */
	public void resetVariables() {
		currentEditText.setText("");
		carbsEditText.setText("");
		insulinAmount.setText("0");
		hideInsulinDoseArea();
		resetButton.setEnabled(false);
	}

	/**
	 * If the user has chosen the mg/dl option, converts blood sugar entries to mmol/L.
	 */
	public void convertMGDLtoMMOL() {
		savedValues = getSharedPreferences(PREFS_NAME, 0);

		if (!savedValues.getBoolean("mmol", true)) {
			desired = desired / 18;
			current = current / 18;
		}
	}

	/**
	 * Sets previousDateTimeCal to the value stored in SharedPreferences
	 */
	public void setPreviousDateTime() {
		// Retrive saved values
		savedValues = getSharedPreferences(PREFS_NAME, 0);
		// Set previousDateTimeCal to saved value
		previousDateTimeCal.setTimeInMillis(savedValues.getLong("previousDateTime", currentDateTimeCal.getTimeInMillis()));
	}

	/**
	 * Calculate difference between current date and time, and previous date and time of calculation.
	 */
	public void calculateTimeDifference() {
		calculationTime.setText(dateFormatter.format(currentDateTimeCal.getTime()));
		dateTimeDiffMillis = currentDateTimeCal.getTimeInMillis() - previousDateTimeCal.getTimeInMillis();
		dateTimeDiffMins = TimeUnit.MILLISECONDS.toMinutes(dateTimeDiffMillis);

	}

	/**
	 * Determines which ratio option the user has selected, and performs the corresponding calculation.
	 */
	public void calculateRatio() {
		if (ratioX2C) {
			ratioCalculation = (ratio * carbs) / 10;
		} else {
			ratioCalculation = carbs / ratio;
		}
	}

	/**
	 * Calculates corrective dose by using the formula (current blood sugar - desired blood sugar) / (100 / total daily dose).
	 */
	public void calculateCorrective() {
		correctionCalculation = (current - desired) / (100 / TDD);
	}

	/**
	 * Calculates how much of the bolus on board is negated by carbs on board.
	 */
	public void calculateNegatedBolus() {
		if (totalCarbs <= 0) {
			bolusAgainstCarbs = bolusOnBoard;
		} else {
			if (ratioX2C) {
				bolusAgainstCarbs = (((((bolusOnBoard / ratio) * 10) - totalCarbs) * ratio) / 10);
			} else {
				bolusAgainstCarbs = (((ratio * bolusOnBoard) - totalCarbs) / ratio);
			}
		}

		if (bolusAgainstCarbs <= 0) {
			bolusAgainstCarbs = 0;
		}
	}

	/**
	 * Adds current carb intake to carbArray, along with the current time to timeArray.
	 */
	public void addToCarbandTimeArrays() {
		carbArray.add(carbs);
		timeArray.add(currentDateTimeCal.getTimeInMillis());
	}

	/**
	 * Calculates how much bolus is still in the system.
	 * @param diffInMins Time difference between current and previous calculation times.
	 */
	public void calculateBolusOnBoard (long diffInMins) {
		if (diffInMins < 30) {
			bolusOnBoard = previousCalculation + previousBolusOnBoard;
		} else if (diffInMins >= 30 && diffInMins < 60) {
			bolusOnBoard = (previousCalculation + previousBolusOnBoard) * 0.95;
		} else if (diffInMins >= 60 && diffInMins < 90) {
			bolusOnBoard = (previousCalculation + previousBolusOnBoard) * 0.80;
		} else if (diffInMins >= 90 && diffInMins < 120) {
			bolusOnBoard = (previousCalculation + previousBolusOnBoard) * 0.6;
		} else if (diffInMins >= 120 && diffInMins < 150) {
			bolusOnBoard = (previousCalculation + previousBolusOnBoard) * 0.45;
		} else if (diffInMins >= 150 && diffInMins < 180) {
			bolusOnBoard = (previousCalculation + previousBolusOnBoard) * 0.4;
		} else if (diffInMins >= 180 && diffInMins < 210) {
			bolusOnBoard = (previousCalculation + previousBolusOnBoard) * 0.3;
		} else if (diffInMins >= 210 && diffInMins < 240) {
			bolusOnBoard = (previousCalculation + previousBolusOnBoard) * 0.2;
		} else if (diffInMins >= 240 && diffInMins < 270) {
			bolusOnBoard = (previousCalculation + previousBolusOnBoard) * 0.1;
		} else if (diffInMins >= 270 && diffInMins < 300) {
			bolusOnBoard = (previousCalculation + previousBolusOnBoard) * 0.05;
		} else if (diffInMins >= 300) {
			bolusOnBoard = (previousCalculation + previousBolusOnBoard) * 0.0;
		}
	}

	/**
	 * Checks carbs/times in array against current time, and removes any older than 120 minutes.
	 */
	public void checkCarbsAgainstTimes() {
		if (carbTally == true) {
			totalCarbs = 0;
		}

		if ((carbArray.size() - 1) >= 0) {
			for (int i = carbArray.size() - 1; i >= 0; i--) {
				long timeArrayDiffMillis = currentDateTimeCal.getTimeInMillis() - timeArray.get(i);
				timeArrayDiffMillis = Math.abs(timeArrayDiffMillis);

				if (TimeUnit.MILLISECONDS.toMinutes(timeArrayDiffMillis) > 120) {
					carbArray.remove(i);
					timeArray.remove(i);
				} else {
					totalCarbs = totalCarbs + carbArray.get(i);
				}
			}
		} else {
			totalCarbs = 0;
		}
	}

	/**
	 * Restores carbs saved to SharedPreferences to the carbArray
	 */
	public void restoreCarbsAndTimes() {
		// Retrieve saved values
		savedValues = getSharedPreferences(PREFS_NAME, 0);


		String restoredCarbs = savedValues.getString("savedCarbArray", "0");
		List<String> restoredCarbArray = Arrays.asList(TextUtils.split(restoredCarbs, ","));

		for (int i = 0; i <= restoredCarbArray.size() - 1; i++) {
			carbArray.add(Double.parseDouble(restoredCarbArray.get(i)));
			if (Double.parseDouble(restoredCarbArray.get(0)) == 0) {
				carbArray.remove(0);
			}
		}

		String restoredTimes = savedValues.getString("savedTimeArray", Long.toString(currentDateTimeCal.getTimeInMillis()));
		List<String> restoredTimeArray = Arrays.asList(TextUtils.split(restoredTimes, ","));

		for (int i = 0; i <= restoredTimeArray.size() - 1; i++) {
			timeArray.add(Long.parseLong(restoredTimeArray.get(i)));
			if (Double.parseDouble(restoredCarbArray.get(0)) == 0) {
				timeArray.remove(0);
			}
		}
	}

	public void setBloodTimeCal() {
		savedValues = getSharedPreferences(PREFS_NAME, 0);
		bloodTimeCal.setTimeInMillis(savedValues.getLong("currentTimestamp", currentDateTimeCal.getTimeInMillis()));
	}

	/**
	 * Checks whether a corrective dose is required.
	 */
	public void isCorrectiveNeeded() {
		savedValues = getSharedPreferences(PREFS_NAME, 0);

		setBloodTimeCal();

		long bloodTimeDiffMillis = currentDateTimeCal.getTimeInMillis() - bloodTimeCal.getTimeInMillis();

		bloodTimeDiffMillis = Math.abs(bloodTimeDiffMillis);

		firstTimeCal = savedValues.getBoolean("firstTimeCal", true);
		if ((firstTimeCal == false) && (TimeUnit.MILLISECONDS.toMinutes(bloodTimeDiffMillis) <= 120)) {
			correctionCalculation = 0;
		}
	}

	/**
	 * Hides breakdown section of activity
	 */
	public void hideBreakdown() {
		breakdownSection.setVisibility(View.GONE);
	}

	/**
	 * Reveals breakdown section of activity
	 */
	public void showBreakdown() {
		breakdownSection.setVisibility(View.VISIBLE);
	}

	/**
	 * Hides insulin dose section of activity
	 */
	public void hideInsulinDoseArea() {
		insulinDoseSection.setVisibility(View.GONE);
		totalBreakdownSection.setVisibility(View.VISIBLE);
		breakdownLabel.setText("Breakdown of Last Calculation");
	}

	/**
	 * Reveals insulin dose section of activity
	 */
	public void showInsulinDoseArea() {
		insulinDoseSection.setVisibility(View.VISIBLE);
		totalBreakdownSection.setVisibility(View.GONE);
		breakdownLabel.setText("Breakdown of Calculation");
	}

	/**
	 * Sets current and desired blood sugar fields hint and info to mmol or mgdl based on settings preference.
	 */
	public void setMMOLorMGDL() {
		// Retrieve saved values
		savedValues = getSharedPreferences(PREFS_NAME, 0);

		// Set mmol or mgdl based on blood measurement preference
		if (savedValues.getBoolean("mmol", true)) {
			desiredEditText.setHint(R.string.hint_mmol);
			desiredInfo.setText(R.string.info_desired_mmol);
			currentEditText.setHint(R.string.hint_mmol);
			currentInfo.setText(R.string.info_current_mmol);
		} else {
			desiredEditText.setHint(R.string.hint_mgdl);
			desiredInfo.setText(R.string.info_desired_mgdl);
			currentEditText.setHint(R.string.hint_mgdl);
			currentInfo.setText(R.string.info_current_mgdl);
		}
	}

	/**
	 * Adds a point to one decimal place of a string of characters.
	 * @param s
	 * @param et
	 */
	public static void addDecimalPlace (CharSequence s, EditText et) {
		List<Integer> storedPoints = new ArrayList<Integer>();

		if (!textChangeRunning) {
			textChangeRunning = true;

			StringBuilder decimalPlaceAuto = new StringBuilder(s.toString());

			while (decimalPlaceAuto.length() > 2 && decimalPlaceAuto.charAt(0) == '0' || decimalPlaceAuto.charAt(0) == '.') {
				decimalPlaceAuto.deleteCharAt(0);
			}

			for (int i = 0; i < decimalPlaceAuto.length(); i++) {
				if (decimalPlaceAuto.charAt(i) == '.') {
					storedPoints.add(i);
				}
			}

			for (int i = 0; i < storedPoints.size(); i++) {
				decimalPlaceAuto.deleteCharAt(storedPoints.get(i));
			}

			while (decimalPlaceAuto.length() < 2) {
				decimalPlaceAuto.insert(0, '0');
			}

			decimalPlaceAuto.insert(decimalPlaceAuto.length() - 1, '.');

			et.setText(decimalPlaceAuto.toString());

			textChangeRunning = false;

			Selection.setSelection(et.getText(), decimalPlaceAuto.toString().length());

		}
	}

}
