package muchbeer.raum.githubsearch.db;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import muchbeer.raum.githubsearch.model.Repo;
import muchbeer.raum.githubsearch.repository.paging.LocalDataSourceFactory;

@Database(entities = {Repo.class},version = 1, exportSchema = false)
public abstract class RepoDatabase extends RoomDatabase {

    private static final String LOG_TAG = RepoDatabase.class.getSimpleName();
    private LiveData<PagedList<Repo>> reposPaged;
    // public LiveData<PagedList<TeamObject>> teamAllList;
    public MutableLiveData<String> filterAllRepo = new MutableLiveData<>();
    public LiveData getFilterRepos = new MutableLiveData<>();

    static final String DATABASE_NAME = "repo_db";
    private static final int NUMBERS_OF_THREADS = 4;
    private static RepoDatabase INSTANCE;


    public abstract RepoDao movieDao();

    public static synchronized RepoDatabase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                    RepoDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    //  .addMigrations(MIGRATION_1_TO_2)
                    //  .addCallback(callback)
                    .build();
            INSTANCE.initAllRepos(INSTANCE.movieDao());
        }
        return INSTANCE;
    }

    private void initAllRepos(RepoDao repoDao) {


        PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
                /*.setEnablePlaceholders(false)
                .setInitialLoadSizeHint(Integer.MAX_VALUE)*/
                .setPageSize(10).build();

        Executor executor = Executors.newFixedThreadPool(NUMBERS_OF_THREADS);


        reposPaged = Transformations.switchMap(filterAllRepo, input -> {
            if (input == null || input.equals("") || input.equals("%%")) {
//check if the current value is empty load all data else search


                LocalDataSourceFactory dataSourceFactory = new LocalDataSourceFactory(movieDao());
                LivePagedListBuilder livePagedListBuilder = new LivePagedListBuilder(dataSourceFactory, pagedListConfig);
                // reposPaged = livePagedListBuilder.setFetchExecutor(executor).build();

                return livePagedListBuilder.setFetchExecutor(executor).build();

                        /*new LivePagedListBuilder<>(
                        repoDao.getAllRepos(), pagedListConfig)
                        .build();*/
            } else {
                Log.d(LOG_TAG, "The display output is : " + input);

                //   LocalDataSourceFactory dataSourceFactory = new LocalDataSourceFactory(movieDao());
                LivePagedListBuilder livePagedListBuilder =
                        new LivePagedListBuilder<Integer, Repo>(repoDao.getSearchedRepo(input), pagedListConfig);
                //  getFilterRepos = livePagedListBuilder.setFetchExecutor(executor).build();



                return livePagedListBuilder.build();

                        /*new LivePagedListBuilder<>(
                                repoDao.getSearchedRepo(input), pagedListConfig)
                                .build();*/
            }

        });


    }
    public LiveData<PagedList<Repo>> getAllReposPagingLocal() {
        return reposPaged;
    }
}
