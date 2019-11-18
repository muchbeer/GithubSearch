package muchbeer.raum.githubsearch.db;

import android.graphics.Movie;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
import muchbeer.raum.githubsearch.model.Repo;

@Dao
public interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRepos(Repo repos);

    @Query("SELECT * FROM repos")
    LiveData<List<Repo>> getAllRepos();

    @Query("SELECT * FROM repos")
    List<Repo> getAllReposPageKey();


    @Query("SELECT * FROM repos WHERE name LIKE :title")
    DataSource.Factory<Integer, Repo> getSearchedRepo(String title);

}
