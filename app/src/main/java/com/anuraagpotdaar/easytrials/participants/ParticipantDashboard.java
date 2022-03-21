package com.anuraagpotdaar.easytrials.participants;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anuraagpotdaar.easytrials.HealthDataFragment;
import com.anuraagpotdaar.easytrials.MedsFragment;
import com.anuraagpotdaar.easytrials.R;
import com.anuraagpotdaar.easytrials.databinding.FragmentParticipantDashboardBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParticipantDashboard extends Fragment {

    private FragmentParticipantDashboardBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentParticipantDashboardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.cvCurrentHR.setOnClickListener(view1 -> replaceFragment());
        binding.cvCurrentO2.setOnClickListener(view1 -> replaceFragment());
        binding.cvCurrentBP.setOnClickListener(view1 -> replaceFragment());

        binding.cvNxtMed.setOnClickListener(view12 -> {
            Fragment medsFrag = new MedsFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.home_frame, medsFrag);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        String selected = getActivity().getIntent().getStringExtra("id");
        DatabaseReference healthRef = FirebaseDatabase.getInstance().getReference("Participants/"+ selected);

        healthRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ignored : snapshot.getChildren()) {
                    binding.tvPartiName.setText(snapshot.child("name").getValue(String.class));
                    binding.tvHRValue.setText(String.format("%s BPM", snapshot.child("Health Data/Heart rate/Current").getValue(String.class)));
                    binding.tvOxyVal.setText(String.format("%s %%", snapshot.child("Health Data/Oxygen/Current").getValue(String.class)));
                    binding.tvBPVal.setText(String.format("%smm Hg", snapshot.child("Health Data/BP/Current").getValue(String.class)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
    private void replaceFragment (){
        Fragment healthDataFrag = new HealthDataFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.home_frame, healthDataFrag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}