package com.example.moshimultiplejsondemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.squareup.moshi.Moshi
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val moshi = Moshi.Builder()
            .add(MyModelAdapter())
            .build()

        val clientBuilder = OkHttpClient.Builder().apply {
            addInterceptor(MyFakeInterceptor())
        }

        val client = clientBuilder.build()

        val retrofit = Retrofit.Builder()
                // I have fake interceptor that returns your data format
                .baseUrl("https://example.com/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()

        val service: ApiService = retrofit.create<ApiService>(ApiService::class.java)

        // I use RxJava for retrofit calls, it is not relevant for your question, you can do
        // whatever way of network call you want, the main point is the jsonAdapter
        service.getMyEntities()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ infoObjects ->
                    // check the logs and match it with data returned in [MyFakeInterceptor]
                    Log.d("MyModels", infoObjects.toString())
                }, { error ->
                    error.printStackTrace()
                    Log.e("Error", error.message ?: "")
                })
    }
}
