package com.bendywalker.insulator;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class Card extends RelativeLayout {

    static boolean textChangeRunning;
    OnTextChangeListener textChangeListener;
    private TextView label, info;
    private EditText entry;
    private String prefKey;
    private MyPreferenceManager preferenceManager;

    public Card(final Context context, AttributeSet attrs) {
        super(context, attrs);

        final LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.card, this);

        if (!isInEditMode()) {
            preferenceManager = new MyPreferenceManager(context);
        }

        label = (TextView) findViewById(R.id.card_title);
        info = (TextView) findViewById(R.id.card_info);
        entry = (EditText) findViewById(R.id.card_entry);

        entry.addTextChangedListener(new MyTextWatcher());
        entry.setOnFocusChangeListener(new MyOnFocusChangeListener());
        entry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    entry.clearFocus();

                    InputMethodManager inputMethodManager = (InputMethodManager) context
                            .getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(entry.getWindowToken(), 0);
                }
                return false;
            }
        });

        this.setOnClickListener(new MyOnClickListener());

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Card, 0, 0);

        try {
            prefKey = typedArray.getString(R.styleable.Card_prefKey);
            label.setText(typedArray.getString(R.styleable.Card_labelText));
            info.setText(typedArray.getString(R.styleable.Card_infoText));
            entry.setHint(typedArray.getString(R.styleable.Card_hintText));
        } finally {
            typedArray.recycle();
        }

        // Changes the hint text of cards related to glucose level based on the preference chose
        boolean isCardGlucoseLevel = (getId() == R.id.card_desired_blood_glucose_level ||
                getId() == R.id.card_corrective_factor ||
                getId() == R.id.card_current_blood_glucose_level);

        if (isCardGlucoseLevel) {
            if (!isInEditMode()) {
                switch (preferenceManager.getBloodGlucoseUnit()) {
                    case mmol:
                        entry.setHint(getResources().getString(R.string.hint_mmol));
                        break;
                    case mgdl:
                        entry.setHint(getResources().getString(R.string.hint_mgdl));
                }
            } else {
                entry.setHint(getResources().getString(R.string.hint_mmol));
            }
        }
    }

    /**
     * Adds a point to one decimal place of a string of characters.
     */
    public static void addDecimalPlace(CharSequence s, EditText et) {
        List<Integer> storedPoints = new ArrayList<>();

        if (!textChangeRunning) {
            textChangeRunning = true;

            StringBuilder decimalPlaceAuto = new StringBuilder(s.toString());

            while (decimalPlaceAuto.length() > 2 && decimalPlaceAuto
                    .charAt(0) == '0' || decimalPlaceAuto.charAt(0) == '.') {
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

    // Methods
    public void resetEntryField() {
        entry.setText("");
    }

    public double getValueFromEntryField() {
        String string = entry.getText().toString();

        if (string.isEmpty()) {
            return 0;
        } else {
            return Double.valueOf(string);
        }
    }

    public void setEntryFieldFromValue(Double value) {
        if (shouldDecimalPlaceBeDisplayed(getId())) {
            entry.setText(String.valueOf(value));
        } else {
            long integer = Math.round(value);
            entry.setText(String.valueOf(integer));
        }
    }

    public boolean isEntryFieldFilled() {
        return (entry.getText().toString().length() > 0);
    }

    private boolean shouldDecimalPlaceBeDisplayed(int cardId) {
        boolean isCardCarbohydratesInMeal = (cardId == R.id.card_carbohydrates_in_meal);

        boolean isCardGlucoseLevel = (cardId == R.id.card_desired_blood_glucose_level ||
                getId() == R.id.card_corrective_factor ||
                getId() == R.id.card_current_blood_glucose_level);

        boolean isTotalDailyDoseCard = (cardId == R.id.card_total_daily_dose);

        if (isCardCarbohydratesInMeal) {
            if (!isInEditMode()) {
                return preferenceManager.allowFloatingPointCarbohydrates();
            } else {
                return false;
            }
        } else if (isCardGlucoseLevel) {
            if (!isInEditMode()) {
                switch (preferenceManager.getBloodGlucoseUnit()) {
                    case mmol:
                        return true;
                    case mgdl:
                        return false;
                }
            } else {
                return true;
            }
        } else if (isTotalDailyDoseCard) {
            return false;
        }

        return true;
    }

    public void setOnTextChangeListener(OnTextChangeListener textChangeListener) {
        this.textChangeListener = textChangeListener;
    }

    public interface OnTextChangeListener {
        void onTextChange();
    }

    public class MyOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            entry.requestFocus();
            entry.selectAll();
        }
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            modifyText(s, shouldDecimalPlaceBeDisplayed(getId()));

            if (textChangeListener != null) {
                textChangeListener.onTextChange();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

        private void modifyText(CharSequence s, boolean addDecimal) {
            if (entry.length() > 0) {
                if (addDecimal) {
                    addDecimalPlace(s, entry);
                }

                entry.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.text_card_entry_filled));
            } else {
                entry.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.text_card_entry_empty));
            }
        }
    }

    private class MyOnFocusChangeListener implements OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                double value = getValueFromEntryField();

                if (value != 0 && prefKey != null) {
                    preferenceManager.editor.putFloat(prefKey, (float) value).commit();
                }
            }
        }
    }
}