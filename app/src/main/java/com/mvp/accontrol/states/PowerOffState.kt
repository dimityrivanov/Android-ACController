package com.mvp.accontrol.states

import android.content.Context
import com.mvp.accontrol.R

class PowerOffState : IState() {
    override fun stateResult(context: Context): String = context.getString(R.string.ac_off)

    override fun stateValue(): Int = 0

    override fun nextState(): IState = PowerOnState()

}
