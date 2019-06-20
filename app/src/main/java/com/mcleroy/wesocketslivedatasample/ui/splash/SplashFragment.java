package com.mcleroy.wesocketslivedatasample.ui.splash;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcleroy.wesocketslivedatasample.R;
import com.mcleroy.wesocketslivedatasample.utils.DebugUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends Fragment {

    private SplashFragmentCallbacks fragmentCallbacks;

    public SplashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        SplashViewModel splashViewModel = ViewModelProviders.of(this).get(SplashViewModel.class);
        splashViewModel.getCountDownLiveData().observe(this, countDownObserver);
    }

    private Observer<Long> countDownObserver = new Observer<Long>() {
        @Override
        public void onChanged(Long millisUntilFinished) {
            DebugUtils.debug(SplashFragment.class, "Counting down: "+millisUntilFinished);
            if (millisUntilFinished == 0)
                fragmentCallbacks.countDownComplete();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentCallbacks = (SplashFragmentCallbacks) context;
        }catch (ClassCastException e) {
            throw new ClassCastException("Must implement SplashFragmentCallbacks: "+e);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentCallbacks = null;
    }

    public interface SplashFragmentCallbacks {
        void countDownComplete();
    }

}
