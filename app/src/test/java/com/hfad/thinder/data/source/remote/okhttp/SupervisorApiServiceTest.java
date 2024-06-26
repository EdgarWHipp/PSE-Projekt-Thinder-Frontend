package com.hfad.thinder.data.source.remote.okhttp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.hfad.thinder.data.model.Degree;
import com.hfad.thinder.data.model.Supervisor;
import com.hfad.thinder.data.model.Thesis;
import com.hfad.thinder.data.source.remote.okhttp.ApiUtils;
import com.hfad.thinder.data.source.remote.okhttp.SupervisorApiService;
import com.hfad.thinder.data.source.repository.UserRepository;
import com.hfad.thinder.data.source.result.Pair;
import com.hfad.thinder.data.source.result.Result;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;


public class SupervisorApiServiceTest {
    private MockWebServer server;
    private SupervisorApiService supervisorApiService;
    private ApiUtils apiUtils;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        supervisorApiService = new SupervisorApiService();
        apiUtils = ApiUtils.getInstance();
        apiUtils.setLiveSetup(false);
        apiUtils.setHost(server.getHostName());
        apiUtils.setPort(server.getPort());
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void editSupervisorProfileFutureSuccess() throws JSONException, IOException, InterruptedException,
            ExecutionException {
        MockResponse response = new MockResponse().setResponseCode(200);
        server.enqueue(response);

        Degree degreeInformatik = new Degree("Informatik Bsc", new UUID(32, 32));
        ArrayList<Degree> degreesOld = new ArrayList<>();
        degreesOld.add(degreeInformatik);

        Supervisor supervisor = SampleSupervisor.supervisorObject();

        UserRepository userRepository = UserRepository.getInstance();
        userRepository.setUser(supervisor);
        userRepository.setPassword("password");
        Degree degreeMathe = new Degree("Mathematik Msc", new UUID(32, 32));
        ArrayList<Degree> degreesNew = new ArrayList<>();
        degreesNew.add(degreeMathe);

        CompletableFuture<Result> result = supervisorApiService
                .editSupervisorProfileFuture("Dr", "21",
                        "100", "Telematik", "0123123123", "Tom", "Müller");

        RecordedRequest request = server.takeRequest();
        String authToken = request.getHeader("Authorization");
        String body = request.getBody().toString();
        assertTrue(result.get().getSuccess());
        assertEquals(UserRepository.getInstance().getUser().getFirstName(), "Tom");
        assertEquals(UserRepository.getInstance().getUser().getLastName(), "Müller");
        assertEquals(((Supervisor) UserRepository.getInstance().getUser()).getAcademicDegree(), "Dr");
        assertEquals(((Supervisor) UserRepository.getInstance().getUser()).getBuilding(), "100");
        assertEquals(((Supervisor) UserRepository.getInstance().getUser()).getInstitute(), "Telematik");
        assertEquals(((Supervisor) UserRepository.getInstance().getUser()).getOfficeNumber(), "21");
        assertEquals(((Supervisor) UserRepository.getInstance().getUser()).getPhoneNumber(), "0123123123");
    }

    @Test
    public void editSupervisorProfileFutureFail() throws JSONException, IOException, InterruptedException,
            ExecutionException {
        MockResponse response = new MockResponse().setResponseCode(500);
        server.enqueue(response);

        Degree degreeInformatik = new Degree("Informatik Bsc", new UUID(32, 32));
        ArrayList<Degree> degreesOld = new ArrayList<>();
        degreesOld.add(degreeInformatik);

        Supervisor supervisor = SampleSupervisor.supervisorObject();

        UserRepository userRepository = UserRepository.getInstance();
        userRepository.setUser(supervisor);
        userRepository.setPassword("hello!");
        Degree degreeMathe = new Degree("Mathematik Msc", new UUID(32, 32));
        ArrayList<Degree> degreesNew = new ArrayList<>();
        degreesNew.add(degreeMathe);

        CompletableFuture<Result> result = supervisorApiService
                .editSupervisorProfileFuture("Msc", "21",
                        "100", "Robotik", "0123123123", "Tom", "Müller");

        RecordedRequest request = server.takeRequest();
        String authToken = request.getHeader("Authorization");
        String body = request.getBody().toString();
        assertFalse(result.get().getSuccess());
        assertEquals(UserRepository.getInstance().getUser().getFirstName(), "Olf");
        assertEquals(UserRepository.getInstance().getUser().getLastName(), "Rieffel");
        assertEquals(((Supervisor) UserRepository.getInstance().getUser()).getAcademicDegree(), "M. Sc.");
        assertEquals(((Supervisor) UserRepository.getInstance().getUser()).getBuilding(), "Building 50.34");
        assertEquals(((Supervisor) UserRepository.getInstance().getUser()).getInstitute(), "Telematik");
        assertEquals(((Supervisor) UserRepository.getInstance().getUser()).getOfficeNumber(), "Room 102");
        assertEquals(((Supervisor) UserRepository.getInstance().getUser()).getPhoneNumber(), "0173 1234567");
    }

    @Test
    public void editThesisFail() throws JSONException, InterruptedException, ExecutionException {
        //set user
        Supervisor supervisor = SampleSupervisor.supervisorObject();

        UserRepository userRepository = UserRepository.getInstance();
        userRepository.setUser(supervisor);
        userRepository.setPassword("hello!");
        MockResponse response = new MockResponse().setResponseCode(500);
        server.enqueue(response);
        Thesis thesis = SampleThesis.thesisObject();
        CompletableFuture<Result> result = supervisorApiService.editThesisFuture(new UUID(31, 32), thesis);
        RecordedRequest request = server.takeRequest();
        assertFalse(result.get().getSuccess());
    }

    @Test
    public void editThesisSuccess() throws InterruptedException, JSONException, ExecutionException {
        //set user
        Supervisor supervisor = SampleSupervisor.supervisorObject();

        UserRepository userRepository = UserRepository.getInstance();
        userRepository.setUser(supervisor);
        userRepository.setPassword("hello!");
        MockResponse response = new MockResponse().setResponseCode(200);
        server.enqueue(response);
        Thesis thesis = SampleThesis.thesisObject();
        CompletableFuture<Result> result = supervisorApiService.editThesisFuture(new UUID(31, 32), thesis);
        RecordedRequest request = server.takeRequest();
        assertTrue(result.get().getSuccess());
    }

    @Test
    public void getCreatedThesisFromSupervisorSuccess() throws ExecutionException, InterruptedException {
        //set user
        Supervisor supervisor = SampleSupervisor.supervisorObject();

        UserRepository userRepository = UserRepository.getInstance();
        userRepository.setUser(supervisor);
        userRepository.setPassword("hello!");
        MockResponse response = new MockResponse().setResponseCode(200);
        server.enqueue(response);
        Pair<CompletableFuture<HashMap<UUID, Thesis>>, CompletableFuture<Result>> values = supervisorApiService.getCreatedThesisFromSupervisorFuture();
        HashMap<UUID, Thesis> map = values.getFirst().get();
        //supervisor has not yet created any theses
        assertEquals(map, null);
        Result result = values.getSecond().get();
        assertTrue(result.getSuccess());
    }

    @Test
    public void getCreatedThesisFromSupervisorFail() throws ExecutionException, InterruptedException {
        //set user
        Supervisor supervisor = SampleSupervisor.supervisorObject();

        UserRepository userRepository = UserRepository.getInstance();
        userRepository.setUser(supervisor);
        userRepository.setPassword("hello!");
        MockResponse response = new MockResponse().setResponseCode(500);
        server.enqueue(response);
        Pair<CompletableFuture<HashMap<UUID, Thesis>>, CompletableFuture<Result>> values = supervisorApiService.getCreatedThesisFromSupervisorFuture();

        Result result = values.getSecond().get();
        RecordedRequest request = server.takeRequest();
        assertFalse(result.getSuccess());
    }

}
