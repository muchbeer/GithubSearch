package muchbeer.raum.githubsearch.repository.paging;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import muchbeer.raum.githubsearch.db.RepoDao;
import muchbeer.raum.githubsearch.db.RepoDatabase;

public class LocalDataSourceFactory extends DataSource.Factory {

    private RepoDatabase mDb;
    private static final String TAG = LocalDataSourceFactory.class.getSimpleName();
    private LocalDataSourcePageKey reposPageKeyedDataSource;

    @NonNull
    @Override
    public DataSource create() {
        return reposPageKeyedDataSource;
    }

    public LocalDataSourceFactory(RepoDao repoDao) {
        reposPageKeyedDataSource = new LocalDataSourcePageKey(repoDao);
    }
}
