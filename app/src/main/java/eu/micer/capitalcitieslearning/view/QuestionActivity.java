package eu.micer.capitalcitieslearning.view;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import eu.micer.capitalcitieslearning.R;
import eu.micer.capitalcitieslearning.databinding.ActivityQuestionBinding;
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
    }
}
