package com.hfad.thinder.data.source.remote.okhttp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import android.util.Log;

import com.hfad.thinder.data.model.Degree;
import com.hfad.thinder.data.model.Student;
import com.hfad.thinder.data.model.Supervisor;
import com.hfad.thinder.data.model.USERTYPE;
import com.hfad.thinder.data.source.repository.UserRepository;
import com.hfad.thinder.data.source.result.Result;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;


public class SupervisorApiServiceTest {
    private MockWebServer server;
    private SupervisorApiService supervisorApiService;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        supervisorApiService = new SupervisorApiService();
        supervisorApiService.setHost(server.getHostName());
        supervisorApiService.setPort(server.getPort());
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
    @Test
    public void editSupervisorProfileFuture() throws JSONException, IOException, InterruptedException,
            ExecutionException {
        MockResponse response = new MockResponse().setResponseCode(200);
        server.enqueue(response);

        Degree degreeInformatik = new Degree("Informatik Bsc", new UUID(32, 32));
        ArrayList<Degree> degreesOld = new ArrayList<>();
        degreesOld.add(degreeInformatik);

        Supervisor supervisor = new Supervisor(USERTYPE.SUPERVISOR, new UUID(0x8a3a5503cd414b9aL, 0xa86eaa3d64c4c314L),false,
                new UUID(0x8a3a5503cd414b9aL, 0xa86eaa3d64c4c314L),
                "hello!","edasd@kit.edu","Olf", "Molf" ,"Dr","101","1092","Telematik","0123123",false);

        UserRepository userRepository = UserRepository.getInstance();
        userRepository.setUser(supervisor);
        userRepository.setPassword(supervisor.getPassword());
        Degree degreeMathe = new Degree("Mathematik Msc", new UUID(32, 32));
        ArrayList<Degree> degreesNew = new ArrayList<>();
        degreesNew.add(degreeMathe);

        CompletableFuture<Result> result = supervisorApiService
                .editSupervisorProfileFuture("Dr","21",
                        "100","Telematik","0123123123", "Tom", "Müller");

        Log.e("",userRepository.getUser().getFirstName().toString());
        RecordedRequest request = server.takeRequest();
        String authToken = request.getHeader("Authorization");
        String body = request.getBody().toString();
        assertTrue(result.get().getSuccess());
        assertEquals(UserRepository.getInstance().getUser().getFirstName(), "Tom");
        assertEquals(UserRepository.getInstance().getUser().getLastName(), "Müller");
        assertEquals(((Supervisor) UserRepository.getInstance().getUser()).getAcademicDegree(),"Dr");
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

        Supervisor supervisor = new Supervisor(USERTYPE.SUPERVISOR, new UUID(0x8a3a5503cd414b9aL, 0xa86eaa3d64c4c314L),false,
                new UUID(0x8a3a5503cd414b9aL, 0xa86eaa3d64c4c314L),
                "hello!","edasd@kit.edu","Olf", "Molf" ,"Dr","101","1092","Telematik","0123123",false);

        UserRepository userRepository = UserRepository.getInstance();
        userRepository.setUser(supervisor);
        userRepository.setPassword(supervisor.getPassword());
        Degree degreeMathe = new Degree("Mathematik Msc", new UUID(32, 32));
        ArrayList<Degree> degreesNew = new ArrayList<>();
        degreesNew.add(degreeMathe);

        CompletableFuture<Result> result = supervisorApiService
                .editSupervisorProfileFuture("Msc","21",
                        "100","Robotik","0123123123", "Tom", "Müller");

        Log.e("",userRepository.getUser().getFirstName().toString());
        RecordedRequest request = server.takeRequest();
        String authToken = request.getHeader("Authorization");
        String body = request.getBody().toString();
        assertFalse(result.get().getSuccess());
        assertEquals(UserRepository.getInstance().getUser().getFirstName(), "Olf");
        assertEquals(UserRepository.getInstance().getUser().getLastName(), "Molf");
        assertEquals(((Supervisor) UserRepository.getInstance().getUser()).getAcademicDegree(),"Dr");
        assertEquals(((Supervisor) UserRepository.getInstance().getUser()).getBuilding(), "101");
        assertEquals(((Supervisor) UserRepository.getInstance().getUser()).getInstitute(), "Telematik");
        assertEquals(((Supervisor) UserRepository.getInstance().getUser()).getOfficeNumber(), "1092");
        assertEquals(((Supervisor) UserRepository.getInstance().getUser()).getPhoneNumber(), "0123123");
    }


}
