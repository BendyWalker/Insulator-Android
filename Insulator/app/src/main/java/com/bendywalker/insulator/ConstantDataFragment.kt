package com.bendywalker.insulator

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Ben David Walker (bendywalker) on 20/01/2017.
 */

class ConstantDataFragment : Fragment(), CardBody.OnTextChangeListener {
    private val desiredBloodGlucoseLevelCardBody by lazy { view?.findViewById(R.id.cardBody_constantdata_desiredBloodGlucoseLevel) as CardBody }
    private val carbohydrateFactorCardBody by lazy { view?.findViewById(R.id.cardBody_constantdata_carbohydrateFactor) as CardBody }
    private val correctiveFactorCardBody by lazy { view?.findViewById(R.id.cardBody_constantdata_correctiveFactor) as CardBody }

    private val persistedValues by lazy { (activity as DashboardActivity).persistedValues }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_constantdata, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        desiredBloodGlucoseLevelCardBody.value = persistedValues.desiredBloodGlucose
        carbohydrateFactorCardBody.value = persistedValues.carbohydrateFactor
        correctiveFactorCardBody.value = persistedValues.correctiveFactor

        desiredBloodGlucoseLevelCardBody.onTextChangeListener = this
        carbohydrateFactorCardBody.onTextChangeListener = this
        correctiveFactorCardBody.onTextChangeListener = this
    }

    override fun onCardBodyTextChange(id: Int, string: String) {
        when (id) {
            desiredBloodGlucoseLevelCardBody.id -> persistedValues.desiredBloodGlucose = desiredBloodGlucoseLevelCardBody.value
            carbohydrateFactorCardBody.id -> persistedValues.carbohydrateFactor = carbohydrateFactorCardBody.value
            correctiveFactorCardBody.id -> persistedValues.correctiveFactor = correctiveFactorCardBody.value
        }    }

    companion object {
        fun newInstance(): ConstantDataFragment {
            return ConstantDataFragment()
        }
    }
}