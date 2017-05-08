package com.example.android.booklist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rmhuneineh on 08/05/2017.
 */

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.ViewHolder> {

    List<Book> mBooks;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView title;
        protected TextView author;
        protected TextView rating;
        protected RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_text_view);
            author = (TextView) itemView.findViewById(R.id.author_text_view);
            rating = (TextView) itemView.findViewById(R.id.rating_text_view);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating_bar);

        }
    }

    public BookRecyclerAdapter(List<Book> books) {
        this.mBooks = books;
    }


    @Override
    public BookRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        ViewHolder vh = new ViewHolder(listItem);
        return vh;
    }

    @Override
    public void onBindViewHolder(BookRecyclerAdapter.ViewHolder holder, int position) {
        Book currentBook = mBooks.get(position);

        holder.title.setText(currentBook.getTitle());

        holder.author.setText(currentBook.getAuthor());

        String ratingText = String.valueOf(currentBook.getRating());
        holder.rating.setText(ratingText);


        float averageRating = (float) currentBook.getRating();
        holder.ratingBar.setRating(averageRating);
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }
}
