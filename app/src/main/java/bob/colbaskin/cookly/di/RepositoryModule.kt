package bob.colbaskin.cookly.di

import bob.colbaskin.cookly.common.user_prefs.data.UserPreferencesRepositoryImpl
import bob.colbaskin.cookly.common.user_prefs.data.dataStore.UserDataStore
import bob.colbaskin.cookly.common.user_prefs.domain.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(dataStore: UserDataStore): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(dataStore)
    }
}
