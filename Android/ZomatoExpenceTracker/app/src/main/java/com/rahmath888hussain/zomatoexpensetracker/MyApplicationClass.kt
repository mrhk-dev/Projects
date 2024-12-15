package com.rahmath888hussain.zomatoexpensetracker

import android.app.Application

class MyApplicationClass : Application() {
    private lateinit var dbHandler: DataBaseHelper

    override fun onCreate() {
        super.onCreate()

        dbHandler = DataBaseHelper(this)

    }

}