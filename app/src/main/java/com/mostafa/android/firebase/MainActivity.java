package com.mostafa.android.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.edt)
    EditText edt;


    @BindView(R.id.add)
    Button add;

    @BindView(R.id.remove)
            Button remove;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference("message");
        databaseReference2=firebaseDatabase.getReference("message2");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message=edt.getText().toString();
                databaseReference.setValue(message);
                databaseReference2.setValue(message);
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d("MainActivity", "Value is: " + value);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("MainActivity", "Failed to read value.", databaseError.toException());

            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.removeValue();
            }
        });
    }


    public void hi(View view) {
        Intent intent = new Intent(MainActivity.this,LoginAndRegister.class);
        startActivity(intent);
    }
}
