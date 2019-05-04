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
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "QuizDialogFragment";

    public interface OnQuestion {
        void sendInput(String question, String answer);
    }

    public QuizDialogFragment.OnQuestion mOnInputSelected;


    public EditText mQuestion, mAnswer;
    Button mActionOK, getmActionCancel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz_dialog, container, false);

        mQuestion = view.findViewById(R.id.mQuestion);
        mAnswer = view.findViewById(R.id.mAnswer);
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
            mOnInputSelected = (QuizDialogFragment.OnQuestion) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCaseException: " + e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mActionOK:
                Log.i(TAG, "pressed send");
                String question = mQuestion.getText().toString();
                String answer = mAnswer.getText().toString();
                if ((question.trim().length() >= 4 && answer.trim().length() >= 4)) {

                    mOnInputSelected.sendInput(question, answer);
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
