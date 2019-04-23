package android.jia.groupstudy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RoomFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    private View view;


    public FirebaseUser firebaseUserRoom;
    public MainActivity value;
    public String testUid;
    String password;
    String roomName;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference findRoom;
    DatabaseReference mkRoom;
    DatabaseReference inputUser;
    DatabaseReference checkUser;
    Button joinRoom;

    Boolean isBanned;

    Room room;

    Button btnRoomMaker;
    Button btnRoomCreate;
    EditText edtMakeRoomName;
    EditText edtMakePassword;


    public RoomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_room, container, false);

        value = (MainActivity) getActivity();
        firebaseUserRoom = value.mFirebaseUser;
        testUid = value.uId;

        inputUser = database.getReference();
        mkRoom = FirebaseDatabase.getInstance().getReference();
        checkUser = FirebaseDatabase.getInstance().getReference("user");


        findRoom = database.getReference("room");

        btnRoomMaker = view.findViewById(R.id.btnRoomMaker);
        btnRoomMaker.setOnClickListener(this);

        btnRoomCreate = view.findViewById(R.id.btnRoomCreate);
        btnRoomCreate.setOnClickListener(this);


        edtMakeRoomName = view.findViewById(R.id.edtMakeRoomName);
        edtMakePassword = view.findViewById(R.id.edtMakePassword);


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_room, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_room) {
            Toast.makeText(getActivity(), "clicked on " + item.getTitle(), Toast.LENGTH_SHORT)
                    .show();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRoomMaker:
                if (edtMakeRoomName.getVisibility() == View.GONE) {
                    edtMakeRoomName.setVisibility(View.VISIBLE);
                    edtMakePassword.setVisibility(View.VISIBLE);
                    btnRoomMaker.setText("Close");
                    btnRoomCreate.setVisibility(View.VISIBLE);
                } else {
                    edtMakeRoomName.setVisibility(View.GONE);
                    edtMakePassword.setVisibility(View.GONE);
                    btnRoomMaker.setText("Create room");
                    btnRoomCreate.setVisibility(View.GONE);
                }
            case R.id.btnRoomCreate:
                roomName = edtMakeRoomName.getText().toString();
                password = edtMakePassword.getText().toString();

                //Check to make sure there is data
                if (checkData(roomName, password)) {

                    findRoom.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(roomName)) {
                                Toast.makeText(getActivity(), "Sorry that name is taken", Toast.LENGTH_SHORT).show();
                            } else {
                                Room room;
                                room = new Room();
                                room.setOwnerDisplayName(firebaseUserRoom.getDisplayName());
                                room.setColor("testBlue");
                                room.setOwnerUid(firebaseUserRoom.getUid());
                                room.setPassword(password);

                                mkRoom.child("room").child(roomName).setValue(room).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(), "Room Created", Toast.LENGTH_SHORT).show();
                                        checkUser.child("/" + firebaseUserRoom.getUid() + "/member").child(roomName).setValue((true));


                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getActivity(), "oops, somthing went wrong, please check internet connection", Toast.LENGTH_SHORT).show();

                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "Please input at least 4 characters in both fields", Toast.LENGTH_SHORT).show();
                }
        }
    }


    private Boolean checkData(String name, String password) {
        if (name.trim().length() >= 4 && password.trim().length() >= 4) {
            Log.i("button clicked", "char 4 or over");
            Toast.makeText(getActivity(), "checking details name of Room: " + name + "checking password" + password, Toast.LENGTH_SHORT).show();
            return true;

        } else {
            return false;
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private Boolean addUser(final String roomName) {

        inputUser.child("user/" + firebaseUserRoom.getUid() + "/banned/" + roomName).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                if (data.exists()) {
                    System.out.println("nah he's banned lol");
                    isBanned = true;
                } else {
                    System.out.println("ok ok he's going in");
                    checkUser.child("/" + firebaseUserRoom.getUid() + "/member").child(roomName).setValue((true));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return isBanned;
    }
}


