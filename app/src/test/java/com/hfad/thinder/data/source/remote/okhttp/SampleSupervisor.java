package com.hfad.thinder.data.source.remote.okhttp;

import com.hfad.thinder.data.model.Supervisor;
import com.hfad.thinder.data.model.USERTYPE;

import org.json.JSONException;
import org.json.JSONObject;

public class SampleSupervisor extends SampleUser {

    public static final String typeSupervisor = "SUPERVISOR";

    public static final String academicDegree = "M. Sc.";
    public static final String officeNumber = "Room 102";
    public static final String building = "Building 50.34";
    public static final String institute = "Telematik";
    public static final String phoneNumber = "0173 1234567";

    public static JSONObject getSupervisorJson() throws JSONException {
        return SampleUser.getUserJson()
                .put("type", typeSupervisor)
                .put("academicDegree", academicDegree)
                .put("officeNumber", officeNumber)
                .put("building", building)
                .put("institute", institute)
                .put("phoneNumber", phoneNumber);
    }

    public static Supervisor supervisorObject() {
        return new Supervisor(USERTYPE.SUPERVISOR, SampleUser.id, SampleUser.active,
                SampleUser.uni_id, SampleUser.mail, SampleUser.firstName, SampleUser.lastName,
                academicDegree, building, officeNumber, institute, phoneNumber,
                SampleUser.complete);
    }
}
