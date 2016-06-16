package controller.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.SearchView;


import java.util.ArrayList;
import java.util.List;

import controller.viewpager.ViewPagerAdapter;
import controller.fragments.*;
import iprog.showtracker.R;

public class MainActivity extends AppCompatActivity {

    private List<DataUpdateListener> mListeners;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollable_tabs);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(5);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        mListeners = new ArrayList<>();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        mSearchView.setSearchableInfo(searchManager.getSearchableInfo((getComponentName())));
        mSearchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    public void onRestart() {
        super.onRestart();

        // Remove focus and previous search query when resumed
        mSearchView.setFocusable(false);
        mSearchView.setQuery("", false);
        mSearchView.clearFocus();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter =  new ViewPagerAdapter(getSupportFragmentManager());

        ShowListFragment listFragment;
        Bundle bd;
        listFragment =  new ShowListFragment();
        bd = new Bundle(1);
        bd.putString("TYPE_OF_LIST",getResources().getString(R.string.db_list_name1));
        listFragment.setArguments(bd);
        adapter.addFrag(listFragment, getResources().getString(R.string.list_label1));

        listFragment =  new ShowListFragment();
        bd = new Bundle(1);
        bd.putString("TYPE_OF_LIST",getResources().getString(R.string.db_list_name2));
        listFragment.setArguments(bd);
        adapter.addFrag(listFragment, getResources().getString(R.string.list_label2));

        listFragment =  new ShowListFragment();
        bd = new Bundle(1);
        bd.putString("TYPE_OF_LIST",getResources().getString(R.string.db_list_name3));
        listFragment.setArguments(bd);
        adapter.addFrag(listFragment, getResources().getString(R.string.list_label3));

        listFragment =  new ShowListFragment();
        bd = new Bundle(1);
        bd.putString("TYPE_OF_LIST",getResources().getString(R.string.db_list_name4));
        listFragment.setArguments(bd);
        adapter.addFrag(listFragment, getResources().getString(R.string.list_label4));

        listFragment =  new ShowListFragment();
        bd = new Bundle(1);
        bd.putString("TYPE_OF_LIST",getResources().getString(R.string.db_list_name5));
        listFragment.setArguments(bd);
        adapter.addFrag(listFragment, getResources().getString(R.string.list_label5));
        viewPager.setAdapter(adapter);

    }

    public synchronized void registerDataUpdateListener(DataUpdateListener listener) {
        mListeners.add(listener);
    }

    public synchronized void unregisterDataUpdateListener(DataUpdateListener listener) {
        mListeners.remove(listener);
    }

    public synchronized void dataUpdated() {
        for (DataUpdateListener listener : mListeners) {
            listener.onDataUpdate();
        }
    }

    public interface DataUpdateListener {
        void onDataUpdate();
    }



}
