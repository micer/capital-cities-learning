package eu.micer.capitalcitieslearning.db;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import eu.micer.capitalcitieslearning.LiveDataTestUtil;
import eu.micer.capitalcitieslearning.repository.db.AppDatabase;
import eu.micer.capitalcitieslearning.repository.db.dao.CountryDao;
import eu.micer.capitalcitieslearning.repository.db.entity.CountryEntity;

import static eu.micer.capitalcitieslearning.db.TestData.COUNTRIES;
import static eu.micer.capitalcitieslearning.db.TestData.COUNTRY_ENTITY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Test the implementation of {@link CountryDao}
 */
@RunWith(AndroidJUnit4.class)
public class CountryDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase database;
    private CountryDao countryDao;

    @Before
    public void initDb() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();

        countryDao = database.countryDao();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void getCountriesWhenNoProductInserted() throws InterruptedException {
        List<CountryEntity> products = LiveDataTestUtil.getValue(countryDao.loadAllCountries());

        assertTrue(products.isEmpty());
    }

    @Test
    public void getProductsAfterInserted() throws InterruptedException {
        countryDao.insertAll(COUNTRIES);

        List<CountryEntity> countries = LiveDataTestUtil.getValue(countryDao.loadAllCountries());

        assertThat(countries.size(), is(COUNTRIES.size()));
    }

    @Test
    public void getProductById() throws InterruptedException {
        countryDao.insertAll(COUNTRIES);

        CountryEntity product = LiveDataTestUtil.getValue(countryDao.loadCountry(
                (COUNTRY_ENTITY.getId())));

        assertThat(product.getId(), is(COUNTRY_ENTITY.getId()));
        assertThat(product.getName(), is(COUNTRY_ENTITY.getName()));
        assertThat(product.getCca2Code(), is(COUNTRY_ENTITY.getCca2Code()));
        assertThat(product.getCapital(), is(COUNTRY_ENTITY.getCapital()));
        assertThat(product.getRegion(), is(COUNTRY_ENTITY.getRegion()));
        assertThat(product.getSubregion(), is(COUNTRY_ENTITY.getSubregion()));
    }
}
