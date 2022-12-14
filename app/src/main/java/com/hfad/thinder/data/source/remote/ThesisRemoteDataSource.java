package com.hfad.thinder.data.source.remote;

import android.util.Log;

import com.hfad.thinder.R;
import com.hfad.thinder.data.model.Thesis;
import com.hfad.thinder.data.source.remote.okhttp.ThesesApiService;
import com.hfad.thinder.data.source.result.Pair;
import com.hfad.thinder.data.source.result.Result;

import org.json.JSONException;

import java.sql.Time;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This class handles all the errors that occur through HTTP requests on the /thesis/ endpoint.
 */
public class ThesisRemoteDataSource {
    private final int TIMEOUT_SECONDS = 10000;
    private final ThesesApiService okHttpService = new ThesesApiService();

    /**
     * Handles the errors that occur during the HTTP POST that creates a new thesis. Also fetches the result of the future.
     *
     * @param thesis
     * @return Result that includes a success value and an error message
     */
    public Result createNewThesis(final Thesis thesis) {

        try {
            CompletableFuture<Result> result = okHttpService.createNewThesisFuture(thesis);
            Result resultValue = result.get(TIMEOUT_SECONDS,TimeUnit.SECONDS);
            return resultValue;
        } catch (JSONException | ExecutionException | InterruptedException  e) {
            return new Result(R.string.exception_during_HTTP_call, false);
        }
        catch(TimeoutException e){
            return new Result(R.string.timeout_exception,false);
        }
    }

    /**
     * Handles the errors that occur during the HTTP GET request that gets a thesis with a specific id from the backend.
     *
     * @param id
     * @return A Pair of both the fetched thesis object and a Result object
     */
    public Pair<Thesis, Result> getNewThesis(final UUID id) {
        try {
            Pair<CompletableFuture<Thesis>, CompletableFuture<Result>> result = okHttpService.getSpecificThesisFuture(id);
            Thesis thesis = result.getFirst().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            Result resultValue = result.getSecond().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return new Pair<>(thesis, resultValue);

        } catch (ExecutionException | InterruptedException  e) {
            return new Pair<>(null, new Result(R.string.exception_during_HTTP_call, false));
        }
        catch(TimeoutException e){
            return new Pair<>(null,new Result(R.string.timeout_exception,false));
        }
    }


    /**
     * Handles the errors that occur during the HTTP DELETE request that deletes the thesis (specified through the id)
     *
     * @param thesisId
     * @return Result that includes a success value and an error message
     */
    public Result deleteThesis(final UUID thesisId) {
        try {
            CompletableFuture<Result> result = okHttpService.deleteThesisFuture(thesisId);
            Result resultValue =  result.get(TIMEOUT_SECONDS,TimeUnit.SECONDS);
            return resultValue;
        } catch (ExecutionException | InterruptedException  e) {
            return new Result(R.string.exception_during_HTTP_call, false);
        }
        catch(TimeoutException e){
            return new Result(R.string.timeout_exception,false);
        }
    }
}
