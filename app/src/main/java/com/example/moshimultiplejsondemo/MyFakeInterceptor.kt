package com.example.moshimultiplejsondemo

import okhttp3.*

class MyFakeInterceptor : Interceptor {

    // Return mock data instead of going to real endpoint
    override fun intercept(chain: Interceptor.Chain): Response? {
        var response: Response? = null
        if (chain.request().url().host().contains("example")) {
            val responseString: String = """
            [
              {
                "Id":188, 
                "image":"\/posts\/5fd9aa6961c6dd54129f51d1.jpeg"
              },
              {
                "Id":188, 
                "image": [
                  "\/posts\/5fd9aa6961c6dd54129f51d1.jpeg",
                  "\/posts\/5fd9aa6961c6dd54129f51d1.jpeg"
                ]
              }
            ]
        """.trimIndent()
            response = Response.Builder()
                .code(200)
                .message(responseString)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(
                    ResponseBody.create(
                        MediaType.parse("application/json"),
                        responseString.toByteArray()
                    )
                )
                .addHeader("content-type", "application/json")
                .build()
        } else {
            response = chain.proceed(chain.request())
        }
        return response
    }
}
