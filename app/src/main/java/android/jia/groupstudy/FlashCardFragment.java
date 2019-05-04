package android.jia.groupstudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class FlashCardFragment extends Fragment implements View.OnClickListener, FlashCardDialogFragment.OnInputSelected {

    private static String TAG = "FlashCardFragment";


    @Override
    public void sendInput(String input, boolean anonymous) {
        Log.d(TAG, "sendInput: " + input);
        makeFlashcard(input, anonymous);

    }

    public static class FlashcardViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        TextView ownerTextView;
        Button deleteButton;


        public FlashcardViewHolder(View v) {
            super(v);
            questionTextView = (TextView) itemView.findViewById(R.id.questionName);
            ownerTextView = (TextView) itemView.findViewById(R.id.questionOwner);
            deleteButton = (Button) itemView.findViewById(R.id.deleteFlashCard);
        }
    }

    Button btnOpenFlashList;
    String roomId;
    private View view;
    Button btnOpenFlashDialog, btnSendAnswer;
    public TextView tvFlashcardQuestion, tvFlashcardOwner, tvSetAnon;
    public EditText etFlastcardAnswer;
    public CheckBox cbSetAnon;
    public String mUsername;

    public EnteredActivity value;
    public DatabaseReference mkFlashcard;
    public DatabaseReference findFlashcard;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public FirebaseUser mFirebaseUser;
    public FirebaseAuth mFirebaseAuth;
    private FirebaseRecyclerAdapter<Flashcard, FlashcardViewHolder>
            mFirebaseAdapter;

    LinearLayout linearLayout;

    private DatabaseReference checkFlash;
    private RecyclerView mFlashcardRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    public FlashCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            Log.i(TAG, "you got kicked out of chat activity");
            startActivity(new Intent(getActivity(), SignInActivity.class));
            getActivity().finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();

            Toast.makeText(getActivity(), "you are: " + mFirebaseUser.getDisplayName() + " in Room: " + roomId + "quiz fragment", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_flash_card, container, false);

        btnSendAnswer = view.findViewById(R.id.btnsendFlashcardAnswer);
        tvSetAnon = view.findViewById(R.id.setAnswerAnon);
        etFlastcardAnswer = view.findViewById(R.id.answerInput);
        cbSetAnon = view.findViewById(R.id.setAnon);


        btnOpenFlashDialog = view.findViewById(R.id.btnOpenFlashDialog);
        btnOpenFlashDialog.setOnClickListener(this);
        btnOpenFlashList = view.findViewById(R.id.btnOpenFlashList);
        btnOpenFlashList.setOnClickListener(this);
        value = (EnteredActivity) getActivity();
        roomId = value.roomId;
        mkFlashcard = database.getReference("flashcard");
        findFlashcard = database.getReference("flashcard/" + roomId);
        checkFlash = database.getReference("flashcard");

        linearLayout = view.findViewById(R.id.answersLayout);

        tvFlashcardOwner = view.findViewById(R.id.tvFlashcardOwner);
        tvFlashcardQuestion = view.findViewById(R.id.tvFlashcardQuestion);

        mFlashcardRecyclerView = (RecyclerView) view.findViewById(R.id.flashcardRecyclerView);

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mFlashcardRecyclerView.setLayoutManager(mLinearLayoutManager);


        // New child entries

        SnapshotParser<Flashcard> parser = new SnapshotParser<Flashcard>() {
            @Override
            public Flashcard parseSnapshot(DataSnapshot dataSnapshot) {
                Flashcard flashcard = dataSnapshot.getValue(Flashcard.class);
                if (flashcard != null) {
                    flashcard.setQuestion(dataSnapshot.getKey());
                }
                return flashcard;
            }
        };

        FirebaseRecyclerOptions<Flashcard> options =
                new FirebaseRecyclerOptions.Builder<Flashcard>()
                        .setQuery(findFlashcard, parser)
                        .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Flashcard, FlashcardViewHolder>(options) {
            @Override
            public FlashcardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new FlashcardViewHolder(inflater.inflate(R.layout.item_flashcard, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final FlashcardViewHolder viewHolder,
                                            int position,
                                            final Flashcard flashcard) {
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Toast.makeText(getContext(), "ayy this is a long hold", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "yeh clicked", Toast.LENGTH_SHORT).show();
                        btnOpenFlashDialog.setVisibility(View.GONE);
                        mFlashcardRecyclerView.setVisibility(View.GONE);
                        btnOpenFlashList.setVisibility(View.VISIBLE);

                        tvFlashcardOwner.setVisibility(View.VISIBLE);
                        if (flashcard.isAnonymous()) {
                            tvFlashcardOwner.setText("anonymous");

                        } else {
                            tvFlashcardOwner.setText(flashcard.getUser());
                        }
                        tvFlashcardQuestion.setVisibility(View.VISIBLE);
                        tvFlashcardQuestion.setText(flashcard.getQuestion());

                        btnSendAnswer.setVisibility(View.VISIBLE);
                        tvSetAnon.setVisibility(View.VISIBLE);
                        etFlastcardAnswer.setVisibility(View.VISIBLE);
                        cbSetAnon.setVisibility(View.VISIBLE);


                        Query bont = checkFlash.child("djdj/Ftff").orderByKey();
                        bont.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    if (!postSnapshot.getKey().equals("question") && !postSnapshot.getKey().equals("anonymous")) {
                                        TextView textView = new TextView(getContext());
                                        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        textView.setGravity(Gravity.CENTER);
                                        textView.setText(postSnapshot.getKey() + ": " + postSnapshot.getValue());
                                        linearLayout.addView(textView);
                                        Log.i(TAG, "The key is " + postSnapshot.getKey() + " it's value is: " + postSnapshot.getValue());
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                });

                if (flashcard.getUser().equals(mFirebaseUser.getDisplayName())) {
                    viewHolder.deleteButton.setVisibility(View.VISIBLE);
                    viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            findFlashcard.child(flashcard.getQuestion()).removeValue();
                        }
                    });
                }

                viewHolder.questionTextView.setText(flashcard.getQuestion());
                if (flashcard.isAnonymous()) {
                    tvFlashcardOwner.setText("anonymous");
                    viewHolder.ownerTextView.setText("anonymous");
                } else {
                    viewHolder.ownerTextView.setText(flashcard.getUser());
                }

            }
        };
        mFlashcardRecyclerView.setAdapter(mFirebaseAdapter);
        mFirebaseAdapter.startListening();





        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        setHasOptionsMenu(true);
        inflater.inflate(R.menu.menu_flashcard, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_flashcard) {
            Toast.makeText(getActivity(), "clicked on " + item.getTitle(), Toast.LENGTH_SHORT)
                    .show();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOpenFlashDialog:
                FlashCardDialogFragment dialog = new FlashCardDialogFragment();
                dialog.setTargetFragment(FlashCardFragment.this, 1);
                dialog.show(getFragmentManager(), "FlashCardDialogFragment");
                break;
            case R.id.btnOpenFlashList:
                mFlashcardRecyclerView.setVisibility(View.VISIBLE);
                btnOpenFlashList.setVisibility(View.GONE);
                btnOpenFlashDialog.setVisibility(View.VISIBLE);

                tvFlashcardOwner.setVisibility(View.GONE);
                tvFlashcardOwner.setText("");
                tvFlashcardQuestion.setVisibility(View.GONE);
                tvFlashcardQuestion.setText("");
                linearLayout.removeAllViewsInLayout();
                btnSendAnswer.setVisibility(View.GONE);
                tvSetAnon.setVisibility(View.GONE);
                etFlastcardAnswer.setVisibility(View.GONE);
                cbSetAnon.setVisibility(View.GONE);
               /*
                    ALSO LIKE IF YOU WANT YOUR ANSWER TO BE ANONYMOUS YOU CHECK THE BOX AND YOUR ANSWER GETS SENT OFF
                    OBVIOUSLY IF IT CANT FIND YOUR ANSWER IT'LL GIVE YOU THE OPTION TO STRAIGHT UP MAKE ONE'

                    WITH ANONYMOUS IN FRONT OF IT SO ANONYMOUSJIAHALL
                    THEN A STRING CHECKS IF IT HAS ANONYMOUS IN FRONT OF IT IF IT DOES IT GETS REMOVED SO YOU CAN EDIT IT
                    BUT TO EVERYBODYT ELSE IT GETS RID OF YOUR USERNAME AND LEAVES ANONAMOUS
                    AFTER THATS DONE AFTER THAT YOU'LL BE FINISHED WITH FLASH CARD COPY AND PASTE AND GET RID OF ALL FUNCTIONALITY FOR
            QUIZ THATS BASICALLY IT THEN YOU GOTTA DO CALENDER/MOVE TO NOTES AND BAN USER THEN CLEAN UP AND YOUY'RE DONE*/
                break;
        }
    }

    private void makeFlashcard(String input, boolean anonymous) {
        Flashcard flashcard = new Flashcard();
        flashcard.setQuestion(input);
        flashcard.setAnonymous(anonymous);
        flashcard.setUser(mFirebaseUser.getDisplayName());
        mkFlashcard.child(roomId).child(input).setValue(flashcard);
    }

}