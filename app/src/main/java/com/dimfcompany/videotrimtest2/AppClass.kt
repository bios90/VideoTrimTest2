package com.dimfcompany.videotrimtest2

import android.app.Application
import android.util.Log


class AppClass : Application()
{
    companion object
    {
        private lateinit var appClass: AppClass

        fun getApp(): AppClass
        {
            return appClass
        }
    }

    override fun onCreate()
    {
        super.onCreate()

        appClass = this
    }

}