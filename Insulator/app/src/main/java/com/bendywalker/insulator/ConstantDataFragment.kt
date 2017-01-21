package com.bendywalker.insulator

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Ben David Walker (bendywalker) on 20/01/2017.
 */

class ConstantDataFragment : Fragment(), Card.OnFocusChangeListener {
    private val desiredBloodGlucoseLevelCard by lazy { view?.findViewById(R.id.card_constantdata_desiredBloodGlucoseLevel) as Card }
    private val carbohydrateFactorCard by lazy { view?.findViewById(R.id.card_constantdata_carbohydrateFactor) as Card }
    private val correctiveFactorCard by lazy { view?.findViewById(R.id.card_constantdata_correctiveFactor) as Card }

    private val persistedValues by lazy { PersistedValues(context) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_constantdata, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        desiredBloodGlucoseLevelCard.value = persistedValues.desiredBloodGlucose
        carbohydrateFactorCard.value = persistedValues.carbohydrateFactor
        correctiveFactorCard.value = persistedValues.correctiveFactor

        desiredBloodGlucoseLevelCard.onFocusChangeListener = this
        carbohydrateFactorCard.onFocusChangeListener = this
        correctiveFactorCard.onFocusChangeListener = this
    }

    override fun onFocusChange(id: Int, hasFocus: Boolean) {
        when (id) {
            R.id.card_constantdata_desiredBloodGlucoseLevel -> persistedValues.desiredBloodGlucose = desiredBloodGlucoseLevelCard.value
            R.id.card_constantdata_carbohydrateFactor -> persistedValues.carbohydrateFactor = carbohydrateFactorCard.value
            R.id.card_constantdata_correctiveFactor -> persistedValues.correctiveFactor = correctiveFactorCard.value
        }
    }

    companion object {
        fun newInstance(): ConstantDataFragment {
            return ConstantDataFragment()
        }
    }
}