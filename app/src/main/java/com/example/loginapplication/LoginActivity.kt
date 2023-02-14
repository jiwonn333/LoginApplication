package com.example.loginapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.loginapplication.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.oauth.view.NidOAuthLoginButton

class LoginActivity : AppCompatActivity(), OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnKakao = findViewById<ImageButton>(R.id.btn_kakao)
        val btnNaver = findViewById<ImageButton>(R.id.btn_naver)
        btnKakao.setOnClickListener(this)
        btnNaver.setOnClickListener(this)


    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_kakao -> startKakaoLogin()
            R.id.btn_naver -> startNaverLogin()
            else -> null
        }
    }

    private fun startKakaoLogin() {
        // kakao 실행 가능 여부
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            // 가능하다면 카카오톡으로 로그인하기
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Toast.makeText(this, "카카오톡으로 로그인 실패!", Toast.LENGTH_SHORT).show()
                    Log.e("test", "카카오톡으로 로그인 실패! " + error.message)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }


                } else if (token != null) {
                    Toast.makeText(this, "카카오톡으로 로그인 성공!", Toast.LENGTH_SHORT).show()
                    Log.d("test", "카카오톡으로 로그인 성공! " + token.accessToken)

                    val intent = Intent(this, LoginSuccessActivity::class.java)
                    intent.putExtra("login", "kakao")
                    startActivity(intent)
                    finish()
                }

                // kakao로 로그인 하지 못 할 경우 계정으로 로그인 시도
                UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoCallback())
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoCallback())
        }
    }

    private fun kakaoCallback(): (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Toast.makeText(this, "카카오 계정으로 로그인 실패!", Toast.LENGTH_SHORT).show()
            Log.e("test", "카카오 계정으로 로그인 실패! " + error.message)

        } else if (token != null) {
            Toast.makeText(this, "카카오 계정으로 로그인 성공!", Toast.LENGTH_SHORT).show()
            Log.d("test", "카카오 계정으로 로그인 성공! " + token.accessToken)

            val intent = Intent(this, LoginSuccessActivity::class.java)
            intent.putExtra("login", "kakao")
            startActivity(intent)
            finish()
        }
    }

    private fun startNaverLogin() {
        /**
         * launcher나 OAuthLoginCallback을 authenticate() 메서드 호출 시
         * 파라미터로 전달하거나 NidOAuthLoginButton 객체에 등록하면 인증이 종료되는 것을 확인할 수 있다.
         * https://developer.android.com/training/basics/intents/result?hl=ko#custom
         */

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // login success 시 Log
                Log.d("test", "네이버 인증 성공")
                Log.d("test", "AccessToken : " + NaverIdLoginSDK.getAccessToken())
                Log.d("test", "client id : " + getString(R.string.naver_client_id))
                Log.d("test", "ReFreshToken : " + NaverIdLoginSDK.getRefreshToken())
                Log.d("test", "Expires : " + NaverIdLoginSDK.getExpiresAt().toString())
                Log.d("test", "TokenType : " + NaverIdLoginSDK.getTokenType())
                Log.d("test", "State : " + NaverIdLoginSDK.getState().toString())

                // 화면이동
                val intent = Intent(baseContext, LoginSuccessActivity::class.java)
                intent.putExtra("login", "naver")
                startActivity(intent)
                finish()

            }


            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.e("test", "$errorCode $errorDescription")
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
    }





}