package android.jia.groupstudy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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


        public FlashcardViewHolder(View v) {
            super(v);
            questionTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            ownerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
        }
    }


    String roomId;
    private View view;
    Button btnOpenFlashDialog;
    public TextView txtDialogText;
    public EnteredActivity value;
    public DatabaseReference mkFlashcard;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public FirebaseUser firebaseUser;

    public FlashCardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_flash_card, container, false);
        btnOpenFlashDialog = view.findViewById(R.id.btnOpenFlashDialog);
        btnOpenFlashDialog.setOnClickListener(this);
        txtDialogText = view.findViewById(R.id.txtDialogText);
        value = (EnteredActivity) getActivity();
        roomId = value.roomId;
        mkFlashcard = database.getReference("flashcard");
        firebaseUser = value.mFirebaseUser;

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
        }
    }

    private void makeFlashcard(String input, boolean anonymous) {
        Flashcard flashcard = new Flashcard();
        flashcard.setQuestion(input);
        flashcard.setAnonymous(anonymous);
        flashcard.setUser(firebaseUser.getDisplayName());
        mkFlashcard.child(roomId).child(input).setValue(flashcard);
    }

}