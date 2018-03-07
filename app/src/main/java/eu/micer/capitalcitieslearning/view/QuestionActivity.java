package eu.micer.capitalcitieslearning.view;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import eu.micer.capitalcitieslearning.R;
import eu.micer.capitalcitieslearning.databinding.ActivityQuestionBinding;
import eu.micer.capitalcitieslearning.util.CommonUtil;
import eu.micer.capitalcitieslearning.viewmodel.QuestionViewModel;

public class QuestionActivity extends AppCompatActivity {
    private static final String TAG = "QuestionActivity";

    private QuestionViewModel viewModel;
    private ActivityQuestionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_question);

        viewModel = ViewModelProviders.of(this).get(QuestionViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.getCountries().observe(this, countryEntities -> {
            int cnt = countryEntities == null ? 0 : countryEntities.size();
            Log.d(TAG, "countries count: " + cnt);
            if (!CommonUtil.getInstance().isNullOrEmpty(countryEntities)) {
                viewModel.selectNextCountry();
            }
        });

        viewModel.getCountriesInRegion().observe(this, countryEntities -> {
            int cnt = countryEntities == null ? 0 : countryEntities.size();
            Log.d(TAG, "countries in region count: " + cnt);
            if (!CommonUtil.getInstance().isNullOrEmpty(countryEntities)) {
                viewModel.updateOptions(countryEntities);
            }
        });
    }
}
