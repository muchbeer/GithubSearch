package muchbeer.raum.githubsearch.repository.paging;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import io.reactivex.subjects.ReplaySubject;
import muchbeer.raum.githubsearch.model.Repo;

public class RemoteDataSourceFactory extends DataSource.Factory<Long, Repo> {

    private static final String TAG = RemoteDataSourceFactory.class.getSimpleName();
   // private MutableLiveData<RemoteDataSourcePageKey> networkStatus;
    private MutableLiveData<String> mError;
    private RemoteDataSourcePageKey remoteDataSourcePageKey;

    public RemoteDataSourceFactory() {

     //   this.networkStatus=new MutableLiveData<>();
        this.mError = new MutableLiveData<>();
        remoteDataSourcePageKey = new RemoteDataSourcePageKey();
    }

    @NonNull
    @Override
    public DataSource<Long, Repo> create() {
       // networkStatus.postValue(moviesPageKeyedDataSource);
        // mError.postValue(moviesPageKeyedDataSource);
        return remoteDataSourcePageKey;
    }

    public MutableLiveData<String> getErrorMessage() {
        return remoteDataSourcePageKey.getErrorStream();
    }
    public ReplaySubject<Repo> getMoviesPaging() {
        return remoteDataSourcePageKey.getMoviesReplay();
    }
}
