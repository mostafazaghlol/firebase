package com.mostafa.android.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginAndRegister extends AppCompatActivity {
    @BindView(R.id.logEmail)
    EditText LEmail;
    @BindView(R.id.regEmail)
    EditText REmail;
    @BindView(R.id.logPassword)
    EditText LPassword;
    @BindView(R.id.regPassword)
    EditText RPassword;
    @BindView(R.id.regName)
    EditText Name;
    @BindView(R.id.regAge)
    EditText age;
    @BindView(R.id.regusername)
    EditText username;
    @BindView(R.id.regGender)
    EditText gender;
    @BindView(R.id.BtL)
    Button login;

    @BindView(R.id.BtS)
    Button Register;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);
        ButterKnife.bind(this);

        mauth= FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    Intent intent = new Intent(LoginAndRegister.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = LEmail.getText().toString().trim();
                String password = LPassword.getText().toString();
                mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginAndRegister.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(LoginAndRegister.this, "Error login "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    }
                });
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email =REmail.getText().toString().trim();
                final String password = RPassword.getText().toString();
                final String username1 =username.getText().toString();
                Query query = FirebaseDatabase.getInstance().getReference().child("User").orderByChild("username").equalTo(username1);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()>0){
                            Toast.makeText(LoginAndRegister.this, "Choose another username", Toast.LENGTH_SHORT).show();
                        }else{
                            mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(LoginAndRegister.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(LoginAndRegister.this, "Error Signup "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }else{
                                        String name=Name.getText().toString();
                                        String Age=age.getText().toString();
                                        String Gender= gender.getText().toString();
                                        String userID= mauth.getCurrentUser().getUid();
                                        DatabaseReference databaseReference ;
                                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                        databaseReference = firebaseDatabase.getReference().child("User").child(userID);
                                        Map newpost = new HashMap();
                                        newpost.put("username",username1);
                                        newpost.put("name",name);
                                        newpost.put("Age",Age);
                                        newpost.put("Gender",Gender);
                                        databaseReference.setValue(newpost);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        mauth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mauth.addAuthStateListener(mAuthStateListener);
    }
}
