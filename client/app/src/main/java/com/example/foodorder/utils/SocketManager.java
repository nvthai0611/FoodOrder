package com.example.foodorder.utils;

import android.util.Log;

import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketManager {
    private static SocketManager instance;
    private Socket mSocket;

    private SocketManager() {
        try {
            IO.Options opts = IO.Options.builder()
                    .setReconnection(true)
                    .setReconnectionAttempts(5)
                    .build();

            mSocket = IO.socket("http://192.168.0.103:9999", opts);

            mSocket.on(Socket.EVENT_CONNECT, args ->
                    Log.d("SOCKET", "Connected: " + mSocket.id()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized SocketManager getInstance() {
        if (instance == null) {
            instance = new SocketManager();
        }
        return instance;
    }

    public void connect() {
        if (mSocket != null && !mSocket.connected()) {
            mSocket.connect();
        }
    }

    public void disconnect() {
        if (mSocket != null) {
            mSocket.disconnect();
        }
    }

    public void emit(String event, Object data) {
        if (mSocket != null && mSocket.connected()) {
            mSocket.emit(event, data);
        }
    }

    public void on(String event, Emitter.Listener listener) {
        if (mSocket != null) {
            mSocket.on(event, listener);
        }
    }

    public void off(String event, Emitter.Listener listener) {
        if (mSocket != null) {
            mSocket.off(event, listener);
        }
    }

    public boolean isConnected() {
        return mSocket != null && mSocket.connected();
    }

    public Socket getSocket() {
        return mSocket;
    }
}

