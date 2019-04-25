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

public class RoomDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "RoomDialogFragment";

    // interface method to send room data back to roomFragment class
    public interface OnRoomCreate {

        void sendInput(String rmName, String rmPass);
    }

    public OnRoomCreate mOnRoomCreate;


    public EditText edtCreateRoomName, edtCreatePassword;
    public Button mActionOK, getmActionCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room_dialog, container, false);

        edtCreatePassword = view.findViewById(R.id.edtCreatePassword);
        edtCreateRoomName = view.findViewById(R.id.edtCreateRoomName);
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
            mOnRoomCreate = (OnRoomCreate) getTargetFragment();
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
                String rmName = edtCreateRoomName.getText().toString();
                String rmPass = edtCreatePassword.getText().toString();
                Log.i(TAG, rmName + " " + rmPass);
                if (!rmName.equals("") && !rmPass.equals("")) {
                    Log.i(TAG, "yeh 4 characters");


                    mOnRoomCreate.sendInput(rmName, rmPass);
                    getDialog().dismiss();


                }

        }
    }
}
