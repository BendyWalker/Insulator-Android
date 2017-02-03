package com.bendywalker.insulator

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Switch

/**
 * Created by Ben David Walker (bendywalker) on 28/01/2017.
 */

class SaveSuggestionOnExitPreference(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0, 0)
    constructor(context: Context) : this(context, null, 0, 0)

    private val saveSuggestionOnExitSwitch by lazy { findViewById(R.id.switch_saveSuggestionOnExit) as Switch }
    private val persistedValues by lazy { PersistedValues(context) }

    init {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.view_preference_savesuggestiononexit, this)

        if (isInEditMode) {
            saveSuggestionOnExitSwitch.isChecked = false
        } else {
            saveSuggestionOnExitSwitch.isChecked = persistedValues.allowFloatingPointCarbohydrates

            setOnClickListener { saveSuggestionOnExitSwitch.isChecked = !saveSuggestionOnExitSwitch.isChecked }
            saveSuggestionOnExitSwitch.setOnCheckedChangeListener { compoundButton, checked ->
                persistedValues.saveSuggestionOnExit = checked
            }
        }
    }
}