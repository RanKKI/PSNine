package xyz.rankki.psnine.data.http.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class RequestInterceptor : okhttp3.Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val newRequest: Request = chain!!.request()
                .newBuilder()
                .addHeader("User-Agent", "Unofficial PSNine Android Application by RanKKI")
                .build()
        return chain.proceed(newRequest)
    }

}