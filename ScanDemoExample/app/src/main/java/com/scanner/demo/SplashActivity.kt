package com.scanner.demo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.scanlibrary.BaseActivity
import com.scanner.demo.camerax.ImageCaptureActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_layout)

        Handler().postDelayed({
            if(isFinishing) return@postDelayed
            startActivity(Intent(this, ImageCaptureActivity::class.java))
            finish()
        }, 800)
    }
}