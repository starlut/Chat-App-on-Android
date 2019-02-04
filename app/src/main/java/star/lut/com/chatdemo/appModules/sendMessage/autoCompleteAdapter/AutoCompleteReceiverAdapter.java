package star.lut.com.chatdemo.appModules.sendMessage.autoCompleteAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import star.lut.com.chatdemo.R;
import star.lut.com.chatdemo.dataModels.UserInfo;

public class AutoCompleteReceiverAdapter extends ArrayAdapter<UserInfo> {
    private List<UserInfo> personModelsFull;
    private OnClickListerner listerner;

    public AutoCompleteReceiverAdapter(@NonNull Context context, @NonNull List<UserInfo> models, OnClickListerner onClickListerner) {
        super(context, 0, models);
        personModelsFull = new ArrayList<>(models);
        listerner = onClickListerner;
    }


    @Override
    public Filter getFilter() {
        return mFilter;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.ac_receiver_name, parent, false
            );
        }

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvPosition = convertView.findViewById(R.id.tvPosition);
        ImageView ivAvatar = convertView.findViewById(R.id.ivAvatar);

        UserInfo model = getItem(position);
        if (model != null){
            tvName.setText(model.fullName);
            tvPosition.setText(model.position);
            Glide.with(getContext())
                    .load(model.picture)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            e.printStackTrace();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(ivAvatar);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listerner.onClick(getItem(position));
                }
            });
        }
        return convertView;
    }

    private Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<UserInfo> suggestion = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){

            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (UserInfo model : personModelsFull) {
                    if (model.fullName.toLowerCase().contains(filterPattern)){
                        suggestion.add(model);
                    }
                }
            }

            results.values = suggestion;
            results.count = suggestion.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List)results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((UserInfo) resultValue).fullName;
        }
    };

    public interface OnClickListerner{
        void onClick(UserInfo model);
    }
}
