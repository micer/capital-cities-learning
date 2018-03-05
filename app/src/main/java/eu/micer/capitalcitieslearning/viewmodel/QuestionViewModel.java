package eu.micer.capitalcitieslearning.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.micer.capitalcitieslearning.Util;
import eu.micer.capitalcitieslearning.model.AnswerOptions;
import eu.micer.capitalcitieslearning.model.Country;

public class QuestionViewModel extends AndroidViewModel {
    private static final String TAG = QuestionViewModel.class.getSimpleName();

    private final MutableLiveData<Country> observableCountry;
    private final MutableLiveData<AnswerOptions> observableAnswerOptions;
    private List<Country> countryList;

    public QuestionViewModel(@NonNull Application application) {
        super(application);

        observableCountry = new MutableLiveData<>();
        observableAnswerOptions = new MutableLiveData<>();
        selectNextCountry();
    }

    private List<Country> getCountryList() {
        if (countryList == null) {
            countryList = loadCountries();
        }
        return countryList;
    }

    private List<Country> loadCountries() {
        String myJson = null;
        try {
            myJson = Util.readFile(this.getApplication(), "countries.json");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return Arrays.asList(new Gson().fromJson(myJson, Country[].class));
    }

    private void selectNextCountry() {
        int randomNumber = ThreadLocalRandom.current().nextInt(0, getCountryList().size() + 1);
        Country country = getCountryList().get(randomNumber);
        observableCountry.setValue(country);
        initOptions(country);
    }

    private void initOptions(Country country) {
        int randomCorrectPosition = ThreadLocalRandom.current().nextInt(0, 4);
        String correctOption = country.getCapital().get(0);
        AnswerOptions answerOptions;

        switch (randomCorrectPosition) {
            case 0:
                answerOptions = new AnswerOptions(correctOption, "Praha", "Brno", "Ostrava");
                break;
            case 1:
                answerOptions = new AnswerOptions("Praha", correctOption, "Brno", "Ostrava");
                break;
            case 2:
                answerOptions = new AnswerOptions("Praha", "Brno", correctOption, "Ostrava");
                break;
            default:
                answerOptions = new AnswerOptions("Praha", "Brno", "Ostrava", correctOption);
        }

        observableAnswerOptions.setValue(answerOptions);
    }

    public MutableLiveData<Country> getSelectedCountry() {
        return observableCountry;
    }

    public MutableLiveData<AnswerOptions> getAnswerOptions() {
        return observableAnswerOptions;
    }

    public void selectOption1() {
        selectNextCountry();
    }

    public void selectOption2() {
        selectNextCountry();
    }

    public void selectOption3() {
        selectNextCountry();
    }

    public void selectOption4() {
        selectNextCountry();
    }
}
