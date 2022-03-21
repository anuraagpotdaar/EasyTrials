package com.anuraagpotdaar.easytrials.doctors;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anuraagpotdaar.easytrials.MedsFragmentArgs;
import com.anuraagpotdaar.easytrials.R;
import com.anuraagpotdaar.easytrials.databinding.FragmentAddMedsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMedsFragment extends BottomSheetDialogFragment {

    private FragmentAddMedsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddMedsBinding.inflate(inflater, container, false);

        String selected = AddMedsFragmentArgs.fromBundle(getArguments()).getSelectedParti();

        DatabaseReference medsReference = FirebaseDatabase.getInstance().getReference("Participants/"+selected+"/meds");

        binding.btnMedDone.setOnClickListener(
                view -> {
                    if (!binding.etMorning.getText().toString().isEmpty()) {
                        medsReference.child(binding.etMedName.getText().toString()).child("Morning").setValue(binding.etMorning.getText().toString());
                    }
                    if (!binding.etAfternoon.getText().toString().isEmpty()) {
                        medsReference.child(binding.etMedName.getText().toString()).child("Afternoon").setValue(binding.etAfternoon.getText().toString());
                    }
                    if (!binding.etEvening.getText().toString().isEmpty()) {
                        medsReference.child(binding.etMedName.getText().toString()).child("Evening").setValue(binding.etEvening.getText().toString());
                    }
                    if (!binding.etNight.getText().toString().isEmpty()) {
                        medsReference.child(binding.etMedName.getText().toString()).child("Night").setValue(binding.etNight.getText().toString());
                    }
                    dismiss();
                });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}