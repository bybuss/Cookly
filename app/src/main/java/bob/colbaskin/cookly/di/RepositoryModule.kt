package bob.colbaskin.cookly.di

import android.content.Context
import bob.colbaskin.cookly.auth.data.AuthRepositoryImpl
import bob.colbaskin.cookly.auth.data.AuthApiService
import bob.colbaskin.cookly.auth.domain.network.AuthRepository
import bob.colbaskin.cookly.common.user_prefs.data.UserPreferencesRepositoryImpl
import bob.colbaskin.cookly.common.user_prefs.data.data_store.UserDataStore
import bob.colbaskin.cookly.common.user_prefs.domain.UserPreferencesRepository
import bob.colbaskin.cookly.create_recipe.data.CreateRecipeApiService
import bob.colbaskin.cookly.create_recipe.data.CreateRecipeRepositoryImpl
import bob.colbaskin.cookly.create_recipe.domain.CreateRecipeRepository
import bob.colbaskin.cookly.di.token.TokenDataStore
import bob.colbaskin.cookly.home.data.HomeRecipeApiService
import bob.colbaskin.cookly.home.data.HomeRecipeRepositoryImpl
import bob.colbaskin.cookly.home.domain.HomeRecipeRepository
import bob.colbaskin.cookly.onboarding_preferences.data.OnboardingPreferencesApiService
import bob.colbaskin.cookly.onboarding_preferences.data.OnboardingPreferencesRepositoryImpl
import bob.colbaskin.cookly.onboarding_preferences.domain.OnboardingPreferencesRepository
import bob.colbaskin.cookly.profile.data.ProfileApiService
import bob.colbaskin.cookly.profile.data.ProfileRepositoryImpl
import bob.colbaskin.cookly.profile.domain.ProfileRepository
import bob.colbaskin.cookly.shopping_cart.data.CartDao
import bob.colbaskin.cookly.shopping_cart.data.ShoppingCartRepositoryImpl
import bob.colbaskin.cookly.shopping_cart.domain.ShoppingCartRepository
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

    @Provides
    @Singleton
    fun provideCreateRecipeRepository(
        @ApplicationContext context: Context,
        apiService: CreateRecipeApiService
    ): CreateRecipeRepository {
        return CreateRecipeRepositoryImpl(
            context = context,
            apiService = apiService
        )
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        @ApplicationContext context: Context,
        profileApiService: ProfileApiService,
        @Named("AuthApiAuthService") authApiAuthService: AuthApiService,
        userPreferencesRepository: UserPreferencesRepository,
        tokenDataStore: TokenDataStore
    ): ProfileRepository {
        return ProfileRepositoryImpl(
            context = context,
            profileApiService = profileApiService,
            authApiAuthService = authApiAuthService,
            userPreferencesRepository = userPreferencesRepository,
            tokenDataStore = tokenDataStore,
        )
    }

    @Provides
    @Singleton
    fun provideHomeRecipeRepository(
        @ApplicationContext context: Context,
        apiService: HomeRecipeApiService
    ): HomeRecipeRepository {
        return HomeRecipeRepositoryImpl(
            context = context,
            apiService = apiService
        )
    }

    @Provides
    @Singleton
    fun provideShoppingCartRepository(cartDao: CartDao): ShoppingCartRepository {
        return ShoppingCartRepositoryImpl(cartDao = cartDao)
    }

    @Provides
    @Singleton
    fun provideOnboardingPreferencesRepository(
        @ApplicationContext context: Context,
        apiService: OnboardingPreferencesApiService
    ): OnboardingPreferencesRepository {
        return OnboardingPreferencesRepositoryImpl(
            context = context,
            apiService = apiService
        )
    }
}
