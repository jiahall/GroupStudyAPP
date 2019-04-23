package android.jia.groupstudy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class FlashCardDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "FlashCardDialogFragment";

    public interface OnInputSelected {
        void sendInput(String input);
    }

    public OnInputSelected mOnInputSelected;


    public EditText mInput;
    Button mActionOK, getmActionCancel;

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


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputSelected = (OnInputSelected) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCaseException: " + e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mActionOK:
                getDialog().dismiss();
            case R.id.getmActionCancel:
                String input = mInput.getText().toString();
                if (!input.equals("")) {


                    mOnInputSelected.sendInput(input);


                }
                getDialog().dismiss();
        }
    }
}
