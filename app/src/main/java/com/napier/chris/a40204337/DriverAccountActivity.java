package com.napier.chris.a40204337;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DriverAccountActivity extends AppCompatActivity {

    private EditText mTypedName, mTypedPhone;
    private Button mBack, mUpdate;

    private FirebaseAuth mAuth;
    private DatabaseReference mDriverDatabase;

    private String userID;
    private String mName;
    private String mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_account);

        mTypedName = findViewById(R.id.name);
        mTypedPhone = findViewById(R.id.phone);

        mBack = findViewById(R.id.back);
        mUpdate = findViewById(R.id.update);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mDriverDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userID);

        addDriverInfo();

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDriverInfo();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }

    private void addDriverInfo() {
        mDriverDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    //if name field isnt blank gets the info and converts to string
                    if (map.get("name") != null) {
                        mName = map.get("name").toString();
                        mTypedName.setText(mName);
                    }
                    //if name field isnt blank gets the info and converts to string
                    if (map.get("phone") != null) {
                        mPhone = map.get("phone").toString();
                        mTypedPhone.setText(mPhone);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveDriverInfo() {

        //converts user typed info to strings
        mName = mTypedName.getText().toString();
        mPhone = mTypedPhone.getText().toString();

        //creates hashmap and adds information to database
        Map custInfo = new HashMap();
        custInfo.put("name", mName);
        custInfo.put("phone", mPhone);
        mDriverDatabase.updateChildren(custInfo);

        finish();
    }
}