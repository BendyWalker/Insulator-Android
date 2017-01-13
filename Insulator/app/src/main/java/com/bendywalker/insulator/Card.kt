package com.bendywalker.insulator

import android.content.Context
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by Ben David Walker (bendywalker) on 11/01/2017.
 */
class Card(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0, 0)
    constructor(context: Context) : this(context, null, 0, 0)

    private val titleTextView by lazy { findViewById(R.id.textView_card_title) as TextView }
    private val descriptionTextView by lazy { findViewById(R.id.textView_card_description) as TextView }
    private val entryEditText by lazy { findViewById(R.id.editText_card_entry) as EditText }
    private val persistedValues by lazy { PersistedValues(context) }
    private val persistedValueKey: String?

    var value: Double
        get() {
            val string = entryEditText.text.toString()
            return if (string.isEmpty()) 0.0 else string.toDouble()
        }
        set(value) {
            if (shouldDisplayFloatingPoint) {
                entryEditText.setText(value.toString())
            } else {
                val roundedValue = Math.round(value)
                entryEditText.setText(roundedValue.toString())
            }
        }

    private val shouldDisplayFloatingPoint: Boolean
        get() {
            // TODO: Write logic for booleans once other layouts have been implemented
            val carbohydratesInMeal = true
            val glucoseLevel = true
            val totalDailyDose = true

            if (carbohydratesInMeal) {
                return persistedValues.allowFloatingPointCarbohydrates
            } else if (glucoseLevel) {
                when (persistedValues.bloodGlucoseUnit) {
                    BloodGlucoseUnit.MMOL -> return true
                    BloodGlucoseUnit.MGDL -> return false
                }
            } else if (totalDailyDose) {
                return false
            }

            return true
        }

    init {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.view_card, this)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Card, 0, 0)
        try {
            persistedValueKey = typedArray.getString(R.styleable.Card_persistedValueKey)
            titleTextView.text = typedArray.getString(R.styleable.Card_titleText)
            descriptionTextView.text = typedArray.getString(R.styleable.Card_descriptionText)
            entryEditText.hint = typedArray.getString(R.styleable.Card_hintText)
        } finally {
            typedArray.recycle()
        }

        // Do this to have standardised values in the layout preview
        entryEditText.setText(R.string.dose_placeholder)
        entryEditText.adjustTextSize()

        if (!isInEditMode) {

            setOnClickListener {
                entryEditText.requestFocus()
                entryEditText.selectAll()
            }

            entryEditText.setOnTextChangedListener { s ->
                if (entryEditText.length() > 0) {
                    if (shouldDisplayFloatingPoint) entryEditText.addFloatingPoint()
                }
                entryEditText.adjustTextSize()
            }

            entryEditText.setOnFocusChangeListener { view, hasFocus -> if (!hasFocus) persistedValues.setValueForKey(value, persistedValueKey) }

            entryEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    entryEditText.clearFocus()
                    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(entryEditText.windowToken, 0)
                }

                false
            }
        }
    }

    fun reset() {
        entryEditText.text = null
    }

    fun EditText.setOnTextChangedListener(closure: (s: CharSequence) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                closure(s as CharSequence)
            }
        })
    }

    fun EditText.addFloatingPoint() {
        val string = Companion.addFloatingPoint(this.text.toString())
        this.setText(string)
        Selection.setSelection(this.text, string.length)
    }

    fun EditText.adjustTextSize() {
        if (this.length() > 0)
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.text_card_entry_filled))
        else this.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.text_card_entry_empty))
    }

    companion object {
        fun addFloatingPoint(string: String, precision: Int = 1): String {
            val stringBuilder = StringBuilder(string)
            val storedPoints = (0 until stringBuilder.length).filter { stringBuilder[it] == '.' }

            while (stringBuilder.length > 2 && stringBuilder.first() == '0' || stringBuilder.first() == '.') stringBuilder.deleteCharAt(0)
            for (i in storedPoints) stringBuilder.deleteCharAt(i)
            while (stringBuilder.length < 2) stringBuilder.insert(0, '0')
            stringBuilder.insert(stringBuilder.length - precision, '.')

            return stringBuilder.toString()
        }
    }
}