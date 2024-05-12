package com.tchaikovsky.androidcomponents

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        findViewById<TextView>(R.id.second_activity_text_view).text =
            intent.getStringExtra(FirstActivity.INPUT_KEY_FOR_ACTIVITY_RESULT)
        findViewById<AppCompatButton>(R.id.second_ok_button).setOnClickListener {
            setResult(
                Activity.RESULT_OK,
                Intent().putExtra(
                    FirstActivity.OUTPUT_KEY_FOR_ACTIVITY_RESULT,
                    findViewById<EditText>(R.id.second_activity_edit_text).text.toString()
                )
            )
            finish()
        }
    }
}