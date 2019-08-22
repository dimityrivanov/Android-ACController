package com.mvp.accontrol.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvp.accontrol.ACUnitAdapter
import com.mvp.accontrol.R
import com.mvp.accontrol.helper.AcDataTemplate
import com.mvp.accontrol.helper.StateManager
import com.mvp.accontrol.viewmodel.ACUnitListViewModel
import com.mvp.accontrol.viewmodel.ACUnitListViewModelFactory
import kotlinx.android.synthetic.main.unit_list_layout.*


class ACUnitListFragment : Fragment() {

    private var mContext: Context? = null
    private lateinit var mViewModel: ACUnitListViewModel
    private lateinit var mStateManager: StateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.title = getString(R.string.select_device_brand)

        context?.let {
            mStateManager = StateManager(it)
        }

        createViewModel()
        setupListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.unit_list_layout, container, false)
    }

    private fun createViewModel() {
        val acUnitListFragmentFactory = ACUnitListViewModelFactory()
        mViewModel = ViewModelProviders.of(this, acUnitListFragmentFactory)
            .get(ACUnitListViewModel::class.java)
    }

    private fun setupListeners() {
        mViewModel.deviceBrand.observe(this, Observer { deviceBrandSelected ->
            Toast.makeText(mContext, "$deviceBrandSelected", Toast.LENGTH_SHORT).show()
            mViewModel.initDevice(deviceBrandSelected + 1)
        })

        mViewModel.mainViewState.observe(this, Observer { viewState ->
            viewState.deviceResponse?.let { deviceResponse ->
                if (deviceResponse.success == true) {
                    val deviceInfo = mStateManager.getAcDataTemplate()
                    if (deviceInfo?.inited == false) {
                        mStateManager.saveAcDataTemplate(
                            AcDataTemplate(
                                deviceID = deviceInfo?.deviceID,
                                inited = true
                            )
                        )
                        findNavController(this).navigate(R.id.action_ACUnitList_to_ACControlFragment)
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mContext = context

        unitList.apply {
            setHasFixedSize(true)

            layoutManager = LinearLayoutManager(mContext)

            adapter = ACUnitAdapter(arrayListOf("Mitsubishi", "Toshiba", "Daikin"), mViewModel)
        }
    }
}