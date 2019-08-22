package com.mvp.accontrol.states

import android.content.Context
import com.mvp.accontrol.R

class NormalFanState : IState() {
    override fun stateResult(context: Context): String = context?.getString(R.string.normal)

    override fun stateValue(): Int = 3

    override fun nextState(): IState = HighFanState()
}