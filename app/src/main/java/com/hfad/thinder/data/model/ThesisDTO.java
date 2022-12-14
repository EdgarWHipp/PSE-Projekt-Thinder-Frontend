package com.hfad.thinder.data.model;

import java.util.List;
import java.util.UUID;

/**
 * This class defines a Thesis Data Transfer Object - this is the object that is passed from the backend. It is later parsed to a Thesis Object
 * that can be used inside the frontend.
 */
public class ThesisDTO {

    private UUID id;

    private String name;

    private String supervisingProfessor;

    private String motivation;

    private String task;

    private String questions;
    private Supervisor supervisor;

    private List<String> images;

    private List<Degree> possibleDegrees;

    private int positivelyRatedNum;
    private int negativelyRatedNum;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupervisingProfessor() {
        return supervisingProfessor;
    }

    public void setSupervisingProfessor(String supervisingProfessor) {
        this.supervisingProfessor = supervisingProfessor;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<Degree> getPossibleDegrees() {
        return possibleDegrees;
    }

    public int getPositivelyRatedNum() {
        return positivelyRatedNum;
    }

    public void setPositivelyRatedNum(int positivelyRatedNum) {
        this.positivelyRatedNum = positivelyRatedNum;
    }

    public int getNegativelyRatedNum() {
        return negativelyRatedNum;
    }

    public void setNegativelyRatedNum(int negativelyRatedNum) {
        this.negativelyRatedNum = negativelyRatedNum;
    }

    public void setPossibleDegrees(List<Degree> possibleDegrees) {
        this.possibleDegrees = possibleDegrees;
    }

    public ThesisDTO() {
    }

    public ThesisDTO(String name, String supervisingProfessor, String motivation, String task, String questions
            , List<String> images, List<Degree> possibleDegrees) {
        this.name = name;
        this.supervisingProfessor = supervisingProfessor;
        this.motivation = motivation;
        this.task = task;
        this.questions = questions;
        this.images = images;
        this.possibleDegrees = possibleDegrees;
    }
}
