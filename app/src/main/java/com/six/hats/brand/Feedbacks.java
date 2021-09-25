package com.six.hats.brand;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.six.hats.brand.adapters.FeedbackListAdapter;
import com.six.hats.brand.model.CentreSingleton;
import com.six.hats.brand.model.FeedBackResponse;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class Feedbacks extends AppCompatActivity {


    List<FeedBackResponse> feedBackResponses = new ArrayList<>();
    private FeedbackListAdapter feedbackListAdapter;
    private List<ALL> quesList = new ArrayList<>();
    AppCompatActivity appCompatActivity;
    String mCenterId, subCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.feedbacks) + "</font>")));
        appCompatActivity = this;
        RecyclerView feedBackList = findViewById(R.id.feedback_list);
        feedBackList.setLayoutManager(new LinearLayoutManager(this));
        feedbackListAdapter = new FeedbackListAdapter(feedBackResponses, quesList, this);
        feedBackList.setAdapter(feedbackListAdapter);

        CentreSingleton centreSingleton = CentreSingleton.getInstance();
        mCenterId = centreSingleton.getBranchId();
        subCat = centreSingleton.getSubCategory();
        feedbackListAdapter.setOnItemClickListener(new FeedbackListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });


        loadFeedbacksData();
        loadFeedbacksQues();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Keep
    public class FeedbackRequest {
        private String branchId;

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }
    }


    private void loadFeedbacksData() {

        String centreId = mCenterId;
        FeedbackRequest feedbackRequest = new FeedbackRequest();
        feedbackRequest.setBranchId(centreId);
        //pass centre id here....
        CentralApis.getInstance().getAPIS().getAllFeedbacks(feedbackRequest).enqueue(new retrofit2.Callback<List<FeedBackResponse>>() {
            @Override
            public void onResponse(Call<List<FeedBackResponse>> call, Response<List<FeedBackResponse>> response) {

                if (response.isSuccessful()) {
                    List<FeedBackResponse> staffServiceClass = response.body();
                    feedBackResponses.clear();
                    if (staffServiceClass != null && staffServiceClass.size() != 0) {
                        feedBackResponses.addAll(staffServiceClass);
                    } else {
                        CommonUtility.showAlertOK(appCompatActivity, "No Feed-backs received yet.");
                    }
                    // feedbackListAdapter.notifyDataSetChanged();
                } else {

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.getString("message").contains("Unauthorized")) {

                            CommonUtility.autoLogin(appCompatActivity);
                        } else {
                            CommonUtility.showErrorAlert(appCompatActivity, jObjError.getString("message"));
                        }
                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(appCompatActivity, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FeedBackResponse>> call, Throwable t) {
                CommonUtility.showErrorAlert(appCompatActivity, getString(R.string.network_error_text));
            }
        });

    }

    @Keep
    public class ALL {
        public String questionId;
        public String question;
        public String optionType;
        public String category;
        public String mainCategory;

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getOptionType() {
            return optionType;
        }

        public void setOptionType(String optionType) {
            this.optionType = optionType;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getMainCategory() {
            return mainCategory;
        }

        public void setMainCategory(String mainCategory) {
            this.mainCategory = mainCategory;
        }
    }

    @Keep
    public class CustomerFeedBackQuestionList {
        @SerializedName("ALL")
        @Expose
        public List<ALL> aLL;

        public List<ALL> getaLL() {
            return aLL;
        }

        public void setaLL(List<ALL> aLL) {
            this.aLL = aLL;
        }
    }

    @Keep
    public class Root {
        public CustomerFeedBackQuestionList customerFeedBackQuestionList = new CustomerFeedBackQuestionList();

        public CustomerFeedBackQuestionList getCustomerFeedBackQuestionList() {
            return customerFeedBackQuestionList;
        }

        public void setCustomerFeedBackQuestionList(CustomerFeedBackQuestionList customerFeedBackQuestionList) {
            this.customerFeedBackQuestionList = customerFeedBackQuestionList;
        }
    }

    @Keep
    public class Data {
        public List<String> getFeedbackQuestion = new ArrayList<>();

        public List<String> getFliterQuestionList() {
            return getFeedbackQuestion;
        }

        public void setFliterQuestionList(List<String> fliterQuestionList) {
            this.getFeedbackQuestion = fliterQuestionList;
        }
    }


    private void loadFeedbacksQues() {

        Data data = new Data();
        data.setFliterQuestionList(new ArrayList<>());
        String subCatgeory = subCat;

        String json = CommonUtility.convertToJson("getAllQuesFeedbacks", data);
        //pass centre id here....
        CentralApis.getInstance().getAPIS().getAllQuesFeedbacks(subCatgeory, data).enqueue(new retrofit2.Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {

                if (response.isSuccessful()) {
                    Root staffServiceClass = response.body();
                    CustomerFeedBackQuestionList questions = staffServiceClass.getCustomerFeedBackQuestionList();
                    quesList = questions.getaLL();
                    feedbackListAdapter.notifyData(feedBackResponses, quesList);
                } else {

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.getString("message").contains("Unauthorized")) {
                            CommonUtility.autoLogin(appCompatActivity);
                        } else {
                            CommonUtility.showErrorAlert(appCompatActivity, jObjError.getString("message"));
                        }
                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(appCompatActivity, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                CommonUtility.showErrorAlert(appCompatActivity, getString(R.string.network_error_text));
            }
        });

    }


}
