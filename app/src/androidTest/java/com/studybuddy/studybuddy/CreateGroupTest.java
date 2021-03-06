package com.studybuddy.studybuddy;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Kolby on 3/26/2018.
 */
public class CreateGroupTest extends ActivityInstrumentationTestCase2<CreateGroup> {
    private CreateGroup mCreateGroup;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;

    public CreateGroupTest() {
        super(CreateGroup.class);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mCreateGroup = getActivity();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("kolby.rottero@gmail.com", "thisisanothertest");
        Thread.sleep(2000);
        mUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();

        assertNotNull(mUser);
        assertNotNull(mFirestore);
    }

    //Currently doesn't have right format (e.g. UTC -4 instead of GMT)
    @Test
    public void testCreateGroup() throws Exception {
        mCreateGroup.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                assertTrue(mCreateGroup.createGroup("Unit Test", "Cyberspace", "This is part of a unit test", ""));
            }
        });
    }

    @Test
    public void testCreateEmpty() throws Exception {
        mCreateGroup.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                assertFalse(mCreateGroup.createGroup("","","", ""));
            }
        });
    }

    @Test
    public void testGetClasses() throws Exception {
        //user doc should exist first
        assertNotNull(mFirestore.collection("users").document(mUser.getUid()));
    }
}