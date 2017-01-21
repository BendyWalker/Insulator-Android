package com.bendywalker.insulator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

/**
 * Created by Ben David Walker (bendywalker) on 09/01/2017.
 */


// Add any functions or properties here that are required in all activities.
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
