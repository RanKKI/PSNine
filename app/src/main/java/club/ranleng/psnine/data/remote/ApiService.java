package club.ranleng.psnine.data.remote;

import io.reactivex.Observable;
import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("{type}")
    Observable<ResponseBody> getTopics(@Path("type") String type, @Query("page") int page);

    @GET("node/{type}")
    Observable<ResponseBody> getTopicsWithNode(@Path("type") String type, @Query("page") int page);

    @GET("{type}/{id}")
    Observable<ResponseBody> getTopic(@Path("type") String type, @Path("id") String id);

    @GET("{type}/{id}/comment")
    Observable<ResponseBody> getTopicComment(@Path("type") String type, @Path("id") String id, @Query("page") int page);

    @POST("sign/signin/ajax")
    Observable<ResponseBody> Login(@Body FormBody body);

    @GET("set/qidao/ajax")
    Observable<ResponseBody> Signin();
}
