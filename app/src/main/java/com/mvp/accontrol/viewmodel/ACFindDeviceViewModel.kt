package com.mvp.accontrol.viewmodel

import android.net.wifi.WifiManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mvp.accontrol.data.DeviceIDResponse
import com.mvp.accontrol.data.DeviceStateResponse
import com.mvp.accontrol.data.service.ACClimateService
import com.mvp.accontrol.helper.RetrofitRestClient
import com.mvp.accontrol.helper.SafeRequestExecutor.execute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class ACFindDeviceViewModel(
    private val viewState: ACFindDeviceViewState
) : BaseViewModel() {

    val mainViewState = MutableLiveData<ACFindDeviceViewState>().apply {
        value = viewState
    }

    private val mClimateService: ACClimateService by lazy {
        RetrofitRestClient.createService(ACClimateService::class.java)
    }


    fun findSSDPDevices(wifi: WifiManager) {

        mUiScope.launch {

            val lock = wifi.createMulticastLock("The Lock")
            var socket: DatagramSocket? = null
            var addressFound: String = ""

            async(Dispatchers.IO) {

                lock.acquire()

                try {
                    val group = InetAddress.getByName("239.255.255.250")
                    val port = 1900
                    val query = "M-SEARCH * HTTP/1.1\r\n" +
                            "HOST: 239.255.255.250:1900\r\n" +
                            "MAN: \"ssdp:discover\"\r\n" +
                            "MX: 1\r\n" +
                            "ST: urn:schemas-upnp-org:device:ACControl:1\r\n" +
                            //"ST: urn:schemas-upnp-org:service:AVTransport:1\r\n"+  // Use for Sonos
                            //"ST: ssdp:all\r\n" +  // Use this for all UPnP Devices
                            "\r\n"

                    socket = DatagramSocket(port)

                    socket?.reuseAddress = true

                    val dgram = DatagramPacket(
                        query.toByteArray(), query.length,
                        group, port
                    )
                    socket?.send(dgram)

                    var count = 0

                    while (count < 10000) {
                        val p = DatagramPacket(ByteArray(12), 12)
                        socket?.receive(p)

                        val s = String(p.data, 0, p.length)
                        if (s.toUpperCase() == "HTTP/1.1 200") {
                            p.address.hostAddress.split(".").lastOrNull()?.let {
                                if (it.length > 1) {
                                    //addresses.postValue(p.address.hostAddress)
                                    addressFound = p.address.hostAddress
                                    socket?.let {
                                        it.close()
                                    }

                                    synchronized(lock) {
                                        try {
                                            lock?.release()
                                        } catch (ex: Exception) {

                                        }
                                    }
                                }
                            }
                        }
                        count++
                    }
                } catch (exception: Exception) {
                    Log.d("Test", "Closing")
                    socket?.let {
                        it.close()
                    }

                    synchronized(lock) {
                        try {
                            lock?.release()
                        } catch (ex: Exception) {

                        }
                    }


                } finally {
                    Log.d("Test", "Closing")
                    socket?.let {
                        it.close()
                    }

                    synchronized(lock) {
                        try {
                            lock?.release()
                        } catch (ex: Exception) {

                        }
                    }
                }
            }.await()

            mainViewState.value = viewState.copy(address = addressFound)
        }
    }

    fun getDeviceID() {
        mUiScope.launch {
            val deviceID = async(Dispatchers.IO) {
                return@async execute(mClimateService.getDeviceID())
            }.await() as? DeviceIDResponse

            mainViewState.value = viewState.copy(deviceID = deviceID)
        }
    }

    fun getDeviceState() {
        mUiScope.launch {
            val deviceState = async(Dispatchers.IO) {
                return@async execute(mClimateService.getDeviceState())
            }.await() as? DeviceStateResponse

            mainViewState.value = viewState.copy(deviceState = deviceState)
        }
    }

}