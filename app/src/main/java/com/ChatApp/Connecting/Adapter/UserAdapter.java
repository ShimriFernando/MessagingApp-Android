package com.ChatApp.Connecting.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ChatApp.Connecting.MessageActivity;
import com.ChatApp.Connecting.Model.Chat;
import com.ChatApp.Connecting.Model.User;
import com.ChatApp.Connecting.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    android.content.Context mContext;
    List<User> mUsers;
    boolean isChat;
    String TheLastMessage;


    public UserAdapter(Context mContext, List<User> mUsers, boolean isChat) {

        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new ViewHolder(view);
    }
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = mUsers.get(position);
        holder.username.setText(user.getUsername());
        if (user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        } if (isChat){
            TheLastMessage(user.getId(),holder.last_msg);
        } else{
            holder.last_msg.setVisibility(View.GONE);
        }
        if (isChat) {
            if (user.getStatus().equals("online")) {
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.VISIBLE);
            }
        } else {
        holder.img_on.setVisibility(View.GONE);
        holder.img_off.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(mContext, MessageActivity.class);
            intent.putExtra("userid",user.getId());
            mContext.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mUsers.size();
    }
    protected static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        public TextView last_msg;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            username = itemView.findViewById(R.id.Username);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_msg = itemView.findViewById(R.id.last_msg);
        }
    }
    private void TheLastMessage(final String userid, final TextView last_msg){
        final String[] TheLastMessage = {"default"};
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    assert chat != null;
                    assert firebaseUser != null;
                    if ((chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid))||(chat.getSender().equals(firebaseUser.getUid()) && chat.getReceiver().equals(userid))){
                         TheLastMessage[0] = chat.getMessage();
                    }
                }
                if ("default".equals(TheLastMessage[0])) {
                    last_msg.setText(R.string.NoMessage);
                } else {
                    last_msg.setText(TheLastMessage[0]);
                }
                TheLastMessage[0] = "default";
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
