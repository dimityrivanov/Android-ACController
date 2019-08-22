package com.mvp.accontrol.views

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mvp.accontrol.ACUnitState
import com.mvp.accontrol.R
import com.mvp.accontrol.helper.StateManager
import com.mvp.accontrol.states.AutoFanState
import com.mvp.accontrol.states.AutoModeState
import com.mvp.accontrol.states.PowerOffState
import com.mvp.accontrol.viewmodel.ControlFragmentViewModel
import com.mvp.accontrol.viewmodel.ControlFragmentViewModelFactory
import kotlinx.android.synthetic.main.air_control_main.*
import me.rorschach.library.ShaderSeekArc


class ACControlFragment : Fragment() {

    private lateinit var mViewModel: ControlFragmentViewModel
    private lateinit var mStateManager: StateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.title = getString(R.string.air_conditioner)

        setHasOptionsMenu(true)

        createViewModel()
        setupListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.air_control_main, container, false)
    }


    private fun updateACState() {
        val authTemplate = mStateManager.getAcDataTemplate()

        authTemplate?.ac_fanState = mViewModel.acUnitState?.fanState?.javaClass.toString()
        authTemplate?.ac_modeState = mViewModel.acUnitState?.modeState?.javaClass.toString()
        authTemplate?.ac_temp = mViewModel.acUnitState?.acTemp!!
        authTemplate?.ac_powerState = mViewModel.acUnitState?.powerState?.javaClass.toString()

        authTemplate?.let { template ->
            mStateManager.saveAcDataTemplate(template)
        }
    }

    private fun updateUIComponents() {
        context?.let { ctx ->
            mViewModel.acUnitState?.acTemp?.toFloat()?.let {
                mCircularSeekBar.progress = it
            }

            txtMode.text = mViewModel.acUnitState?.modeState?.stateResult(ctx)
            txtSpeed.text = mViewModel.acUnitState?.fanState?.stateResult(ctx)

            //turned off
            if (mViewModel.acUnitState?.powerState?.stateValue() == 0) {
                floatingActionButton.backgroundTintList =
                    ColorStateList.valueOf(Color.WHITE)

                floatingActionButton.imageTintList =
                    ColorStateList.valueOf(Color.parseColor("#FF0A92DD"))
            } else {
                floatingActionButton.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#FF0A92DD"))

                floatingActionButton.imageTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        context?.let { ctx ->
            mStateManager = StateManager(ctx)
            val savedAcUnitState = mStateManager.getAcUnitState()

            if (savedAcUnitState != null) {
                mViewModel.acUnitState = savedAcUnitState
            } else {
                mViewModel.acUnitState =
                    ACUnitState(
                        mCircularSeekBar.progress.toInt(),
                        AutoModeState(),
                        AutoFanState(),
                        PowerOffState()
                    )
            }
        }

        textView3.text =
            HtmlCompat.fromHtml(
                "${mCircularSeekBar.progress.toInt()} &#8451;",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

        setupUIEvents()
        updateUIComponents()
    }


    private fun createViewModel() {
        val controlFragmentFactory = ControlFragmentViewModelFactory()
        mViewModel = ViewModelProviders.of(this, controlFragmentFactory)
            .get(ControlFragmentViewModel::class.java)
    }

    private fun setupListeners() {
        mViewModel.mainViewState.observe(this, Observer { viewState ->
            viewState.deviceResponse?.let { status ->
                if (status.success == true) {
                    txtOff.isEnabled = true
                    updateACState()
                    mViewModel.deviceEmit()
                }
            }
        })
    }

    private fun setupUIEvents() {
        cardMode.setOnClickListener {
            mViewModel.acUnitState?.modeState = mViewModel.acUnitState?.modeState?.nextState()
            updateUIComponents()
            updateACState()
        }

        cardSpeed.setOnClickListener {
            mViewModel.acUnitState?.fanState = mViewModel.acUnitState?.fanState?.nextState()
            updateUIComponents()
        }

        floatingActionButton.setOnClickListener {
            mViewModel.acUnitState?.powerState = mViewModel.acUnitState?.powerState?.nextState()
            updateUIComponents()
        }

        txtOff.setOnClickListener {
            mViewModel.setDeviceState()
            txtOff.isEnabled = false
        }

        mCircularSeekBar.setOnSeekArcChangeListener(object : ShaderSeekArc.OnSeekArcChangeListener {
            override fun onProgressChanged(p0: ShaderSeekArc?, p1: Float) {
                mViewModel.acUnitState?.acTemp = p1.toInt()
                textView3.text = HtmlCompat.fromHtml(
                    "<b>${p1.toInt()}</b> &#8451;",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }

            override fun onStartTrackingTouch(p0: ShaderSeekArc?) {
            }

            override fun onStopTrackingTouch(p0: ShaderSeekArc?) {

            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }
}