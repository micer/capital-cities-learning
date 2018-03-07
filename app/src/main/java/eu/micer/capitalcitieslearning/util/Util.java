package eu.micer.capitalcitieslearning.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.micer.capitalcitieslearning.model.gson.Country;
import eu.micer.capitalcitieslearning.repository.db.entity.CountryEntity;

public class Util {
    private static final String TAG = Util.class.getSimpleName();
    private static Util instance;

    public static Util getInstance() {
        if (instance == null) {
            synchronized (Util.class) {
                if (instance == null) {
                    instance = new Util();
                }
            }
        }
        return instance;
    }

    public List<CountryEntity> getCountriesFromJsonFile(Context appContext) {
        String myJson = null;
        try {
            myJson = readFile(appContext, "countries.json");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        List<Country> countryGsonList = Arrays.asList(new Gson().fromJson(myJson, Country[].class));

        return convertCountryGsonListToCountryEntityList(countryGsonList);
    }

    private String readFile(@NonNull Context context, String fileName) throws IOException {
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName), "UTF-8"));

        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }

        return content.toString();
    }

    private List<CountryEntity> convertCountryGsonListToCountryEntityList(List<Country> countryGsonList) {
        List<CountryEntity> countryEntityList = new ArrayList<>();
        for (int i = 0; i < countryGsonList.size(); i++) {
            Country countryGson = countryGsonList.get(i);
            CountryEntity countryEntity = new CountryEntity();

            countryEntity.setId(i);

            countryEntity.setArea(countryGson.getArea());

            String name;
            if (countryGson.getName() == null
                    || countryGson.getName().getCommon() == null
                    && countryGson.getName().getOfficial() == null) {
                continue;
            }
            if (countryGson.getName().getCommon() == null) {
                name = countryGson.getName().getOfficial();
            } else {
                name = countryGson.getName().getCommon();
            }
            countryEntity.setName(name);

            String capital = null;
            if (!isNullOrEmpty(countryGson.getCapital())) {
                capital = countryGson.getCapital().get(0);
            }
            if (Util.getInstance().isNullOrEmpty(capital)) {
                // This applies i.e. for Macao
                capital = name;
            }
            countryEntity.setCapital(capital);

            countryEntity.setCca2Code(countryGson.getCca2());
            countryEntity.setRegion(countryGson.getRegion());
            countryEntity.setSubregion(countryGson.getSubregion());

            countryEntityList.add(countryEntity);
        }
        return countryEntityList;
    }

    public boolean isNullOrEmpty(@Nullable List list) {
        return list == null || list.isEmpty();
    }

    public boolean isNullOrEmpty(@Nullable String string) {
        return string == null || string.isEmpty();
    }
}
