package com.faranegar.customtoolbar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() ,
CustomToolbarContract.NormalContract,
CustomToolbarContract.SearchSupportContract{
    override fun onSearchQuery(query: CharSequence?) {

    }


    override fun onBackClicked() {
        onBackPressed()
    }

    var customToolbar : CustomToolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        customToolbar = findViewById(R.id.toolbar)
        customToolbar?.setSearchSupportContract(this)
        customToolbar?.setToolbarTextColor(R.color.colorAccent)
    }
}
