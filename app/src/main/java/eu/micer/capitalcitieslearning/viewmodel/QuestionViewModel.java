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
import eu.micer.capitalcitieslearning.model.AnswerOptions;
import eu.micer.capitalcitieslearning.repository.db.entity.CountryEntity;
import eu.micer.capitalcitieslearning.util.CommonUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class QuestionViewModel extends AndroidViewModel {
    private static final String TAG = QuestionViewModel.class.getSimpleName();
    private static final long TIME_FOR_ANSWER = TimeUnit.SECONDS.toMillis(2);

    private MainApplication mainApplication;
    private Disposable timerDisposable;

    private final MediatorLiveData<List<CountryEntity>> observableCountries;
    private final MutableLiveData<CountryEntity> observableSelectedCountry;
    private final MediatorLiveData<List<CountryEntity>> observableCountriesInRegion;
    private final MutableLiveData<AnswerOptions> observableAnswerOptions;
    private final MutableLiveData<Integer> observableTotalAnswers;
    private final MutableLiveData<Integer> observableCorrectAnswers;
    private final MutableLiveData<Boolean> observableAnswerWasCorrect;
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

        observableSelectedCountry = new MutableLiveData<>();
        observableAnswerOptions = new MutableLiveData<>();

        // Answer statistics
        observableTotalAnswers = new MutableLiveData<>();
        observableTotalAnswers.setValue(0);
        observableCorrectAnswers = new MutableLiveData<>();
        observableCorrectAnswers.setValue(0);

        observableAnswerWasCorrect = new MutableLiveData<>();
        observableAnswerWasCorrect.setValue(null);

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
        observableSelectedCountry.setValue(country);

        LiveData<List<CountryEntity>> countriesInRegion = mainApplication
                .getRepository()
                .getCountriesInRegion(country.getRegion());

        observableCountriesInRegion.addSource(countriesInRegion, observableCountriesInRegion::setValue);
    }

    /**
     *
     * @param option options 1-4 or -1 when time's up = no answer
     */
    public void onOptionSelected(int option) {
        increaseIntegerLiveDataValue(observableTotalAnswers);
        boolean answerIsCorrect;

        if (option == AnswerOptions.OPTION_TIME_IS_UP) {
            answerIsCorrect = false;
        } else {
            if (observableAnswerOptions.getValue() != null
                    && observableAnswerOptions.getValue().getCorrectOption() == option) {
                answerIsCorrect = true;
                Log.d(TAG, "Answer is correct!");
                increaseIntegerLiveDataValue(observableCorrectAnswers);
            } else {
                answerIsCorrect = false;
                Log.d(TAG, "Answer is incorrect!");
            }
        }

        showFeedbackOnUi(answerIsCorrect, () -> {
            selectNextCountry();
            startTimer();
        });
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

    private void increaseIntegerLiveDataValue(MutableLiveData<Integer> liveData) {
        if (liveData.getValue() == null) {
            return;
        }
        liveData.postValue(liveData.getValue() + 1);
    }

    private void showFeedbackOnUi(boolean answerIsCorrect, Action doAfterDelay) {
        if (timerDisposable != null) {
            timerDisposable.dispose();
        }
        observableFreezeUi.setValue(true);
        observableAnswerWasCorrect.postValue(answerIsCorrect);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            observableAnswerWasCorrect.postValue(null);
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
        observableRemainingTime.setValue(TIME_FOR_ANSWER);
        timerDisposable = Observable.timer(1, TimeUnit.MILLISECONDS, Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .repeatUntil(() -> observableRemainingTime.getValue() == null || observableRemainingTime.getValue() <= 0)
                .subscribe(tick -> {
                    long time;
                    if (observableRemainingTime.getValue() == null) {
                        time = TIME_FOR_ANSWER;
                    } else {
                        time = observableRemainingTime.getValue() - 1;
                    }
                    observableRemainingTime.setValue(time);
                }, Throwable::printStackTrace, () -> {
                    onOptionSelected(-1);
                });
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

    public LiveData<CountryEntity> getSelectedCountry() {
        return observableSelectedCountry;
    }

    public LiveData<List<CountryEntity>> getCountriesInRegion() {
        return observableCountriesInRegion;
    }

    public LiveData<AnswerOptions> getAnswerOptions() {
        return observableAnswerOptions;
    }

    public LiveData<Integer> getTotalAnswers() {
        return observableTotalAnswers;
    }

    public LiveData<Integer> getCorrectAnswers() {
        return observableCorrectAnswers;
    }

    public MutableLiveData<Boolean> getAnswerWasCorrect() {
        return observableAnswerWasCorrect;
    }

    public MutableLiveData<Boolean> getFreezeUi() {
        return observableFreezeUi;
    }

    public MutableLiveData<Long> getRemainingTime() {
        return observableRemainingTime;
    }
}
