package eu.micer.capitalcitieslearning.repository.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.List;

import eu.micer.capitalcitieslearning.repository.db.dao.CountryDao;
import eu.micer.capitalcitieslearning.repository.db.entity.CountryEntity;
import eu.micer.capitalcitieslearning.util.Util;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

@Database(entities = {CountryEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "ccl-db";

    public abstract CountryDao countryDao();

    private final MutableLiveData<Boolean> isDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = buildDatabase(context.getApplicationContext());
                    instance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Completable.create(e -> {
                            // Generate the data for pre-population
                            AppDatabase database = AppDatabase.getInstance(appContext);
                            List<CountryEntity> countries = Util.getInstance().getCountriesFromJsonFile(appContext);

                            insertData(database, countries);
                            // notify that the database was created and it's ready to be used
                            database.setDatabaseCreated();
                        }).subscribeOn(Schedulers.io())
                                .subscribe();
                    }
                }).build();
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated() {
        isDatabaseCreated.postValue(true);
    }

    private static void insertData(final AppDatabase database, final List<CountryEntity> countries) {
        database.runInTransaction(() -> database.countryDao().insertAll(countries));
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return isDatabaseCreated;
    }
}
