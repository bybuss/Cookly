package bob.colbaskin.cookly.di

import android.content.Context
import bob.colbaskin.cookly.auth.data.AuthRepositoryImpl
import bob.colbaskin.cookly.auth.data.AuthApiService
import bob.colbaskin.cookly.auth.domain.network.AuthRepository
import bob.colbaskin.cookly.common.user_prefs.data.UserPreferencesRepositoryImpl
import bob.colbaskin.cookly.common.user_prefs.data.dataStore.UserDataStore
import bob.colbaskin.cookly.common.user_prefs.domain.UserPreferencesRepository
import bob.colbaskin.cookly.di.token.TokenDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
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
    fun provideAuthRepository(
        @ApplicationContext context: Context,
        tokenDataStore: TokenDataStore,
        @Named("AuthApiAuthService") authApiAuthService: AuthApiService,
        @Named("AuthApiRecipeService") authApiRecipeService: AuthApiService,
        userPreferencesRepository: UserPreferencesRepository
    ): AuthRepository {
        return AuthRepositoryImpl(
            context = context,
            tokenDataStore = tokenDataStore,
            authApiAuthService = authApiAuthService,
            authApiRecipeService = authApiRecipeService,
            userPreferencesRepository = userPreferencesRepository
        )
    }
}
