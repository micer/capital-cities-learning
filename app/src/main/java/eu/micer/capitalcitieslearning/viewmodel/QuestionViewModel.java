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
import eu.micer.capitalcitieslearning.util.CommonUtil;

public class QuestionViewModel extends AndroidViewModel {
    private static final String TAG = QuestionViewModel.class.getSimpleName();
    private MainApplication mainApplication;
    private int totalAnswers;
    private int correctAnswers;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<CountryEntity>> observableCountries;
    private final MutableLiveData<CountryEntity> observableSelectedCountry;
    private final MediatorLiveData<List<CountryEntity>> observableCountriesInRegion;
    private final MutableLiveData<AnswerOptions> observableAnswerOptions;

    public QuestionViewModel(@NonNull Application application) {
        super(application);

        mainApplication = (MainApplication) application;

        // All countries
        observableCountries = new MediatorLiveData<>();
        observableCountries.setValue(null);

        LiveData<List<CountryEntity>> countries = mainApplication
                .getRepository()
                .getCountries();

        observableCountries.addSource(countries, observableCountries::setValue);

        // Countries in region
        observableCountriesInRegion = new MediatorLiveData<>();
        observableCountriesInRegion.setValue(null);

        observableSelectedCountry = new MutableLiveData<>();
        observableAnswerOptions = new MutableLiveData<>();

        correctAnswers = 0;
    }

    public void selectNextCountry() {
        if (getCountries().getValue() == null) {
            Log.e(TAG, "No countries available!");
            return;
        }
        int randomNumber = ThreadLocalRandom.current().nextInt(0, getCountries().getValue().size() + 1);
        CountryEntity country = getCountries().getValue().get(randomNumber);
        observableSelectedCountry.setValue(country);

        LiveData<List<CountryEntity>> countriesInRegion = mainApplication
                .getRepository()
                .getCountriesInRegion(country.getRegion());

        observableCountriesInRegion.addSource(countriesInRegion, observableCountriesInRegion::setValue);
    }

    public LiveData<List<CountryEntity>> getCountries() {
        return observableCountries;
    }

    public MutableLiveData<CountryEntity> getSelectedCountry() {
        return observableSelectedCountry;
    }

    public LiveData<List<CountryEntity>> getCountriesInRegion() {
        return observableCountriesInRegion;
    }

    public MutableLiveData<AnswerOptions> getAnswerOptions() {
        return observableAnswerOptions;
    }

    public void onOptionSelected(int option) {
        totalAnswers++;
        boolean answerIsCorrect;
        if (observableAnswerOptions.getValue() != null
                && observableAnswerOptions.getValue().getCorrectOption() == option) {
            answerIsCorrect = true;
            Log.d(TAG, "Answer is correct!");
            correctAnswers++;
        } else {
            answerIsCorrect = false;
            Log.d(TAG, "Answer is incorrect!");
        }

        // TODO show feedback on UI

        // Proceed to next question
        selectNextCountry();
    }

    public void updateOptions(List<CountryEntity> countriesInRegion) {
        CountryEntity countryEntity = observableSelectedCountry.getValue();

        if (countryEntity == null) {
            return;
        }

        countriesInRegion.remove(countryEntity);
        CountryEntity option1 = CommonUtil.getInstance().getRandomItem(countriesInRegion);
        countriesInRegion.remove(option1);
        CountryEntity option2 = CommonUtil.getInstance().getRandomItem(countriesInRegion);
        countriesInRegion.remove(option2);
        CountryEntity option3 = CommonUtil.getInstance().getRandomItem(countriesInRegion);

        int randomCorrectPosition = ThreadLocalRandom.current().nextInt(0, 4);
        String correctOption = countryEntity.getCapital();
        AnswerOptions answerOptions;

        switch (randomCorrectPosition) {
            case 0:
                answerOptions = new AnswerOptions(correctOption, option1.getCapital(), option2.getCapital(), option3.getCapital());
                break;
            case 1:
                answerOptions = new AnswerOptions(option1.getCapital(), correctOption, option2.getCapital(), option3.getCapital());
                break;
            case 2:
                answerOptions = new AnswerOptions(option1.getCapital(), option2.getCapital(), correctOption, option3.getCapital());
                break;
            default:
                answerOptions = new AnswerOptions(option1.getCapital(), option2.getCapital(), option3.getCapital(), correctOption);
        }
        answerOptions.setCorrectOption(randomCorrectPosition + 1);

        observableAnswerOptions.setValue(answerOptions);
    }
}
