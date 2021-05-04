/**
 * The About activity of the app displaying email, important info, version of game and icon URL
 *
 * @author John Duong
 */
package com.example.duon3457minesweeperandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_about);
    }
}