package com.ltu.m7019e.forktales.database

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ltu.m7019e.forktales.network.ForkTalesApiService
import com.ltu.m7019e.forktales.utils.Constants
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * An interface that defines the repositories required by the application.
 */
interface AppContainer {
    val recipesRepository: RecipesRepository
    val recipesDetailsRepository: RecipesDetailsRepository
    val categoriesRepository: CategoriesRepository
}

/**
 * A class that provides the repositories required by the application.
 * It sets up the Retrofit service and provides the repositories.
 *
 * @param context The application context.
 */
class DefaultAppContainer(private val context: Context) : AppContainer {
    /**
     * A function that returns a HttpLoggingInterceptor with the log level set to BODY.
     * This interceptor can be added to the OkHttpClient to log the HTTP request and response data.
     *
     * @return A HttpLoggingInterceptor with the log level set to BODY.
     */
    private fun getLoggerInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    /**
     * A Json object with some specific configuration for the application.
     */
    private val forkTalesJson = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    /**
     * A Retrofit object that is used to create the Retrofit service.
     * It is configured with a OkHttpClient, a converter factory and a base URL.
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .client(
            okhttp3.OkHttpClient.Builder()
                .addInterceptor(getLoggerInterceptor())
                .connectTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .build()
        )
        .addConverterFactory(forkTalesJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl(Constants.THE_MEAL_DB_API_BASE_URL + Constants.API_KEY + "/")
        .build()

    /**
     * A lazy property that creates the Retrofit service.
     */
    private val retrofitService: ForkTalesApiService by lazy {
        retrofit.create(ForkTalesApiService::class.java)
    }

    /**
     * A lazy property that provides the RecipesRepository.
     */
    override val recipesRepository: RecipesRepository by lazy {
        NetworkRecipesRepository(retrofitService)
    }

    /**
     * A lazy property that provides the RecipesDetailsRepository.
     */
    override val recipesDetailsRepository: RecipesDetailsRepository by lazy {
        NetworkRecipesDetailsRepository(retrofitService)
    }

    /**
     * A lazy property that provides the CategoriesRepository.
     */
    override val categoriesRepository: CategoriesRepository by lazy {
        NetworkCategoriesRepository(retrofitService)
    }
}