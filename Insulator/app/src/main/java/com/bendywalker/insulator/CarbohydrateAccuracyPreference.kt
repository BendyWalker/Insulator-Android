package com.bendywalker.insulator

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_preference_carbohydrateaccuracy.view.*

/**
 * Created by Ben David Walker (bendywalker) on 28/01/2017.
 */

/**
 * Custom view that uses [PersistedValues] to read and display whether floating point carbohydrates should be used.
 * Tapping the view will toggle the preference, saving it in the process.
 */
class CarbohydrateAccuracyPreference(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0, 0)
    constructor(context: Context) : this(context, null, 0, 0)

    private val persistedValues by lazy { PersistedValues(context) }

    var onChangeListener: OnChangeListener? = null

    init {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.view_preference_carbohydrateaccuracy, this)

        if (isInEditMode) {
            carbohydrateAccuracySwitch.isChecked = false
        } else {
            carbohydrateAccuracySwitch.isChecked = persistedValues.allowFloatingPointCarbohydrates

            setOnClickListener { carbohydrateAccuracySwitch.isChecked = !carbohydrateAccuracySwitch.isChecked }
            carbohydrateAccuracySwitch.setOnCheckedChangeListener { compoundButton, checked ->
                persistedValues.allowFloatingPointCarbohydrates = checked
                onChangeListener?.onCarbohydrateAccuracyChanged(checked)
            }
        }
    }

    interface OnChangeListener {
        fun onCarbohydrateAccuracyChanged(allowFloatingPointCarbohydrates: Boolean)
    }
}