package android.jia.groupstudy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class FlashCardFragment extends Fragment implements View.OnClickListener, FlashCardDialogFragment.OnInputSelected {

    private static String TAG = "FlashCardFragment";

    @Override
    public void sendInput(String input) {
        Log.d(TAG, "sendInput: " + input);
        txtDialogText.setText(input);

    }

    private View view;
    Button btnOpenFlashDialog;
    public TextView txtDialogText;

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


}