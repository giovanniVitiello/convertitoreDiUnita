package com.example.convertitorediunita

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.convertitorediunita.ui.converter.MoneyUtilResult
import com.example.convertitorediunita.ui.model.Money
import com.example.convertitorediunita.ui.model.MoneyUtil
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val API_KEY = "35160723af91450bb52cb10b2a3c88ba"

const val SHARED_PREFS_MAIN_NAME = "Money"
const val SHARED_PREFS_EURO = "lastEuro"
const val SHARED_PREFS_POUND = "lastDollar"
const val SHARED_PREFS_DATA = "lastData"

interface MoneyUtilResultReceiver {
    fun receive(result: MoneyUtilResult)
}

class RetrofitService(val context: Context) {
    //the okhttp Interceptor, need to view in the logCat the response of the server
    //in json file
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(SHARED_PREFS_MAIN_NAME, Context.MODE_PRIVATE)
    var euro = sharedPreferences.getFloat(SHARED_PREFS_EURO, 0f)
    var pound = sharedPreferences.getFloat(SHARED_PREFS_POUND, 0f)
    var data: String? = sharedPreferences.getString(SHARED_PREFS_DATA, " ")

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://openexchangerates.org/api/")
        .client(client)
        .build()

    private val service: RetrofitServiceInterface = retrofit.create(RetrofitServiceInterface::class.java)

    fun getDataFromApi(receveir: MoneyUtilResultReceiver) = service
        .getEuroMoney(API_KEY, "USD")
        .enqueue(object : Callback<Money> {
            override fun onResponse(call: Call<Money>, response: Response<Money>) {
                val success = response.body()
                if (success != null) {
                    val moneyUtil = success.toMoneyUtil()
                    receveir.receive(MoneyUtilResult.Success(moneyUtil))

                    sharedPreferences.edit().putFloat(SHARED_PREFS_EURO, success.toEuro()!!.toFloat()).apply()
                    sharedPreferences.edit().putFloat(SHARED_PREFS_POUND, success.toGbp()!!.toFloat()).apply()

                    val simpleDateFormat = SimpleDateFormat("dd MM yyyy HH mm ss", Locale.ITALY)
                    val currentDateAndTime = simpleDateFormat.format(Date())
                    sharedPreferences.edit().putString(SHARED_PREFS_DATA, currentDateAndTime).apply()

                    Log.d("OnSuccess", success.toString())
                } else {
                    receveir.receive(MoneyUtilResult.Error(Throwable(response.errorBody().toString())))
                }
            }

            override fun onFailure(call: Call<Money>, t: Throwable) {

                if (euro.toDouble() != 0.0 && pound.toDouble() != 0.0) {
                    val moneyUtil = MoneyUtil(MoneyUtil.Rates(euro.toDouble(), pound.toDouble()))
                    receveir.receive(MoneyUtilResult.SuccessWithoutNetwork(moneyUtil))
                } else
                    receveir.receive(MoneyUtilResult.Error(Throwable("error")))
            }
        })

    suspend fun moneyDetail(): MoneyUtil {
        val response = service.getEuroMoneyDetail(app_id = API_KEY, base = "USD")
        val success = response.body()

        if (success != null) {
            return success.toMoneyUtil()
        } else {
            @Suppress("TooGenericExceptionThrown")
            throw Exception(Throwable(response.errorBody().toString()))
        }
    }
}

interface RetrofitServiceInterface {
    @Suppress("FunctionParameterNaming")
    @GET("latest.json")
    fun getEuroMoney(@Query(value = "app_id") app_id: String, @Query("base") base: String): Call<Money>

    @Suppress("FunctionParameterNaming")
    @GET("latest.json?{app_id}")
    suspend fun getEuroMoneyDetail(@Path("app_id") app_id: String, @Query("base") base: String): Response<Money>
}
