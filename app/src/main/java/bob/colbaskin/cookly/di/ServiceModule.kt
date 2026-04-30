package bob.colbaskin.cookly.di

import bob.colbaskin.cookly.auth.data.AuthApiService
import bob.colbaskin.cookly.create_recipe.data.CreateRecipeApiService
import bob.colbaskin.cookly.home.data.HomeRecipeApiService
import bob.colbaskin.cookly.onboarding_preferences.data.OnboardingPreferencesApiService
import bob.colbaskin.cookly.profile.data.ProfileApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    @Named("AuthApiAuthService")
    fun provideAuthApiAuthService(
        @Named("AuthServiceRetrofit") authServiceRetrofit: Retrofit
    ): AuthApiService {
        return authServiceRetrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("AuthApiRecipeService")
    fun provideAuthApiRecipeService(
        @Named("RecipeServiceRetrofit") recipeServiceRetrofit: Retrofit
    ): AuthApiService {
        return recipeServiceRetrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCreateRecipeApiService(
        @Named("RecipeServiceRetrofit") recipeServiceRetrofit: Retrofit
    ): CreateRecipeApiService {
        return recipeServiceRetrofit.create(CreateRecipeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileApiService(
        @Named("RecipeServiceRetrofit") recipeServiceRetrofit: Retrofit
    ): ProfileApiService {
        return recipeServiceRetrofit.create(ProfileApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeRecipeApiService(
        @Named("RecipeServiceRetrofit") recipeServiceRetrofit: Retrofit
    ): HomeRecipeApiService {
        return recipeServiceRetrofit.create(HomeRecipeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOnboardingPreferencesApiService(
        @Named("RecipeServiceRetrofit") recipeServiceRetrofit: Retrofit
    ): OnboardingPreferencesApiService {
        return recipeServiceRetrofit.create(OnboardingPreferencesApiService::class.java)
    }
}
