package com.anuraagpotdaar.easytrials.participants;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anuraagpotdaar.easytrials.MedsFragmentArgs;
import com.anuraagpotdaar.easytrials.R;
import com.anuraagpotdaar.easytrials.databinding.FragmentAddSymptomsBinding;
import com.anuraagpotdaar.easytrials.databinding.FragmentParticipantDashboardBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddSymptomsFragment extends BottomSheetDialogFragment {

    private FragmentAddSymptomsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddSymptomsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        String selected = AddSymptomsFragmentArgs.fromBundle(getArguments()).getSelectedParti();

        DatabaseReference sympsReference = FirebaseDatabase.getInstance().getReference("Participants/"+selected+"/symptoms");

        binding.btnAddSymp.setOnClickListener(
                view1 -> {
                    if (!binding.etSymp.getText().toString().isEmpty()) {
                        Long tsLong = System.currentTimeMillis()/1000;
                        String ts = tsLong.toString();
                        sympsReference.child(ts).setValue(binding.etSymp.getText().toString());
                    }
                    dismiss();
                });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}