package com.sinotrans.samsung.activity;

import java.io.IOException;


import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;


public class MyAndroidHttpTransport extends HttpTransportSE {

    private int timeout = 10000; // 默认超时时间为30s

    public MyAndroidHttpTransport(String url) {
        super(url);
    }

    public MyAndroidHttpTransport(String url, int timeout) {
        super(url);
        this.timeout = timeout;
    }

    @Override
    protected ServiceConnection getServiceConnection() throws IOException {
        ServiceConnectionSE serviceConnection = new ServiceConnectionSE(url);
        serviceConnection.setConnectionTimeOut(timeout);

        return serviceConnection;
    }
}