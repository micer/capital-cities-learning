package eu.micer.capitalcitieslearning.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import java.util.List;

import eu.micer.capitalcitieslearning.repository.db.AppDatabase;
import eu.micer.capitalcitieslearning.repository.db.entity.CountryEntity;

public class DataRepository {
    private static DataRepository instance;

    private final AppDatabase database;
    private MediatorLiveData<List<CountryEntity>> observableCountries;

    private DataRepository(final AppDatabase database) {
        this.database = database;
        observableCountries = new MediatorLiveData<>();

        observableCountries.addSource(this.database.countryDao().loadAllCountries(),
                countryEntities -> {
                    if (this.database.getDatabaseCreated().getValue() != null) {
                        observableCountries.postValue(countryEntities);
                    }
                });
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (instance == null) {
            synchronized (DataRepository.class) {
                if (instance == null) {
                    instance = new DataRepository(database);
                }
            }
        }
        return instance;
    }

    public LiveData<List<CountryEntity>> getCountries() {
        return observableCountries;
    }

    public LiveData<CountryEntity> loadCountry(final int countryId) {
        return database.countryDao().loadCountry(countryId);
    }

    public LiveData<List<CountryEntity>> getCountriesInRegion(String region) {
        return database.countryDao().getCountriesInRegion(region);
    }
}
