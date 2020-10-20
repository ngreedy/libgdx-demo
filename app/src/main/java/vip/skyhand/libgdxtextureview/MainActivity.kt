package vip.skyhand.libgdxtextureview

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

/**
 * @author Skyhand
 */
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 0
            );
        }
//        val assetNameList = assets.list("files").asList()
        val path = intent.getStringExtra("path")


        var srcFile = File(Environment.getExternalStorageDirectory().absolutePath + "/files")
        if (!TextUtils.isEmpty(path)) {
            srcFile = File(path)
        }
        val assetNameList = srcFile.listFiles();

        if (assetNameList == null || assetNameList.isEmpty()) {
            Toast.makeText(this, "no available files!!!", Toast.LENGTH_SHORT).show()
            return
        }

        assetList.adapter = object : BaseAdapter() {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val textView = TextView(this@MainActivity)
                textView.setTextSize(24f)
                textView.setText(assetNameList[position].name)

                return textView
            }

            override fun getItem(position: Int): Any {
                return assetNameList[position].name
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getCount(): Int {
                return assetNameList.size
            }
        }

        assetList.setOnItemClickListener { parent, view, position, id ->
            val fileName = assetNameList[position].name
            val fullNameList = fileName.split(".")
            if (fullNameList.size > 1) {
                val name = fullNameList[0]
                val extra = fullNameList[1]
                if (extra.contains("atlas") || extra.contains("json")) {
                    ShowActivity.start(this, false, true, assetNameList[position].absolutePath)
                } else if (extra.contains("zip")) {
                    val result = ZipFileToolKit.unZip(
                        assetNameList[position].absolutePath,
                        srcFile.absolutePath + "/"
                    )
                    if (result.contains("mp4")) {
                        PlayerActivity.start(this, false, true, result)
                        return@setOnItemClickListener
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("path", result)
                    startActivity(intent)
                } else if (extra.contains("mp4")) {
                    PlayerActivity.start(this, false, true, assetNameList[position].absolutePath)
                } else if (extra.contains("bundle")) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("path", assetNameList[position].absolutePath)
                    startActivity(intent)
                } else {

                }
            }
        }

        initListener()
    }

    private fun initListener() {
    }

    fun jump(view: View) {
        ShowActivity.start(this, false, true, "")
    }


}
