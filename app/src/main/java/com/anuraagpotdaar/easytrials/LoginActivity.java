package com.anuraagpotdaar.easytrials;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.anuraagpotdaar.easytrials.databinding.ActivityLoginBinding;
import com.anuraagpotdaar.easytrials.doctors.DoctorDashboard;
import com.anuraagpotdaar.easytrials.participants.ParticipantDashboard;
import com.anuraagpotdaar.easytrials.participants.ParticipantHome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Set;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.btnLogin.setOnClickListener(view1 -> {
            String userEnterdUsername,userEnteredPassword;

            userEnterdUsername = binding.etUsername.getEditableText().toString().trim();
            userEnteredPassword = binding.etPassword.getEditableText().toString().trim();

            if (userEnterdUsername.length()==3 && userEnteredPassword!=null){
                    //Database
                    DatabaseReference UserReference = FirebaseDatabase.getInstance().getReference("Doctors");
                    Query checkUser = UserReference.orderByChild("username").equalTo(userEnterdUsername);
                    //Checking User
                Toast.makeText(this, "ghs", Toast.LENGTH_SHORT).show();
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                binding.etUsername.setError(null);
                                String passwordFromDB = snapshot.child(userEnterdUsername).child("password").getValue(String.class).trim();
                                Toast.makeText(LoginActivity.this, passwordFromDB, Toast.LENGTH_SHORT).show();
                                Log.e("user",passwordFromDB);

                                if (passwordFromDB.equals(userEnteredPassword)){
                                    binding.etPassword.setError(null);
                                    //String patients = snapshot.child(userEnterdUsername).child("patients").getValue(int.class).toString();

                                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, ParticipantHome.class);

                                    //intent.putExtra("patients",patients);
                                    intent.putExtra("id",userEnterdUsername);

                                    startActivity(intent);
                                    finish();
                                }else {
                                    binding.etPassword.setError("Wrong Password");
                                    binding.etPassword.requestFocus();
                                }
                            }else{
                                binding.etUsername.setError("No Such A User");
                                binding.etUsername.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            else if (userEnterdUsername.length()==5 && userEnteredPassword!=null){

                //Database
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                Query checkUser = reference.orderByChild("username").equalTo(userEnterdUsername);
                //Checking User
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            binding.etUsername.setError(null);
                            String passwordFromDB = snapshot.child(userEnterdUsername).child("password").getValue(String.class).trim();

                            if (passwordFromDB.equals(userEnteredPassword)){
                                binding.etPassword.setError(null);
                                //String patients = snapshot.child(userEnterdUsername).child("patients").getValue(int.class).toString();

                                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), DoctorDashboard.class);

                                //intent.putExtra("patients",patients);
                                intent.putExtra("id",userEnterdUsername);

                                startActivity(intent);
                                finish();
                            }else {
                                binding.etPassword.setError("Wrong Password");
                                binding.etPassword.requestFocus();
                            }
                        }else{
                            binding.etUsername.setError("No Such A User");
                            binding.etUsername.requestFocus();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
            else {
                Toast.makeText(this, "Not A User", Toast.LENGTH_SHORT).show();
            }

        });


    }

}