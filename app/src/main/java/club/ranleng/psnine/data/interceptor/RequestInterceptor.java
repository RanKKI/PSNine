package club.ranleng.psnine.data.interceptor;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements okhttp3.Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;
        newRequest = request.newBuilder()
                .addHeader("User-Agent", "Unofficial PSNine Android Application by RanKKI")
                .build();
        return chain.proceed(newRequest);
    }
}
