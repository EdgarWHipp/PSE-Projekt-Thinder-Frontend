package com.hfad.thinder.data.source.remote.okhttp;

import androidx.annotation.NonNull;

import com.hfad.thinder.data.model.Degree;
import com.hfad.thinder.data.model.USERTYPE;
import com.hfad.thinder.data.model.User;
import com.hfad.thinder.data.source.repository.UserRepository;
import com.hfad.thinder.data.source.result.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Set;
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

public class UsersApiService {
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    com.hfad.thinder.data.source.remote.okhttp.UsersApiService okHttpService;
    OkHttpClient client = new OkHttpClient();
    String url = "http://localhost:8080";
    String emulatorLocalHost = "http://10.0.2.2:8080";

    /**
     * This function creates the HTTP GET request that firstly makes sure the email, password tuple exists in the database and then fetches a JSON with attributes type and id.
     *
     * @param password of user loging in
     * @param eMail    of user loging in
     * @return Response message of the backend which isnt yet parsed.
     * @throws JSONException
     * @throws IOException
     */
    public Response usersLoginResponse(String password, String eMail) throws JSONException, IOException {
        JSONObject loginJson = new JSONObject()
                .put("password", password)
                .put("mail", eMail);
        RequestBody body = RequestBody.create(loginJson.toString(), JSON);
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost:8080")
                .addPathSegment("users")
                .addQueryParameter("password", password)
                .addQueryParameter("mail", eMail)
                .build();
        System.out.println(url);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = client.newCall(request);
        return call.execute();
    }

    /**
     * This function creates the HTTP PUT request that completes the user profile by extending the profile through either the additional attributes from the supervisor.
     *
     * @param degree
     * @param location
     * @param institute
     * @param phoneNumber
     * @return Response message of the backend which isnt yet parsed.
     * @throws JSONException
     * @throws IOException
     */
    public Response extendUserToSupervisorResponse(String degree, String location, String institute, String phoneNumber) throws JSONException, IOException {
        JSONObject supervisorJson = new JSONObject()
                .put("academicDegree", degree)
                .put("location", location)
                .put("institute", institute)
                .put("phoneNumber", phoneNumber)
                .put("theses", null);

        RequestBody body = RequestBody.create(supervisorJson.toString(), JSON);

        Request request = new Request.Builder()
                .url(url + "/supervisor/" + UserRepository.getInstance().getCurrentUUID())
                .put(body)
                .build();

        Call call = client.newCall(request);
        return call.execute();
    }

    /**
     * This function creates the HTTP PUT request that completes the user profile by extending the profile through either the additional attributes from the student.
     *
     * @param degrees
     * @return Response message of the backend which isnt yet parsed.
     */
    public Response extendUserToStudentResponse(Set<Degree> degrees) throws JSONException, IOException {
        JSONObject studentJson = new JSONObject()
                .put("degrees", degrees)
                .put("thesesRatings", null);


        RequestBody body = RequestBody.create(studentJson.toString(), JSON);


        Request request = new Request.Builder()
                .url(url + "/student/" + UserRepository.getInstance().getCurrentUUID())
                .put(body)
                .build();

        Call call = client.newCall(request);
        return call.execute();
    }

    /**
     * @param token
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public Response verifyResponse(String token) throws JSONException, IOException {

        JSONObject tokenJson = new JSONObject()
                .put("token", token);
        RequestBody body = RequestBody.create(tokenJson.toString(), JSON);


        Request request = new Request.Builder()
                .url(url + "/users/verify/")
                .put(body)
                .build();

        Call call = client.newCall(request);

        return call.execute();
    }

    /**
     * This function creates the HTTP POST request and thus, if no error occurs, leads to the creation of a new user in the postgres database.
     *
     * @param user
     * @return Call message of the backend which isnt yet parsed.
     * @throws JSONException
     */
    public CompletableFuture<Result> createNewUserCall(User user) throws JSONException {

        JSONObject userJson = new JSONObject()
                .put("firstName", user.getFirstName())
                .put("lastName", user.getLastName())
                .put("password", user.getPassword())
                .put("mail", user.geteMail());


        RequestBody body = RequestBody.create(userJson.toString(), JSON);


        Request request = new Request.Builder()
                .url(emulatorLocalHost + "/users/")
                .post(body)
                .build();
        CompletableFuture<Result> resultCompletableFuture = new CompletableFuture<>();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                resultCompletableFuture.complete(new Result(e.toString(), false));
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






    /**
     * This function creates the HTTP DELETE request that removes a user from the database (if successful)
     *
     * @return Response message of the backend which isnt yet parsed.
     * @throws IOException
     */
    public Call deleteUserResponse() throws IOException {
        Request request= new Request.Builder()
                .url(url+"/users/"+UserRepository.getInstance().getCurrentUUID())
                .delete()
                .build();
        Call call = client.newCall(request);
        return call;
    }

    /**
     * This function creates the HTTP DELETE request for a thesis that the user has unliked. Unliking it removes it
     * from the users liked theses page.
     *
     * @param thesisId
     * @return Response
     */
    public Response deleteUserThesisCall(final UUID thesisId) throws IOException {

        Request request = new Request.Builder()
                .url(url + "/users/thesis/" + thesisId)
                .delete()
                .build();
        Call call = client.newCall(request);
        return call.execute();

    }

    /**
     * This function creates the HTTP GET request for a thesis that the user has already liked - it is used to make sure
     * that the user can click on a liked thesis and receive a more detailed overview.
     *
     * @param thesisId
     * @return Response
     */
    public Response getUserThesisResponse(final UUID thesisId) throws IOException {
        Request request = new Request.Builder()
                .url(url + "/users/thesis/" + thesisId)
                .get()
                .build();
        Call call = client.newCall(request);
        return call.execute();
    }

}
