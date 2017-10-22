package club.ranleng.psnine.data.remote;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("{type}")
    Observable<ResponseBody> getTopics(@Path("type") String type, @Query("page") int page);

    @GET("topic/{id}")
    Observable<ResponseBody> getTopic(@Path("id") String id);

    @GET("topic/{id}/comment")
    Observable<ResponseBody> getTopicComment(@Path("id") String id, @Query("page") int page);
}
