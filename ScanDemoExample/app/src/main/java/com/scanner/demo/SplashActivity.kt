package com.scanner.demo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.scanlibrary.BaseActivity
import com.scanner.demo.home.HomeActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_layout)

        Handler(Looper.getMainLooper()).postDelayed({
            if(isFinishing) return@postDelayed
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 800)
    }
}