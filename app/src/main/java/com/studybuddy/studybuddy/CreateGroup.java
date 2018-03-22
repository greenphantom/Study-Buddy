package com.studybuddy.studybuddy;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class CreateGroup extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private EditText mClass;
    private Spinner mLocation;
    private EditText mDescription;
    private Button mCreateGroup;

    private static final String TAG = "GoogleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        mAuth = FirebaseAuth.getInstance();
        mClass = findViewById(R.id.group_class_field);
        mLocation = findViewById(R.id.group_location_field);

        mDescription = findViewById(R.id.group_desc_field);
        mCreateGroup = findViewById(R.id.create_group_button);
        //Have to make an adapter to fill in drop down menu items with string array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.locations_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLocation.setAdapter(adapter);
        mLocation.setOnItemSelectedListener(this);
        mFirestore = FirebaseFirestore.getInstance();

        mCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupClass = mClass.getText().toString();
                String groupLocation = mLocation.getSelectedItem().toString();
                String groupDesc = mDescription.getText().toString();
                createGroup(groupClass, groupLocation, groupDesc);
                finish();
            }
        });
    }

    //Methods for getting selected drop down list item
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        parent.getItemAtPosition(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean createGroup(String groupClass, String groupLocation, String groupDesc) {
        if(TextUtils.isEmpty(groupClass)){
            Toast.makeText(getApplicationContext(), "Enter a class", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(TextUtils.isEmpty(groupLocation)){
            Toast.makeText(getApplicationContext(), "Enter a location", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(TextUtils.isEmpty(groupDesc)){
            Toast.makeText(getApplicationContext(), "Add a description", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            Map<String, Object> groupMap = new HashMap<>();
            groupMap.put("class", groupClass);
            groupMap.put("location", groupLocation);
            groupMap.put("description", groupDesc);
            groupMap.put("creationTime", FieldValue.serverTimestamp());
            groupMap.put("user", mAuth.getUid());

            mFirestore.collection("study_groups")
                    .add(groupMap)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });

            Toast.makeText(getApplicationContext(), "Study group created!", Toast.LENGTH_SHORT).show();

            return true;
        }
    }
}
