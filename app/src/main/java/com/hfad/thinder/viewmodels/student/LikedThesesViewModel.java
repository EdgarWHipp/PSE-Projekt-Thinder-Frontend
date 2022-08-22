package com.hfad.thinder.viewmodels.student;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hfad.thinder.data.model.Thesis;
import com.hfad.thinder.data.source.repository.StudentRepository;
import com.hfad.thinder.data.source.repository.ThesisRepository;
import com.hfad.thinder.viewmodels.ThesisCardItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class LikedThesesViewModel extends ViewModel {
    private static final ThesisRepository thesisRepository = ThesisRepository.getInstance();
    private static final StudentRepository studentRepository = StudentRepository.getInstance();
    private MutableLiveData<ArrayList<ThesisCardItem>> likedTheses;


    //--------------------getter and setter----------------------------------------------------


    public MutableLiveData<ArrayList<ThesisCardItem>> getLikedTheses() {
        if (likedTheses == null) {
            likedTheses = new MutableLiveData<>();
            loadLikedTheses();
        }
        return likedTheses;
    }

    //----------------private methods--------------------------
    private void loadLikedTheses() {
        ArrayList<ThesisCardItem> thesisCards = new ArrayList<>();
        Log.e("","how often is this called");
        //HashMap<UUID, Thesis> likedTheses = thesisRepository.getThesisMap();
        HashMap<UUID, Thesis> likedTheses =null;
        Log.e("","call done");
        if (likedTheses != null && !(likedTheses.isEmpty())) {
            for (Thesis thesis : likedTheses.values()) {
                byte[] byteArray = thesis.getImages().iterator().next().getImage();
                Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                ThesisCardItem thesisCardItem =
                        new ThesisCardItem(thesis.getId(), thesis.getName(), thesis.getTask(), image);
                thesisCards.add(thesisCardItem);
            }
        }

        this.likedTheses.setValue(thesisCards);
    }


}



