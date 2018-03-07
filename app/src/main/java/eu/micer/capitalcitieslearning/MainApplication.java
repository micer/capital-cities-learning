package eu.micer.capitalcitieslearning;

import android.app.Application;

import eu.micer.capitalcitieslearning.repository.DataRepository;
import eu.micer.capitalcitieslearning.repository.db.AppDatabase;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}
