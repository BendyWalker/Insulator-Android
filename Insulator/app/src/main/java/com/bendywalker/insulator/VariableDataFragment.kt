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

class VariableDataFragment : Fragment(), CardBody.OnTextChangeListener {
    private val rootView by lazy { view?.findViewById(R.id.root) as LinearLayout }
    private val currentBloodGlucoseLevelCardBody by lazy { view?.findViewById(R.id.cardBody_variableData_currentBloodGlucoseLevel) as CardBody }
    private val carbohydratesInMealCardBody by lazy { view?.findViewById(R.id.cardBody_variableData_carbohydratesInMeal) as CardBody }
    private val totalDoseTextView by lazy { view?.findViewById(R.id.textView_variableData_suggestedDose) as TextView }
    private val carbohydrateDoseTextView by lazy { view?.findViewById(R.id.textView_variableData_carbohydrateDose) as TextView }
    private val correctiveDoseTextView by lazy { view?.findViewById(R.id.textView_variableData_correctiveDose) as TextView }

    private val persistedValues by lazy { PersistedValues(context) }
    private val calculator by lazy { Calculator(persistedValues = persistedValues) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_variabledata, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        currentBloodGlucoseLevelCardBody.onTextChangeListener = this
        carbohydratesInMealCardBody.onTextChangeListener = this

        val parentActivity = activity as DashboardActivity
        parentActivity.toolbar.inflateMenu(R.menu.variabledata)
        parentActivity.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_reset -> { resetCards(); true }
                else -> false
            }
        }
    }

    private fun resetCards() {
        currentBloodGlucoseLevelCardBody.reset()
        carbohydratesInMealCardBody.reset()
        totalDoseTextView.text = "0.0"
        carbohydrateDoseTextView.text = "0.0"
        correctiveDoseTextView.text = "0.0"
        rootView.requestFocus()
    }

    override fun onTextChange(view: View, string: String) {
        calculator.currentBloodGlucose = currentBloodGlucoseLevelCardBody.value
        calculator.carbohydratesInMeal = carbohydratesInMealCardBody.value
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