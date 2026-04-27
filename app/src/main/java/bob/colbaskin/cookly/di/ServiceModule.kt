package bob.colbaskin.cookly.di

import bob.colbaskin.cookly.auth.data.AuthApiService
import bob.colbaskin.cookly.create_recipe.data.CreateRecipeApiService
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
}
