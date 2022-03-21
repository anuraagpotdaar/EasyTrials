package com.anuraagpotdaar.easytrials.participants;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.anuraagpotdaar.easytrials.MedsFragment;
import com.anuraagpotdaar.easytrials.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ParticipantHome extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_home);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.action_home);

        replaceFragment(new ParticipantDashboard());
        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.action_home:
                    replaceFragment(new ParticipantDashboard());
                    break;
                case R.id.action_watch:
                    replaceFragment(new DeviceFragment());
                    break;
                case R.id.action_calendar:
                    replaceFragment(new MedsFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_frame, fragment);
        fragmentTransaction.commit();
    }
}