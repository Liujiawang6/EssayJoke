// IMessageListener.aidl
package com.qiyei.sdk.server.base;

// Declare any non-default types here with import statements

import com.qiyei.sdk.server.base.BMessage;

interface IMessageListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onProcessMessage(in BMessage message);

}
