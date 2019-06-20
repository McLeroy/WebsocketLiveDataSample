package com.mcleroy.wesocketslivedatasample.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mcleroy.wesocketslivedatasample.R;
import com.mcleroy.wesocketslivedatasample.models.pojo.SocketEventModel;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageHolder>{

    private final Context context;
    private List<SocketEventModel>socketEventModels;

    MessagesAdapter(Context context) {
        this.context = context;
        socketEventModels = new ArrayList<>();
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == SocketEventModel.TYPE_OUTGOING)
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_message_outgoing, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_message_incoming, parent, false);
        return new MessageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        SocketEventModel socketEventModel = socketEventModels.get(position);
        holder.eventText.setText(String.format("%s: %s", context.getString(R.string.event), socketEventModel.getEvent()));
        holder.messageText.setText(socketEventModel.getPayloadAsString());
    }

    @Override
    public int getItemCount() {
        return socketEventModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        SocketEventModel socketEventModel = socketEventModels.get(position);
        return socketEventModel.getType();
    }

    int addToList(SocketEventModel socketEventModel) {
        socketEventModels.add(socketEventModel);
        int index = socketEventModels.indexOf(socketEventModel);
        notifyItemInserted(index);
        return index;
    }

    final class MessageHolder extends RecyclerView.ViewHolder {

        private TextView eventText;
        private TextView messageText;

        MessageHolder(@NonNull View itemView) {
            super(itemView);
            eventText = itemView.findViewById(R.id.event_text);
            messageText = itemView.findViewById(R.id.message_text);
        }
    }
}
