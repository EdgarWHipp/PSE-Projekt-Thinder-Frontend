package com.hfad.thinder.viewmodels.student;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.hfad.thinder.data.model.Degree;
import com.hfad.thinder.data.model.Image;
import com.hfad.thinder.data.model.Supervisor;
import com.hfad.thinder.data.model.Thesis;
import com.hfad.thinder.data.source.repository.StudentRepository;
import com.hfad.thinder.data.source.repository.ThesisRepository;
import com.hfad.thinder.data.source.result.Pair;
import com.hfad.thinder.data.source.result.Result;
import com.hfad.thinder.ui.student.swipescreen.SwipeScreenCard;
import com.hfad.thinder.ui.student.swipescreen.SwipeScreenFragment;
import com.hfad.thinder.ui.student.swipescreen.SwipeScreenImageFragment;
import com.hfad.thinder.ui.student.swipescreen.SwipeScreenInfoFragment;
import com.hfad.thinder.ui.student.swipescreen.SwipeScreenTextFragment;
import com.hfad.thinder.ui.student.swipescreen.SwipeScreenTopFragment;
import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;

/**
 * A class providing a shared {@link ViewModel} for the {@link SwipeScreenFragment}, the {@link SwipeScreenImageFragment SwipeScreenImageFragment}
 * , the {@link SwipeScreenTopFragment SwipeScreenTopFragment}, the {@link SwipeScreenInfoFragment SwipeScreenInfoFragment}
 * and the {@link SwipeScreenTextFragment SwipeScreenTextFragment}.
 */
public class SwipeScreenViewModel extends ViewModel {
  private static final ThesisRepository thesisRepository = ThesisRepository.getInstance();
  private static final StudentRepository studentRepository = StudentRepository.getInstance();
  private final Stack<Pair<UUID, Boolean>> ratings = new Stack();

  private ArrayList<SwipeScreenCard> cardDeck;
  private MutableLiveData<Integer> currentDeckPosition;

  private ArrayList<SwipeScreenFragment.DetailViewStates> detailViewOrder;
  private MutableLiveData<Integer> currentDetailViewPosition;
  private MutableLiveData<Bitmap> currentImage;
  private MutableLiveData<Bitmap> nextImage;

  private MutableLiveData<String> coursesOfStudy;
  private MutableLiveData<String> superVisorName;
  private MutableLiveData<String> phoneNumber;
  private MutableLiveData<String> building;
  private MutableLiveData<String> mail;
  private MutableLiveData<String> professorName;
  private MutableLiveData<String> institute;
  private MutableLiveData<String> currentTask;
  private MutableLiveData<String> currentMotivation;

  private MutableLiveData<Boolean> isLoading;


  /**
   * Use this method to like the current thesis.
   */
  public void like() {
    if (getCurrentDeckPosition().getValue() >= cardDeck.size() - 2) {
      return;
    }
    SwipeScreenCard currentCard = cardDeck.get(getCurrentDeckPosition().getValue());
    ratings.push(new Pair<>(currentCard.getUUID(), true));
    incrementCurrentDeckPosition();
    thesisRepository.setThesesDirty(true);
  }

  /**
   * Use this method to dislike the current thesis.
   */
  public void dislike() {
    if (getCurrentDeckPosition().getValue() >= cardDeck.size() - 2) {
      return;
    }
    SwipeScreenCard currentCard = cardDeck.get(getCurrentDeckPosition().getValue());
    ratings.push(new Pair<>(currentCard.getUUID(), false));
    incrementCurrentDeckPosition();
  }

  /**
   * Use this method to redraw the last rated thesis.
   */
  public void redraw() {
    if (decrementCurrentDeckPosition()) {
      ratings.pop();
    }
  }

  /**
   * Use this method to relay the ratings of the thesis to the model.
   */
  public void pushRatings() {
    if (ratings.size() != 0) {
      new PushRatingsTask().execute(ratings);
    }
  }

  /**
   * Use this method to go to the next page of the detail view for the thesis.
   *
   * @return true if there is a next page, false otherwise.
   */
  public boolean incrementCurrentDetailViewPosition() {
    Integer oldPosition = getCurrentDetailViewPosition().getValue();
    if (getCurrentDetailViewPosition().getValue() < getDetailViewOrder().size() - 1) {
      getCurrentDetailViewPosition().setValue(oldPosition + 1);
      return true;
    }
    return false;
  }

  /**
   * Use this method to go to the previous page of the detail view for the thesis.
   *
   * @return true if there is a previous page, false otherwise.
   */
  public boolean decrementCurrentDetailViewPosition() {
    Integer oldPosition = getCurrentDetailViewPosition().getValue();
    if (getCurrentDetailViewPosition().getValue() > 0) {
      getCurrentDetailViewPosition().setValue(oldPosition - 1);
      return true;
    }
    return false;
  }


  //--------------getter and setter---------------------------------

  /**
   * @return the currently displayed {@link SwipeScreenCard}.
   */
  public SwipeScreenCard getCurrentCard() {
    SwipeScreenCard currentCard = getCardDeck().get(getCurrentDeckPosition().getValue());
    return currentCard;
  }

  /**
   * @return the {@link SwipeScreenCard} that is to be displayed after the current card.
   */
  public SwipeScreenCard getNextCard() {
    SwipeScreenCard nextCard = getCardDeck().get(getCurrentDeckPosition().getValue() + 1);
    return nextCard;
  }

  /**
   * @return the current {@link SwipeScreenFragment.DetailViewStates DetailViewStates} of the currently displayed {@link SwipeScreenCard}
   */
  public SwipeScreenFragment.DetailViewStates getCurrentDetailViewState() {
    return getDetailViewOrder().get(getCurrentDetailViewPosition().getValue());
  }

  /**
   * @return the current image to be displayed in the {@link SwipeScreenImageFragment SwipeScreenImageFragment}.
   */
  public Bitmap getCurrentDetailViewImage() {
    if (getCurrentDetailViewPosition().getValue() > 0 &&
        getCurrentDetailViewPosition().getValue() <= getCurrentCard().getImages().size()) {
      return getCurrentCard().getImages().get(getCurrentDetailViewPosition().getValue() - 1);
    }
    return null;
  }


  /**
   * @return a {@link MutableLiveData} object containing the the position of the currently displayed {@link SwipeScreenCard} in the {@link ArrayList} of all {@link SwipeScreenCard SwipeScreenCards} received from the model.
   */
  public MutableLiveData<Integer> getCurrentDeckPosition() {
    if (currentDeckPosition == null) {
      currentDeckPosition = new MutableLiveData<>();
      currentDeckPosition.setValue(0);
    }
    return currentDeckPosition;
  }

  /**
   * @return the Image that is to be displayed at the non detail view of the currently displayed {@link SwipeScreenCard}. In case there are no images belonging to the thesis this method return null.
   */
  public MutableLiveData<Bitmap> getCurrentImage() {
    if (currentImage == null) {
      currentImage = new MutableLiveData<>();
    }
    if (getCurrentCard().getImages() == null || getCurrentCard().getImages().size() == 0) {
      return null;
    }
    currentImage.setValue(getCurrentCard().getImages().get(0));
    return currentImage;
  }

  /**
   * @return the Image that is to displayed at the non detail view of the {@link SwipeScreenCard} to be displayed next. In case there are no images belonging to the thesis this method returns null.
   */
  public MutableLiveData<Bitmap> getNextImage() {
    if (nextImage == null) {
      nextImage = new MutableLiveData<>();
    }
    if (getNextCard().getImages() == null || getNextCard().getImages().size() == 0) {
      return null;
    }
    nextImage.setValue(getNextCard().getImages().get(0));
    return nextImage;
  }

  /**
   * @return the courses of study belonging to the currently displayed thesis.
   */
  public MutableLiveData<String> getCoursesOfStudy() {
    if (coursesOfStudy == null) {
      coursesOfStudy = new MutableLiveData<>();
    }
    if (getCurrentCard().getCoursesOfStudy() == null) {
      return null;
    }
    coursesOfStudy.setValue(String.join("\n", getCurrentCard().getCoursesOfStudy()));
    return coursesOfStudy;
  }

  /**
   * @return the name of the supervisor of the currently displayed thesis.
   */
  public MutableLiveData<String> getSuperVisorName() {
    if (superVisorName == null) {
      superVisorName = new MutableLiveData<>();
    }
    if (getCurrentCard().getSupervisorLastName() == null ||
        getCurrentCard().getSupervisorFirstName() == null) {
      return null;
    }
    superVisorName.setValue(String.join(" ", getCurrentCard().getAcademicDegree(),
        getCurrentCard().getSupervisorFirstName(), getCurrentCard().getSupervisorLastName()));
    return superVisorName;
  }

  /**
   * @return the phone number belonging to the supervisor of the currently displayed thesis.
   */
  public MutableLiveData<String> getPhoneNumber() {
    if (phoneNumber == null) {
      phoneNumber = new MutableLiveData<>();
    }
    if (getCurrentCard().getPhoneNumber() == null) {
      return null;
    }
    phoneNumber.setValue(getCurrentCard().getPhoneNumber());
    return phoneNumber;
  }

  /**
   * @return the address of the office of the supervisor of the currently displayed thesis.
   */
  public MutableLiveData<String> getBuilding() {
    if (building == null) {
      building = new MutableLiveData<>();
    }
    if (getCurrentCard().getRoomNumber() == null || getCurrentCard().getBuilding() == null) {
      return null;
    }
    building.setValue(
        String.join(" ", getCurrentCard().getRoomNumber(), getCurrentCard().getBuilding()));
    return building;
  }

  /**
   * @return the mail address of the supervisor of the currently displayed thesis.
   */
  public MutableLiveData<String> getMail() {
    if (mail == null) {
      mail = new MutableLiveData<>();
    }
    if (getCurrentCard().getMail() == null) {
      return null;
    }
    mail.setValue(getCurrentCard().getMail());
    return mail;
  }

  /**
   * @return the name of the supervising professor belonging to the currently displayed thesis.
   */
  public MutableLiveData<String> getProfessorName() {
    if (professorName == null) {
      professorName = new MutableLiveData<>();
    }
    if (getCurrentCard().getProfessor() == null) {
      return null;
    }
    professorName.setValue(getCurrentCard().getProfessor());
    return professorName;
  }

  /**
   * @return the name of the institute belonging to the supervisor of the currently displayed thesis.
   */
  public MutableLiveData<String> getInstitute() {
    if (institute == null) {
      institute = new MutableLiveData<>();
    }
    if (getCurrentCard().getInstitute() == null) {
      return null;
    }
    institute.setValue(getCurrentCard().getInstitute());
    return institute;
  }

  /**
   * @return the task description of the currently displayed thesis.
   */
  public MutableLiveData<String> getCurrentTask() {
    if (currentTask == null) {
      currentTask = new MutableLiveData<>();
    }
    if (getCurrentCard().getTask() == null) {
      return null;
    }
    currentTask.setValue(getCurrentCard().getTask());
    return currentTask;
  }

  /**
   * @return the motivation description for the currently displayed thesis.
   */
  public MutableLiveData<String> getCurrentMotivation() {
    if (currentMotivation == null) {
      currentMotivation = new MutableLiveData<>();
    }
    if (getCurrentCard().getMotivation() == null) {
      return null;
    }
    currentMotivation.setValue(getCurrentCard().getMotivation());
    return currentMotivation;
  }

  /** Use this method to determine if a model call has finished or not.
   * @return true if the call to the model is not finished, false otherwise.
   */
  public MutableLiveData<Boolean> getIsLoading() {
    if (isLoading == null) {
      isLoading = new MutableLiveData<>();
      isLoading.setValue(true);
    }
    return isLoading;
  }

  //----------------private methods-----------------------------------

  private MutableLiveData<Integer> getCurrentDetailViewPosition() {
    if (currentDetailViewPosition == null) {
      currentDetailViewPosition = new MutableLiveData<>();
      currentDetailViewPosition.setValue(0);
    }
    return currentDetailViewPosition;
  }

  private ArrayList<SwipeScreenFragment.DetailViewStates> getDetailViewOrder() {
    if (detailViewOrder == null) {
      detailViewOrder = new ArrayList<>();
      setDetailedViewOrder();
    }
    return detailViewOrder;
  }

  private ArrayList<SwipeScreenCard> getCardDeck() {
    if (cardDeck == null || thesisRepository.isSwipeDirty()) {
      loadCardDeck();
      thesisRepository.setSwipeDirty(false);
    }
    return cardDeck;
  }

  private void loadCardDeck() {
    cardDeck = new ArrayList<>();
    addDummyCards(cardDeck);
    new LoadCardDeckTask().execute();
  }

  private void addDummyCards(ArrayList<SwipeScreenCard> cardDeck) {

    cardDeck.add(
        new SwipeScreenCard(null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null));
    cardDeck.add(
        new SwipeScreenCard(null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null));
  }

  private ArrayList<String> getCoursesOfStudyList(Set<Degree> degrees) {
    ArrayList<String> degreeList = new ArrayList<>();
    for (Degree degree : degrees) {
      degreeList.add(degree.getDegree());
    }
    return degreeList;
  }

  private ArrayList<Bitmap> convertImagesToBitmaps(Set<Image> imageSet) {
    ArrayList<Bitmap> images = new ArrayList<>();
    for (Image image : imageSet) {
      Bitmap bitmap = BitmapFactory.decodeByteArray(image.getImage(), 0, image.getImage().length);
      images.add(bitmap);
    }
    return images;
  }

  private boolean incrementCurrentDeckPosition() {
    Integer oldPosition = getCurrentDeckPosition().getValue();
    if (oldPosition < getCardDeck().size() - 2) {
      setDetailedViewOrder();
      getCurrentDeckPosition().setValue(oldPosition + 1);
      return true;
    }
    return false;
  }

  private boolean decrementCurrentDeckPosition() {
    Integer oldPosition = getCurrentDeckPosition().getValue();
    if (oldPosition > 0) {
      setDetailedViewOrder();
      getCurrentDeckPosition().setValue(oldPosition - 1);
      return true;
    }
    return false;
  }

  private void setDetailedViewOrder() {
    getCurrentDetailViewPosition().setValue(0);
    getDetailViewOrder().clear();
    detailViewOrder.add(SwipeScreenFragment.DetailViewStates.TOP);
    for (int i = 0; cardDeck.get(getCurrentDeckPosition().getValue()).getImages() != null &&
        i < cardDeck.get(getCurrentDeckPosition().getValue()).getImages().size(); ++i) {
      detailViewOrder.add(SwipeScreenFragment.DetailViewStates.IMAGE);
    }
    detailViewOrder.add(SwipeScreenFragment.DetailViewStates.TEXT);
    detailViewOrder.add(SwipeScreenFragment.DetailViewStates.INFO);
  }

  private class PushRatingsTask extends AsyncTask<Stack<Pair<UUID, Boolean>>, Void, Result> {

    @Override
    protected Result doInBackground(Stack<Pair<UUID, Boolean>>... stacks) {
      return studentRepository.rateThesis(stacks[0]);
    }
  }

  private class LoadCardDeckTask extends AsyncTask<Void, Void, ArrayList<Thesis>> {

    @Override
    protected void onPreExecute() {
      getIsLoading().setValue(true);
    }

    @Override
    protected ArrayList<Thesis> doInBackground(Void... voids) {
      return studentRepository.fetchAllSwipeableThesis();
    }

    @Override
    protected void onPostExecute(ArrayList<Thesis> theses) {
      cardDeck = new ArrayList<>();
      if (theses != null) {
        for (Thesis thesis : theses) {
          ArrayList<Bitmap> bitmaps = convertImagesToBitmaps(thesis.getImages());
          ArrayList<String> selectedCoursesOfStudy =
              getCoursesOfStudyList(thesis.getPossibleDegrees());
          Supervisor supervisor = thesis.getSupervisor();
          SwipeScreenCard swipeScreenCard =
              new SwipeScreenCard(bitmaps, thesis.getId(), thesis.getName(), thesis.getTask(),
                  thesis.getMotivation(), thesis.getSupervisingProfessor(), selectedCoursesOfStudy,
                  supervisor.getFirstName(), supervisor.getLastName(), supervisor.getBuilding(),
                  supervisor.getOfficeNumber(), supervisor.getPhoneNumber(),
                  supervisor.getInstitute(),
                  supervisor.getMail(), supervisor.getAcademicDegree());
          cardDeck.add(swipeScreenCard);
        }
      }
      addDummyCards(cardDeck);
      // force card refresh by setting deck position value observed in fragment
      getCurrentDeckPosition().setValue(0);
      isLoading.setValue(false);
    }
  }


}
