package com.hfad.thinder.data.source.repository;

import com.hfad.thinder.data.model.Degree;

import java.util.ArrayList;
import java.util.List;

public class DegreeRepository {
    public static DegreeRepository INSTANCE;

    private DegreeRepository(){}

    /**
     * @return current instance of DegreeRepository singleton class.
     */
    public static DegreeRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DegreeRepository();
        }
        return INSTANCE;
    }

    public final static List<String> getAcademicTitles(){
        List<String> list = null;
        list.add("M. Sc.");
        list.add("B. Sc.");
        list.add("Dr.");
        list.add("Prof.");
        list.add("Prof. Dr.");
        list.add("Dr. Dr.");
        list.add("Prof. Dr. Dr.");
        return list;
    }

    public ArrayList<String> getAllStudentDegrees(){
        return null;

    }


}
