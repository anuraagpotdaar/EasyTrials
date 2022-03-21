package com.anuraagpotdaar.easytrials.helperClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.anuraagpotdaar.easytrials.ParticipantDetailsFragmentDirections;
import com.anuraagpotdaar.easytrials.R;
import com.anuraagpotdaar.easytrials.doctors.DoctorDashboardDirections;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ParticipantListAdapter extends RecyclerView.Adapter<ParticipantListAdapter.ParticipantViewHolder> {

    Context context;

    ArrayList<ParticipantListModel> list;

    String phone;

    public ParticipantListAdapter(Context context, ArrayList<ParticipantListModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_participants, parent, false);
        return new ParticipantViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {
        ParticipantListModel parti = list.get(position);
        holder.name.setText(parti.name);
        holder.ageGender.setText(parti.gender);
        phone = parti.phone;
        switch (parti.priority) {
            case 1:
                holder.priority.setImageResource(R.drawable.ic_high);
                break;
            case 2:
                holder.priority.setImageResource(R.drawable.ic_medium);
                break;
            case 3:
                holder.priority.setImageResource(R.drawable.ic_all_ok);
                break;
        }

        boolean isExpanded = list.get(position).isExpanded();
        holder.expandable.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Participants/" + parti.phone + "/Health Data");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentHealthDetails = snapshot.child("Heart rate/Current").getValue(String.class) + " BPH\n" +
                        snapshot.child("Oxygen/Current").getValue(String.class) + " %\n" +
                        snapshot.child("BP/Current").getValue(String.class) + " mm Hg\n";

                holder.details.setText(currentHealthDetails);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ParticipantViewHolder extends RecyclerView.ViewHolder {

        TextView name, ageGender, details, btnMore;
        ImageView priority;
        ConstraintLayout expandable;
        CardView card;

        public ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.cvParti);

            name = itemView.findViewById(R.id.tvSearchParticipantName);
            ageGender = itemView.findViewById(R.id.tvSearchParticipantAgeGender);
            priority = itemView.findViewById(R.id.ivSatusSeverity);
            details = itemView.findViewById(R.id.tvPartiHealthData);
            btnMore = itemView.findViewById(R.id.btnSeeDetails);

            expandable = itemView.findViewById(R.id.expandable);

            card.setOnClickListener(view -> {
                ParticipantListModel parti = list.get(getBindingAdapterPosition());
                parti.setExpanded(!parti.isExpanded());
                notifyItemChanged(getAbsoluteAdapterPosition());
            });

            btnMore.setOnClickListener(view -> {
                ParticipantListModel parti = list.get(getBindingAdapterPosition());
                Navigation.findNavController(view).navigate(DoctorDashboardDirections.actionOpenParticipantDetailsFragment(parti.phone));
            });
        }
    }
}
