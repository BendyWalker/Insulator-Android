package com.bendywalker.insulator

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.view_preference_bloodglucoseunit.view.*

/**
 * Created by Ben David Walker (bendywalker) on 28/01/2017.
 */

/**
 * Custom view that uses [PersistedValues] to read and display the currently selected [BloodGlucoseUnit].
 * Tapping the view will show a dialog allowing a different unit to be selected and saved.
 */
class BloodGlucoseUnitPreference @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val persistedValues by lazy { PersistedValues(context) }

    var onChangeListener: OnChangeListener? = null

    init {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.view_preference_bloodglucoseunit, this)

        selectedUnitTextView.text = if (isInEditMode) resources.getString(R.string.card_hint_mmol) else persistedValues.bloodGlucoseUnit.displayString

        setOnClickListener {
            val dialogBuilder = MaterialDialog.Builder(context)
            dialogBuilder.title(R.string.cardBody_title_bloodGlucoseUnit)
                    .items(R.array.bloodGlucoseUnits)
                    .itemsCallbackSingleChoice(persistedValues.bloodGlucoseUnit.ordinal, object : MaterialDialog.ListCallbackSingleChoice {
                        override fun onSelection(dialog: MaterialDialog?, itemView: View?, which: Int, text: CharSequence?): Boolean {
                            when (which) {
                                0 -> persistedValues.bloodGlucoseUnit = BloodGlucoseUnit.MMOL
                                1 -> persistedValues.bloodGlucoseUnit = BloodGlucoseUnit.MGDL
                            }

                            selectedUnitTextView.text = persistedValues.bloodGlucoseUnit.displayString
                            onChangeListener?.onBloodGlucoseUnitPreferenceChange(persistedValues.bloodGlucoseUnit)
                            return true
                        }
                    }).show()
        }
    }

    interface OnChangeListener {
        fun onBloodGlucoseUnitPreferenceChange(bloodGlucoseUnit: BloodGlucoseUnit)
    }
}

