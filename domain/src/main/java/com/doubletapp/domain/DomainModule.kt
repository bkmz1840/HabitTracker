package com.doubletapp.domain

import com.doubletapp.domain.interfaces.IHabitsRepo
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class DomainModule {
    @Provides
    fun provideLoadHabitsUseCases(
        @Named("HABITS_REPO") habitsRepo: IHabitsRepo
    ): HabitsUseCases = HabitsUseCases(habitsRepo)
}