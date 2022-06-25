package com.hfad.thinder.data.source.repository;

import com.hfad.thinder.data.model.Thesis;
import com.hfad.thinder.data.source.remote.ThesisRemoteDataSource;

import java.util.List;
import java.util.Optional;

/**
 * Singleton instance of a ThesisRepository.
 * Database access to fetch theses is launched over this class.
 */
public final class ThesisRepository {
    private static ThesisRepository INSTANCE;
    private ThesisRemoteDataSource thesisRemoteDataSource;

    private ThesisRepository() {

    }

    /**
     * @return current instance of ThesisRepository singleton class.
     */
    public static ThesisRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ThesisRepository();
        }
        return INSTANCE;
    }


   public Optional<List<Thesis>> getAll() {return thesisRemoteDataSource.getAllTheses();
    }


    public Optional<Thesis> getById(final int id) {
        return thesisRemoteDataSource.getThesis(id);
    }


    public boolean save(final Thesis thesis) {
        return thesisRemoteDataSource.createNewThesis(thesis);
    }


    public boolean delete(final int id) {
        return false;
    }
}
