package com.sinotrans.samsung.activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.ksoap2.transport.ServiceConnection;

public class ServiceConnectionSE implements ServiceConnection {
    private HttpURLConnection connection;

    public ServiceConnectionSE(String url) throws IOException {
        this.connection = ((HttpURLConnection) new URL(url).openConnection());
        this.connection.setUseCaches(false);
        this.connection.setDoOutput(true);
        this.connection.setDoInput(true);
    }

    public void connect() throws IOException {
        this.connection.connect();
    }

    public void disconnect() {
        this.connection.disconnect();
    }

    public void setRequestProperty(String string, String soapAction) {
        this.connection.setRequestProperty(string, soapAction);
    }

    public void setRequestMethod(String requestMethod) throws IOException {
        this.connection.setRequestMethod(requestMethod);
    }

    public OutputStream openOutputStream() throws IOException {
        return this.connection.getOutputStream();
    }

    public InputStream openInputStream() throws IOException {
        return this.connection.getInputStream();
    }

    public InputStream getErrorStream() {
        return this.connection.getErrorStream();
    }

    // 锟斤拷锟斤拷锟斤拷锟接凤拷锟斤拷锟斤拷锟侥筹拷时时锟斤拷,锟斤拷锟诫级,锟斤拷为锟皆硷拷锟斤拷拥姆锟斤拷锟�
    public void setConnectionTimeOut(int timeout) {
        this.connection.setConnectTimeout(timeout);
    }

	@Override
	public String getHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List getResponseProperties() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
