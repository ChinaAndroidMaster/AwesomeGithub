package com.idisfkj.awesome.home.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.idisfkj.awesome.basic.fragment.BaseFragment
import com.idisfkj.awesome.home.BR
import com.idisfkj.awesome.home.R
import com.idisfkj.awesome.home.databinding.HomeFragmentHomeBinding
import com.idisfkj.awesome.home.fragment.repository.HomeRepository
import com.idisfkj.awesome.home.fragment.vm.HomeVM
import com.idisfkj.awesome.network.HttpClient
import timber.log.Timber

/**
 * Created by idisfkj on 2019-09-02.
 * Email : idisfkj@gmail.com.
 */
class HomeFragment : BaseFragment<HomeFragmentHomeBinding, HomeVM>() {

    override fun getVariableId(): Int = BR.vm

    override fun getLayoutId(): Int = R.layout.home_fragment_home

    override fun getViewModelInstance(): HomeVM =
        HomeVM(activity!!.application, HomeRepository(HttpClient.getService()))

    override fun getViewModelClass(): Class<HomeVM> = HomeVM::class.java

    companion object {
        fun getInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObserver()
    }

    private fun addObserver() {
        viewModel.userData.observe(this, Observer {
            Timber.d("addObserver %s", it.followers)
        })
    }
}