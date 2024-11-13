package com.example.yoga_class_app_coursework.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.yoga_class_app_coursework.R;
import com.example.yoga_class_app_coursework.databinding.ActivityMainBinding;
import com.example.yoga_class_app_coursework.fragments.AddFragment;
import com.example.yoga_class_app_coursework.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        replaceFragment(new HomeFragment());

        mBinding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.add_mn) {
                replaceFragment(new AddFragment());
            } else if (id == R.id.home_mn) {
                replaceFragment(new HomeFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}