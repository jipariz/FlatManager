package com.jipariz.flatmanager.flat_choose

import android.app.ActionBar
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jipariz.flatmanager.R

class FlatChooseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flat_choose)

        actionBar?.title = "Enter a flat"
    }
}
