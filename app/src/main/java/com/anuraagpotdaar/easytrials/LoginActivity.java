package com.anuraagpotdaar.easytrials;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anuraagpotdaar.easytrials.databinding.ActivityLoginBinding;
import com.anuraagpotdaar.easytrials.doctors.DoctorDashboard;
import com.anuraagpotdaar.easytrials.doctors.DoctorHome;
import com.anuraagpotdaar.easytrials.participants.ParticipantHome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnLogin.setOnClickListener(view1 -> {
            String userEnterdUsername, userEnteredPassword;

            userEnterdUsername = binding.etUsername.getEditableText().toString().trim();
            userEnteredPassword = binding.etPassword.getEditableText().toString().trim();

            if (userEnterdUsername.length() == 3) {
                DatabaseReference UserReference = FirebaseDatabase.getInstance().getReference("Doctors");
                Query checkUser = UserReference.orderByChild("username").equalTo(userEnterdUsername);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            binding.etUsername.setError(null);
                            String passwordFromDB = snapshot.child(userEnterdUsername).child("password").getValue(String.class).trim();

                            if (passwordFromDB.equals(userEnteredPassword)) {
                                binding.etPassword.setError(null);
                                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, DoctorHome.class);
                                intent.putExtra("id", userEnterdUsername);
                                startActivity(intent);
                                finish();
                            } else {
                                binding.etPassword.setError("Wrong Password");
                                binding.etPassword.requestFocus();
                            }
                        } else {
                            binding.etUsername.setError("Invalid credentials");
                            binding.etUsername.requestFocus();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            } else if (userEnterdUsername.length() > 3) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Participants");
                Query checkUser = reference.orderByChild("username").equalTo(userEnterdUsername);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            binding.etUsername.setError(null);
                            String passwordFromDB = snapshot.child(userEnterdUsername).child("password").getValue(String.class).trim();

                            if (passwordFromDB.equals(userEnteredPassword)) {
                                binding.etPassword.setError(null);
                                Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ParticipantHome.class);
                                intent.putExtra("id", userEnterdUsername);
                                startActivity(intent);
                                finish();
                            } else {
                                binding.etPassword.setError("Wrong Password");
                                binding.etPassword.requestFocus();
                            }
                        } else {
                            binding.etUsername.setError("Invalid credentials");
                            binding.etUsername.requestFocus();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }

        });
    }
}