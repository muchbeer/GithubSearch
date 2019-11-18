package muchbeer.raum.githubsearch.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import muchbeer.raum.githubsearch.db.RepoDatabase;
import muchbeer.raum.githubsearch.model.Repo;
import muchbeer.raum.githubsearch.repository.GitRepository;
import muchbeer.raum.githubsearch.repository.GithubInterface;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String TAG = MainActivityViewModel.class.getSimpleName();
    final private RepoDatabase database;
    private GithubInterface mGithubInterface;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mGithubInterface = GitRepository.getInstance(application);
        database = RepoDatabase.getINSTANCE(application);
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "onCleared() called");
        super.onCleared();
    }
    public LiveData<PagedList<Repo>> getReposMoviesPaging() {
        return mGithubInterface.getReposDataPaging();
    }

    public LiveData<String> getErrorUpdates() {
        return mGithubInterface.getErrorStream();
    }

    public MutableLiveData<String> filterRepoAll() {
        return database.filterAllRepo;
    }


}
