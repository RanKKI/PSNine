package club.ranleng.psnine.data.interceptor;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;

import java.io.IOException;

import club.ranleng.psnine.R;
import okhttp3.Interceptor;
import okhttp3.Response;

public class NetworkInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        int code = response.code();
        if (code >= 400 && code <= 600) {
            ToastUtils.showShort(parseErrorCode(code));
            return new Response.Builder().request(chain.request())
                    .protocol(chain.connection().protocol())
                    .code(200)
                    .body(response.body()).build();
        }
        return response;
    }

    private String parseErrorCode(int code) {
        String error_message;
        switch (code) {
            case 400:
                error_message = Utils.getApp().getString(R.string.http_code_bad_request);
                break;
            case 401:
                error_message = Utils.getApp().getString(R.string.http_code_unauthorized);
                break;
            case 402:
                error_message = Utils.getApp().getString(R.string.http_code_payment_required);
                break;
            case 403:
                error_message = Utils.getApp().getString(R.string.http_code_forbidden);
                break;
            case 404:
                error_message = Utils.getApp().getString(R.string.http_code_not_found);
                break;
            case 405:
                error_message = Utils.getApp().getString(R.string.http_code_method_not_allowed);
                break;
            case 406:
                error_message = Utils.getApp().getString(R.string.http_code_not_acceptable);
                break;
            case 500:
                error_message = Utils.getApp().getString(R.string.http_code_internal_server_error);
                break;
            case 501:
                error_message = Utils.getApp().getString(R.string.http_code_not_implemented);
                break;
            case 502:
                error_message = Utils.getApp().getString(R.string.http_code_bad_gateway);
                break;
            case 503:
                error_message = Utils.getApp().getString(R.string.http_code_service_unavailable);
                break;
            case 504:
                error_message = Utils.getApp().getString(R.string.http_code_gateway_timeout);
                break;
            default:
                error_message = Utils.getApp().getString(R.string.http_code_unknown_error);
                break;
        }
        return error_message;
    }
}
