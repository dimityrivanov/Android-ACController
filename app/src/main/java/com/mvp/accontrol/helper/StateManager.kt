package com.mvp.accontrol.helper

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.mvp.accontrol.ACUnitState
import com.mvp.accontrol.data.DeviceStateResponse
import com.mvp.accontrol.states.AutoModeState
import com.mvp.accontrol.states.CoolModeState
import com.mvp.accontrol.states.HeatModeState
import com.mvp.accontrol.states.IState


class StateManager(ctx: Context) {
    companion object {
        private const val PRIVATE_MODE = 0
        private const val PREF_NAME = "accontroller"
        private const val DEVICE = "DEVICEID"
        private const val EMPTY = ""
    }

    private var sharedPref: SharedPreferences

    init {
        sharedPref = ctx.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    }

    fun saveAcDataTemplate(acTemplate: AcDataTemplate): Boolean {
        return sharedPref.edit().putString(DEVICE, Gson().toJson(acTemplate))
            .commit()
    }

    fun getAcDataTemplate(): AcDataTemplate? {
        sharedPref.getString(DEVICE, EMPTY)?.let { json ->
            return Gson().fromJson(json, AcDataTemplate::class.java)
        }
        return null
    }

    //hardware device can be changed from mobile and google/alexa assistance
    //this method will handle the different state that wasn't passed threw the application
    fun fixDiffCheckSum(deviceState: DeviceStateResponse): Boolean {
        val currentState = getAcUnitState()

        currentState?.let {
            //power state is different,fix it!
            if (it.powerState?.stateValue() != deviceState.powerStateValue) {
                it.powerState =
                    it.powerState?.nextState()
            }

            //temp is different, fix it!
            if (it.acTemp != deviceState.temp) {
                deviceState.temp?.let { deviceTemp ->
                    it.acTemp = deviceTemp
                }
            }

            //fanMode is different, fix it!
            if (it.modeState?.stateValue() != deviceState.fanModeValue) {
                when (deviceState.fanModeValue) {
                    AutoModeState().stateValue() -> {
                        it.modeState = AutoModeState()
                    }

                    CoolModeState().stateValue() -> {
                        it.modeState = CoolModeState()
                    }

                    HeatModeState().stateValue() -> {
                        it.modeState = HeatModeState()
                    }
                }
            }

            //!! is ok here because we have validated above they are not null
            val currentCheckSum =
                it.acTemp + it.powerState!!.stateValue() + it.fanState!!.stateValue() + it.modeState!!.stateValue()

            if (deviceState.checksum == currentCheckSum) {
                //TODO: Workout other way to store the state object, this is not elegant
                val authTemplate = getAcDataTemplate()
                authTemplate?.ac_fanState = currentState.fanState?.javaClass.toString()
                authTemplate?.ac_modeState =
                    currentState.modeState?.javaClass.toString()
                authTemplate?.ac_temp = currentState.acTemp
                authTemplate?.ac_powerState =
                    currentState.powerState?.javaClass.toString()

                authTemplate?.let { template ->
                    saveAcDataTemplate(template)
                }

                return true
            }
        }
        return false
    }


    fun getAcUnitState(): ACUnitState? {
        val authTemplate = getAcDataTemplate()
        //TODO: Workout other way to store the state object, this is not elegant
        if (authTemplate?.ac_modeState != null && authTemplate.ac_fanState != null && authTemplate.ac_powerState != null) {
            val modeCls = Class.forName(authTemplate.ac_modeState?.split(' ')?.lastOrNull())
            val fanCls = Class.forName(authTemplate?.ac_fanState?.split(' ')?.lastOrNull())
            val powerCls = Class.forName(authTemplate?.ac_powerState?.split(' ')?.lastOrNull())

            val modeState = modeCls.newInstance() as IState
            val fanState = fanCls.newInstance() as IState
            val powerState = powerCls.newInstance() as IState

            return ACUnitState(
                authTemplate.ac_temp,
                modeState,
                fanState,
                powerState
            )

        } else {
            return null
        }
    }

}