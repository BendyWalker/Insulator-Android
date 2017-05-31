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
class CarbohydrateAccuracyPreference @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val persistedValues by lazy { PersistedValues(context) }

    var onChangeListener: OnChangeListener? = null

    init {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.view_preference_carbohydrateaccuracy, this)

        carbohydrateAccuracySwitch.isChecked = if (isInEditMode) false else persistedValues.allowFloatingPointCarbohydrates

        setOnClickListener { carbohydrateAccuracySwitch.isChecked = !carbohydrateAccuracySwitch.isChecked }
        carbohydrateAccuracySwitch.setOnCheckedChangeListener { compoundButton, checked ->
            persistedValues.allowFloatingPointCarbohydrates = checked
            onChangeListener?.onCarbohydrateAccuracyChanged(checked)
        }
    }

    interface OnChangeListener {
        fun onCarbohydrateAccuracyChanged(allowFloatingPointCarbohydrates: Boolean)
    }
}