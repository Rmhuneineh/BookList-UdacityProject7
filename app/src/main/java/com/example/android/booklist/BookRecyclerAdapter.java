package com.example.android.booklist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by rmhuneineh on 08/05/2017.
 */

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.ViewHolder> {

    private String mBookName;
    private String mBookAuthor;
    private double mBookRating;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mItem;
        public ViewHolder(View itemView) {
            super(itemView);
            mItem = itemView;
        }
    }

    public BookRecyclerAdapter(String bookName, String bookAuthor, double bookRating) {
        this.mBookName = bookName;
        this.mBookAuthor = bookAuthor;
        this.mBookRating = bookRating;
    }


    @Override
    public BookRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        ViewHolder vh = new ViewHolder(listItem);
        return vh;
    }

    @Override
    public void onBindViewHolder(BookRecyclerAdapter.ViewHolder holder, int position) {
        TextView title = (TextView) holder.itemView.findViewById(R.id.title_text_view);
        title.setText(mBookName);

        TextView author = (TextView) holder.itemView.findViewById(R.id.author_text_view);
        author.setText(mBookAuthor);

        TextView rating = (TextView) holder.mItem.findViewById(R.id.rating_text_view);
        String ratingText = String.valueOf(mBookRating);
        rating.setText(ratingText);

        RatingBar progressBar = (RatingBar) holder.mItem.findViewById(R.id.rating_bar);
        float averageRating = (float) mBookRating;

        progressBar.setRating(averageRating);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
