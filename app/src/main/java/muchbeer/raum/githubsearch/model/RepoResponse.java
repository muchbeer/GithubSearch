package muchbeer.raum.githubsearch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RepoResponse {


    @SerializedName("total_count")
    @Expose
    private Integer total;

    @SerializedName("items")
    @Expose
    private List<Repo> Repos = null;

    public List<Repo> getRepos() {
        return Repos;
    }
}
