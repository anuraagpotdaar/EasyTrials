package com.anuraagpotdaar.easytrials.doctors;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anuraagpotdaar.easytrials.databinding.FragmentDoctorDashboardBinding;
import com.anuraagpotdaar.easytrials.helperClasses.ParticipantListAdapter;
import com.anuraagpotdaar.easytrials.helperClasses.ParticipantListModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;

public class DoctorDashboard extends Fragment {

    private FragmentDoctorDashboardBinding binding;

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ParticipantListAdapter participantListAdapter;
    ArrayList<ParticipantListModel> list;

    ArrayList<ParticipantListModel> searchList = new ArrayList<>();
    ArrayList<ParticipantListModel> priorityList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDoctorDashboardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        DatabaseReference partiCount = FirebaseDatabase.getInstance().getReference("Doctors/"+getActivity().getIntent().getStringExtra("id"));

        partiCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.tvDashboardUsername.setText(String.format("Dr %s", snapshot.child("name").getValue(String.class)));
                binding.tvParticipantCount.setText(String.format("%s participants are under your observation", snapshot.child("participants").getValue(int.class)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView = binding.rvParticipantList;
        databaseReference = FirebaseDatabase.getInstance().getReference("Participants");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list= new ArrayList<>();

        binding.etParticipantSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()){
                    if (binding.btnPriority.isChecked()) {
                        loadPriorityData();
                    }else if(binding.btnAll.isChecked()) {
                        participantListAdapter = new ParticipantListAdapter(getActivity().getApplicationContext(), list);
                        recyclerView.setAdapter(participantListAdapter);
                        participantListAdapter.notifyDataSetChanged();
                    }
                } else {
                    Search(editable.toString());
                }
            }
        });

        binding.toggleButton.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (binding.btnPriority.isChecked()) {
                loadPriorityData();
            } else if(binding.btnAll.isChecked()) {
                participantListAdapter = new ParticipantListAdapter(getActivity().getApplicationContext(), list);
                recyclerView.setAdapter(participantListAdapter);
                participantListAdapter.notifyDataSetChanged();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    ParticipantListModel participantModel =dataSnapshot.getValue(ParticipantListModel.class);
                    list.add(participantModel);
                    loadPriorityData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void loadPriorityData() {
        priorityList.clear();
        for(ParticipantListModel currentList :list) {
            if(currentList.getPriority() == 1 || currentList.getPriority() == 2) {
                priorityList.add(currentList);
            }
        }

        priorityList.sort(Comparator.comparingInt(ParticipantListModel::getPriority));

        recyclerView.setAdapter(new ParticipantListAdapter(getContext(), priorityList));
    }

    private void Search(String searchText) {
        searchList.clear();
        if (binding.btnPriority.isChecked()) {
            for(ParticipantListModel currentList :priorityList) {
                if(currentList.getName().toLowerCase().startsWith(searchText.toLowerCase())) {
                    searchList.add(currentList);
                }
            }
        } else if(binding.btnAll.isChecked()) {
            for(ParticipantListModel currentList :list) {
                if(currentList.getName().toLowerCase().startsWith(searchText.toLowerCase())) {
                    searchList.add(currentList);
                }
            }
        }
        recyclerView.setAdapter(new ParticipantListAdapter(getContext(), searchList));
    }
}