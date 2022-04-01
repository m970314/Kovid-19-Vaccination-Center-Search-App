package com.example.koronaapp.network
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.room.RoomDatabase
import com.example.koronaapp.Model.HospitalInfoResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.gun0912.tedpermission.provider.TedPermissionProvider.context
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
@AndroidEntryPoint
class HospitalRepositori {
    @Inject
    lateinit var db : AppDatabase
    companion object {
        private const val TAG = "HospitalRepositori"
        private const val BASE_URL = "https://api.odcloud.kr/api/15077586/"
    }

    private var hospitalcontents: HospitalContents
    private var haspitalInfoMutableLiveData: MutableLiveData<HospitalInfoResponse> = MutableLiveData()

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val gson: Gson = GsonBuilder().setLenient().create()

        hospitalcontents = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(HospitalContents::class.java)
    }
    fun searchhaspital(page: String, perPage: String, servicekey: String) {
        hospitalcontents.searchhaspital(page, perPage, servicekey)
            .enqueue(object : Callback<HospitalInfoResponse?> {
                override fun onResponse(
                    call: Call<HospitalInfoResponse?>,
                    response: Response<HospitalInfoResponse?>
                ) {
                    CoroutineScope(Dispatchers.IO).launch {
                        var gson  = AppDatabase.DatabaseModule.provideGson()
                        db = AppDatabase.DatabaseModule.provideDatabase(context,gson)
                        db.roomDao().insert(response.body()!!)
                    }
                }
                override fun onFailure(call: Call<HospitalInfoResponse?>, t: Throwable) {
                    Log.e(TAG, "onFailure: error. cause: ${t.message}")
                    haspitalInfoMutableLiveData.postValue(null)
                }
            })
    }

    fun getHospitalResponseLiveData(): LiveData<HospitalInfoResponse> {
        return this.haspitalInfoMutableLiveData
    }
}