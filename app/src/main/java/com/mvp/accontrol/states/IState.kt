package com.mvp.accontrol.states

import android.content.Context


abstract class IState {
    //to support multilanguage at some point
    abstract fun stateResult(context: Context): String
    //state value comes from the hardare device enums,also used to calculate checksum
    abstract fun stateValue(): Int
    //looping threw the actual ui states without making an array and hardcoding them in it
    abstract fun nextState(): IState
}


