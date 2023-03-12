package it.unipi.gardenfit.bluetooth

import io.reactivex.Flowable
import java.io.InputStream

interface BluetoothSerialDevice {
    /**
     * @return The MAC address of the closed bluetooth device
     */
    val mac: String

    /**
     * @return The underlying InputStream representing the device's output.
     * This can be used to manually manage the device's output.
     * This is useful in cases where the device does not send
     * a newline character at the end of each message, though this
     * situation may also be helped by using [openMessageStream] with a specific delimiter.
     * **Should not be used in conjunction with [openMessageStream]**
     */
    val inputStream: InputStream

    /**
     * @return An RxJava Flowable that, when observed,
     * will provide a stream of messages from the device.
     * A message is considered to be terminated by a
     * newline ('\n') character. If a newline is not
     * received, the message will continue buffering
     * forever. If this is not the desired behaviour,
     * please manage the input yourself via [inputStream]
     */
    fun openMessageStream(): Flowable<String>

    /**
     * Wrap using a SimpleBluetoothDeviceInterface.
     * This makes things a lot simpler within the class accessing this device.
     * Calling this method for the first time constructs the [SimpleBluetoothDeviceInterface],
     * which will immediately open a message stream.
     *
     * @return a SimpleBluetoothDeviceInterface that will access this device object
     */
    fun toSimpleDeviceInterface(): SimpleBluetoothDeviceInterface
}