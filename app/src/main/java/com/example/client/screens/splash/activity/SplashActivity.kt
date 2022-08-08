package com.example.client.screens.splash.activity

import android.os.Bundle
import com.example.client.R
import com.example.client.app.Preferences
import com.example.client.base.BaseActivity
import com.example.client.screens.login.activity.LoginEmailActivity
import com.example.client.screens.main.activity.MainActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun bindEvent() {
        add(Observable.just(123)
            .delay(3000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                finish()
                val token = Preferences.newInstance().accessToken
                val profileModel = Preferences.newInstance().profile
                if (token.isNullOrEmpty() || profileModel == null ) {
                    startActivity(LoginEmailActivity.newInstance(this))
                } else {
                    startActivity(MainActivity.newInstance(this))
                }
            }, {
                it.printStackTrace()
            }))
    }
}