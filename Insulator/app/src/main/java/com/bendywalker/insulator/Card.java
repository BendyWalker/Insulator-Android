package com.bendywalker.insulator;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class Card extends RelativeLayout
{

    static boolean textChangeRunning;
    boolean isMmolSelected;
    OnTextChangeListener listener;
    private TextView label, info;
    private EditText entry;
    private String prefKey;
    private SharedPreferences preferences;

    public Card(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.card, this);

        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        isMmolSelected = (preferences
                .getString(context.getString(R.string.preference_blood_glucose_units), "mmol"))
                .equals("mmol");

        label = (TextView) findViewById(R.id.card_title);
        info = (TextView) findViewById(R.id.card_info);
        entry = (EditText) findViewById(R.id.card_entry);

        entry.addTextChangedListener(new MyTextWatcher());
        entry.setOnFocusChangeListener(new MyOnFocusChangeListener());
        entry.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                entry.selectAll();
            }
        });

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Card, 0, 0);

        try
        {
            prefKey = typedArray.getString(R.styleable.Card_prefKey);
            label.setText(typedArray.getString(R.styleable.Card_labelText));
            info.setText(typedArray.getString(R.styleable.Card_infoText));
            entry.setHint(typedArray.getString(R.styleable.Card_hintText));
        }
        finally
        {
            typedArray.recycle();
        }
    }

    /**
     * Adds a point to one decimal place of a string of characters.
     *
     * @param s
     * @param et
     */
    public static void addDecimalPlace(CharSequence s, EditText et)
    {
        List<Integer> storedPoints = new ArrayList<Integer>();

        if (!textChangeRunning)
        {
            textChangeRunning = true;

            StringBuilder decimalPlaceAuto = new StringBuilder(s.toString());

            while (decimalPlaceAuto.length() > 2 && decimalPlaceAuto
                    .charAt(0) == '0' || decimalPlaceAuto.charAt(0) == '.')
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

            Selection.setSelection(et.getText(), decimalPlaceAuto.toString().length());

        }
    }

    public void resetEntry()
    {
        entry.setText("");
    }

    public String getStringFromEntry()
    {
        return entry.getText().toString();
    }

    public float getFloatFromEntry()
    {
        String string = entry.getText().toString();

        if (string.isEmpty())
        {
            return 0;
        }
        else
        {
            return Float.valueOf(string);
        }
    }

    public void setEntryFromFloat(Float flot)
    {
        entry.setText(String.valueOf(flot));
    }

    public void setOnTextChangeListener(OnTextChangeListener listener)
    {
        this.listener = listener;
    }

    public interface OnTextChangeListener
    {
        void onTextChange();
    }

    private class MyTextWatcher implements TextWatcher
    {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            if (entry.length() > 0)
            {
                addDecimalPlace(s, entry);
                entry.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                  getResources().getDimension(R.dimen.text_card_entry_filled));
            }
            else
            {
                entry.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                  getResources().getDimension(R.dimen.text_card_entry_empty));
                ;
            }

            if (listener != null)
            {
                listener.onTextChange();
            }
        }

        @Override
        public void afterTextChanged(Editable editable)
        {

        }
    }

    private class MyOnFocusChangeListener implements OnFocusChangeListener
    {

        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            if (!hasFocus)
            {
                float entryFloat = getFloatFromEntry();

                if (entryFloat != 0 && prefKey != null)
                {
                    preferences.edit().putFloat(prefKey, getFloatFromEntry()).commit();
                }
            }
        }
    }

}
