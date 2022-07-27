package com.hfad.thinder.data.source.remote;

import com.hfad.thinder.data.model.Thesis;
import com.hfad.thinder.data.source.remote.okhttp.ThesesApiService;
import com.hfad.thinder.data.source.remote.okhttp.UsersApiService;
import com.hfad.thinder.data.source.result.Result;
import com.hfad.thinder.data.source.result.Tuple;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ThesisRemoteDataSource {
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    String url = "http://localhost:8080";
    private final ThesesApiService okHttpService = new ThesesApiService();


    public Result createNewThesis(final Thesis thesis) {

        try {
            CompletableFuture<Result> result = okHttpService.createNewThesisFuture(thesis);
            return result.get(100, TimeUnit.SECONDS);
        } catch (JSONException e) {
            return new Result("not successful", false);
        } catch (ExecutionException e) {
            return new Result("not successful", false);
        } catch (InterruptedException e) {
            return new Result("not successful", false);
        } catch (TimeoutException e) {
            return new Result("not successful", false);
        }


    }

    public Tuple<Thesis,Result> getNewThesis(UUID id){
            try {
                Tuple<CompletableFuture<Thesis>,CompletableFuture<Result>> result = okHttpService.getSpecificThesisFuture(id);
                return new Tuple<>(result.x.get(10000, TimeUnit.SECONDS),result.y.get(10000, TimeUnit.SECONDS));

            } catch (ExecutionException e) {
                return new Tuple<>(null,new Result("error", false));
            } catch (InterruptedException e) {
                return new Tuple<>(null,new Result("error", false));
            } catch (TimeoutException e) {
                return new Tuple<>(null,new Result("error", false));
            }
    }

    public Result editThesis (UUID thesisId,Thesis thesis) {
        try{
            CompletableFuture<Result> result = okHttpService.editThesisFuture(thesisId, thesis);
            return result.get(100, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            return new Result("not successful", false);
        } catch (InterruptedException e) {
            return new Result("not successful", false);
        } catch (TimeoutException e) {
            return new Result("not successful", false);
        } catch (JSONException e) {
            return new Result("not successful", false);
        }

    }
    public Result deleteThesis(final UUID thesisId){
        try{
            CompletableFuture<Result> result = okHttpService.deleteThesisFuture(thesisId);
            return result.get(100, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            return new Result("not successful", false);
        } catch (InterruptedException e) {
            return new Result("not successful", false);
        } catch (TimeoutException e) {
            return new Result("not successful", false);
        }
    }
}
