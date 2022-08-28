package com.hfad.thinder.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.hfad.thinder.R;
import com.hfad.thinder.databinding.ActivityRegisterBinding;
import com.hfad.thinder.viewmodels.ViewModelResult;
import com.hfad.thinder.viewmodels.user.RegistrationFormState;
import com.hfad.thinder.viewmodels.user.RegistrationViewModel;


public class RegisterActivity extends AppCompatActivity {

  private ActivityRegisterBinding mBinding;
  private RegistrationViewModel viewmodel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Inflate view and obtain an instance of the binding class
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
    mBinding.setActivity(this);
    // obtain the viewmodel component
    viewmodel = new ViewModelProvider(this).get(RegistrationViewModel.class);
    // assign the component to a property in the binding class
    mBinding.setViewmodel(viewmodel);
    mBinding.setLifecycleOwner(this);

    // my_child_toolbar is defined in the layout file
    Toolbar myChildToolbar = mBinding.toolbar;

    setSupportActionBar(myChildToolbar);
    // Get a support ActionBar corresponding to this toolbar
    ActionBar ab = getSupportActionBar();

    // Enable the Up button
    ab.setDisplayHomeAsUpEnabled(true);


    //Setzt Fehlermeldungen für das Eingabeformat
    viewmodel.getRegistrationFormState().observe(this, new Observer<RegistrationFormState>() {
      @Override
      public void onChanged(RegistrationFormState registrationFormState) {
        if (registrationFormState.getEmailErrorMessage() != null) {
          mBinding.etLoginEMail.setError(
              getResources().getString(registrationFormState.getEmailErrorMessage()));
        }
        if (registrationFormState.getFirstNameErrorMessage() != null) {
          mBinding.etfirstname.setError(
              getResources().getString(registrationFormState.getFirstNameErrorMessage()));
        }
        if (registrationFormState.getLastNameErrorMessage() != null) {
          mBinding.etlastname.setError(
              getResources().getString(registrationFormState.getLastNameErrorMessage()));
        }
        if (registrationFormState.getPasswordErrorMessage() != null) {
          mBinding.etLoginPassword.setError(
              getResources().getString(registrationFormState.getPasswordErrorMessage()));
        }
        if (registrationFormState.getPasswordConfirmationErrorMessage() != null) {
          mBinding.etconfirmpassword.setError(getResources().getString(
              registrationFormState.getPasswordConfirmationErrorMessage()));
        }
      }
    });

    TextWatcher afterTextChangedListener = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //ignore
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        //ignore
      }

      @Override
      public void afterTextChanged(Editable s) {
        viewmodel.registrationDataChanged();
      }
    };

    final Observer<ViewModelResult> registrationSuccessfulObserver =
        new Observer<ViewModelResult>() {
          @Override
          public void onChanged(@Nullable final ViewModelResult registrationResult) {
            if (registrationResult.isSuccess()) {
              goToVerifyTokenActivity();
            }
          }
        };

    viewmodel.getRegistrationResult().observe(this, registrationSuccessfulObserver);


    mBinding.etLoginEMail.addTextChangedListener(afterTextChangedListener);
    mBinding.etfirstname.addTextChangedListener(afterTextChangedListener);
    mBinding.etlastname.addTextChangedListener(afterTextChangedListener);
    mBinding.etLoginPassword.addTextChangedListener(afterTextChangedListener);
    mBinding.etconfirmpassword.addTextChangedListener(afterTextChangedListener);

  }

  private void goToVerifyTokenActivity() {
    Intent intent = new Intent(this, VerifyTokenActivity.class);
    startActivity(intent);
  }

  public void register(){
    InputMethodManager inputManager = (InputMethodManager)
            getSystemService(Context.INPUT_METHOD_SERVICE);

    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
            InputMethodManager.HIDE_NOT_ALWAYS);
    viewmodel.register();
  }
}
