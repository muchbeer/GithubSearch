package muchbeer.raum.githubsearch.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import muchbeer.raum.githubsearch.R;
import muchbeer.raum.githubsearch.databinding.RepoListItemBinding;
import muchbeer.raum.githubsearch.model.Repo;

public class RepoAdapter extends PagedListAdapter<Repo, RepoAdapter.RepoViewHolder> {

    private Context mcontext;
    private static String LOG_TAG = RepoAdapter.class.getSimpleName();


    public RepoAdapter(@NonNull Context context) {
        super(Repo.DIFF_CALLBACK);
        this.mcontext = context;
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RepoListItemBinding repoListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.repo_list_item,
                parent,
                false);
        return new RepoViewHolder(repoListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        Repo repo = getItem(position);
        Log.d(LOG_TAG, "The movie items are: " + repo);
        holder.repoListItemBinding.setRepo(repo);
    }

    public class RepoViewHolder extends RecyclerView.ViewHolder {

       private RepoListItemBinding repoListItemBinding;
        public RepoViewHolder(@NonNull RepoListItemBinding repoItemBinding) {
            super(repoItemBinding.getRoot());

            this.repoListItemBinding = repoItemBinding;

            repoItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        //  Movie selctedMovie = movieArrayList.get(position);

                        //paging here
                        Repo selctedRepo = getItem(position);
                        Log.d(LOG_TAG, "The item Selected is : " + selctedRepo);
                       /* Intent intent=new Intent(mcontext, MovieActivity.class);
                        intent.putExtra("movie",selctedMovie);
                        mcontext.startActivity(intent);*/
                    }
                }
            });
        }
    }
}
