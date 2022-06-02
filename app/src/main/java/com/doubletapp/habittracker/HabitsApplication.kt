package com.doubletapp.habittracker

import android.app.Application
import com.doubletapp.data.DataModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class HabitsApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    lateinit var appComponent: IAppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerIAppComponent
            .builder()
            .dataModule(DataModule(applicationContext, applicationScope))
            .build()
    }
}