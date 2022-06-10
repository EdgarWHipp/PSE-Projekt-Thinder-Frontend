package com.hfad.thinder;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.hfad.thinder.data.source.repository.StudentRepository;
import com.hfad.thinder.databinding.ActivityLoginBinding;
import com.hfad.thinder.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

  private ActivityLoginBinding mBinding;
  private LoginViewModel viewmodel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Inflate view and obtain an instance of the binding class
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    // obtain the viewmodel component
    viewmodel = new ViewModelProvider(this).get(LoginViewModel.class);
    // assign the component to a property in the binding class
    mBinding.setViewmodel(viewmodel);
    mBinding.setLifecycleOwner(this);

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
        viewmodel.loginDataChanged();
      }
    };
    mBinding.etLoginEMail.addTextChangedListener(afterTextChangedListener);
    mBinding.etLoginPassword.addTextChangedListener(afterTextChangedListener);
  }


  public void goToRegisterActivity(View view) {
    Intent intent = new Intent(this, RegisterActivity.class);
    startActivity(intent);
  }

  public void goToForgotPasswordActivity(View view) {
    Intent intent = new Intent(this, ForgotPasswordActivity.class);
    startActivity(intent);
  }
}