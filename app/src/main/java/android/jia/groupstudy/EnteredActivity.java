package android.jia.groupstudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EnteredActivity extends AppCompatActivity {
    private static final String TAG = "EnteredActivity";

    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    private String mUsername;
    private String mPhotoUrl;
    String key;

    String uId;

    String fragPass;
    DatabaseReference test;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference getTest = database.getReference("message");
    DatabaseReference mDataBase;
    DatabaseReference isThere;
    DatabaseReference checkMem;
    String roomId, roomCreator;


    User userTst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataBase = database.getReference();
        roomId = getIntent().getStringExtra("ROOM_DATA");
        roomCreator = getIntent().getStringExtra("ROOM_OWNER");
        Log.i(TAG, "the room id is: " + roomId);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        Log.i(TAG, "the guy who created the room is: " + roomCreator);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        final TabLayout tabLayout = findViewById(R.id.tablayout);


        final ViewPager viewPager = findViewById(R.id.viewPager);

        setSupportActionBar(toolbar);

        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());
        pageAdapter.AddFragment(new ChatFragment(), "Chat");
        pageAdapter.AddFragment(new QuizFragment(), "Quiz");
        pageAdapter.AddFragment(new FlashCardFragment(), "FlashCard");
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(EnteredActivity.this,
                            R.color.flashcard));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(EnteredActivity.this,
                            R.color.flashcard));
                    Log.i(TAG, "Position is" + viewPager.getCurrentItem());

                    viewPager.getCurrentItem();
                } else if (tab.getPosition() == 2) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(EnteredActivity.this,
                            R.color.quiz));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(EnteredActivity.this,
                            R.color.quiz));
                    Log.i(TAG, "Position is" + viewPager.getCurrentItem());
                } else {
                    toolbar.setBackgroundColor(ContextCompat.getColor(EnteredActivity.this,
                            R.color.colorPrimary));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(EnteredActivity.this,
                            R.color.colorPrimary));
                    Log.i(TAG, "Position is" + viewPager.getCurrentItem());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            Log.i(TAG, "The userid is " + mFirebaseUser.getUid());
            uId = mFirebaseUser.getUid();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            Log.i(TAG, "The userid is " + mFirebaseUser.getUid());
            uId = mFirebaseUser.getUid();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }







            /*checkMem.child("member/" + mFirebaseUser.getUid() + "/test1/status").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot data) {
                    if (data.exists()) {
                        Log.i(TAG, "the value we're getting is: " + data.getValue());
                        String info = data.getValue().toString();
                        if (info.equals("member")) {
                            Log.i(TAG, "yeh he's good");

                        } else if (info.equals("banned")) {
                            Log.i(TAG, "yikes he a no go");

                        }

                    } else {
                        Log.i(TAG, "nobody lives here captain");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });*/







/*            checkMem.child("room/aaaa").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        System.out.println(dataSnapshot.getKey());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/
           /* THIS WORKS GETS A MEMBER THEN FILDS ALL ROOMS THEIR IN isThere.child("Bipper/member").orderByKey().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (final DataSnapshot child : dataSnapshot.getChildren()){
                         key = child.getKey();

                        System.out.println("checking" + key);
                        checkMem.child("room/"+key).addListenerForSingleValueEvent(new ValueEventListener() {
                            String zoop = key;
                            @Override
                            public void onDataChange(@NonNull DataSnapshot data) {
                                if (data.exists()){
                                    System.out.println(data.getValue()+"is real");
                                }
                                else{
                                    System.out.println(zoop +"is not real");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/


           /*
           GET ALL CHILDREN WITH KEY EQUAL TO BEEF= 12
           */



            /*isThere.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("Bipper/member/boony")){
                        System.out.println("yeh he's here");
                    }
                    else{
                        System.out.println("yo where he at");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/

           /* mDataBase.orderByValue().addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String prevChildKey) {
                    System.out.println("The " + dataSnapshot.getKey() + " score is " + dataSnapshot.getValue());
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
*/





/*            userTst = new User();
            userTst.setMember("hi");
            userTst.setBanned("hello");
            us
            test.child("goffer").child("member").child("goony").setValue((true));*/

        }

    }


}

