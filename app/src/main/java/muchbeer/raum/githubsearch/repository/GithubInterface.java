package muchbeer.raum.githubsearch.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import muchbeer.raum.githubsearch.model.Repo;

public interface GithubInterface {

    LiveData<PagedList<Repo>> getReposDataPaging();
    LiveData<String> getErrorStream();
    // LiveData<Double> getTotalMarketCapStream();
    void fetchData();
}
