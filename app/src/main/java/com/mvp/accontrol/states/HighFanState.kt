package com.mvp.accontrol.states

import android.content.Context
import com.mvp.accontrol.R

class HighFanState : IState() {
    override fun stateResult(context: Context): String = context?.getString(R.string.high)

    override fun stateValue(): Int = 4

    override fun nextState(): IState = AutoFanState()
}