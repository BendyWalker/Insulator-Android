package com.bendywalker.insulator

import android.content.Context
import android.text.*
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
class CardBody(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0, 0)
    constructor(context: Context) : this(context, null, 0, 0)

    private val titleTextView by lazy { findViewById(R.id.textView_card_title) as TextView }
    private val descriptionTextView by lazy { findViewById(R.id.textView_card_description) as TextView }
    private val entryEditText by lazy { findViewById(R.id.editText_card_entry) as EditText }
    private val persistedValues by lazy { PersistedValues(context) }

    var onTextChangeListener: OnTextChangeListener? = null
    var onFocusChangeListener: OnFocusChangeListener? = null

    var value: Double
        get() {
            val string = entryEditText.text.toString()
            return if (string.isEmpty()) 0.0 else string.toDouble()
        }
        set(value) {
            entryEditText.setText(if (displayFloatingPoint) value.toString() else value.toInt().toString())
        }

    private var addingFloatingPoint = false

    private val displayFloatingPoint: Boolean
        get() {
            val grams = entryEditText.hint == resources.getString(R.string.card_hint_grams)
            val bloodGlucoseUnit = entryEditText.hint == resources.getString(R.string.card_hint_mmol) ||
                    entryEditText.hint == resources.getString(R.string.card_hint_mgdl)
            val units = entryEditText.hint == resources.getString(R.string.card_hint_units)

            if (grams) {
                return persistedValues.allowFloatingPointCarbohydrates
            } else if (bloodGlucoseUnit) {
                when (persistedValues.bloodGlucoseUnit) {
                    BloodGlucoseUnit.MMOL -> return true
                    BloodGlucoseUnit.MGDL -> return false
                }
            } else if (units) {
                return false
            }

            return true
        }

    private val maxEntryLength: Int
        get() {
            return if (displayFloatingPoint) {
                if (entryEditText.hint == resources.getString(R.string.card_hint_grams)) { 5 } else { 4 }
            } else { 3 }
        }

    init {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.view_cardbody, this)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CardBody, 0, 0)
        try {
            titleTextView.text = typedArray.getString(R.styleable.CardBody_titleText)
            descriptionTextView.text = typedArray.getString(R.styleable.CardBody_descriptionText)
            entryEditText.hint = typedArray.getString(R.styleable.CardBody_hintText)
        } finally {
            typedArray.recycle()
        }

        if (isInEditMode) {
            // Do this to have standardised values in the layout preview
            entryEditText.setText(R.string.dose_placeholder_long)
            entryEditText.adjustTextSize()
            entryEditText.setMaxLength(4)
        } else {
            setOnClickListener {
                entryEditText.requestFocus()
                entryEditText.selectAll()
            }

            entryEditText.onTextChanged { charSequence ->
                if (!TextUtils.isEmpty(entryEditText.text)) {
                    if (displayFloatingPoint && !addingFloatingPoint) entryEditText.addFloatingPoint()
                }
                entryEditText.adjustTextSize()
                onTextChangeListener?.onCardBodyTextChange(id, charSequence.toString())
            }

            entryEditText.setOnFocusChangeListener { view, hasFocus -> onFocusChangeListener?.onCardBodyFocusChange(id, hasFocus) }

            entryEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    entryEditText.clearFocus()
                    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(entryEditText.windowToken, 0)
                }

                false
            }

            entryEditText.setMaxLength(maxEntryLength)
            updateUi()
        }
    }

    fun reset() {
        entryEditText.setText("")
    }

    fun updateUi() {
        entryEditText.setMaxLength(maxEntryLength)
        updateHint()
        reset()
    }

    private fun updateHint() {
        if (entryEditText.hint == resources.getString(R.string.card_hint_mmol) ||
                entryEditText.hint == resources.getString(R.string.card_hint_mgdl)) {
            when (persistedValues.bloodGlucoseUnit) {
                BloodGlucoseUnit.MMOL -> entryEditText.hint = resources.getString(R.string.card_hint_mmol)
                BloodGlucoseUnit.MGDL -> entryEditText.hint = resources.getString(R.string.card_hint_mgdl)
            }
        }
    }

    fun EditText.onTextChanged(closure: (s: CharSequence) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                closure(s as CharSequence)
            }
        })
    }

    fun EditText.addFloatingPoint() {
        addingFloatingPoint = true
        val string = addFloatingPoint(this.text.toString())
        this.setText(string)
        addingFloatingPoint = false
        Selection.setSelection(this.text, string.length)
    }

    fun EditText.adjustTextSize() {
        if (TextUtils.isEmpty(this.text))
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.text_card_entry_empty))
        else this.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.text_card_entry_filled))
    }

    fun EditText.setMaxLength(length: Int) {
        this.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(length))
    }

    companion object {
        fun addFloatingPoint(string: String, precision: Int = 1): String {
            val stringBuilder = StringBuilder(string)
            while (stringBuilder.length > 2 && stringBuilder.first() == '0' || stringBuilder.first() == '.') stringBuilder.deleteCharAt(0)
            val storedPoints = (0 until stringBuilder.length).filter { stringBuilder[it] == '.' }
            for (i in storedPoints) stringBuilder.deleteCharAt(i)
            while (stringBuilder.length < 2) stringBuilder.insert(0, '0')
            stringBuilder.insert(stringBuilder.length - precision, '.')
            return stringBuilder.toString()
        }
    }

    interface OnTextChangeListener {
        fun onCardBodyTextChange(id: Int, string: String)
    }

    interface OnFocusChangeListener {
        fun onCardBodyFocusChange(id: Int, hasFocus: Boolean)
    }
}