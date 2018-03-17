package com.example.android.booklist;


import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Book>> {

    private static final String GOOGLE_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    private static final int BOOK_LOADER_ID = 1;

    @BindView(R.id.state_text_view)
    TextView state;

    @BindView(R.id.book_name)
    EditText bookName;

    @BindView(R.id.progress_bar)
    ProgressBar mLoadingSpinner;

    @BindView(R.id.list_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.search)
    ImageView mSearchIV;

    private RecyclerView.Adapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private boolean isClick;

    static ArrayList<Book> mBooks = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        isClick = false;

        mLayoutManager = new LinearLayoutManager(this);

        if (savedInstanceState==null) {
            mRecyclerView.setVisibility(GONE);
            mRecyclerView.setLayoutManager(mLayoutManager);

            state.setText(getString(R.string.state_empty_view));
            state.setVisibility(View.VISIBLE);
        } else {
            mRecyclerAdapter = new BookRecyclerAdapter(this, mBooks);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mRecyclerAdapter);
        }


        if (!TextUtils.isEmpty(bookName.getText().toString().trim())) {
            updateInfo();
        }

        mLoadingSpinner.setVisibility(GONE);

        mSearchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBooks();
            }
        });

        bookName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    loadBooks();
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (bookName.getText().length() !=0) {
            bookName.setText("");
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!mBooks.isEmpty()) {
            outState.putParcelableArrayList("books", mBooks);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mBooks = savedInstanceState.getParcelableArrayList("books");
        mRecyclerAdapter = new BookRecyclerAdapter(this, mBooks);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        hideSoftKeyboard(this);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void loadBooks() {
        isClick = true;

        final ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        if (isConnected) {

            state.setVisibility(GONE);
            state.setText(getString(R.string.state_empty_view));
            mRecyclerAdapter = new BookRecyclerAdapter(MainActivity.this, new ArrayList<Book>());

            mRecyclerView.setAdapter(mRecyclerAdapter);

            if (!TextUtils.isEmpty(bookName.getText().toString().trim())) {
                updateInfo();
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.toast), Toast.LENGTH_SHORT).show();
                state.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        } else {
            mLoadingSpinner.setVisibility(GONE);
            mRecyclerView.setVisibility(GONE);

            state.setText(getString(R.string.state_no_internet));
            state.setVisibility(View.VISIBLE);
        }
    }

    public void showDetailsDialog(String description) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Html.fromHtml("<font color = '#FF4081'>Book Description</font>"));
        builder.setMessage(description);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateInfo() {
        String title = bookName.getText().toString();
        title = title.replace(" ", "+");
        String uriString = GOOGLE_REQUEST_URL + title;
        Bundle args = new Bundle();
        args.putString("Uri", uriString);
        android.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, args, MainActivity.this);
        if (loaderManager.getLoader(BOOK_LOADER_ID).isStarted()) {
            //restart it if there's one
            getLoaderManager().restartLoader(BOOK_LOADER_ID, args, MainActivity.this);
        }
    }

    @Override
    public android.content.Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        mRecyclerView.setVisibility(GONE);
        mLoadingSpinner.setVisibility(View.VISIBLE);
        return new BookLoader(this, args.getString("Uri"));
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Book>> loader, List<Book> books) {
        hideSoftKeyboard(this);
        mLoadingSpinner.setVisibility(GONE);

        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerAdapter = new BookRecyclerAdapter(MainActivity.this, new ArrayList<Book>());
        if (books != null && !books.isEmpty()) {
            mBooks = (ArrayList<Book>) books;
            mRecyclerAdapter = new BookRecyclerAdapter(MainActivity.this, books);
            mRecyclerView.setAdapter(mRecyclerAdapter);
        } else {
            if (isClick){
                Toast.makeText(MainActivity.this, "Not Found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Book>> loader) {
    }

    public static class BookLoader extends AsyncTaskLoader<List<Book>> {


        private final String mUrl;

        public BookLoader(Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<Book> loadInBackground() {
            if (mUrl == null) {
                return null;
            }

            List<Book> books = Utils.fetchBookData(mUrl);
            return books;
        }
    }
}