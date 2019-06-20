package com.mcleroy.wesocketslivedatasample.ui.splash;

import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.TimeUnit;

public class SplashViewModel extends ViewModel {

    private CountDownLiveData countDownLiveData;

    public SplashViewModel() {
        countDownLiveData = new CountDownLiveData();
    }

    public CountDownLiveData getCountDownLiveData() {
        return countDownLiveData;
    }

    public static final class CountDownLiveData extends MutableLiveData<Long> {

        private CountDownTimer countDownTimer;

        public CountDownLiveData() {
            countDownTimer = new CountDownTimer(TimeUnit.SECONDS.toMillis(2), 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    postValue(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    postValue(0L);
                }
            };
        }

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super Long> observer) {
            super.observe(owner, observer);
            countDownTimer.start();
        }
    }
}
