package eu.micer.capitalcitieslearning.repository.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import eu.micer.capitalcitieslearning.repository.db.entity.CountryEntity;

@Dao
public interface CountryDao {
    @Query("SELECT * FROM countries")
    LiveData<List<CountryEntity>> loadAllCountries();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CountryEntity> countries);

    @Query("select * from countries where id = :countryId")
    LiveData<CountryEntity> loadCountry(int countryId);

    @Query("select * from countries where id = :countryId")
    CountryEntity loadCountrySync(int countryId);

    @Query("select * from countries where region = :region")
    LiveData<List<CountryEntity>> getCountriesInRegion(String region);
}
