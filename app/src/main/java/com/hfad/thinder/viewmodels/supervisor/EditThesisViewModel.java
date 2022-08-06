package com.hfad.thinder.viewmodels.supervisor;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.hfad.thinder.R;
import com.hfad.thinder.data.model.Form;
import com.hfad.thinder.data.model.Image;
import com.hfad.thinder.data.model.Supervisor;
import com.hfad.thinder.data.model.USERTYPE;
import com.hfad.thinder.data.source.repository.DegreeRepository;
import com.hfad.thinder.data.source.repository.ThesisRepository;
import com.hfad.thinder.data.source.repository.UserRepository;
import com.hfad.thinder.data.source.result.Tuple;
import com.hfad.thinder.ui.CourseOfStudyItem;
import com.hfad.thinder.viewmodels.CoursesOfStudyPicker;
import com.hfad.thinder.viewmodels.ImageGalleryPicker;
import com.hfad.thinder.viewmodels.ViewModelResult;
import com.hfad.thinder.data.model.Degree;
import com.hfad.thinder.data.source.result.Result;
import com.hfad.thinder.viewmodels.ViewModelResultTypes;
import com.hfad.thinder.data.model.Thesis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;
import java.nio.ByteBuffer;
import java.util.UUID;

public class EditThesisViewModel extends ThesisViewModel {
  private static final DegreeRepository degreeRepository = DegreeRepository.getInstance();
  private static final ThesisRepository thesisRepository = ThesisRepository.getInstance();
  private static final UserRepository userRepository = UserRepository.getInstance();
  private ListIterator<Bitmap> iterator;
  private UUID thesisId;
  // likes, dislikes
  private Tuple<Integer, Integer> thesisStatistics;
  private MutableLiveData<ViewModelResult> deleteThesisResult;


  public void save() {
    Form form = new Form(getQuestions().getValue());
    Set<Image> imageSet = ThesisUtility.getImageSet(getImages().getValue());
    Set<Degree> degreeSet = ThesisUtility.getSelectedDegreeSet(getCoursesOfStudyList().getValue());
    Thesis thesis = new Thesis(getProfessor().getValue(), getTitle().getValue(), getMotivation().getValue(), getTask().getValue(), form, imageSet, (Supervisor) userRepository.getUser(), degreeSet);
    Result result = thesisRepository.editThesis(thesisId, thesis);
    if (result.getSuccess()) {
      getSaveResult().setValue(new ViewModelResult(null, ViewModelResultTypes.SUCCESSFUL));
    } else {
      getSaveResult().setValue(new ViewModelResult(result.getErrorMessage(), ViewModelResultTypes.ERROR));
    }
  }

  public void delete() {
    Result result = thesisRepository.deleteThesis(thesisId);
    if (result.getSuccess()) {
      getDeleteThesisResult().setValue(new ViewModelResult(null, ViewModelResultTypes.SUCCESSFUL));
    } else {
      getDeleteThesisResult().setValue(new ViewModelResult(result.getErrorMessage(), ViewModelResultTypes.ERROR));
    }
  }


  //------------------getter and setter-----------------------------------------------------


  public void setThesisId(UUID thesisId) {
    this.thesisId = thesisId;
    loadThesis();
  }


  public MutableLiveData<ViewModelResult> getDeleteThesisResult() {
    if (deleteThesisResult == null) {
      deleteThesisResult = new MutableLiveData<>();
    }
    return deleteThesisResult;
  }


  public Tuple<Integer, Integer> getThesisStatistics() {
    return thesisStatistics;
  }


  //-------------private methods-----------------------------------------------------



  private void loadThesis() {
    //UUID uuid = UUID.fromString(thesisId);
    //Thesis thesis = thesisRepository.getThesis(uuid).x; //todo uncomment
    Thesis thesis = generateThesis();//Todo löschen

    getTitle().setValue(thesis.getName());
    getTask().setValue(thesis.getTask());
    getMotivation().setValue(thesis.getMotivation());
    getQuestions().setValue(thesis.getForm().getQuestions());

    //courses of Study
    getSelectedCoursesOfStudy().setValue(coursesOfStudyStringAdapter(thesis.getPossibleDegrees()));
    getCoursesOfStudyList().setValue(coursesOfStudyListAdapter(degreeRepository.getAllDegrees(), thesis.getPossibleDegrees()));

    //images
    //images = convertImages(thesis.getImages()); //todo: uncomment
    if (getImages().getValue() != null) {
      iterator = getImages().getValue().listIterator();
      getCurrentImage().setValue(iterator.next());
    }


    thesisStatistics = thesisRepository.getThesisStatistics(thesisId); //todo set actual uuid

  }

  private ArrayList<CourseOfStudyItem> coursesOfStudyListAdapter(ArrayList<String> allDegrees, Set<Degree> selectedDegrees) {
    ArrayList<CourseOfStudyItem> convertedDegrees = new ArrayList<>();
    for (String degree : allDegrees) {
      if (selectedDegrees.contains(new Degree(degree))) {
        convertedDegrees.add(new CourseOfStudyItem(degree, true));
      } else {
        convertedDegrees.add(new CourseOfStudyItem(degree, false));
      }
    }
    return convertedDegrees;
  }

  private String coursesOfStudyStringAdapter(Set<Degree> degrees) {
    ArrayList<String> selectedCoursesOfStudy = new ArrayList<>();
    for (Degree degree : degrees) {
      selectedCoursesOfStudy.add(degree.getDegree());
    }
    return String.join(", ", selectedCoursesOfStudy);
  }

  private ArrayList<Bitmap> convertImages(java.util.Set<Image> imageSet) {
    ArrayList<Bitmap> convertedImages = new ArrayList<>();
    for (Image image : imageSet) {
      byte[] byteArray = image.getImage();
      Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
      convertedImages.add(bitmap);
    }
    return convertedImages;
  }

  // todo: remove
  Thesis generateThesis() {
    Bitmap image1 = Bitmap.createBitmap(100, 50, Bitmap.Config.ARGB_8888);
    Bitmap image2 = Bitmap.createBitmap(100, 50, Bitmap.Config.ARGB_8888);
    ArrayList<Bitmap> newImages = new ArrayList<>();
    newImages.add(image1);
    newImages.add(image2);
    Supervisor supervisor = new Supervisor(USERTYPE.SUPERVISOR, UUID.randomUUID(), true, UUID.randomUUID(), "gubert", "gubert@mail", "firstname", "lastName", "academicDegree", "building", "officeNumber", "insitute", "phoneNumber");
    HashSet<Degree> possibleDegrees = new HashSet<>();
    possibleDegrees.add(new Degree("Bachelor Informatik"));
    possibleDegrees.add(new Degree("Bachelor Mathematik"));

    Thesis thesis = new Thesis("Prof. Hartmann", "", "", "task", new Form("questions"), null, supervisor, possibleDegrees);

    return thesis;
  }
}


