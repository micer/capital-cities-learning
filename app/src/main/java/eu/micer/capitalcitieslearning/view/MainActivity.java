package eu.micer.capitalcitieslearning.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import eu.micer.capitalcitieslearning.R;
import eu.micer.capitalcitieslearning.databinding.ActivityMainBinding;
import eu.micer.capitalcitieslearning.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        binding.btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), QuestionActivity.class);
            startActivity(intent);
        });
    }
}
