package com.anuraagpotdaar.easytrials;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anuraagpotdaar.easytrials.databinding.FragmentMedsBinding;
import com.anuraagpotdaar.easytrials.doctors.ParticipantDetailsFragmentDirections;
import com.anuraagpotdaar.easytrials.helperClasses.MedsDispAdapter;
import com.anuraagpotdaar.easytrials.helperClasses.MedsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MedsFragment extends Fragment {

    private FragmentMedsBinding binding;

    RecyclerView recyclerView;
    MedsDispAdapter medsDispAdapter;
    ArrayList<MedsModel> medsList;
    String selected,user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMedsBinding.inflate(inflater, container, false);

        try {
            selected = MedsFragmentArgs.fromBundle(getArguments()).getSelectedParti();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            selected = getActivity().getIntent().getStringExtra("id");
            user = "Participant";
            binding.btnAddMeds.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.btnAddMeds.setOnClickListener(
                view -> {
                    Navigation.findNavController(view).navigate(MedsFragmentDirections.actionAddMeds(selected));
                });

        DatabaseReference partiRef = FirebaseDatabase.getInstance().getReference("Participants/"+ selected);

        partiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String dob =  snapshot.child("dob").getValue(String.class);
                int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(dob.substring(dob.length() - 4));

                String quickInfo = "for " + age + " years old " + snapshot.child("gender").getValue(String.class);
                binding.subHeadingMed.setText(quickInfo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView = binding.rvCurMeds;
        DatabaseReference medsRef = FirebaseDatabase.getInstance().getReference("Participants/"+ selected + "/meds");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        medsList= new ArrayList<>();

        medsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    MedsModel medsModel = new MedsModel();
                    medsModel.setMedName(dataSnapshot.getKey());

                    DatabaseReference medsTimeRef = FirebaseDatabase.getInstance().getReference("Participants/"+ selected + "/meds/" + medsModel.getMedName());

                    medsTimeRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                                medsModel.setMorning(snapshot.child("Morning").getValue(String.class));
                                medsModel.setAfternoon(snapshot.child("Afternoon").getValue(String.class));
                                medsModel.setEvening(snapshot.child("Evening").getValue(String.class));
                                medsModel.setNight(snapshot.child("Night").getValue(String.class));
                            }
                            medsDispAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    medsList.add(medsModel);
                }
                medsDispAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        medsDispAdapter = new MedsDispAdapter(getContext(),medsList,user);
        recyclerView.setAdapter(medsDispAdapter);


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}