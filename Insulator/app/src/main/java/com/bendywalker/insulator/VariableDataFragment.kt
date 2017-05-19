package com.bendywalker.insulator

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_variabledata.*
import kotlinx.android.synthetic.main.view_suggestions.*

/**
 * Created by Ben David Walker (bendywalker) on 14/01/2017.
 */

class VariableDataFragment : Fragment(), CardBody.OnTextChangeListener {
    private val parentActivity by lazy { activity as DashboardActivity }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_variabledata, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        currentBloodGlucoseLevelCardBody.onTextChangeListener = this
        carbohydratesInMealCardBody.onTextChangeListener = this

        parentActivity.toolbar.inflateMenu(R.menu.variabledata)
        parentActivity.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_reset -> { resetCards(); true }
                R.id.action_settings -> {startActivity(Intent(parentActivity, SettingsActivity::class.java)); true } // TODO: Figure out how to have multiple menus inflated
                else -> false
            }
        }
    }

    override fun onCardBodyTextChange(id: Int, string: String) {
        calculate()
    }

    private fun calculate() {
        val calculator = Calculator(persistedValues = parentActivity.persistedValues)
        calculator.currentBloodGlucose = currentBloodGlucoseLevelCardBody.value
        calculator.carbohydratesInMeal = carbohydratesInMealCardBody.value
        totalDoseTextView.text = calculator.totalDose.toString()
        carbohydrateDoseTextView.text = calculator.carbohydrateDose.toString()
        correctiveDoseTextView.text = calculator.correctiveDose.toString()
        parentActivity.totalDoseSuggestion = calculator.totalDose.toString()
    }

    private fun resetCards() {
        currentBloodGlucoseLevelCardBody.reset()
        carbohydratesInMealCardBody.reset()
        totalDoseTextView.text = "0.0"
        carbohydrateDoseTextView.text = "0.0"
        correctiveDoseTextView.text = "0.0"
        rootView.requestFocus()
    }

    companion object {
        fun newInstance(): VariableDataFragment { return VariableDataFragment() }
    }
}