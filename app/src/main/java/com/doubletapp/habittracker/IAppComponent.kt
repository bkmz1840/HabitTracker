package com.doubletapp.habittracker

import com.doubletapp.data.DataModule
import com.doubletapp.domain.DomainModule
import com.doubletapp.domain.HabitsInteractor
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, DomainModule::class])
interface IAppComponent {
    fun loadHabitsInteractor(): HabitsInteractor
}