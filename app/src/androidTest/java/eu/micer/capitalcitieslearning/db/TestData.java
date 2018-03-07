package eu.micer.capitalcitieslearning.db;


import java.util.Arrays;
import java.util.List;

import eu.micer.capitalcitieslearning.repository.db.entity.CountryEntity;

/**
 * Utility class that holds values to be used for testing.
 */
public class TestData {

    static final CountryEntity COUNTRY_ENTITY = new CountryEntity(1, "CZ", "Czech Republic", "Prague", "Europe", "", 78000);
    static final CountryEntity COUNTRY_ENTITY2 = new CountryEntity(2, "DE", "Germany", "Berlin", "Europe", "", 280000);

    static final List<CountryEntity> COUNTRIES = Arrays.asList(COUNTRY_ENTITY, COUNTRY_ENTITY2);
}