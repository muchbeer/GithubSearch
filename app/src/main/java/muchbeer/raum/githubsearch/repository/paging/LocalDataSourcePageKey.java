package muchbeer.raum.githubsearch.repository.paging;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import java.util.List;

import muchbeer.raum.githubsearch.db.RepoDao;
import muchbeer.raum.githubsearch.model.Repo;

public class LocalDataSourcePageKey extends PageKeyedDataSource<Long, Repo> {

    public static final String TAG = LocalDataSourcePageKey.class.getSimpleName();
    private final RepoDao repoDao;

    public LocalDataSourcePageKey(RepoDao repoDao) {
        this.repoDao = repoDao;
    }

    private final MutableLiveData<String> mError=new MutableLiveData<>();


    public void insertReposOnline2Local(Repo repos) {
        try {
            repoDao.insertRepos(repos);
        }catch(Exception e)
        {
            e.printStackTrace();
            mError.postValue(e.getMessage());
        }
    }

    public LiveData<String> getErrorStream() {
        return mError;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, Repo> callback) {
        Log.i(TAG, "Loading Initial Rang, Count " + params.requestedLoadSize);
        List<Repo> repos = repoDao.getAllReposPageKey();
        if(repos.size() != 0) {
            callback.onResult(repos, (long)0, (long)1);
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Repo> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Repo> callback) {

    }
}
