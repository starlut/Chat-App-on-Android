package star.lut.com.chatdemo.appModules.chatThreads.chatListAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import star.lut.com.chatdemo.R;
import star.lut.com.chatdemo.constants.ApiConstants;
import star.lut.com.chatdemo.dataModels.MessageThreadList;
import star.lut.com.chatdemo.dataModels.UserInfo;

/**
 * Created by kamrulhasan on 3/10/18.
 */
public class ChatListRvAdapter extends RecyclerView.Adapter<ChatListRvAdapter.ChatDetailsViewHolder>{
    private List<MessageThreadList> chatThreadModels;
    private List<UserInfo> userInfos;
    private Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String otherid, String fullName, String pic, String threadid);
    }

    public ChatListRvAdapter(Context context, List<MessageThreadList> chatThreadModels, OnItemClickListener listener, List<UserInfo> userInfos) {
        this.chatThreadModels = chatThreadModels;
        this.context = context;
        this.listener = listener;
        this.userInfos = userInfos;
    }

    class ChatDetailsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.cvChatThread) public ConstraintLayout cardView;
        @BindView(R.id.tvChatClientName) public TextView tvChatClientName;
        @BindView(R.id.tvLastChat) public TextView tvLastChat;
        @BindView(R.id.tvLastChatTime) public TextView tvLastChatTime;
        @BindView(R.id.ivSenderPic) public ImageView ivSenderPic;
        @BindView(R.id.clChatThread) public ConstraintLayout clChatThread;

        public ChatDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public ChatDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ChatDetailsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_chat_thread, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatDetailsViewHolder holder, final int i) {
        String otherId = " ";
        String fullName = " ";
        String pic = " ";
        String threadId = chatThreadModels.get(i).threadId;
        if (chatThreadModels.get(i) != null) {

            for (int x = 0 ; x < userInfos.size() ; x++){
                if (userInfos.get(x).uid.equals(chatThreadModels.get(i).otherId)){
                    holder.tvChatClientName.setText(userInfos.get(x).fullName);
                    otherId = userInfos.get(x).uid;
                    fullName = userInfos.get(x).fullName;
                    pic = userInfos.get(x).picture;
                    setAvatar(holder.ivSenderPic, userInfos.get(x).picture);
                }
            }
//            holder.tvChatClientName.setText(chatThreadModels.get(i).);
            holder.tvLastChat.setText(chatThreadModels.get(i).lastMessage);
            holder.tvLastChatTime.setText(chatThreadModels.get(i).lastMessageTime);
        }

        String finalOtherId = otherId;
        String finalFullName = fullName;
        String finalPic = pic;
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(finalOtherId, finalFullName, finalPic, threadId);
            }
        });
    }

    private void setAvatar(ImageView ivAvatar, String pic){
        Glide.with(context)
                .load(ApiConstants.BASE_URL+pic)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ivAvatar.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(ivAvatar);
    }

    @Override
    public int getItemCount() {
        return chatThreadModels.size();
    }
}
