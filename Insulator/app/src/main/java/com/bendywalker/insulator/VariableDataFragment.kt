package com.bendywalker.insulator

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by Ben David Walker (bendywalker) on 14/01/2017.
 */

class VariableDataFragment : Fragment(), Card.OnTextChangeListener {
    private val rootView by lazy { view?.findViewById(R.id.root) as LinearLayout }
    private val currentBloodGlucoseLevelCard by lazy { view?.findViewById(R.id.card_variableData_currentBloodGlucoseLevel) as Card }
    private val carbohydratesInMealCard by lazy { view?.findViewById(R.id.card_variableData_carbohydratesInMeal) as Card }
    private val totalDoseTextView by lazy { view?.findViewById(R.id.textView_variableData_suggestedDose) as TextView }
    private val carbohydrateDoseTextView by lazy { view?.findViewById(R.id.textView_variableData_carbohydrateDose) as TextView }
    private val correctiveDoseTextView by lazy { view?.findViewById(R.id.textView_variableData_correctiveDose) as TextView }

    private val persistedValues by lazy { PersistedValues(context) }
    private val calculator by lazy { Calculator(persistedValues = persistedValues) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_variabledata, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        currentBloodGlucoseLevelCard.onTextChangeListener = this
        carbohydratesInMealCard.onTextChangeListener = this

        val parentActivity = activity as DashboardActivity
        parentActivity.toolbar.inflateMenu(R.menu.variabledata)
        parentActivity.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_reset -> {
                    resetCards()
                    true
                }
                else -> false
            }
        }
    }

    private fun resetCards() {
        currentBloodGlucoseLevelCard.reset()
        carbohydratesInMealCard.reset()
        totalDoseTextView.text = "0.0"
        carbohydrateDoseTextView.text = "0.0"
        correctiveDoseTextView.text = "0.0"
        rootView.requestFocus()
    }

    override fun onTextChange() {
        calculator.currentBloodGlucose = currentBloodGlucoseLevelCard.value
        calculator.carbohydratesInMeal = carbohydratesInMealCard.value
        totalDoseTextView.text = calculator.totalDose.toString()
        carbohydrateDoseTextView.text = calculator.carbohydrateDose.toString()
        correctiveDoseTextView.text = calculator.correctiveDose.toString()
    }

    companion object {
        fun newInstance(): VariableDataFragment {
            return VariableDataFragment()
        }
    }
}