package muchbeer.raum.githubsearch.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;

import java.util.List;

import io.reactivex.schedulers.Schedulers;
import muchbeer.raum.githubsearch.api.RepoNetwork;
import muchbeer.raum.githubsearch.db.RepoDatabase;
import muchbeer.raum.githubsearch.model.Repo;
import muchbeer.raum.githubsearch.repository.paging.LocalDataSourcePageKey;
import muchbeer.raum.githubsearch.repository.paging.RemoteDataSourceFactory;

public class GitRepository implements GithubInterface {


    private static final String TAG = GitRepository.class.getSimpleName();
    private static GitRepository instance;
    final private RepoNetwork network;
    final private RepoDatabase database;
    final private MediatorLiveData liveDataMerger;
    MediatorLiveData<String> mRepoErrorMerger = new MediatorLiveData<>();


    public GitRepository(Context context) {

        RemoteDataSourceFactory netDataSourceFactory = new RemoteDataSourceFactory();
        network = new RepoNetwork(netDataSourceFactory, boundaryCallback);
        database = RepoDatabase.getINSTANCE(context.getApplicationContext());
        LocalDataSourcePageKey localDataSourceFactory2 = new LocalDataSourcePageKey(database.movieDao());

        // when we get new movies from net we set them into the database
        liveDataMerger = new MediatorLiveData<>();

        liveDataMerger.addSource(network.getPagedRepoByPaging(), onlineValue -> {
            liveDataMerger.setValue(onlineValue);
            Log.d(TAG, "The main repository online collected is: " + onlineValue.toString());
        });

        mRepoErrorMerger.addSource(network.getmLocalError(), onlineErrorValue -> {
            liveDataMerger.setValue(onlineErrorValue);
            Log.d(TAG, onlineErrorValue.toString());
        });

        mRepoErrorMerger.addSource(localDataSourceFactory2.getErrorStream(), new Observer<String>() {
            @Override
            public void onChanged(String errorString) {
                mRepoErrorMerger.setValue(errorString);
            }
        });

        // save the movies into db
        netDataSourceFactory.getMoviesPaging().
                observeOn(Schedulers.io()).
                subscribe(repos -> {

                    LocalDataSourcePageKey localDataSourceFactory = new LocalDataSourcePageKey(database.movieDao());
                    localDataSourceFactory.insertReposOnline2Local(repos);
                    //  database.movieDao().insertMoviePaging(movie);
                });

    }

    private PagedList.BoundaryCallback<Repo> boundaryCallback = new PagedList.BoundaryCallback<Repo>() {
        @Override
        public void onZeroItemsLoaded() {
            super.onZeroItemsLoaded();
            liveDataMerger.addSource(database.getAllReposPagingLocal(), mlocalValue -> {
                liveDataMerger.setValue(mlocalValue);
                liveDataMerger.removeSource(database.getAllReposPagingLocal());
            });
        }
    };

    public static GitRepository getInstance(Context context){
        if(instance == null){
            instance = new GitRepository(context);
        }
        return instance;
    }

    @Override
    public LiveData<PagedList<Repo>> getReposDataPaging() {
        return liveDataMerger;
    }

    @Override
    public LiveData<String> getErrorStream() {
        return mRepoErrorMerger;
    }

    @Override
    public void fetchData() {

    }
}
