package app.popularmoviestage.project;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidmvp.bondol.project1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/***
 * @author TUON BONDOL on 5/23/17.
 *
 */

class PopularMovieStageAdapter extends RecyclerView.Adapter<PopularMovieStageAdapter.ListMovieViewHolder> {

    static final String BASE_HOST_IMAGE = "http://image.tmdb.org/t/p/";
    static final String IMAGE_WIDTH = "w185/";
    private ItemClickListener mItemClickListener;

    private Context mContext;
    private List<Result> mResultList;

    public PopularMovieStageAdapter(Context myContext, ItemClickListener listener){
        mContext = myContext;
        mItemClickListener = listener;
    }

    @Override
    public ListMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListMovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ListMovieViewHolder holder, final int position) {
        final Result mResultModel = mResultList.get(position);
        Picasso.with(mContext).load(BASE_HOST_IMAGE + IMAGE_WIDTH + mResultModel.getPosterPath()).into(holder.mItemRowImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener != null){
                    mItemClickListener.onItemClickCallback(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mResultList == null){
            return 0;
        }
        return mResultList.size();
    }

    class ListMovieViewHolder extends RecyclerView.ViewHolder{

        ImageView mItemRowImageView;

        ListMovieViewHolder(View itemView) {
            super(itemView);

            mItemRowImageView = (ImageView) itemView.findViewById(R.id.iv_image_item);
        }
    }

    public void setData(List<Result> myResultList){
        mResultList = myResultList;
        notifyDataSetChanged();
    }
}
