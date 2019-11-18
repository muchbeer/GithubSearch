package muchbeer.raum.githubsearch.repository.paging;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.subjects.ReplaySubject;
import muchbeer.raum.githubsearch.api.RepoDataService;
import muchbeer.raum.githubsearch.api.RetroInstance;
import muchbeer.raum.githubsearch.model.Repo;
import muchbeer.raum.githubsearch.model.RepoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RemoteDataSourcePageKey extends PageKeyedDataSource<Long, Repo> {

    private static final String LOG_TAG = RemoteDataSourcePageKey.class.getSimpleName();;
    private final RepoDataService repoDataService;
    private MutableLiveData<String> mError=new MutableLiveData<>();
    private final ReplaySubject<Repo> reposObservable;


    public RemoteDataSourcePageKey() {

        repoDataService = RetroInstance.getService();
        mError = new MutableLiveData<>();
        reposObservable = ReplaySubject.create();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, Repo> callback) {

        Call<RepoResponse> call = repoDataService.getPopularRepositories("android");

        call.enqueue(new Callback<RepoResponse>() {
            @Override
            public void onResponse(Call<RepoResponse> call, Response<RepoResponse> response) {

                RepoResponse repoDbResponse = response.body();
                if (repoDbResponse != null && repoDbResponse.getRepos() != null) {

                    // moview = (ArrayList<Movie>) movieDbResponse.getMovies();
                    Log.d(LOG_TAG, "All the repos listed as : "+ repoDbResponse.getRepos());

                    // mDataApi.setValue(moview);

                    callback.onResult(repoDbResponse.getRepos(), (long)1, (long)2);
                 //   networkState.postValue(NetworkState.LOADED);
                    repoDbResponse.getRepos().forEach(reposObservable::onNext);
                } else {
                    Log.e("API CALL FAILURE: ", response.toString());
                   // networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.toString()));
                    mError.postValue(response.toString());

                }
            }

            @Override
            public void onFailure(Call<RepoResponse> call, Throwable response) {

                String errorMessage;
                if (response.getMessage() == null) {
                    errorMessage = "unknown error";
                    mError.postValue(errorMessage);
                  //  networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    Log.d(LOG_TAG, "The error catched is as follows : "+ errorMessage);


                } else {
                    errorMessage = response.getMessage();
                    mError.postValue(errorMessage);
                    Log.d(LOG_TAG, "The error catched is as follows : "+ errorMessage);
                   // networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));

                }



                callback.onResult(new ArrayList<>(), (long) 1, (long) 1);
            }
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Repo> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Repo> callback) {

        Log.i(LOG_TAG, "Loading page " + params.key );
       // networkState.postValue(NetworkState.LOADING);
        final AtomicInteger page = new AtomicInteger(0);
        try {
            page.set(Integer.parseInt(String.valueOf(params.key)));
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        Call<RepoResponse> call = repoDataService.getPopularRepositories("android");
        call.enqueue(new Callback<RepoResponse>() {
            @Override
            public void onResponse(Call<RepoResponse> call, Response<RepoResponse> response) {

                RepoResponse repoDbResponse = response.body();
                if (repoDbResponse != null && repoDbResponse.getRepos() != null) {

                    // moview = (ArrayList<Movie>) movieDbResponse.getMovies();
                    Log.d(LOG_TAG, "All the movies listed as : "+ repoDbResponse.getRepos());

                    // mDataApi.setValue(moview);

                    callback.onResult(repoDbResponse.getRepos(), params.key+1);
                   // networkState.postValue(NetworkState.LOADED);
                    repoDbResponse.getRepos().forEach(reposObservable::onNext);
                } else {
                    Log.e("API CALL FAILURE: ", response.toString());
                 //   networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.toString()));
                    mError.postValue(response.toString());
                }
            }

            @Override
            public void onFailure(Call<RepoResponse> call, Throwable response) {
                String errorMessage;
                if (response.getMessage() == null) {
                    errorMessage = "unknown error";
                    mError.postValue(errorMessage);
                    Log.d(LOG_TAG, "The error catched is as follows : "+ errorMessage);
                  //  networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                } else {
                    errorMessage = response.getMessage();
                    mError.postValue(errorMessage);
                    Log.d(LOG_TAG, "The error catched is as follows : "+ errorMessage);
                  //  networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                }

                callback.onResult(new ArrayList<>(), (long)(page.get()));
                //  mError.postValue(response.toString());

            }
        });
    }

    public MutableLiveData<String> getErrorStream() {
        return mError;
    }


    public ReplaySubject<Repo> getMoviesReplay() {
        return reposObservable;
    }


}
