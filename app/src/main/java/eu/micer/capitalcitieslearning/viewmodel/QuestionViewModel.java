package eu.micer.capitalcitieslearning.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.micer.capitalcitieslearning.MainApplication;
import eu.micer.capitalcitieslearning.model.AnswerOptions;
import eu.micer.capitalcitieslearning.repository.db.entity.CountryEntity;

public class QuestionViewModel extends AndroidViewModel {
    private static final String TAG = QuestionViewModel.class.getSimpleName();

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<CountryEntity>> observableCountries;
    private final MutableLiveData<CountryEntity> observableCountry;
    private final MutableLiveData<AnswerOptions> observableAnswerOptions;

    public QuestionViewModel(@NonNull Application application) {
        super(application);

        observableCountries = new MediatorLiveData<>();
        observableCountries.setValue(null);

        LiveData<List<CountryEntity>> countries = ((MainApplication) application)
                .getRepository()
                .getCountries();

        // observe the changes of the countries from the database and forward them
        observableCountries.addSource(countries, observableCountries::setValue);

        observableCountry = new MutableLiveData<>();
        observableAnswerOptions = new MutableLiveData<>();
    }

    public void selectNextCountry() {
        if (getCountries().getValue() == null) {
            Log.e(TAG, "No countries available!");
            return;
        }
        int randomNumber = ThreadLocalRandom.current().nextInt(0, getCountries().getValue().size() + 1);
        CountryEntity country = getCountries().getValue().get(randomNumber);
        observableCountry.setValue(country);
        initOptions(country);
    }

    private void initOptions(CountryEntity country) {
        int randomCorrectPosition = ThreadLocalRandom.current().nextInt(0, 4);
        String correctOption = country.getCapital();
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

    public LiveData<List<CountryEntity>> getCountries() {
        return observableCountries;
    }

    public MutableLiveData<CountryEntity> getSelectedCountry() {
        return observableCountry;
    }

    public MutableLiveData<AnswerOptions> getAnswerOptions() {
        return observableAnswerOptions;
    }

    public void selectOption(int option) {
        selectNextCountry();
    }
}
