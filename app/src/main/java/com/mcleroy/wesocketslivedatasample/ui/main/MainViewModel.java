package com.mcleroy.wesocketslivedatasample.ui.main;

import androidx.lifecycle.ViewModel;

import com.mcleroy.wesocketslivedatasample.ui.common.SocketLiveData;

public class MainViewModel extends ViewModel {

    private SocketLiveData socketLiveData;

    public MainViewModel() {
        socketLiveData = SocketLiveData.get();
    }

    public SocketLiveData getSocketLiveData() {
        return socketLiveData;
    }
}
