package com.bendywalker.insulator

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_constantdata.*

/**
 * Created by Ben David Walker (bendywalker) on 20/01/2017.
 */

class ConstantDataFragment : Fragment(), CardBody.OnTextChangeListener {
    private val parentActivity by lazy { activity as DashboardActivity }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_constantdata, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        desiredBloodGlucoseLevelCardBody.onTextChangeListener = this
        carbohydrateFactorCardBody.onTextChangeListener = this
        correctiveFactorCardBody.onTextChangeListener = this
    }

    override fun onResume() {
        super.onResume()
        desiredBloodGlucoseLevelCardBody.value = parentActivity.persistedValues.desiredBloodGlucose
        carbohydrateFactorCardBody.value = parentActivity.persistedValues.carbohydrateFactor
        correctiveFactorCardBody.value = parentActivity.persistedValues.correctiveFactor
    }

    override fun onCardBodyTextChange(id: Int, string: String) {
        when (id) {
            desiredBloodGlucoseLevelCardBody.id -> parentActivity.persistedValues.desiredBloodGlucose = desiredBloodGlucoseLevelCardBody.value
            carbohydrateFactorCardBody.id -> parentActivity.persistedValues.carbohydrateFactor = carbohydrateFactorCardBody.value
            correctiveFactorCardBody.id -> parentActivity.persistedValues.correctiveFactor = correctiveFactorCardBody.value
        }
    }

    companion object {
        fun newInstance(): ConstantDataFragment {
            return ConstantDataFragment()
        }
    }
}