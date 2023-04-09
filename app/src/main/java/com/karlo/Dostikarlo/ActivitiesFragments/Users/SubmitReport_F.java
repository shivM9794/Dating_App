package com.karlo.Dostikarlo.ActivitiesFragments.Users;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import Dostikarlo.R;

import com.karlo.binderstatic.ApiClasses.ApiLinks;
import com.karlo.binderstatic.ApiClasses.ApiRequest;
import com.karlo.binderstatic.Interfaces.Callback;
import com.karlo.binderstatic.RelateToFragment_OnBack.RootFragment;
import com.karlo.binderstatic.SimpleClasses.Functions;
import com.karlo.binderstatic.SimpleClasses.Variables;
import com.karlo.binderstatic.Interfaces.FragmentCallback;

import org.json.JSONException;
import org.json.JSONObject;


public class SubmitReport_F extends RootFragment implements View.OnClickListener {

    View view;

    TextView reportTypeTxt;
    EditText reportDescriptionTxt;
    String reportId, txtReportType, userId;
    FragmentCallback fragmentCallBack;


    public SubmitReport_F() {
        //Required Empty
    }

    public SubmitReport_F(FragmentCallback callback) {
        this.fragmentCallBack = callback;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_submit_report, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            reportId = bundle.getString("report_id");
            txtReportType = bundle.getString("report_type");
            userId = bundle.getString("user_id");
        }


        reportTypeTxt = view.findViewById(R.id.report_type);
        reportTypeTxt.setText(txtReportType);

        reportDescriptionTxt = view.findViewById(R.id.report_description_txt);


        view.findViewById(R.id.back_btn).setOnClickListener(this);
        view.findViewById(R.id.report_reason_layout).setOnClickListener(this);
        view.findViewById(R.id.submit_btn).setOnClickListener(this);

        return view;
    }


    public boolean checkValidation() {
        if (txtReportType.equals("Others") && TextUtils.isEmpty(reportDescriptionTxt.getText())) {
            Toast.makeText(getContext(), "Please give some reason.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    // call the api for report against apis
    public void callApiReportUser() {
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", Functions.getSharedPreference(getActivity()).getString(Variables.uid,""));
            params.put("report_user_id", userId);
            params.put("report_reason_id", reportId);
            params.put("description", reportDescriptionTxt.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Functions.showLoader(getContext(), false, false);
        ApiRequest.callApi(getActivity(), ApiLinks.reportUser, params, new Callback() {
            @Override
            public void response(String resp) {
                Functions.cancelLoader();

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.optString("code");
                    if (code.equals("200")) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("check", true);
                        if (fragmentCallBack != null)
                            fragmentCallBack.responce(bundle);

                        getActivity().onBackPressed();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.submit_btn) {
            if (checkValidation()) {
                callApiReportUser();
            }
        } else if (id == R.id.back_btn) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("check", false);
            if (fragmentCallBack != null)
                fragmentCallBack.responce(bundle);
            getActivity().onBackPressed();
        }
    }

}