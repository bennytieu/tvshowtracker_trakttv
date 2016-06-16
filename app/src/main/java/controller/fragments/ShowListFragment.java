package controller.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import controller.activity.MainActivity;
import model.Database;
import model.GetImageOnline;
import model.TVShow;
import iprog.showtracker.R;

/**
 * Each tab in MainActivity is a ShowListFragment. Displays the tv-shows in the given list
 */
public class ShowListFragment extends Fragment implements MainActivity.DataUpdateListener {
    final static String DB_LIST1 = "watching";
    final static String DB_LIST2 = "plan_to_watch";
    final static String DB_LIST3 = "completed";
    final static String DB_LIST4 = "on_hold";
    final static String DB_LIST5 = "dropped";
    private LayoutInflater mInflater;

    public ShowListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return loadList();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadList();
    }


    @SuppressLint("NewApi")
    public View loadList() {
        final String listName = getArguments().getString("TYPE_OF_LIST");
        final LayoutInflater inflater = this.getActivity().getLayoutInflater();
        final Database mDB = new Database(getActivity().getApplicationContext());
        final ArrayList<TVShow> tvShows = mDB.getTVShowsFromList(listName);


        final View fragmentView = inflater.inflate(R.layout.show_list_fragment,null);
        ScrollView scrollView = (ScrollView) fragmentView.findViewById(R.id.fragmentScrollView);
        LinearLayout llFragmentContainer = (LinearLayout) scrollView.getChildAt(0);
        int fragmentID = 0;
        switch (listName) {
            case (DB_LIST1):
                fragmentID = View.generateViewId();
                break;
            case (DB_LIST2):
                fragmentID = View.generateViewId();
                break;
            case (DB_LIST3):
                fragmentID = View.generateViewId();
                break;
            case (DB_LIST4):
                fragmentID = View.generateViewId();
                break;
            case (DB_LIST5):
                fragmentID = View.generateViewId();
                break;

        }
        llFragmentContainer.setId(fragmentID);


        if(tvShows.size()<=0) {
            View listRow = inflater.inflate(R.layout.nothing_to_show, null);
            llFragmentContainer.addView(listRow);
        }

        for(int i=0; i < tvShows.size(); i++) {
            final View listRow = inflater.inflate(R.layout.list_row, null);


            TextView titleView = (TextView) listRow.findViewById(R.id.searchTitle);
            TextView yearView = (TextView) listRow.findViewById(R.id.searchYear);
            TextView overviewView = (TextView) listRow.findViewById(R.id.searchOverview);

            ImageView thumbView = (ImageView) listRow.findViewById(R.id.searchImage);
            GetImageOnline apiCommGetImage = new GetImageOnline();

            Bitmap bmp = null;

            try {
                bmp = apiCommGetImage.execute(tvShows.get(i).getImgThumb()).get();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(bmp != null) {
                thumbView.setImageBitmap(bmp);
            }

            Button removeBtn = (Button) listRow.findViewById(R.id.removeBtn);

            final int showIndex = i;
            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                    builder1.setMessage("Are you sure you want to remove this show?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mDB.removeShowFromList(listName,tvShows.get(showIndex).getID());
                                    onDataUpdate();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            });

            Button moveButton = (Button) listRow.findViewById(R.id.moveBtn);


            CharSequence listOptions[] = null;
            switch (listName) {
                case DB_LIST1: listOptions = new CharSequence[] {
                        getResources().getString(R.string.list_label2),
                        getResources().getString(R.string.list_label3),
                        getResources().getString(R.string.list_label4),
                        getResources().getString(R.string.list_label5)};
                    break;
                case DB_LIST2: listOptions = new CharSequence[] {
                        getResources().getString(R.string.list_label1),
                        getResources().getString(R.string.list_label3),
                        getResources().getString(R.string.list_label4),
                        getResources().getString(R.string.list_label5)};
                    break;
                case DB_LIST3: listOptions = new CharSequence[] {
                        getResources().getString(R.string.list_label1),
                        getResources().getString(R.string.list_label2),
                        getResources().getString(R.string.list_label4),
                        getResources().getString(R.string.list_label5)};
                    break;
                case DB_LIST4: listOptions = new CharSequence[] {
                        getResources().getString(R.string.list_label1),
                        getResources().getString(R.string.list_label2),
                        getResources().getString(R.string.list_label3),
                        getResources().getString(R.string.list_label5)};
                    break;
                case DB_LIST5: listOptions = new CharSequence[] {
                        getResources().getString(R.string.list_label1),
                        getResources().getString(R.string.list_label2),
                        getResources().getString(R.string.list_label3),
                        getResources().getString(R.string.list_label4)};
                    break;

            }

            final CharSequence[] finalListOptions = listOptions;
            moveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                    builder.setTitle("Move to...");
                    builder.setItems(finalListOptions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int listIndex) {
                            mDB.addShowToList(listIndex, tvShows.get(showIndex));
                            mDB.removeShowFromList(listName,tvShows.get(showIndex).getID());
                            onDataUpdate();
                        }
                    });

                    builder.show();

                }
            });

            titleView.setText(tvShows.get(i).getTitle());
            yearView.setText(tvShows.get(i).getYear());

            final String overview = tvShows.get(i).getOverview();
            if (overview.length() > 130) {
                overviewView.setText(overview.substring(0, 130) + "...");
            } else {
                overviewView.setText(overview);
            }

            overviewView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(overview);
                    builder.create().show();
                }
            });

            llFragmentContainer.addView(listRow);
        }
        return fragmentView;
    }

    @Override
    public void onDataUpdate() {
        ViewPager viewPager =(ViewPager) getActivity().findViewById(R.id.viewpager);
        viewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;
        if(context instanceof  Activity) {
            activity = (Activity) context;
            ((MainActivity) activity).registerDataUpdateListener(this);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).unregisterDataUpdateListener(this);
    }
}
