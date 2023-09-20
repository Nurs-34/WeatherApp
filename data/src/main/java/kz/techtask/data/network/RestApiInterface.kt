package kz.techtask.data.network

import kz.techtask.data.model.CityWeather
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApiInterface {

    @GET("/current.json")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
    ): Response<CityWeather>

    @GET("/forecast.json")
    suspend fun getForecastWeather(
        @Query("q") city: String,
        @Query("days") days: Int,
    ): Response<CityWeather>

    @GET("/history.json")
    suspend fun getHistoryWeather(
        @Query("q") city: String,
        @Query("dt") date: String,
        @Query("lang") lang: String = DEFAULT_LANGUAGE,
    ): Response<CityWeather>

    companion object {
        private const val BASE_URL = "https://weatherapi-com.p.rapidapi.com"
        private const val HEADER_API_KEY = "d6ca70e3c2msh2fb9f75056c62f9p10feccjsn3c12325d47ca"
        private const val HEADER_API_HOST = "weatherapi-com.p.rapidapi.com"
        private const val DEFAULT_LANGUAGE = "eu"

        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val url = originalRequest.url.newBuilder().build()

                val modifierRequest = originalRequest.newBuilder()
                    .addHeader("X-RapidAPI-Key", HEADER_API_KEY)
                    .addHeader("X-RapidAPI-Host", HEADER_API_HOST)
                    .url(url)
                    .build()
                chain.proceed(modifierRequest)
            }
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        operator fun invoke(): RestApiInterface {
            return retrofit.create(RestApiInterface::class.java)
        }
    }
}
