package com.mcleroy.wesocketslivedatasample.ui.common;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.mcleroy.wesocketslivedatasample.App;
import com.mcleroy.wesocketslivedatasample.R;
import com.mcleroy.wesocketslivedatasample.api.Constants;
import com.mcleroy.wesocketslivedatasample.models.pojo.SocketEventModel;
import com.mcleroy.wesocketslivedatasample.taskSchedulars.SocketReconnectionScheduler;
import com.mcleroy.wesocketslivedatasample.utils.DebugUtils;
import com.mcleroy.wesocketslivedatasample.utils.PreferenceUtils;

import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class SocketLiveData extends LiveData<SocketEventModel> {
    private static SocketLiveData instance = new SocketLiveData();
    private static WebSocket webSocket;
    private static AtomicBoolean disconnected = new AtomicBoolean(true);

    private SocketLiveData() {

    }

    public static SocketLiveData get() {
        return instance;
    }

    @Override
    protected synchronized void onActive() {
        super.onActive();
        connect();
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super SocketEventModel> observer) {
        super.observe(owner, observer);
        DebugUtils.debug(SocketLiveData.class, "Observing");
        connect();
    }

    public synchronized void connect() {
        try {
            DebugUtils.debug(SocketLiveData.class, "Attempting to connect");
            if (disconnected.compareAndSet(true, false)) {
                DebugUtils.debug(SocketLiveData.class, "Connecting...");
                String socketUrl = String.format("%s?%s", Constants.getSocketUrl(), String.format("deviceId=%s", PreferenceUtils.getDeviceId()));
                DebugUtils.debug(SocketLiveData.class, "Socket url: " + socketUrl);
                Request request = new Request.Builder().url(socketUrl)
                        .addHeader("deviceId", PreferenceUtils.getDeviceId()).build();
                webSocket = App.getOkHttpClient().newWebSocket(request, webSocketListener);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendEvent(SocketEventModel eventModel) {
        if (webSocket == null)return;
        setData(eventModel);
        webSocket.send(eventModel.toString());
    }

    public void setData(SocketEventModel socketEventModel) {
        if (socketEventModel == null || TextUtils.isEmpty(socketEventModel.getEvent())) return;
        postValue(socketEventModel);
    }

    private WebSocketListener webSocketListener = new WebSocketListener() {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            disconnected.set(false);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            handleEvent(text);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            postValue(new SocketEventModel(SocketEventModel.EVENT_OFFLINE, App.getContext().getString(R.string.socket_offline_message))
                    .setType(SocketEventModel.TYPE_INCOMING));
            disconnected.set(true);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            disconnected.set(true);
            int code = response != null ? response.code() : t != null ? 400 : 0;
            @Nullable String message = response != null ? response.message() : t != null ? t.getMessage() : "null";
            DebugUtils.debug(SocketLiveData.class, String.format("On Failure. Code: %s, message: %s", code, message));
            postValue(new SocketEventModel(SocketEventModel.EVENT_ERROR, code == 401? message : App.getContext().getString(R.string.socket_connection_error_message))
                    .setType(SocketEventModel.TYPE_INCOMING));
            if (code == 400 && message != null && !message.contains("closed")) {
                SocketReconnectionScheduler.schedule();
            }
        }
    };

    private synchronized void handleEvent(String message){
        try {
            SocketEventModel eventModel = SocketEventModel.fromJson(message, SocketEventModel.class)
                    .setType(SocketEventModel.TYPE_INCOMING);
            DebugUtils.debug(SocketLiveData.class, "Handling event: "+message);
            if (TextUtils.isEmpty(eventModel.getEvent()))
                throw new Exception("Invalid event model");
            processEvent(eventModel);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private synchronized void processEvent(SocketEventModel eventModel) throws Exception{
        DebugUtils.debug(SocketLiveData.class, "Processing event: "+eventModel.toString());
        postValue(eventModel);

    }
    @Override
    protected void onInactive() {
        super.onInactive();
        disconnect();
        DebugUtils.debug(SocketLiveData.class, "Inactive. Has observers observers? "+hasActiveObservers());
    }

    public boolean isDisconnected() {
        return disconnected.get();
    }

    public void disconnect() {
        if (!hasActiveObservers())
            if (webSocket != null)
                webSocket.close(1000, "Done using");
    }

}
