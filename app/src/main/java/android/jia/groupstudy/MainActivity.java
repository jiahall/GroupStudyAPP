package android.jia.groupstudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    private String mUsername;
    private String mPhotoUrl;

/*    String key, flump;
    String dip = "";
    long num;
    int count= 0;*/


    String uId;
    String fragPass;
    DatabaseReference test;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference getTest = database.getReference("message");
    DatabaseReference mDataBase;
    DatabaseReference isThere;
    DatabaseReference checkMem;
    DatabaseReference checkFlash;


    User userTst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataBase = database.getReference("user/Bipper/member");
        isThere = database.getReference("user");
        checkMem = database.getReference();
        checkFlash = database.getReference();


        /*flump ="";*/
        test = FirebaseDatabase.getInstance().getReference()
                .child("user");
        fragPass = "it works";
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        final TabLayout tabLayout = findViewById(R.id.tablayout);


        final ViewPager viewPager = findViewById(R.id.viewPager);

        setSupportActionBar(toolbar);

        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());
        pageAdapter.AddFragment(new RoomFragment(), "Room");
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                            R.color.flashcard));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                            R.color.flashcard));
                    Log.i(TAG, "Position is" + viewPager.getCurrentItem());

                    viewPager.getCurrentItem();
                } else if (tab.getPosition() == 2) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                            R.color.quiz));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                            R.color.quiz));
                    Log.i(TAG, "Position is" + viewPager.getCurrentItem());
                } else {
                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                            R.color.colorPrimary));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
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





/*        readData(new FirebaseCallback() {
            @Override
            public void onCallBack(String flump) {
                dip = "";
                dip += flump;
                count++;
                if(count==num){
                    Log.i(TAG,dip);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, dip);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "send to"));
                }

                Log.i(TAG,"This should come in second? " +num);


            }
            @Override
            public void onCallBack(long doo) {

                num= doo;
                Log.i(TAG,"The number comes first? "+ num);
            }
        });*/



    }
/*    private void readData(final FirebaseCallback firebaseCallback){
        checkFlash.child("flashcard/djdj").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot child : dataSnapshot.getChildren()){
                    key = child.getKey();


                    firebaseCallback.onCallBack(dataSnapshot.getChildrenCount());


                    System.out.println("checking" + key);
                    Query bont = checkMem.child("flashcard/djdj/"+ key);
                    bont.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            flump += "\n"+ "Question: " + dataSnapshot.child("question").getValue();

                            if(dataSnapshot.child("anonymous").getValue().equals(true)){
                                flump += "\n"+ "User wishes to remain anonymous";
                            }else{
                                flump += "\n"+ "User: " + dataSnapshot.child("user").getValue();
                            }

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                if (!postSnapshot.getKey().equals("question") && !postSnapshot.getKey().equals("anonymous") && !postSnapshot.getKey().equals("user")){
                                    String firstSixChars = postSnapshot.getValue().toString();
                                    if (firstSixChars.length() > 6) {

                                        String amount = firstSixChars.substring(0, 6);

                                        if(amount.equals("an17on")){

                                            flump += "\n"+ "Anonymous: " + firstSixChars.substring(6);
                                        }else{
                                            //if the value is above the subString but does not start with an17on it's not meant to be anonymous
                                            flump += "\n"+ postSnapshot.getKey()+ ": " + postSnapshot.getValue();
                                        }
                                    }else{
                                        //if the value is below the substring amount, it cannot be anonamous
                                        flump += "\n"+ postSnapshot.getKey()+ ": " + postSnapshot.getValue();
                                    }

                            }
                            }
                            flump += "\n";
                            flump += "\n";
                            firebaseCallback.onCallBack(flump);


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
        });
        }*/


   /* private interface FirebaseCallback{

        void onCallBack(String flump);
        void onCallBack(long num);

    }*/

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
                RoomCheck();
            }





            /*test.child("goffer").child("member").child("goony").setValue((true));*/

            //If there is not already a user in the database this will make them
            isThere.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("/" + uId)) {
                        System.out.println("yeh he's here");


                    } else {
                        userTst = new User();
                        userTst.setDisplayName(mUsername);
                        test.child(uId).setValue(userTst);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



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
           /* THIS WORKS GETS A MEMBER THEN FILDS ALL ROOMS THEIR IN
           isThere.child("Bipper/member").orderByKey().addValueEventListener(new ValueEventListener() {
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





/* checkFlash.child("flashcard/djdj").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot child : dataSnapshot.getChildren()){
                    key = child.getKey();
                    flump += "\n THE FLASH CARD QUESSTION IS: " +key+"--------";

                    System.out.println("checking" + key);
                    Query bont = checkMem.child("flashcard/djdj/"+ key);
                    bont.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                flump += "\n"+ postSnapshot.getKey()+ " " + postSnapshot.getValue();
                            }
                            }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });Log.i(TAG, flump);
                }Log.i(TAG, flump);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/



/*        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "send to"));*/


    public void RoomCheck() {
        Query query = checkMem.child("member/" + mFirebaseUser.getUid()).orderByKey();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String prevChildKey) {
                Log.i(TAG, " got 12 whole beef" + dataSnapshot.getKey());

                checkMem.child("room/" + dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            System.out.println("the room is real");


                        } else {
                            System.out.println("this room aint real");
                            checkMem.child("member/" + mFirebaseUser.getUid() + "/" + dataSnapshot.getKey()).removeValue();
                            checkMem.child("messages/" + dataSnapshot.getKey()).removeValue();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @android.support.annotation.Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @android.support.annotation.Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}
