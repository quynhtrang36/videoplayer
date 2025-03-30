package com.example.playvideo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.VideoView
import android.widget.MediaController
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ComponentActivity() {
    private lateinit var videoView: VideoView
    private lateinit var edtVideoUrl: EditText
    private lateinit var btnPlayUrl: Button
    private lateinit var btnPickVideo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videoView = findViewById(R.id.videoView)
        edtVideoUrl = findViewById(R.id.edtVideoUrl)
        btnPlayUrl = findViewById(R.id.btnPlayUrl)
        btnPickVideo = findViewById(R.id.btnPickVideo)

        // Gắn MediaController vào VideoView
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        // Xử lý phát video từ URL
        btnPlayUrl.setOnClickListener {
            val url = edtVideoUrl.text.toString().trim()
            if (url.isNotEmpty()) {
                playVideo(Uri.parse(url))
            }
        }

        // Xử lý chọn video từ thiết bị
        btnPickVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            pickVideoLauncher.launch(intent)
        }
    }

    // Lắng nghe kết quả chọn video từ thư viện
    private val pickVideoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedVideoUri = result.data?.data
            selectedVideoUri?.let { playVideo(it) }
        }
    }

    // Hàm phát video
    private fun playVideo(videoUri: Uri) {
        videoView.setVideoURI(videoUri)
        videoView.start()
    }
}