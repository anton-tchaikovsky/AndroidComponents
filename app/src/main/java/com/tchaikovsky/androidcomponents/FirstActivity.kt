package com.tchaikovsky.androidcomponents

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView

class FirstActivity : AppCompatActivity() {

    // контракт для запроса разрешений на доступ к камере
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { grande ->
            if (grande) cameraLauncher.launch(null)
            else {
                // срабатывает после "отмена" с галочкой "больше не спрашивать"
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                    Toast.makeText(
                        this,
                        getString(R.string.no_permission_camera),
                        Toast.LENGTH_SHORT
                    ).show()
                // срабатывает после "отмена" без галочки "больше не спрашивать"
                else
                    Toast.makeText(
                        this,
                        getString(R.string.no_access_camera),
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

    // контракт на открытие камеры
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            findViewById<AppCompatImageView>(R.id.camera_image_view).setImageBitmap(it)
        }
    }

    // контракт на открытие secondActivity
    private val secondActivityResultLauncher =
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        initStartSecondActivity()
        initStartCamera()
    }

    private fun initStartCamera() {
        findViewById<AppCompatButton>(R.id.camera_button).setOnClickListener {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun initStartSecondActivity() {
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
                    secondActivityResultLauncher.launch(
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