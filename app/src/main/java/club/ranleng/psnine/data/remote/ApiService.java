package club.ranleng.psnine.data.remote;

import io.reactivex.Observable;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // get topics list
    @GET("{type}")
    Observable<ResponseBody> getTopic(@Path("type") String type, @Query("ob") String ob, @Query("title") String search_word, @Query("ele") String ele, @Query("page") int page);

    @GET("node/{node}")
    Observable<ResponseBody> getNode(@Path("node") String type, @Query("ob") String ob, @Query("title") String search_word, @Query("page") int page);

    @GET("my/notice")
    Observable<ResponseBody> getNotice();

    @GET("my/fav")
    Observable<ResponseBody> getFav(@Query("channel") String channel);

    @GET("my/element")
    Observable<ResponseBody> getElement();

    // get topic
    @GET("topic/{id}")
    Observable<ResponseBody> getArticle(@Path("id") int id, @Query("page") int page);

    @GET("gene/{id}")
    Observable<ResponseBody> getGene(@Path("id") int id);

    @GET("qa/{id}")
    Observable<ResponseBody> getQA(@Path("id") int id);

    // action
    @POST("set/fav/ajax")
    Call<ResponseBody> Fav(@Body FormBody body);

    @POST("set/updown/ajax")
    Call<ResponseBody> Up(@Body FormBody body);

    //TODO
    //多页评论
    @GET("topic/{id}/comment")
    Observable<ResponseBody> getArticleComment(@Path("id") int id);

    @GET("gene/{id}/comment")
    Observable<ResponseBody> getGeneComment(@Path("id") int id);

    //comment
    @POST("set/comment/post")
    Call<ResponseBody> Reply(@Body FormBody body);

    @POST("set/edit/ajax")
    Call<ResponseBody> editReply(@Body FormBody body);

    @GET("set/qidao/ajax")
    Call<ResponseBody> signIn();

    @Headers("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.93 Safari/537.36")
    @POST("sign/in")
    Call<ResponseBody> Login(@Body FormBody body);

    @Headers("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.93 Safari/537.36")
    @GET("sign/out")
    Call<ResponseBody> Logout();

    @Multipart
    @POST("my/photo")
    Call<ResponseBody> upload(@Part MultipartBody.Part file);

    @POST("my/photo")
    Call<ResponseBody> del(@Body FormBody body);

    @GET("my/photo")
    Observable<ResponseBody> getPhoto();

    @GET("psnid/{id}")
    Observable<ResponseBody> getPSNINFO(@Path("id") String psnid);

    @GET("psnid/{id}/psngame")
    Observable<ResponseBody> getGame(@Path("id") String psnid);

    @GET("psnid/{id}/comment")
    Observable<ResponseBody> getMsg(@Path("id") String psnid);

    @GET("psnid/{id}/topic")
    Observable<ResponseBody> getTopic(@Path("id") String psnid);

    @GET("psnid/{id}/gene")
    Observable<ResponseBody> getGene(@Path("id") String psnid);

    @GET("psnid/{id}/upbase")
    Call<ResponseBody> upBase(@Path("id") String psnid);

    @GET("psnid/{id}/upgame")
    Call<ResponseBody> upGame(@Path("id") String psnid);

    @POST("set/blocked/ajax")
    Call<ResponseBody> Block(@Body FormBody body);

}
