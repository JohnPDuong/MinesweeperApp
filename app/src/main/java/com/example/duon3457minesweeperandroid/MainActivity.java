/**
 * MainActivity of the app
 *
 * @author John Duong
 */

package com.example.duon3457minesweeperandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import duon3457Minesweeper.MinesweeperPresenter;

public class MainActivity extends AppCompatActivity {
    final int RANDOM = 1;

    int mDifficulty = MinesweeperPresenter.EASY;
    int mSeed = MinesweeperPresenter.SEED_ZERO;

    MinesweeperView mView;

    /**
     * When app instance is created, creates a new game defaulted to easy and seed zero
     *
     * @param savedInstanceState the saved instance of the game
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        //setContentView (R.layout.activity_main);

        mView = new MinesweeperView (this, MinesweeperPresenter.EASY,
                                     MinesweeperPresenter.SEED_ZERO);

        setContentView (mView);
    }
    /**
     * Inflates the option menus
     *
     * @param menu the menu
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        MenuInflater inflater = getMenuInflater ();
        inflater.inflate (R.menu.menu, menu);

        return true;
    }
    /**
     * Displays menu items and configures game with settings
     *
     * @param item the menu items
     */
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        // Handle item selection
        switch (item.getItemId ()) {
            case R.id.menuAbout:
                startActivity (new Intent (this, About.class));
                return true;

            case R.id.menuDifficulty:
                return true;

            case R.id.menuDifficultyEasy:
                mDifficulty = MinesweeperPresenter.EASY;
                newGame ();
                return true;

            case R.id.menuDifficultyMedium:
                mDifficulty = MinesweeperPresenter.MEDIUM;
                newGame ();
                return true;

            case R.id.menuDifficultyHard:
                mDifficulty = MinesweeperPresenter.HARD;
                newGame ();
                return true;

            case R.id.menuSeed:
                return true;

            case R.id.seedZero:
                mSeed = MinesweeperPresenter.SEED_ZERO;
                newGame ();
                return true;

            case R.id.seedTimeOfDay:
                mSeed = RANDOM;
                newGame ();
                return true;
            default:
                return super.onOptionsItemSelected (item);
        }
    }
    /**
     * When screen rotation is detected, starts a new game
     *
     * @param newConfig new configuration of device
     */
    @Override
    public void onConfigurationChanged (Configuration newConfig)
    {
        newGame ();
    }
    /**
     * Creates a new game and resets content view
     */
    public void newGame ()
    {
        mView = new com.example.duon3457minesweeperandroid.MinesweeperView (this,
                                                                            mDifficulty, mSeed);
        setContentView (mView);
    }
}