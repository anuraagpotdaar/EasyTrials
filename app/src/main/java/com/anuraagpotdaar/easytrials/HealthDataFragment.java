package com.anuraagpotdaar.easytrials;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anuraagpotdaar.easytrials.databinding.FragmentHealthDataBinding;
import com.anuraagpotdaar.easytrials.helperClasses.HealthDataModel;
import com.anuraagpotdaar.easytrials.helperClasses.HealthDataReadingAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HealthDataFragment extends Fragment {

    private FragmentHealthDataBinding binding;

    RecyclerView recyclerView ;
    DatabaseReference databaseReference;
    HealthDataReadingAdapter healthDataReadingAdapter;
    ArrayList<HealthDataModel> healthDataList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHealthDataBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        String selected = HealthDataFragmentArgs.fromBundle(getArguments()).getSelectedParti();

        recyclerView = binding.rvReadingList;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        healthDataList= new ArrayList<>();

        binding.ivDataType.setImageResource(R.drawable.ic_heart);
        binding.tvHaedingHealthData.setText("Heart rate");
        displayData(selected,"Heart rate");

        binding.toggleButton.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (binding.btnHeart.isChecked()) {
                binding.ivDataType.setImageResource(R.drawable.ic_heart);
                binding.tvHaedingHealthData.setText("Heart rate");
                displayData(selected,"Heart rate");
            } else if (binding.btnOxy.isChecked()) {
                binding.ivDataType.setImageResource(R.drawable.ic_oxygen);
                binding.tvHaedingHealthData.setText("Oxygen");
                displayData(selected,"Oxygen");

            } else if (binding.btnBP.isChecked()) {
                binding.ivDataType.setImageResource(R.drawable.ic_bp);
                binding.tvHaedingHealthData.setText("Blood pressure");
                displayData(selected,"BP");
            }
        });

        return binding.getRoot();
    }

    private void displayData(String selected, String selectedHealthData) {

        databaseReference = FirebaseDatabase.getInstance().getReference("Participants/"+selected+"/Health Data/"+ selectedHealthData);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                healthDataList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    binding.tvCurrent.setText(snapshot.child("Current").getValue(String.class));
                    HealthDataModel healthDataModel = new HealthDataModel();

                    healthDataModel.setDate(dataSnapshot.getKey());

                    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Participants/"+ selected + "/Health Data/" +selectedHealthData);

                    dataRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                healthDataModel.setValue(snapshot.child(healthDataModel.getDate()).getValue(String.class));
                            }
                            healthDataReadingAdapter.notifyDataSetChanged();
                        }

                        @Override public void onCancelled(@NonNull DatabaseError error) {}
                    });
                    healthDataList.add(healthDataModel);
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
        healthDataReadingAdapter = new HealthDataReadingAdapter(getContext(),healthDataList);
        recyclerView.setAdapter(healthDataReadingAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}