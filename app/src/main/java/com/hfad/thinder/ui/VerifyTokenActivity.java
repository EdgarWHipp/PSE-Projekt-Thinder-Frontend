package com.hfad.thinder.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.hfad.thinder.R;
import com.hfad.thinder.databinding.ActivityVerifyTokenBinding;
import com.hfad.thinder.viewmodels.user.RegistrationViewModel;
import com.hfad.thinder.viewmodels.user.VerifyTokenResult;
import com.hfad.thinder.viewmodels.user.VerifyTokenViewModel;

public class VerifyTokenActivity extends AppCompatActivity {

  private ActivityVerifyTokenBinding binding;
  private VerifyTokenViewModel viewmodel;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


    binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_token);

    viewmodel = new ViewModelProvider(this).get(VerifyTokenViewModel.class);

    binding.setViewmodel(viewmodel);
    binding.setLifecycleOwner(this);

    final Observer<VerifyTokenResult> verifyTokenResultObserver =
        new Observer<VerifyTokenResult>() {
          @Override
          public void onChanged(VerifyTokenResult verifyTokenResult) {
            if (verifyTokenResult.isSuccess()) {
              if (verifyTokenResult.getSuccess() == RegistrationViewModel.ResultTypes.STUDENT) {
                goToStudentProfileFragment();
              }
              if (verifyTokenResult.getSuccess() == RegistrationViewModel.ResultTypes.SUPERVISOR) {
                goToSupervisorProfileFragment();
              }
            }
          }
        };
    viewmodel.getVerifyTokenResult().observe(this, verifyTokenResultObserver);
  }


  private void goToStudentProfileFragment() {
    Intent intent = new Intent(this, StudentProfileFragment.class);
    startActivity(intent);
  }

  private void goToSupervisorProfileFragment() {
    Intent intent = new Intent(this, SupervisorProfileFragment.class);
    startActivity(intent);
  }
}