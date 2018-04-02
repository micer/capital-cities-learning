package eu.micer.capitalcitieslearning.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import eu.micer.capitalcitieslearning.MainApplication;
import eu.micer.capitalcitieslearning.model.AnswerOption;
import eu.micer.capitalcitieslearning.model.QuestionData;
import eu.micer.capitalcitieslearning.repository.db.entity.CountryEntity;
import eu.micer.capitalcitieslearning.util.CommonUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class QuestionViewModel extends AndroidViewModel {
    private static final String TAG = QuestionViewModel.class.getSimpleName();
    public static final long TIME_FOR_ANSWER = TimeUnit.SECONDS.toMillis(5);

    private MainApplication mainApplication;
    private Disposable timerDisposable;
    private long time = TIME_FOR_ANSWER;
    private CountryEntity selectedCountry;

    private final MediatorLiveData<List<CountryEntity>> observableCountries;
    private final MediatorLiveData<List<CountryEntity>> observableCountriesInRegion;
    private final MutableLiveData<QuestionData> observableQuestionData;
    private final MutableLiveData<Integer> observableTotalAnswers;
    private final MutableLiveData<Integer> observableCorrectAnswers;
    private final MutableLiveData<Boolean> observableFreezeUi;
    private final MutableLiveData<Long> observableRemainingTime;

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

        observableQuestionData = new MutableLiveData<>();

        // Answer statistics
        observableTotalAnswers = new MutableLiveData<>();
        observableTotalAnswers.setValue(0);
        observableCorrectAnswers = new MutableLiveData<>();
        observableCorrectAnswers.setValue(0);

        observableFreezeUi = new MutableLiveData<>();
        observableFreezeUi.setValue(false);

        // Timer - remaining time
        observableRemainingTime = new MutableLiveData<>();
    }

    public void selectNextCountry() {
        if (getCountries().getValue() == null) {
            Log.e(TAG, "No countries available!");
            return;
        }
        int randomNumber = ThreadLocalRandom.current().nextInt(0, getCountries().getValue().size());
        CountryEntity country = getCountries().getValue().get(randomNumber);
        selectedCountry = country;

        LiveData<List<CountryEntity>> countriesInRegion = mainApplication
                .getRepository()
                .getCountriesInRegion(country.getRegion());

        observableCountriesInRegion.addSource(countriesInRegion, observableCountriesInRegion::setValue);
    }

    public void onOptionSelected(AnswerOption option) {
        increaseIntegerLiveDataValue(observableTotalAnswers);

        QuestionData data = observableQuestionData.getValue();
        if (data != null) {
            data.setAnswered(true);
            observableQuestionData.postValue(data);
        }

        if (option != null) {
            option.setSelectedAsAnswer(true);
        }

        showFeedbackOnUi(() -> {
            selectNextCountry();
            time = TIME_FOR_ANSWER;
            startTimer();
        });
    }

    public void updateOptions(List<CountryEntity> countriesInRegion) {

        if (selectedCountry == null) {
            return;
        }

        countriesInRegion.remove(selectedCountry);
        CountryEntity countryEntity1 = CommonUtil.getInstance().getRandomItem(countriesInRegion);
        countriesInRegion.remove(countryEntity1);
        CountryEntity countryEntity2 = CommonUtil.getInstance().getRandomItem(countriesInRegion);
        countriesInRegion.remove(countryEntity2);
        CountryEntity countryEntity3 = CommonUtil.getInstance().getRandomItem(countriesInRegion);

        int randomCorrectPosition = ThreadLocalRandom.current().nextInt(0, 4);
        QuestionData questionData;

        AnswerOption option = new AnswerOption(selectedCountry.getCapital(), true);
        AnswerOption option1 = new AnswerOption(countryEntity1.getCapital(), false);
        AnswerOption option2 = new AnswerOption(countryEntity2.getCapital(), false);
        AnswerOption option3 = new AnswerOption(countryEntity3.getCapital(), false);

        switch (randomCorrectPosition) {
            case 0:
                questionData = new QuestionData(selectedCountry, option, option1, option2, option3);
                break;
            case 1:
                questionData = new QuestionData(selectedCountry, option3, option, option1, option2);
                break;
            case 2:
                questionData = new QuestionData(selectedCountry, option2, option3, option, option1);
                break;
            default:
                questionData = new QuestionData(selectedCountry, option1, option2, option3, option);
        }

        observableQuestionData.setValue(questionData);
    }

    private void increaseIntegerLiveDataValue(MutableLiveData<Integer> liveData) {
        if (liveData.getValue() == null) {
            return;
        }
        liveData.postValue(liveData.getValue() + 1);
    }

    private void showFeedbackOnUi(Action doAfterDelay) {
        if (timerDisposable != null) {
            timerDisposable.dispose();
        }
        observableFreezeUi.setValue(true);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            try {
                doAfterDelay.run();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage(), e);
            } finally {
                observableFreezeUi.setValue(false);
            }
        }, 1000);
    }

    public void startTimer() {
        int delay = 25;
        observableRemainingTime.setValue(TIME_FOR_ANSWER);
        timerDisposable = Observable.timer(delay, TimeUnit.MILLISECONDS, Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .repeatUntil(() -> time <= 0)
                .subscribe(tick -> {
                            time = time - delay;
                            observableRemainingTime.setValue(time);
                        },
                        Throwable::printStackTrace,
                        () -> onOptionSelected(null)
                );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        timerDisposable.dispose();
    }

    // LiveData getters
    public LiveData<List<CountryEntity>> getCountries() {
        return observableCountries;
    }

    public LiveData<List<CountryEntity>> getCountriesInRegion() {
        return observableCountriesInRegion;
    }

    public LiveData<QuestionData> getQuestionData() {
        return observableQuestionData;
    }

    public LiveData<Integer> getTotalAnswers() {
        return observableTotalAnswers;
    }

    public LiveData<Integer> getCorrectAnswers() {
        return observableCorrectAnswers;
    }

    public MutableLiveData<Boolean> getFreezeUi() {
        return observableFreezeUi;
    }

    public MutableLiveData<Long> getRemainingTime() {
        return observableRemainingTime;
    }
}
