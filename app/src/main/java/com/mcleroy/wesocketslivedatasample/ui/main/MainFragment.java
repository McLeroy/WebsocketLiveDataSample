package com.mcleroy.wesocketslivedatasample.ui.main;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcleroy.wesocketslivedatasample.R;
import com.mcleroy.wesocketslivedatasample.models.pojo.SocketEventModel;
import com.mcleroy.wesocketslivedatasample.utils.DebugUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private View rootView;
    private TextView contentText;

    private MainViewModel mainViewModel;
    private MessagesAdapter messageAdapter;
    private LinearLayoutManager linearLayoutManager;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return rootView = inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        contentText = rootView.findViewById(R.id.content_text);
        recyclerView.setLayoutManager(linearLayoutManager = new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(messageAdapter = new MessagesAdapter(getContext()));
        linearLayoutManager.setStackFromEnd(true);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getSocketLiveData().observe(this, socketEventModelObserver);
        mainViewModel.getSocketLiveData().connect();
        rootView.findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String text = contentText.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;
        contentText.setText("");
        mainViewModel.getSocketLiveData().sendEvent(
                new SocketEventModel(SocketEventModel.EVENT_MESSAGE, text)
                .setType(SocketEventModel.TYPE_OUTGOING)
        );
    }

    private Observer<SocketEventModel>socketEventModelObserver = new Observer<SocketEventModel>() {
        @Override
        public void onChanged(SocketEventModel socketEventModel) {
            DebugUtils.debug(MainFragment.class, "New socket event: "+socketEventModel.toString());
            int index = messageAdapter.addToList(socketEventModel);
            linearLayoutManager.scrollToPosition(index);
        }
    };

}
