package muchbeer.raum.githubsearch.api;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import muchbeer.raum.githubsearch.model.Repo;
import muchbeer.raum.githubsearch.repository.paging.RemoteDataSourceFactory;

public class RepoNetwork {

    public static final int LOADING_PAGE_SIZE = 20;

    final private static String TAG = RepoNetwork.class.getSimpleName();
    private static final int NUMBERS_OF_THREADS = 4;
    final private LiveData<PagedList<Repo>> moviesPaged;
    //   final private LiveData<NetworkState> networkState;
    private LiveData<String> mLocalError;

    public RepoNetwork(RemoteDataSourceFactory dataSourceFactory, PagedList.BoundaryCallback<Repo> boundaryCallback){

        PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(LOADING_PAGE_SIZE)
                .setPageSize(LOADING_PAGE_SIZE).build();

        /*networkState = Transformations.switchMap(dataSourceFactory.getNetworkStatus(),
                (Function<RemoteDataSourcePageKey,
                        LiveData<NetworkState>>)
                        RemoteDataSourcePageKey::getNetworkState);*/

        Executor executor = Executors.newFixedThreadPool(NUMBERS_OF_THREADS);

        LivePagedListBuilder livePagedListBuilder = new LivePagedListBuilder(dataSourceFactory, pagedListConfig);
        moviesPaged =       livePagedListBuilder.
                setFetchExecutor(executor).
                setBoundaryCallback(boundaryCallback).
                build();

        mLocalError = dataSourceFactory.getErrorMessage();
    }
    public LiveData<PagedList<Repo>> getPagedRepoByPaging(){
        Log.d(TAG, "tHE data collected is : " +  moviesPaged);
        return moviesPaged;
    }

    public LiveData<String>  getmLocalError() {
        return mLocalError;
    }


}
