package club.ranleng.psnine.utils;

import java.io.IOException;
import java.lang.annotation.Annotation;

import club.ranleng.psnine.model.ResponseError;
import club.ranleng.psnine.widget.Internet;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {

    public static ResponseError parseError(Response<?> response) {
        Converter<ResponseBody, ResponseError> converter =
                Internet.retrofit.responseBodyConverter(ResponseError.class, new Annotation[0]);

        ResponseError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ResponseError();
        }

        return error;
    }
}

