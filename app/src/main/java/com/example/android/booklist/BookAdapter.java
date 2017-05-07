package com.example.android.booklist;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rmhuneineh on 29/04/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    private MainActivity mContext;

    public BookAdapter(Activity context, ArrayList<Book> books){
        super(context, 0, books);
        this.mContext = (MainActivity) context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        final Book currentBook = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_text_view);
        titleTextView.setText(currentBook.getTitle());

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_text_view);
        authorTextView.setText(currentBook.getAuthor());

        TextView ratingTextView = (TextView) listItemView.findViewById(R.id.rating_text_view);
        String rating = String.valueOf(currentBook.getRating());
        ratingTextView.setText(rating);

        float averageRating = (float)currentBook.getRating();

        RatingBar ratingBar = (RatingBar) listItemView.findViewById(R.id.rating_bar);
        ratingBar.setRating(averageRating);

        titleTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mContext.showDetailsDialog(currentBook.getDescription());
                return true;
            }
        });

        return listItemView;
    }
}
