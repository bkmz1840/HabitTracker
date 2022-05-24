package com.doubletapp.habittracker

import com.doubletapp.data.DataModule
import com.doubletapp.domain.DomainModule
import com.doubletapp.domain.HabitsUseCases
import com.doubletapp.habittracker.viewModels.AddHabitViewModel
import com.doubletapp.habittracker.viewModels.HabitListViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, DomainModule::class])
interface IAppComponent {
    fun loadHabitsUseCases(): HabitsUseCases
}