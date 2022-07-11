package com.hfad.thinder.ui;

import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hfad.thinder.R;
import com.hfad.thinder.databinding.ActivityStudentBinding;

public class StudentActivity extends AppCompatActivity {

    private ActivityStudentBinding mBinding;
    private Toolbar myChildToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_student);
        BottomNavigationView bottomNavigationView = mBinding.bottomNavigationView;
        NavController navController = findNavController(this, R.id.studentFragmentContainer);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // my_child_toolbar is defined in the layout file
        myChildToolbar = mBinding.toolbar;

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.studentProfileFragment, R.id.swipeScreenFragment, R.id.likedThesesFragment).build();

        setSupportActionBar(myChildToolbar);
        NavigationUI.setupWithNavController(myChildToolbar, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.swipeScreenFragment) {
                    myChildToolbar.setVisibility(View.GONE);
                } else {
                    myChildToolbar.setVisibility(View.VISIBLE);
                }
            }
        });
    }





}