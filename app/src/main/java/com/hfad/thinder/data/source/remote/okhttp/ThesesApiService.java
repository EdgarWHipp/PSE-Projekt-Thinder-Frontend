package com.hfad.thinder.data.source.remote.okhttp;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.hfad.thinder.data.model.Student;
import com.hfad.thinder.data.model.Supervisor;
import com.hfad.thinder.data.model.Thesis;
import com.hfad.thinder.data.model.User;
import com.hfad.thinder.data.source.repository.UserRepository;
import com.hfad.thinder.data.source.result.Result;
import com.hfad.thinder.data.source.result.Tuple;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ThesesApiService {
  private static final MediaType JSON
          = MediaType.parse("application/json; charset=utf-8");
  private static final OkHttpClient client = new OkHttpClient();
  private static final String url = "http://localhost:8080";
  private static final String emulatorLocalHost = "http://10.0.2.2:8080";

  public Tuple<CompletableFuture<List<Thesis>>, CompletableFuture<Result>> getAllPositivRatedThesesFuture(UUID id) {
    OkHttpClient clientAuth = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor
                    (UserRepository.getInstance().getUser().geteMail(),
                            UserRepository.getInstance().getUser().getPassword()))
            .build();
    CompletableFuture<Result> resultCompletableFuture = new CompletableFuture<>();
    CompletableFuture<List<Thesis>> thesisListFuture = new CompletableFuture<>();

    HttpUrl url = new HttpUrl.Builder()
            .scheme("http")
            .host("10.0.2.2")
            .port(8080)
            .addPathSegment("users")
            .addPathSegment(id.toString())
            .addPathSegment("rated-theses")
            .build();

    Request request = new Request.Builder()
            .url(url)
            .get()
            .build();

    Call call = clientAuth.newCall(request);
    call.enqueue(new Callback() {
      @Override
      public void onFailure(@NonNull Call call, @NonNull IOException e) {
        resultCompletableFuture.complete(new Result(e.toString(), false));
        thesisListFuture.complete(null);
      }

      @Override
      public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        if (response.isSuccessful()) {
          resultCompletableFuture.complete(new Result(true));
          // parse all returned theses.
          thesisListFuture.complete(null);
        } else {
          resultCompletableFuture.complete(new Result("not successful", false));
          thesisListFuture.complete(null);
        }
      }
    });

    Tuple<CompletableFuture<List<Thesis>>, CompletableFuture<Result>> resultCompletableFutureTuple
            = new Tuple<>(thesisListFuture, resultCompletableFuture);
    return resultCompletableFutureTuple;
  }

  public CompletableFuture<Result> createNewThesisFuture(Thesis thesis) throws JSONException {
    OkHttpClient clientAuth = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor
                    (UserRepository.getInstance().getUser().geteMail(),
                            UserRepository.getInstance().getUser().getPassword()))
            .build();
    CompletableFuture<Result> resultCompletableFuture = new CompletableFuture<>();
    JSONObject thesisJSON = new JSONObject()
            .put("name", thesis.getName())
            .put("motivation", thesis.getMotivation())
            .put("task", thesis.getTask())
            .put("questionForm", thesis.getForm())
            .put("images", thesis.getImages())
            .put("possibleDegrees", thesis.getPossibleDegrees())
            .put("supervisor", UserRepository.getInstance().getUser())
            .put("supervisingProfessor", thesis.getSupervisingProfessor());


    RequestBody body = RequestBody.create(thesisJSON.toString(), JSON);

    HttpUrl url = new HttpUrl.Builder()
            .scheme("http")
            .host("10.0.2.2")
            .port(8080)
            .addPathSegment("thesis")
            .build();
    Request request = new Request.Builder()
            .url(url)
            .post(body)
            .build();
    Call call = clientAuth.newCall(request);
    call.enqueue(new Callback() {
      @Override
      public void onFailure(@NonNull Call call, @NonNull IOException e) {
        resultCompletableFuture.complete(new Result("failure", false));
      }

      @Override
      public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        if (response.isSuccessful()) {
          resultCompletableFuture.complete(new Result(true));
        } else {
          resultCompletableFuture.complete(new Result("not successful", false));
        }
      }

    });
    return resultCompletableFuture;
  }

  public Tuple<CompletableFuture<Thesis>,CompletableFuture<Result>>  getSpecificThesisFuture(UUID thesisId){
    CompletableFuture<Result> resultCompletableFuture = new CompletableFuture<>();
    CompletableFuture<Thesis> resultThesis = new CompletableFuture<>();
    OkHttpClient clientAuth = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor
                    (UserRepository.getInstance().getUser().geteMail(),
                            UserRepository.getInstance().getUser().getPassword()))
            .build();
    HttpUrl url = new HttpUrl.Builder()
            .scheme("http")
            .host("10.0.2.2")
            .port(8080)
            .addPathSegment("thesis")
            .addPathSegment(thesisId.toString()).build();
    Request request = new Request.Builder()
            .url(url)
            .get()
            .build();
    Call call = clientAuth.newCall(request);
    call.enqueue(new Callback() {
      @Override
      public void onFailure(@NonNull Call call, @NonNull IOException e) {
        resultCompletableFuture.complete(new Result("failure",false));
      }

      @Override
      public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            if(response.isSuccessful()){
              //Parse thesis object from thesis json.
              Gson gson = new Gson();
              Thesis thesis = gson.fromJson(response.body().string(), Thesis.class);
              resultThesis.complete(thesis);
              resultCompletableFuture.complete(new Result(true));
            }else {
              resultCompletableFuture.complete(new Result("not successful",false));
            }
      }
    });




    return new Tuple<>(resultThesis,resultCompletableFuture);
  }


}
