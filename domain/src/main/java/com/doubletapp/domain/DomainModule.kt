package com.doubletapp.domain

import com.doubletapp.domain.interfaces.IHabitsRepo
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class DomainModule {
    @Provides
    fun provideLoadHabitsInteractor(
        @Named("HABITS_REPO") habitsRepo: IHabitsRepo
    ): HabitsInteractor = HabitsInteractor(habitsRepo)
}