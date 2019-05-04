package android.jia.groupstudy;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class QuizFragment extends Fragment implements View.OnClickListener, QuizDialogFragment.OnQuestion {

    private static String TAG = "QuizFragment";


    @Override
    public void sendInput(String question, String answer) {
        Log.d(TAG, "question: " + question + " Answer: " + answer);
        makeQuiz(question, answer);

    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        TextView ownerTextView;
        TextView answerTextView;
        Button deleteButton, showAnswer;


        public QuizViewHolder(View v) {
            super(v);
            questionTextView = (TextView) itemView.findViewById(R.id.questionName);
            ownerTextView = (TextView) itemView.findViewById(R.id.questionOwner);
            answerTextView = (TextView) itemView.findViewById(R.id.questionAnswer);
            deleteButton = (Button) itemView.findViewById(R.id.deleteQuiz);
            showAnswer = (Button) itemView.findViewById(R.id.showAnswerQuiz);
        }
    }


    private String mUsername;
    private FirebaseAuth mFirebaseAuth;
    String roomId;
    private View view;
    Button btnOpenQuizDialog;
    public EnteredActivity value;
    public DatabaseReference mkQuiz;
    public DatabaseReference findQuiz;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public FirebaseUser mFirebaseUser;
    private FirebaseRecyclerAdapter<Quiz, QuizFragment.QuizViewHolder>
            mFirebaseAdapter;

    private DatabaseReference checkQuiz;
    private RecyclerView mQuizRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    public QuizFragment() {
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
        view = inflater.inflate(R.layout.fragment_quiz, container, false);
        btnOpenQuizDialog = view.findViewById(R.id.btnOpenQuizDialog);
        btnOpenQuizDialog.setOnClickListener(this);
        value = (EnteredActivity) getActivity();
        roomId = value.roomId;
        mkQuiz = database.getReference("quiz");
        findQuiz = database.getReference("quiz/" + roomId);
        checkQuiz = database.getReference("quiz");


        mQuizRecyclerView = (RecyclerView) view.findViewById(R.id.quizRecyclerView);

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mQuizRecyclerView.setLayoutManager(mLinearLayoutManager);


        // New child entries

        SnapshotParser<Quiz> parser = new SnapshotParser<Quiz>() {
            @Override
            public Quiz parseSnapshot(DataSnapshot dataSnapshot) {
                Quiz quiz = dataSnapshot.getValue(Quiz.class);
                if (quiz != null) {
                    quiz.setQuestion(dataSnapshot.getKey());
                }
                return quiz;
            }
        };

        FirebaseRecyclerOptions<Quiz> options =
                new FirebaseRecyclerOptions.Builder<Quiz>()
                        .setQuery(findQuiz, parser)
                        .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Quiz, QuizFragment.QuizViewHolder>(options) {
            @Override
            public QuizFragment.QuizViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new QuizFragment.QuizViewHolder(inflater.inflate(R.layout.item_quiz, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final QuizFragment.QuizViewHolder viewHolder,
                                            int position,
                                            final Quiz quiz) {
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Toast.makeText(getContext(), "ayy this is a long hold", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });


                if (quiz.getUser().equals(mFirebaseUser.getDisplayName())) {
                    viewHolder.deleteButton.setVisibility(View.VISIBLE);
                    viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            findQuiz.child(quiz.getQuestion()).removeValue();
                        }
                    });
                }

                viewHolder.questionTextView.setText(quiz.getQuestion());
                viewHolder.answerTextView.setText(quiz.getAnswer());
                viewHolder.ownerTextView.setText(quiz.getUser());
                viewHolder.showAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewHolder.answerTextView.setVisibility(View.VISIBLE);
                    }
                });


            }
        };
        mQuizRecyclerView.setAdapter(mFirebaseAdapter);
        mFirebaseAdapter.startListening();


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        setHasOptionsMenu(true);
        inflater.inflate(R.menu.menu_quiz, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_quiz) {
            Toast.makeText(getActivity(), "clicked on " + item.getTitle(), Toast.LENGTH_SHORT)
                    .show();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOpenQuizDialog:
                QuizDialogFragment dialog = new QuizDialogFragment();
                dialog.setTargetFragment(QuizFragment.this, 1);
                dialog.show(getFragmentManager(), "QuizDialogFragment");
                break;

        }
    }

    private void makeQuiz(String question, String answer) {
        Log.d(TAG, "in makeQuiz question: " + question + " Answer: " + answer + "user: " + mFirebaseUser.getDisplayName());
        Quiz quiz = new Quiz();
        quiz.setQuestion(question);
        quiz.setAnswer(answer);
        quiz.setUser(mFirebaseUser.getDisplayName());
        mkQuiz.child(roomId).child(question).setValue(quiz);
    }

}