package com.example.loginapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK

class LoginSuccessActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_success)

        var btnKakaoLogout = findViewById<Button>(R.id.btn_logout)

        setLoginSuccess()

        btnKakaoLogout.setOnClickListener {
            when (intent.getStringExtra("login")) {
                "kakao" -> {
                    getKakaoInfo()
                    kakaoLogout()
                    startIntent()
                }
                "naver" -> {
                    naverLogout()
                    startIntent()
                }
                else -> Toast.makeText(this, "로그아웃 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startIntent() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
    }

    private fun naverLogout() {
        NaverIdLoginSDK.logout()
    }

    private fun setLoginSuccess() {
        val tvLogin = findViewById<TextView>(R.id.tv_login)
        val btnLogout = findViewById<Button>(R.id.btn_logout)
        val loginType = intent.getStringExtra("login").toString()

        tvLogin.text = "$loginType(으)로 로그인 성공!"
        btnLogout.text = "$loginType 로그아웃"
    }


    // 카카오 로그아웃
    fun kakaoLogout() {
        // 성공 여부와 관계 없이 토근 삭제 (카카오)
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e("test", "로그아웃 실패. SDK에서 토큰 삭제됨 / " + error.message)
            } else {
                Log.e("test", "로그아웃 성공. SDK에서 토큰 삭제됨", error)
            }
        }
    }

    // 카카오 연결 끊기
    fun kakaoUnlink() {
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e("test", "연결 끊기 실패", error)
            } else {
                Log.i("test", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }
    }

    // 카카오 정보 불러오기
    fun getKakaoInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("info", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(
                    "info", "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이름: ${user.kakaoAccount?.profile?.nickname}"
                )
            }
        }
    }
}