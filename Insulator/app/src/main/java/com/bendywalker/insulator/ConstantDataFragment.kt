package com.bendywalker.insulator

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Ben David Walker (bendywalker) on 20/01/2017.
 */

class ConstantDataFragment : Fragment(), CardBody.OnFocusChangeListener {
    private val desiredBloodGlucoseLevelCardBody by lazy { view?.findViewById(R.id.cardBody_constantdata_desiredBloodGlucoseLevel) as CardBody }
    private val carbohydrateFactorCardBody by lazy { view?.findViewById(R.id.cardBody_constantdata_carbohydrateFactor) as CardBody }
    private val correctiveFactorCardBody by lazy { view?.findViewById(R.id.cardBody_constantdata_correctiveFactor) as CardBody }

    private val persistedValues by lazy { PersistedValues(context) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_constantdata, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        desiredBloodGlucoseLevelCardBody.value = persistedValues.desiredBloodGlucose
        carbohydrateFactorCardBody.value = persistedValues.carbohydrateFactor
        correctiveFactorCardBody.value = persistedValues.correctiveFactor

        desiredBloodGlucoseLevelCardBody.onFocusChangeListener = this
        carbohydrateFactorCardBody.onFocusChangeListener = this
        correctiveFactorCardBody.onFocusChangeListener = this
    }

    override fun onFocusChange(id: Int, hasFocus: Boolean) {
        when (id) {
            R.id.cardBody_constantdata_desiredBloodGlucoseLevel -> if (!hasFocus) persistedValues.desiredBloodGlucose = desiredBloodGlucoseLevelCardBody.value
            R.id.cardBody_constantdata_carbohydrateFactor -> if (!hasFocus) persistedValues.carbohydrateFactor = carbohydrateFactorCardBody.value
            R.id.cardBody_constantdata_correctiveFactor -> if (!hasFocus) persistedValues.correctiveFactor = correctiveFactorCardBody.value
        }
    }

    companion object {
        fun newInstance(): ConstantDataFragment {
            return ConstantDataFragment()
        }
    }
}