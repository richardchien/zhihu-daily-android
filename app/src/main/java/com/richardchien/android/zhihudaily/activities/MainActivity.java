package com.richardchien.android.zhihudaily.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.richardchien.android.zhihudaily.R;
import com.richardchien.android.zhihudaily.fragments.ListFragment;

public class MainActivity extends BaseActivity {
    ListFragment mListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.fg_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                mListFragment.refreshNews();
                break;
            case R.id.action_clear_cache:
                mListFragment.clearAllCache();
                mListFragment.notifyListViewAdapter();
        }
        return true;
    }
}
