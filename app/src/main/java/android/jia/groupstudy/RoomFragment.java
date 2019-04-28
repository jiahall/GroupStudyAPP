package android.jia.groupstudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RoomFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, RoomDialogFragment.OnRoomCreate {
    private static String TAG = "RoomFragment";


    //connection to roomDialogFragment interface
    @Override
    public void sendInput(String rmName, String rmPass) {
        Log.i(TAG, "just sent data back from dialog ");
        if (checkData(rmName, rmName)) {

            addRoom(rmName, rmPass);

        } else {
            Toast.makeText(getActivity(), "Please input at least 4 characters in both fields", Toast.LENGTH_SHORT).show();
        }

    }


    private View view;
    private RecyclerView myRoomList;
    public FirebaseUser firebaseUserRoom;
    public MainActivity value;
    public String testUid;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference findRoom;
    DatabaseReference mkRoom;
    DatabaseReference inputUser;
    DatabaseReference checkUser;
    DatabaseReference roomRef;
    private DatabaseReference userRef;
    Boolean isBanned;
    FirebaseRecyclerAdapter<Room, RoomViewHolder> adapter;
    String roomID;


    EditText edtJoinRoom;
    Button btnRoomMaker, joinRoom;



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
        findRoom = database.getReference("member");
        roomRef = database.getReference().child("room");


        userRef = database.getReference().child("user").child(firebaseUserRoom.getUid()).child("member");


        btnRoomMaker = view.findViewById(R.id.btnRoomMaker);
        btnRoomMaker.setOnClickListener(this);
        joinRoom = view.findViewById(R.id.joinRoom);
        joinRoom.setOnClickListener(this);

        edtJoinRoom = view.findViewById(R.id.edtJoinRoom);


        myRoomList = (RecyclerView) view.findViewById(R.id.roomList);
        myRoomList.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Room> options = new FirebaseRecyclerOptions.Builder<Room>()
                .setQuery(findRoom.child(firebaseUserRoom.getUid()), Room.class).build();

        adapter = new FirebaseRecyclerAdapter<Room, RoomViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RoomViewHolder holder, final int position, @NonNull Room model) {
                final String membership = getRef(position).getKey();
                DatabaseReference getMembership = getRef(position).child("status").getRef();


                //This is where you make the buttons visible

                getMembership.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String type = dataSnapshot.getValue().toString();
                            if (type.equals("member")) {
                                roomRef.child(membership).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                        final String requestRoomName = dataSnapshot.getKey().toString();
                                        Log.i(TAG, "data i got is: " + requestRoomName);
                                        final String requestRoomOwner = dataSnapshot.child("ownerDisplayName").getValue().toString();
                                        Log.i(TAG, "data i got is: " + requestRoomOwner);
                                        final String requestOwnerUid = dataSnapshot.child("ownerUid").getValue().toString();
                                        Log.i(TAG, "Data for uid u got is: " + requestOwnerUid);
                                        Log.i(TAG, "Data for uid u got is: " + firebaseUserRoom.getUid());


                                        holder.roomOwner.setText(requestRoomOwner);
                                        holder.roomName.setText(requestRoomName);
                                        if (firebaseUserRoom.getUid().equals(requestOwnerUid)) {
                                            //set buttons depending on if room owner or not
                                            holder.deleteRoom.setVisibility(View.VISIBLE);
                                            holder.leaveRoom.setVisibility(View.GONE);

                                        } else {
                                            holder.deleteRoom.setVisibility(View.GONE);
                                            holder.leaveRoom.setVisibility(View.VISIBLE);
                                        }


                                        holder.leaveRoom.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Toast.makeText(getActivity(), "you just left: " + holder.roomName.getText().toString(), Toast.LENGTH_SHORT).show();
                                                inputUser.child("member/" + firebaseUserRoom.getUid() + "/" + holder.roomName.getText().toString()).setValue(null);
                                            }
                                        });
                                        holder.deleteRoom.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Toast.makeText(getActivity(), "you just deleted: " + holder.roomName.getText().toString(), Toast.LENGTH_SHORT).show();
                                                getRef(position).removeValue();
                                                inputUser.child("room/" + holder.roomName.getText().toString()).removeValue();
                                                inputUser.child("messages/" + holder.roomName.getText().toString()).removeValue();

                                            }
                                        });
                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Toast.makeText(getActivity(), "going to room " + holder.roomName.getText().toString()
                                                        , Toast.LENGTH_SHORT)
                                                        .show();
                                                roomID = holder.roomName.getText().toString();
                                                Intent intent = new Intent(getContext(), ChatActivity.class);
                                                intent.putExtra("ROOM_DATA", roomID);
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            } else {
                                //Hide's rooms user is banned from
                                holder.roomOwner.setVisibility(View.GONE);
                                holder.roomName.setVisibility(View.GONE);
                                holder.deleteRoom.setVisibility(View.GONE);
                                holder.leaveRoom.setVisibility(View.GONE);
                            }


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_room, viewGroup, false);
                RoomViewHolder viewHolder = new RoomViewHolder(view);
                Log.i(TAG, "this should only make 2 rooms");
                return viewHolder;
            }
        };
        myRoomList.setAdapter(adapter);
        adapter.startListening();
    }


    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView roomOwner, roomName;
        Button deleteRoom, leaveRoom;


        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);

            roomName = itemView.findViewById(R.id.roomName);
            roomOwner = itemView.findViewById(R.id.roomOwner);
            deleteRoom = itemView.findViewById(R.id.deleteRoom);
            leaveRoom = itemView.findViewById(R.id.leaveRoom);

        }
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
                RoomDialogFragment dialog = new RoomDialogFragment();
                dialog.setTargetFragment(RoomFragment.this, 2);
                dialog.show(getFragmentManager(), "RoomDialogFragment");

            case R.id.joinRoom:
                if (!edtJoinRoom.getText().toString().equals("")) {
                    Log.i(TAG, "the room you're trying to enter is: " + edtJoinRoom.getText().toString());
                    roomExist(edtJoinRoom.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "gotta type some numbers", Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void roomExist(final String room) {

        roomRef.child(room).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.i(TAG, dataSnapshot.getKey() + " is the room name");
                    addUser(room);


                } else {
                    Toast.makeText(getActivity(), "Sorry this room doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void addUser(final String roomName) {
        Log.i(TAG, "well we're in the method");
        inputUser.child("member/" + firebaseUserRoom.getUid() + "/" + roomName + "/status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                Log.i(TAG, "getting data: " + data.toString());
                if (data.exists()) {
                    Log.i(TAG, "the value we're getting is: " + data.getValue());
                    String info = data.getValue().toString();
                    if (info.equals("banned")) {
                        Log.i(TAG, "User is banned");

                    }

                } else {
                    inputUser.child("member/" + firebaseUserRoom.getUid() + "/" + roomName).child("status").setValue("member");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addRoom(final String roomName, final String password) {
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
                            inputUser.child("member/" + firebaseUserRoom.getUid() + "/" + roomName).child("status").setValue("member");


                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "oops, somthing went wrong, please check internet connection", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private Boolean checkData(String name, String password) {
        if (name.trim().length() >= 4 && password.trim().length() >= 4) {
            Log.i(TAG, "char 4 or over");
            Toast.makeText(getActivity(), "checking details name of Room: " + name + "checking password" + password, Toast.LENGTH_SHORT).show();
            return true;

        } else {
            Toast.makeText(getActivity(), "gotta be 4 char or more", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    }


