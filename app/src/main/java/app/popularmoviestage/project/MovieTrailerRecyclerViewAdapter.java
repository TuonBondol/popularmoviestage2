package app.popularmoviestage.project;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidmvp.bondol.project1.R;

import java.util.List;

/***
 * @author TUON BONDOL on 5/23/17.
 *
 */

public class MovieTrailerRecyclerViewAdapter extends RecyclerView.Adapter<MovieTrailerRecyclerViewAdapter.MovieTrailerViewHolder> {

    private ItemClickListener mItemClickListener;
    List<MovieTrailer.Result> movieTrailerList;

    public MovieTrailerRecyclerViewAdapter(List< MovieTrailer.Result> movieList) {
        movieTrailerList = movieList;
    }

    @Override
    public MovieTrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieTrailerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_trailer_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(MovieTrailerViewHolder holder, int position) {
        holder.mTopTitleTextView.setVisibility(View.VISIBLE);
        if (position != 0) {
            holder.mTopTitleTextView.setVisibility(View.GONE);
        }
        holder.mTrailerTextView.setText(movieTrailerList.get(position).getName());
    }

    public void setmItemClickListener(ItemClickListener itemClickListener){
        mItemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return movieTrailerList.size();
    }

    class MovieTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTopTitleTextView;
        private ImageView mPlayImageView;
        private TextView mTrailerTextView;

        MovieTrailerViewHolder(View itemView) {
            super(itemView);

            mTopTitleTextView = (TextView) itemView.findViewById(R.id.tv_trailer_top_title);
            mPlayImageView = (ImageView) itemView.findViewById(R.id.iv_play);
            mTrailerTextView = (TextView) itemView.findViewById(R.id.tv_trailer_title);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                MovieDetailActivity.movieKey = movieTrailerList.get(getAdapterPosition()).getKey();
                mItemClickListener.onItemClickCallback(getAdapterPosition());
            }
        }
    }
}
