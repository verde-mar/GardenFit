package it.unipi.gardenfit.bluetooth

interface SimpleBluetoothDeviceInterface {
    /**
     * @return The BluetoothSerialDevice instance that the interface is wrapping.
     */
    val device: BluetoothSerialDevice

    /**
     * Set all of the listeners for the interfact
     *
     * @param messageReceivedListener Receive message callback
     * @param errorListener Error callback
     */
    fun setListeners(messageReceivedListener: OnMessageReceivedListener?,
                     errorListener: OnErrorListener?)

    /**
     * Set the message received listener
     *
     * @param listener Receive message callback
     */
    fun setMessageReceivedListener(listener: OnMessageReceivedListener?)


    /**
     * Set the error listener
     *
     * @param listener Error callback
     */
    fun setErrorListener(listener: OnErrorListener?)

    interface OnMessageReceivedListener {
        fun onMessageReceived(message: String)
    }


    interface OnErrorListener {
        fun onError(error: Throwable)
    }
}