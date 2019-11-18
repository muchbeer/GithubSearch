package muchbeer.raum.githubsearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import muchbeer.raum.githubsearch.databinding.ActivityMainBinding;
import muchbeer.raum.githubsearch.model.Repo;
import muchbeer.raum.githubsearch.ui.RepoAdapter;
import muchbeer.raum.githubsearch.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private PagedList<Repo> reposPaging;
    private RecyclerView recyclerView;
    private RepoAdapter reposAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainActivityViewModel mainActivityViewModel;
    private ActivityMainBinding activityMainBinding;
    private final static int DATA_FETCHING_INTERVAL=5*1000; //5 seconds
    private long mLastFetchedDataTimeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().setTitle("Github Android");

        activityMainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        getPopularRepos();

        swipeRefreshLayout = activityMainBinding.swRefresh;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (System.currentTimeMillis() - mLastFetchedDataTimeStamp < DATA_FETCHING_INTERVAL) {
                    Log.d(LOG_TAG, "\tNot fetching from network because interval didn't reach");
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                } else if(System.currentTimeMillis() == DATA_FETCHING_INTERVAL) {
                    Log.d(LOG_TAG, "\tclose the refresh");
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }
                getPopularRepos();
            }
        });
    }
    private void getPopularRepos() {

        mainActivityViewModel.getReposMoviesPaging().observe(this, new Observer<PagedList<Repo>>() {
            @Override
            public void onChanged(PagedList<Repo> reposOnDbandNet) {
                reposPaging = reposOnDbandNet;
                showOnRecyclerView();
                if(reposPaging!=null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });


        mainActivityViewModel.getErrorUpdates().observe(this, errorInfo-> {
            setError(errorInfo);
        });
    }

    private void showErrorToastUpdate(String error) {
        Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
    }

    private void showOnRecyclerView() {

        recyclerView = activityMainBinding.rvMovies;
        //    movieAdapter = new MovieAdapter(this, moviesMain);
//paging here below
        reposAdapter = new RepoAdapter(this);
        reposAdapter.submitList(reposPaging);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {


            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));


        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(reposAdapter);
        reposAdapter.notifyDataSetChanged();
    }

    public void setError(String msg) {
        showErrorToastUpdate(msg);
    }
}
