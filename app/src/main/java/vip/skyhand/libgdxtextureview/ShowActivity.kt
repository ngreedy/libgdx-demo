package vip.skyhand.libgdxtextureview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.view.TextureView
import android.view.View
import android.widget.Toast
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import kotlinx.android.synthetic.main.activity_spine_test.*

class ShowActivity : Activity() {

    lateinit var mGdxAdapter: GdxAdapter
    lateinit var mNewGdxAdapter: NewGdxAdapter
    lateinit var mGdxView: View

    companion object {
        fun start(
            context: Context,
            useTextureView: Boolean,
            isTranlate: Boolean = true,
            name: String
        ) {
            val intent = Intent(context, ShowActivity::class.java)
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
        useTextureView = intent.getBooleanExtra("USETEXTUREVIEW", true)
        isTranlate = intent.getBooleanExtra("ISTRANLATE", true)
        name = intent.getStringExtra("NAME")
        Log.e("ShowActivity", "onCreate: " + name)
        initListener()

        initGDX()

        ripple.startRipple()
    }

    private fun initListener() {
    }


    lateinit var attach: GdxAttach

    private fun initGDX() {
        ScreenUtils.initialization(this)
        val cfg = AndroidApplicationConfiguration()

        cfg.useTextureView = useTextureView  //是否使用TextureView

        cfg.useImmersiveMode = true
        cfg.a = 8
        cfg.b = cfg.a
        cfg.g = cfg.b
        cfg.r = cfg.g
        mGdxAdapter = GdxAdapter()
//        mNewGdxAdapter = NewGdxAdapter()
//        mNewGdxAdapter.setName(name)
//        mGdxView = initializeForView(mNewGdxAdapter, cfg)
        attach = GdxAttach()
        attach.setLifeOwner(this, gdxContainer)
        mGdxView = attach.initializeForView(mGdxAdapter, cfg)

        if (mGdxView is SurfaceView) {
            //Log.e("@@", "当前是SurfaceView")
            Toast.makeText(this, "当前是SurfaceView", Toast.LENGTH_SHORT).show()
            if (isTranlate) {
                (mGdxView as SurfaceView).holder.setFormat(PixelFormat.TRANSLUCENT)
                (mGdxView as SurfaceView).setZOrderOnTop(true)
            } else {
                (mGdxView as SurfaceView).setZOrderMediaOverlay(true)
            }
        } else if (mGdxView is TextureView) {
            Toast.makeText(this, "当前是TextureView", Toast.LENGTH_SHORT).show()
            //Log.e("@@", "当前是TextureView")
        }

        gdxContainer.addView(mGdxView)



        button2.setOnClickListener {
//            mGdxAdapter?.setAnimate()
        }

        button.setOnClickListener {
            ShowActivity.start(this, useTextureView = true, isTranlate = true, name = "")
        }
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        attach.onWindowFocusChanged(hasFocus)
    }

    override fun onPause() {
        attach.onPause()
        super.onPause()
    }

    override fun onResume() {
        attach.onResume()
        super.onResume()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        attach.onConfigurationChanged(newConfig)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        attach.onActivityResult(requestCode,resultCode,data)
    }
}