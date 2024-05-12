package com.tchaikovsky.androidcomponents

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.widget.AppCompatButton

class FirstActivity : AppCompatActivity() {

    private val activityResultContract: ActivityResultContract<String, String?> =
        object : ActivityResultContract<String, String?>() {
            override fun createIntent(context: Context, input: String): Intent =
                Intent(context, SecondActivity::class.java).putExtra(
                    INPUT_KEY_FOR_ACTIVITY_RESULT,
                    input
                )

            override fun parseResult(resultCode: Int, intent: Intent?): String? =
                if (resultCode != Activity.RESULT_OK) null
                else intent?.getStringExtra(OUTPUT_KEY_FOR_ACTIVITY_RESULT)

            override fun getSynchronousResult(
                context: Context,
                input: String
            ): SynchronousResult<String?>? =
                if (input.isNotBlank()) null
                else SynchronousResult(getString(R.string.default_result))

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        val activityResultLauncher = registerForActivityResult(activityResultContract) {
            Toast.makeText(this, if (it.isNullOrBlank()) getString(R.string.default_result) else it, Toast.LENGTH_SHORT)
                .show()
        }

        findViewById<AppCompatButton>(R.id.first_ok_button).setOnClickListener {
            activityResultLauncher.launch(findViewById<EditText>(R.id.first_activity_edit_text).text.toString())
        }
    }

    companion object {
        internal const val INPUT_KEY_FOR_ACTIVITY_RESULT = "InputKeyForActivityResult"
        internal const val OUTPUT_KEY_FOR_ACTIVITY_RESULT = "OutputKeyForActivityResult"
    }
}