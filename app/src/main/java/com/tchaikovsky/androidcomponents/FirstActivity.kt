package com.tchaikovsky.androidcomponents

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class FirstActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        val activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                if (it.resultCode != Activity.RESULT_OK)
                    Toast.makeText(this, getString(R.string.default_result), Toast.LENGTH_SHORT)
                        .show()
                else {
                    val data = it.data?.getStringExtra(OUTPUT_KEY_FOR_ACTIVITY_RESULT)
                    Toast.makeText(
                        this,
                        if (data.isNullOrBlank()) getString(R.string.default_result) else data,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

        findViewById<AppCompatButton>(R.id.first_ok_button).setOnClickListener {
            with(findViewById<EditText>(R.id.first_activity_edit_text).text.toString()) {
                if (this.isBlank())
                    Toast.makeText(
                        this@FirstActivity,
                        getString(R.string.default_result),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                else
                    activityResultLauncher.launch(
                        Intent(this@FirstActivity, SecondActivity::class.java).putExtra(
                            INPUT_KEY_FOR_ACTIVITY_RESULT,
                            this
                        )
                    )
            }
        }
    }

    companion object {
        internal const val INPUT_KEY_FOR_ACTIVITY_RESULT = "InputKeyForActivityResult"
        internal const val OUTPUT_KEY_FOR_ACTIVITY_RESULT = "OutputKeyForActivityResult"
    }
}