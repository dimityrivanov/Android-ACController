package com.mvp.accontrol.states

import android.content.Context
import com.mvp.accontrol.R

class PowerOnState : IState() {
    override fun stateResult(context: Context): String = context.getString(R.string.ac_on)

    override fun stateValue(): Int = 1

    override fun nextState(): IState = PowerOffState()
}