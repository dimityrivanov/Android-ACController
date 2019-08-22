package com.mvp.accontrol.views

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.mvp.accontrol.ACControllerApplication
import com.mvp.accontrol.R
import com.mvp.accontrol.helper.AcDataTemplate
import com.mvp.accontrol.helper.StateManager
import com.mvp.accontrol.viewmodel.ACFindDeviceViewModel
import com.mvp.accontrol.viewmodel.ACFindDeviceViewModelFactory
import kotlinx.android.synthetic.main.ac_find_device.*


class ACFindDeviceFragment : Fragment() {

    private lateinit var mViewModel: ACFindDeviceViewModel
    private lateinit var mStateManager: StateManager
    private val mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.title = getString(R.string.searching_for_devices)

        createViewModel()
        attachListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ac_find_device, container, false)
    }

    var failure: Runnable = Runnable {
        bottomPanel.slideUp(parent)

        val animationDelayHandler = Handler()
        animationDelayHandler.postDelayed({
            done.setAnimation("not_found.json")
            done.playAnimation()
            btnNext.text = getString(R.string.try_again)
            textView.text = getString(R.string.device_not_found)
            btnNext.isEnabled = true
        }, 500)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        context?.let {
            mStateManager = StateManager(it)
            mViewModel.findSSDPDevices(it.getSystemService(Context.WIFI_SERVICE) as WifiManager)
        }

        mHandler.postDelayed(failure, 5000)

        btnNext.isEnabled = false
        btnNext.setOnClickListener {
            if (btnNext.text == getString(R.string.try_again)) {
                bottomPanel.slideUp(parent)
            } else {
                val manager = mStateManager.getAcDataTemplate()

                if (manager?.inited == true) {
                    findNavController(this).navigate(R.id.action_ACFindDeviceFragment_to_ACControlFragment)
                } else {
                    findNavController(this).navigate(R.id.action_ACFindDeviceFragment_to_ACUnitList)
                }
            }
        }
    }

    private fun createViewModel() {
        val findDeviceFragmentFactory = ACFindDeviceViewModelFactory()
        mViewModel = ViewModelProviders.of(this, findDeviceFragmentFactory)
            .get(ACFindDeviceViewModel::class.java)
    }

    private fun View.slideUp(parent: ViewGroup) {
        val transition = Slide(Gravity.BOTTOM)
        transition.duration = 600
        transition.addTarget(this)

        TransitionManager.beginDelayedTransition(parent, transition)

        if (visibility == View.GONE) {
            bottomPanel.visibility = View.VISIBLE
        } else {
            bottomPanel.visibility = View.GONE
        }

        val animationDelayHandler = Handler()
        animationDelayHandler.postDelayed({
            done.setAnimation("checked_done.json")
            done.repeatCount = 0
            done.playAnimation()
            btnNext.isEnabled = true
        }, 500)
    }

    private fun Int.isNegative(): Boolean {
        if (this < 0) {
            return true
        }
        return false
    }


    private fun attachListeners() {

        mViewModel.mainViewState.observe(this, Observer { viewState ->
            viewState.address?.let { address ->

                if (address.isNotEmpty()) {
                    ACControllerApplication().changeBaseUrl("http://$address:1900")

                    mViewModel.getDeviceID()
                }
            }

            viewState.deviceState?.let { deviceState ->
                deviceState.checksum?.let { checksum ->
                    //negative checksum means no interaction with the device so far
                    //empty cell is -1, so 4*-1 = -4 as initial value
                    if (checksum.isNegative()) {
                        //command to device is not yet issued
                        mHandler.removeCallbacks(failure)
                        bottomPanel.slideUp(parent)
                    } else {
                        val success = mStateManager.fixDiffCheckSum(deviceState)

                        if (success) {
                            mHandler.removeCallbacks(failure)
                            bottomPanel.slideUp(parent)
                        } else {
                            //on failure it will show device not found error, comming here should be an extreme case
                            // when mobile app and hardware checksums missmatch
                            Log.d(ACFindDeviceFragment::class.java.toString(), "Unhandled case!!!")
                        }
                    }
                }
            }

            viewState.deviceID?.let { deviceResponse ->

                //we already have that device setup!!!
                if (mStateManager.getAcDataTemplate()?.deviceID == deviceResponse.deviceID) {
                    //check device checksum
                    mViewModel.getDeviceState()
                } else {
                    val template = AcDataTemplate()
                    template.deviceID = deviceResponse.deviceID
                    mStateManager.saveAcDataTemplate(template)

                    mHandler.removeCallbacks(failure)
                    bottomPanel.slideUp(parent)
                }
            }

        })
    }
}