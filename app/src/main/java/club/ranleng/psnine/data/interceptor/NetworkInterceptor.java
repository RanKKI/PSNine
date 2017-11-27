package club.ranleng.psnine.data.interceptor;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import club.ranleng.psnine.utils.Parse;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NetworkInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        try {
            Response response = chain.proceed(chain.request());
            int code = response.code();
            if (code >= 400 && code <= 600) {
                ToastUtils.showShort(Parse.parseHTTPErrorCode(code));
                return createNew(chain, response.body());
            }
            return response;
        } catch (SocketTimeoutException exception) {
            exception.printStackTrace();
        }
        return createNew(chain, null);
    }

    private Response createNew(Chain chain, ResponseBody responseBody) {
        if (responseBody == null) {
            responseBody = ResponseBody.create(MediaType.parse("text/html; charset=utf-8"), "");
        }
        return new Response.Builder().request(chain.request())
                .protocol(chain.connection().protocol())
                .code(211)
                .message("OK")
                .body(responseBody).build();
    }

}
