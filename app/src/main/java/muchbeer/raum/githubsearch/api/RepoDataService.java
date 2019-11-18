package muchbeer.raum.githubsearch.api;
import muchbeer.raum.githubsearch.model.RepoResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RepoDataService {

    @GET("search/repositories")
    Call<RepoResponse> getPopularRepositories(@Query("q") String queryString);
}

