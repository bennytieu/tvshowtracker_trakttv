package controller.activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.ArrayList;

import model.GetImageOnline;
import model.TVShow;
import model.TraktTVAPI;
import model.Database;
import iprog.showtracker.R;

/**
 * Activity for displaying the search results.
 * The search activity fetches data from TraktTVAPI and stores data in
 * local database on the device.
 */
public class SearchableActivity extends AppCompatActivity {
    private TraktTVAPI traktTVAPI;
    private Toolbar toolbar;
    private Database mDB;

    public SearchableActivity() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search);
        traktTVAPI = new TraktTVAPI();
        mDB = new Database(this);

        handleIntent(getIntent());

        // Init. actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Disable app label
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo((getComponentName())));
        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            // Fetch data from TraktTV API
            ArrayList<TVShow> tvShows = traktTVAPI.getTVShows(query);

            // Display the shows fetched
            if (tvShows.size() > 0) {

                LinearLayout llSearchResultContainer = (LinearLayout) this.findViewById(R.id.searchResultContainer);
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                for (final TVShow tvshow: tvShows) {
                    View searchRow = inflater.inflate(R.layout.search_row, null);
                    ImageView thumbView = (ImageView) searchRow.findViewById(R.id.searchImage);
                    TextView titleView = (TextView) searchRow.findViewById(R.id.searchTitle);
                    TextView yearView = (TextView) searchRow.findViewById(R.id.searchYear);
                    TextView overviewView = (TextView) searchRow.findViewById(R.id.searchOverview);
                    Button addToBtn = (Button) searchRow.findViewById(R.id.addToBtn);

                    GetImageOnline apiCommGetImage = new GetImageOnline();

                    Bitmap bmp = null;

                    try {
                        bmp = apiCommGetImage.execute(tvshow.getImgThumb()).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if(bmp != null) {
                        thumbView.setImageBitmap(bmp);
                    }

                    titleView.setText(tvshow.getTitle());
                    yearView.setText(tvshow.getYear());

                    final String overview = tvshow.getOverview();
                    if(overview.length() > 130) {
                        overviewView.setText(tvshow.getOverview().substring(0, 130) + "...");
                    } else {
                        overviewView.setText(tvshow.getOverview());
                    }

                    overviewView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            builder.setMessage(overview);
                            builder.create().show();
                        }
                    });

                    addToBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {

                            final CharSequence listOptions[] = new CharSequence[]
                                    {getResources().getString(R.string.list_label1),
                                            getResources().getString(R.string.list_label2),
                                            getResources().getString(R.string.list_label3),
                                            getResources().getString(R.string.list_label4),
                                            getResources().getString(R.string.list_label5)};

                            AlertDialog.Builder builder = new AlertDialog.Builder(SearchableActivity.this);
                            builder.setTitle("Add to list...");
                            builder.setItems(listOptions, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int listIndex) {
                                    mDB.addShowToList(listIndex,tvshow);

                                    finish();


                                }
                            });
                            builder.show();

                        }
                    });

                    llSearchResultContainer.addView(searchRow);

                }

            } else { // No show results
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.searchResultContainer);
                TextView valueTV = new TextView(this);
                valueTV.setText("No results... Try another search term.");

                linearLayout.addView(valueTV);

            }
        }
    }






}



