package it.unipi.gardenfit.bluetooth

import android.bluetooth.BluetoothSocket
import android.text.TextUtils
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.concurrent.atomic.AtomicBoolean

internal class BluetoothSerialDeviceImpl constructor(
    override val mac: String,
    private val socket: BluetoothSocket,
    private val charset: Charset
) : BluetoothSerialDevice {
    private val closed = AtomicBoolean(false)
    override val inputStream: InputStream = socket.inputStream

    private var owner: SimpleBluetoothDeviceInterfaceImpl? = null

    override fun openMessageStream(): Flowable<String> {
        checkNotClosed()
        return Flowable.create({ emitter ->
            val reader = BufferedReader(InputStreamReader(inputStream, charset))
            while (!emitter.isCancelled && !closed.get()) {
                synchronized(inputStream) {
                    try {
                        val receivedString = reader.readLine()
                        if (!TextUtils.isEmpty(receivedString)) {
                            emitter.onNext(receivedString)
                        }
                    } catch (e: Exception) {
                        if (!emitter.isCancelled && !closed.get()) {
                            emitter.onError(e)
                        }
                    }
                }
            }
            emitter.onComplete()
        }, BackpressureStrategy.BUFFER)
    }

    fun close() {
        if (!closed.get()) {
            closed.set(true)
            inputStream.close()
            socket.close()
        }
        owner?.close()
        owner = null
    }

    override fun toSimpleDeviceInterface(): SimpleBluetoothDeviceInterfaceImpl {
        checkNotClosed()
        owner?.let { return it }
        val newOwner = SimpleBluetoothDeviceInterfaceImpl(this)
        owner = newOwner
        return newOwner
    }

    /**
     * Checks that this instance has not been closed
     */
    fun checkNotClosed() {
        check(!closed.get()) { "Device connection closed" }
    }
}