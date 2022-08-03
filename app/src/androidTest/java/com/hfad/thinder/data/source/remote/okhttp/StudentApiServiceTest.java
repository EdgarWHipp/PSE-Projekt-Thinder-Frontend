package com.hfad.thinder.data.source.remote.okhttp;

import static org.junit.Assert.*;

import com.hfad.thinder.data.model.Degree;
import com.hfad.thinder.data.model.Login;
import com.hfad.thinder.data.model.Student;
import com.hfad.thinder.data.model.USERTYPE;
import com.hfad.thinder.data.model.User;
import com.hfad.thinder.data.source.repository.UserRepository;
import com.hfad.thinder.data.source.result.Result;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class StudentApiServiceTest {
    private MockWebServer server;
    private StudentApiService studentApiService;
    private Login login;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        login = new Login("mail@gmail.com", "password");

        studentApiService =  new StudentApiService();
        studentApiService.setHost(server.getHostName());
        studentApiService.setPort(server.getPort());
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void editStudentProfileFuture() throws JSONException, IOException, InterruptedException,
            ExecutionException {
        MockResponse response = new MockResponse().setResponseCode(200);
        server.enqueue(response);

        Degree degreeInformatik = new Degree("Informatik","Bsc");
        Set<Degree> degreesOld = new HashSet<>();
        degreesOld.add(degreeInformatik);

        Student student = new Student(USERTYPE.STUDENT,
                new UUID(0x8a3a5503cd414b9aL, 0xa86eaa3d64c4c314L),
                true, new UUID(0x8a3a5503cd414b9aL, 0xa86eaa3d64c4c314L),
                "password", "mail@gmail.com", "Olf", "Molf", degreesOld);

        UserRepository userRepository = UserRepository.getInstance();
        userRepository.setType(USERTYPE.STUDENT);
        userRepository.setUser(student);

        Degree degreeMathe = new Degree("Mathematik","Msc");
        Set<Degree> degreesNew = new HashSet<>();
        degreesNew.add(degreeMathe);

        CompletableFuture<Result> result = studentApiService
                .editStudentProfileFuture(degreesNew, "Tom", "Müller");

        RecordedRequest request = server.takeRequest();
        String authToken = request.getHeader("Authorization");
        String body = request.getBody().toString();

        assertTrue(result.get().getSuccess());
        assertEquals("Basic bWFpbEBnbWFpbC5jb206cGFzc3dvcmQ=", authToken);

        // TODO assertEquals("{...}", body); <-- AUSSCHLAGGEBEND!! BODY MUSS KORREKT SEIN
    }
    @Test
    public void rateThesisFuture() throws JSONException, InterruptedException, ExecutionException {
        //Set current loged in user
        Degree degreeInformatik = new Degree("Informatik","Bsc");
        Set<Degree> degreesOld = new HashSet<>();
        degreesOld.add(degreeInformatik);

        Student student = new Student(USERTYPE.STUDENT,
                new UUID(0x8a3a5503cd414b9aL, 0xa86eaa3d64c4c314L),
                true, new UUID(0x8a3a5503cd414b9aL, 0xa86eaa3d64c4c314L),
                "password", "mail@gmail.com", "Olf", "Molf", degreesOld);
        // Actual Thesis Rating call
        UserRepository.getInstance().setUser(student);
        MockResponse response = new MockResponse().setResponseCode(200);
        server.enqueue(response);
        CompletableFuture<Result> result = studentApiService.rateThesisFuture(
                new UUID(0x8a3a5503cd414b9aL, 0xa86eaa3d64c4c314L),
                new UUID(0x8a3a5503cd414b9aL, 0xa86eaa3d64c4c314L),true);
        RecordedRequest request = server.takeRequest();
        String authToken = request.getHeader("Authorization");
        String body = request.getBody().toString();

        assertTrue(result.get().getSuccess());
        assertEquals("Basic bWFpbEBnbWFpbC5jb206cGFzc3dvcmQ=", authToken);
    }
    @Test
    public void rateThesisFutureFail() throws JSONException, InterruptedException, ExecutionException {
        //Set current loged in user
        Degree degreeInformatik = new Degree("Informatik","Bsc");
        Set<Degree> degreesOld = new HashSet<>();
        degreesOld.add(degreeInformatik);

        Student student = new Student(USERTYPE.STUDENT,
                new UUID(0x8a3a5503cd414b9aL, 0xa86eaa3d64c4c314L),
                true, new UUID(0x8a3a5503cd414b9aL, 0xa86eaa3d64c4c314L),
                "password", "mail@gmail.com", "Olf", "Molf", degreesOld);
        // Actual Thesis Rating call
        UserRepository.getInstance().setUser(student);
        MockResponse response = new MockResponse().setResponseCode(500);
        server.enqueue(response);
        CompletableFuture<Result> result = studentApiService.rateThesisFuture(
                new UUID(0x8a3a5503cd414b9aL, 0xa86eaa3d64c4c314L),
                new UUID(0x8a3a5503cd414b9aL, 0xa86eaa3d64c4c314L),true);
        RecordedRequest request = server.takeRequest();
        String authToken = request.getHeader("Authorization");
        String body = request.getBody().toString();

        assertFalse(result.get().getSuccess());
        assertEquals("Basic bWFpbEBnbWFpbC5jb206cGFzc3dvcmQ=", authToken);
    }

    @Test
    public void getSwipeOrder() {
    }



    @Test
    public void getLikedThesesFuture() {

    }
}