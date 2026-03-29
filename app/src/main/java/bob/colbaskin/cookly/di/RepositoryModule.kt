package bob.colbaskin.cookly.di

import android.content.Context
import bob.colbaskin.cookly.auth.data.AuthDataStoreRepositoryImpl
import bob.colbaskin.cookly.auth.data.AuthRepositoryImpl
import bob.colbaskin.cookly.auth.domain.local.AuthDataStoreRepository
import bob.colbaskin.cookly.auth.domain.network.AuthApiService
import bob.colbaskin.cookly.auth.domain.network.AuthRepository
import bob.colbaskin.cookly.common.user_prefs.data.UserPreferencesRepositoryImpl
import bob.colbaskin.cookly.common.user_prefs.data.dataStore.UserDataStore
import bob.colbaskin.cookly.common.user_prefs.domain.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    
    @Provides
    @Singleton
    fun provideAuthDataStoreRepository(
        @ApplicationContext context: Context
    ): AuthDataStoreRepository {
        return AuthDataStoreRepositoryImpl(
            context = context
        )
    }
    
    @Provides
    @Singleton
    fun provideAuthRepository(
        @ApplicationContext context: Context,
        authDataStoreRepository: AuthDataStoreRepository,
        authApiService: AuthApiService
    ): AuthRepository {
        return AuthRepositoryImpl(
            context = context,
            authDataStoreRepository = authDataStoreRepository,
            authApiService = authApiService
        )
    }
}
