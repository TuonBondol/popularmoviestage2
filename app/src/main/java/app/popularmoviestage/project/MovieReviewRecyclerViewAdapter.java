package app.popularmoviestage.project;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidmvp.bondol.project1.R;

import java.util.List;

/***
 * @author TUON BONDOL on 6/18/17.
 *
 */

class MovieReviewRecyclerViewAdapter extends RecyclerView.Adapter<MovieReviewRecyclerViewAdapter.MovieReviewHolder> {

    private List<MovieReview.Result> mDataList;
    private ItemClickListener mItemClickListener;

    MovieReviewRecyclerViewAdapter(ItemClickListener itemClickListener, List<MovieReview.Result> mDataList) {
        mItemClickListener = itemClickListener;
        this.mDataList = mDataList;
    }

    @Override
    public MovieReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieReviewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_review_custom_row, parent, false));
    }

    @Override
    public void onBindViewHolder(MovieReviewHolder holder, int position) {
        holder.bind(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class MovieReviewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private TextView mContent;

        MovieReviewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.movie_review_author);
            mContent = (TextView) itemView.findViewById(R.id.movie_review_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItemClickListener != null){
                        mItemClickListener.onItemClickCallback(getAdapterPosition());
                    }
                }
            });
        }

        void bind(MovieReview.Result mResult) {
            mTitle.setText(mResult.getAuthor());
            mContent.setText(mResult.getContent());
        }


    }
}
