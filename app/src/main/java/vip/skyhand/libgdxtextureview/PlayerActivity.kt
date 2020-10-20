package vip.skyhand.libgdxtextureview

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.ss.ugc.android.alpha_player.controller.PlayerController
import com.ss.ugc.android.alpha_player.model.AlphaVideoViewType
import com.ss.ugc.android.alpha_player.model.Configuration
import com.ss.ugc.android.alpha_player.model.DataSource
import com.ss.ugc.android.alpha_player.player.DefaultSystemPlayer
import kotlinx.android.synthetic.main.activity_spine_test.*
import java.io.File

class PlayerActivity : AppCompatActivity() {


    companion object {
        fun start(
            context: Context,
            useTextureView: Boolean,
            isTranlate: Boolean = true,
            name: String
        ) {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("USETEXTUREVIEW", useTextureView)
            intent.putExtra("ISTRANLATE", isTranlate)
            intent.putExtra("NAME", name)
            context.startActivity(intent)
        }
    }

    private var useTextureView = true
    private var isTranlate = true
    private var name = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spine_test)

        button2.setOnClickListener {
            AlertDialog.Builder(this).setTitle("I am on the top").setMessage("hahaha").show()
            Toast.makeText(this, "i can touch through the player", Toast.LENGTH_SHORT).show()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0);
        }

        name = intent.getStringExtra("NAME")
        initListener()


        val config = Configuration(this, this)
        // 支持GLSurfaceView&GLTextureView, 默认使用GLSurfaceView
        config.alphaVideoViewType = AlphaVideoViewType.GL_TEXTURE_VIEW
        // 也可以设置自行实现的Player, demo中提供了基于ExoPlayer的实现


        val playerController = PlayerController.get(config, DefaultSystemPlayer())
        playerController.attachAlphaView(mLayoutGdx)
//        playerController.mediaPlayer.setLooping(true)

//        val playerController2 = PlayerController.get(config, DefaultSystemPlayer())
//        playerController2.attachAlphaView(mLayoutGdx)

        button.postDelayed(Runnable {
            var file = File(name)
            Log.e("wlSurfaceView", "onCreate: " + file.exists())
            val baseDir = file.parent
            val portraitFileName = file.name
            val portraitScaleType = 2
            val landscapeFileName = file.name
            val landscapeScaleType = 2
            val dataSource = DataSource().setBaseDir(baseDir)
                .setPortraitPath(portraitFileName, portraitScaleType)
                .setLandscapePath(landscapeFileName, landscapeScaleType)
            if (dataSource.isValid()) {
                Log.e("wlSurfaceView", "isValid:111111111111")
                playerController.start(dataSource)
            }

            Log.e("wlSurfaceView", "onCreate: " + file.exists())
        },1000)
        button.setOnClickListener {
//            val baseDir2 = Environment.getExternalStorageDirectory().absolutePath
//            val portraitFileName2 = "demo_video.mp4"
//            val landscapeFileName2 = file.name
//            val dataSource2 = DataSource().setBaseDir(baseDir2)
//                .setPortraitPath(portraitFileName2, portraitScaleType)
//                .setLandscapePath(landscapeFileName2, landscapeScaleType)
//            playerController2.start(dataSource2)
        }
    }

    private fun initListener() {
    }


}