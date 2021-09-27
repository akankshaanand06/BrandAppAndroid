package com.six.hats.brand.ui;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.six.hats.brand.R;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;


import java.util.ArrayList;
import java.util.List;

public class MultiPersonSheetDialog extends BottomSheetDialogFragment {


    int personCount = 0;
    private EditText enter_name_1, enter_name_2, enter_name_3, enter_name_4;
    private List<String> names = new ArrayList<>();
    String mCentreId;

    public MultiPersonSheetDialog(int persons, String centreId) {
        this.mCentreId = centreId;
        this.personCount = persons;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.multiprsn_bottom_sheet,
                container, false);

        enter_name_1 = view.findViewById(R.id.enter_name_1);
        enter_name_2 = view.findViewById(R.id.enter_name_2);
        enter_name_3 = view.findViewById(R.id.enter_name_3);
        enter_name_4 = view.findViewById(R.id.enter_name_4);

        enter_name_1.setText(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_NAME, ""));

        switch (personCount) {
            case 1:
                enter_name_1.setVisibility(View.VISIBLE);
                enter_name_1.setImeOptions(EditorInfo.IME_ACTION_DONE);
                enter_name_2.setVisibility(View.GONE);
                enter_name_3.setVisibility(View.GONE);
                enter_name_4.setVisibility(View.GONE);
                break;
            case 2:
                enter_name_1.setVisibility(View.VISIBLE);
                enter_name_1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                enter_name_2.setText("");
                enter_name_2.setVisibility(View.VISIBLE);
                enter_name_2.setImeOptions(EditorInfo.IME_ACTION_DONE);
                enter_name_3.setVisibility(View.GONE);
                enter_name_4.setVisibility(View.GONE);
                break;
            case 3:
                enter_name_1.setVisibility(View.VISIBLE);
                enter_name_1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                enter_name_2.setText("");
                enter_name_2.setVisibility(View.VISIBLE);
                enter_name_2.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                enter_name_3.setText("");
                enter_name_3.setVisibility(View.VISIBLE);
                enter_name_3.setImeOptions(EditorInfo.IME_ACTION_DONE);
                enter_name_4.setVisibility(View.GONE);
                break;
            case 4:
                enter_name_1.setVisibility(View.VISIBLE);
                enter_name_1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                enter_name_2.setText("");
                enter_name_2.setVisibility(View.VISIBLE);
                enter_name_2.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                enter_name_3.setText("");
                enter_name_3.setVisibility(View.VISIBLE);
                enter_name_3.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                enter_name_4.setText("");
                enter_name_4.setVisibility(View.VISIBLE);
                enter_name_4.setImeOptions(EditorInfo.IME_ACTION_DONE);
                break;
        }
        Button done_next = view.findViewById(R.id.done_next);

        done_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openServicesPage();
            }
        });


        return view;
    }

    public void openServicesPage() {

        names.clear();
        switch (personCount) {

            case 1:
                if (CommonUtility.chkString(enter_name_1.getText().toString())) {
                    if (!names.contains(enter_name_1.getText().toString())) {
                        names.add(enter_name_1.getText().toString());
                    }
                } else {
                    enter_name_1.setError("Please fill the name of the person.");
                }

                break;
            case 2:
                if (CommonUtility.chkString(enter_name_1.getText().toString())) {
                    if (!names.contains(enter_name_1.getText().toString())) {
                        names.add(enter_name_1.getText().toString());
                    }
                } else {
                    enter_name_1.setError("Please fill the name of the person.");
                }
                if (CommonUtility.chkString(enter_name_2.getText().toString())) {
                    if (!names.contains(enter_name_2.getText().toString())) {
                        names.add(enter_name_2.getText().toString());
                    }
                } else {
                    enter_name_2.setError("Please fill the name of all the person/s.");
                }

                break;
            case 3:
                if (CommonUtility.chkString(enter_name_1.getText().toString())) {
                    if (!names.contains(enter_name_1.getText().toString())) {
                        names.add(enter_name_1.getText().toString());
                    }
                } else {
                    enter_name_1.setError("Please fill the name of the person.");
                }
                if (CommonUtility.chkString(enter_name_2.getText().toString())) {
                    if (!names.contains(enter_name_2.getText().toString())) {
                        names.add(enter_name_2.getText().toString());
                    }
                } else {
                    enter_name_2.setError("Please fill the name of all the person.");
                }
                if (CommonUtility.chkString(enter_name_3.getText().toString())) {
                    if (!names.contains(enter_name_3.getText().toString())) {
                        names.add(enter_name_3.getText().toString());
                    }
                } else {
                    enter_name_3.setError("Please fill the name of all the person.");
                }

                break;
            case 4:

                if (CommonUtility.chkString(enter_name_1.getText().toString())) {
                    if (!names.contains(enter_name_1.getText().toString())) {
                        names.add(enter_name_1.getText().toString());
                    }
                } else {
                    enter_name_1.setError("Please fill the name of the person/s.");
                }
                if (CommonUtility.chkString(enter_name_2.getText().toString())) {
                    if (!names.contains(enter_name_2.getText().toString())) {
                        names.add(enter_name_2.getText().toString());
                    }
                } else {
                    enter_name_2.setError("Please fill the name of all the person/s.");
                }
                if (CommonUtility.chkString(enter_name_3.getText().toString())) {
                    if (!names.contains(enter_name_3.getText().toString())) {
                        names.add(enter_name_3.getText().toString());
                    }
                } else {
                    enter_name_3.setError("Please fill the name of all the person/s.");
                }
                if (CommonUtility.chkString(enter_name_4.getText().toString())) {
                    if (!names.contains(enter_name_4.getText().toString())) {
                        names.add(enter_name_4.getText().toString());
                    }
                } else {
                    enter_name_4.setError("Please fill the name of all the person/s.");
                }
                break;

        }
        boolean error = false;
        /*for (int i = 0; i < names.size(); i++) {
            if (!CommonUtility.chkString(names.get(i))) {

                CommonUtility.showAlertOK(getActivity(), "Please fill the name of all the person/s.");
                error = true;
                break;
            }
        }*/

        if (names.size() < personCount) {
            error = true;
        }

        if (!error) {
            PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_NAMES_LIST, CommonUtility.convertListToCommaString(names));
            Intent intent = new Intent("doneSelectionReceiver");
            getActivity().sendBroadcast(intent);
        }else{
            CommonUtility.showAlertOK(getActivity(), "Please fill the name of all the person/s.\nOR\nPlease type unique name of each person. Name of two or more persons cannot be same.");

        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);

    }


}
