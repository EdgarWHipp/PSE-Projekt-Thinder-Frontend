package com.pse.app.thinder.data.source.repository;

import com.pse.app.thinder.data.model.Thesis;

import java.util.List;
import java.util.Optional;

public class ThesisRepository implements BaseRepository {


    @Override
    public List getAll() {
        return null;
    }

    @Override
    public Optional getById(int id) {
        return Optional.empty();
    }

    @Override
    public Exception save(Object obj) {
        return null;
    }

    @Override
    public Exception delete(int id) {
        return null;
    }
}