package com.hfad.thinder.viewmodels.user;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hfad.thinder.data.source.repository.UserRepository;
import com.hfad.thinder.viewmodels.user.ForgotPasswordFormState;
import com.hfad.thinder.viewmodels.user.ForgotPasswordResult;

public class ForgotPasswordViewModel extends ViewModel {
  private final UserRepository forgotPasswordRepository = UserRepository.getInstance();
  private MutableLiveData<ForgotPasswordFormState> formState;
  private MutableLiveData<ForgotPasswordResult> result;

  private MutableLiveData<String> code;
  private MutableLiveData<String> newPassword;
  private MutableLiveData<String> newPasswordConfirmation;

  public void login() {
    //Todo: implementieren
  }

  public void passwordForgotDataChanged() {
    //Todo: implementieren
  }

  public MutableLiveData<ForgotPasswordFormState> getFormState() {
    if (formState == null) {
      formState = new MutableLiveData<>();
    }
    return formState;
  }

  public MutableLiveData<ForgotPasswordResult> getResult() {
    if (result == null) {
      result = new MutableLiveData<>();
    }
    return result;
  }

  public MutableLiveData<String> getCode() {
    if (code == null) {
      code = new MutableLiveData<>();
    }
    return code;
  }

  public void setCode(MutableLiveData<String> code) {
    this.code = code;
  }

  public MutableLiveData<String> getNewPassword() {
    if (newPassword == null) {
      newPassword = new MutableLiveData<>();
    }
    return newPassword;
  }

  public void setNewPassword(MutableLiveData<String> newPassword) {
    this.newPassword = newPassword;
  }

  public MutableLiveData<String> getNewPasswordConfirmation() {
    if (newPasswordConfirmation == null) {
      newPasswordConfirmation = new MutableLiveData<>();
    }
    return newPasswordConfirmation;
  }

  public void setNewPasswordConfirmation(
      MutableLiveData<String> newPasswordConfirmation) {
    this.newPasswordConfirmation = newPasswordConfirmation;
  }
}