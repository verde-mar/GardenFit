package it.unipi.gardenfit.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import io.reactivex.Single
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*

internal class BluetoothManagerImpl(private val adap: BluetoothAdapter) : BluetoothManagerr {
    private val devices: MutableMap<String, BluetoothSerialDeviceImpl> = mutableMapOf()

    override val pairedDevices: Collection<BluetoothDevice>
        @SuppressLint("MissingPermission")
        get() = adap.bondedDevices

    override fun openSerialDevice(mac: String): Single<BluetoothSerialDevice> {
        return openSerialDevice(mac, StandardCharsets.UTF_8)
    }

    @SuppressLint("MissingPermission")
    override fun openSerialDevice(mac: String, charset: Charset): Single<BluetoothSerialDevice> {
        return if (devices.containsKey(mac)) {
            Single.just(devices[mac]!!)
        } else {
                Single.fromCallable {
                    try {
                        val device = adap.getRemoteDevice(mac)
                        val socket = device.createInsecureRfcommSocketToServiceRecord(SPP_UUID)
                        adap.cancelDiscovery()
                        socket.connect()
                        val serialDevice = BluetoothSerialDeviceImpl(mac, socket, charset)
                        devices[mac] = serialDevice
                        return@fromCallable serialDevice
                    } catch (e: Exception) {
                        throw BluetoothConnectException(e)
                    }
                }
        }
    }

    override fun closeDevice(mac: String) {
        devices.remove(mac)?.close()
    }

    override fun closeDevice(device: BluetoothSerialDevice) {
        closeDevice(device.mac)
    }

    override fun closeDevice(deviceInterface: SimpleBluetoothDeviceInterface) {
        closeDevice(deviceInterface.device.mac)
    }

    override fun close() {
        for (device in devices.values) {
            try {
                device.close()
            } catch (ignored: Throwable) {
            }
        }
        devices.clear()
    }

    companion object {
        val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        val TAG = "BluetoothManager"
    }
}