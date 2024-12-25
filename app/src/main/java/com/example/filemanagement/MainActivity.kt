package com.example.filemanagement

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.File


class MainActivity : AppCompatActivity() {
    private var listViewFiles: ListView? = null
    private var tvPath: TextView? = null
    private var currentDirectory: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listViewFiles = findViewById(R.id.listViewFiles)
        tvPath = findViewById(R.id.tvPath)

        // Yêu cầu quyền truy cập bộ nhớ
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSION
        )
    }

    private fun loadFiles(directory: File) {
        currentDirectory = directory
        tvPath!!.text = directory.absolutePath
        val files = directory.listFiles()
        if (files != null) {
            val fileList = ArrayList<String>()
            for (file in files) {
                fileList.add(file.name)
            }
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1, fileList
            )
            listViewFiles!!.adapter = adapter
            listViewFiles!!.onItemClickListener =
                OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                    val selectedFile = files[position]
                    if (selectedFile.isDirectory) {
                        loadFiles(selectedFile)
                    } else {
                        // Mở file nếu là file văn bản
                        val intent = Intent(this, FileViewerActivity::class.java)
                        intent.putExtra("filePath", selectedFile.absolutePath)
                        startActivity(intent)
                    }
                }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            val root = Environment.getExternalStorageDirectory()
            loadFiles(root)
        } else {
            Toast.makeText(this, "Quyền truy cập bị từ chối", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        if (currentDirectory != null && currentDirectory != Environment.getExternalStorageDirectory()) {
            loadFiles(currentDirectory!!.parentFile)
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val REQUEST_PERMISSION = 100
    }
}
