package com.richardchien.android.zhihudaily.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.richardchien.android.zhihudaily.R;
import com.richardchien.android.zhihudaily.fragments.StoryFragment;
import com.richardchien.android.zhihudaily.others.C;

public class StoryActivity extends BaseActivity {
    public static void start(Context context, int id, String title) {
        Intent starter = new Intent(context, StoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(C.KEY_ID, id);
        bundle.putString(C.KEY_TITLE, title);
        starter.putExtras(bundle);
        context.startActivity(starter);
    }

    StoryFragment mStoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        mStoryFragment = StoryFragment.newInstance(getIntent().getIntExtra(C.KEY_ID, 0), getIntent().getStringExtra(C.KEY_TITLE));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_story_container, mStoryFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.story_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_in_browser:
                mStoryFragment.openInBrowser();
        }
        return true;
    }
}
