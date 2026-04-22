package bob.colbaskin.cookly.di

import android.content.Context
import android.util.Log
import bob.colbaskin.cookly.BuildConfig
import bob.colbaskin.cookly.auth.data.AuthApiService
import bob.colbaskin.cookly.di.token.TokenAuthenticator
import bob.colbaskin.cookly.di.token.TokenInterceptor
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton
import dagger.Lazy

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("AuthServiceOkHttpClient")
    fun provideAuthServiceOkHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient {
        val cookieJar = PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(context)
        )

        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
            .addInterceptor { chain ->
                val request = chain.request()
                Log.i("Cookies", "Sending cookies in auth service: ${request.headers["Cookie"]}")
                val response = chain.proceed(request)
                Log.i("Cookies", "Received cookies in auth service: ${response.headers["Set-Cookie"]}")
                response
            }
            .build()
    }

    @Provides
    @Singleton
    @Named("AuthServiceRetrofit")
    fun provideAuthServiceRetrofit(
        @Named("AuthServiceOkHttpClient") okHttpClient: OkHttpClient
    ): Retrofit {
        val jsonConfig = Json {
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .baseUrl(BuildConfig.AUTH_SERVICE_BASE_API_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConfig.asConverterFactory("application/ld+json".toMediaType()))
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    @Named("RecipeServiceOkHttpClient")
    fun provideRecipeServiceOkHttpClient(
        @ApplicationContext context: Context,
        tokenInterceptor: TokenInterceptor,
        tokenAuthenticator: Lazy<TokenAuthenticator>
    ): OkHttpClient {
        val cookieJar = PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(context)
        )

        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
            .authenticator { route, response ->
                tokenAuthenticator.get().authenticate(route, response)
            }
            .addInterceptor(tokenInterceptor)
            .addInterceptor { chain ->
                val request = chain.request()
                Log.d("Cookies", "Sending cookies in recipe service: ${request.headers["Cookie"]}")
                val response = chain.proceed(request)
                Log.d("Cookies", "Received cookies in recipe service: ${response.headers["Set-Cookie"]}")
                response
            }
            .build()
    }

    @Provides
    @Singleton
    @Named("RecipeServiceRetrofit")
    fun provideRecipeServiceRetrofit(
        @Named("RecipeServiceOkHttpClient") okHttpClient: OkHttpClient
    ): Retrofit {
        val jsonConfig = Json {
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .baseUrl(BuildConfig.RECIPE_SERVICE_BASE_API_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConfig.asConverterFactory("application/ld+json".toMediaType()))
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

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
}
