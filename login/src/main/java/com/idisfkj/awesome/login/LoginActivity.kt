package com.idisfkj.awesome.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.idisfkj.awesome.basic.activity.BaseActivity
import com.idisfkj.awesome.common.*
import com.idisfkj.awesome.componentbridge.app.AppBridgeInterface
import com.idisfkj.awesome.componentbridge.provider.BridgeProviders
import com.idisfkj.awesome.login.databinding.LoginActivityLoginBinding
import com.idisfkj.awesome.network.HttpClient

/**
 * Created by idisfkj on 2019-08-30.
 * Email : idisfkj@gmail.com.
 */
@Route(path = ARouterPaths.PATH_LOGIN_LOGIN)
class LoginActivity : BaseActivity<LoginActivityLoginBinding, LoginVM>() {

    override fun getVariableId(): Int = BR.vm

    private var mAuthorizationCode: String? = null

    override fun getLayoutId(): Int = R.layout.login_activity_login

    override fun getViewModelInstance(): LoginVM = LoginVM(
        LoginRepository(
            BridgeProviders.instance.getBridge(AppBridgeInterface::class.java),
            HttpClient.getService()
        ), application
    )

    override fun getViewModelClass(): Class<LoginVM> = LoginVM::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addObserve()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        getExtra(intent)
    }

    private fun getExtra(intent: Intent?) {
        // from author login
        mAuthorizationCode = intent?.data?.getQueryParameter(Constants.AUTHORIZATION_CODE)
        if (!TextUtils.isEmpty(mAuthorizationCode)) {
            viewModel.getAccessTokenFromCode(mAuthorizationCode ?: "")
        }
    }

    private fun addObserve() {
        viewModel.hideSoftInput.observe(this, Observer {
            if (it) hideSoftInput()
        })
        viewModel.toPage.observe(this, Observer {
            when (it) {
                is LoginToMain -> {
                    ARouter.getInstance().build(ARouterPaths.PATH_APP_MAIN).navigation(this)
                    finish()
                }
                is LoginToAuthorize -> authorize()
                PageDefault -> Any()
            }
        })
    }

    private fun authorize() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.AUTHORIZATION_URL)))
    }
}