package android.jia.groupstudy;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "FlashCardDialogFragment";

    public interface OnInputSelected {
        void sendInput(String input, boolean anonymous);
    }

    public FlashCardDialogFragment.OnInputSelected mOnInputSelected;


    public EditText mInput;
    Button mActionOK, getmActionCancel;
    public CheckBox setAnon;
    boolean anonymous;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flash_card_dialog, container, false);

        mInput = view.findViewById(R.id.mInput);
        mActionOK = view.findViewById(R.id.mActionOK);
        mActionOK.setOnClickListener(this);
        getmActionCancel = view.findViewById(R.id.getmActionCancel);
        getmActionCancel.setOnClickListener(this);
        setAnon = view.findViewById(R.id.setAnon);


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputSelected = (FlashCardDialogFragment.OnInputSelected) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCaseException: " + e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mActionOK:
                Log.i(TAG, "pressed send");
                if (setAnon.isChecked()) {
                    anonymous = true;
                }
                String input = mInput.getText().toString();
                if ((input.trim().length() >= 4)) {

                    mOnInputSelected.sendInput(input, anonymous);
                    getDialog().dismiss();
                } else {
                    Toast.makeText(getActivity(), "Please input at least 4 characters in both fields", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.getmActionCancel:
                Log.i(TAG, "pressed cancel");

                getDialog().dismiss();
                break;
        }
    }
}
