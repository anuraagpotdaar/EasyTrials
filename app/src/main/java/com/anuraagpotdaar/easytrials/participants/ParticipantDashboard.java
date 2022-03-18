package com.anuraagpotdaar.easytrials.participants;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anuraagpotdaar.easytrials.R;
import com.anuraagpotdaar.easytrials.databinding.FragmentParticipantDashboardBinding;

public class ParticipantDashboard extends Fragment {

    private FragmentParticipantDashboardBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_participant_dashboard, container, false);
    }
}