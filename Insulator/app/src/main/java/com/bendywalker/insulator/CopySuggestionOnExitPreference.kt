package com.bendywalker.insulator

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_preference_copysuggestiononexit.view.*

/**
 * Created by Ben David Walker (bendywalker) on 28/01/2017.
 */

/**
 * Custom view that uses [PersistedValues] to read and display whether dose suggestions should be copied to clipboard on exiting the app.
 * Tapping the view will toggle the preference, saving it in the process.
 */
class CopySuggestionOnExitPreference(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0, 0)
    constructor(context: Context) : this(context, null, 0, 0)

    private val persistedValues by lazy { PersistedValues(context) }

    init {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.view_preference_copysuggestiononexit, this)

        if (isInEditMode) {
            copySuggestionOnExitSwitch.isChecked = false
        } else {
            copySuggestionOnExitSwitch.isChecked = persistedValues.copySuggestionOnExit

            setOnClickListener { copySuggestionOnExitSwitch.isChecked = !copySuggestionOnExitSwitch.isChecked }
            copySuggestionOnExitSwitch.setOnCheckedChangeListener { compoundButton, checked ->
                persistedValues.copySuggestionOnExit = checked
            }
        }
    }
}