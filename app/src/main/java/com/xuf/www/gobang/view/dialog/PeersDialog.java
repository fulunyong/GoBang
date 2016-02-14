package com.xuf.www.gobang.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.peak.salut.SalutDevice;
import com.xuf.www.gobang.R;
import com.xuf.www.gobang.util.EventBus.BusProvider;
import com.xuf.www.gobang.util.EventBus.WifiConnectPeerEvent;
import com.xuf.www.gobang.util.EventBus.WifiCancelPeerEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenov0 on 2015/12/27.
 */
public class PeersDialog extends BaseDialog {

    public static final String TAG = "PeersDialog";

    private ListView mListView;
    private DeviceAdapter mAdapter;
    private List<SalutDevice> mData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_peer_list, container, false);

        mListView = (ListView)view.findViewById(R.id.lv_peers);
        mAdapter = new DeviceAdapter();
        mListView.setAdapter(mAdapter);

        Button cancel = (Button)view.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.getInstance().post(new WifiCancelPeerEvent());
            }
        });

        return view;
    }

    public void updatePeers(List<SalutDevice> data){
        mAdapter.setData(data);
    }

    private class DeviceAdapter extends BaseAdapter{
        public void setData(List<SalutDevice> data){
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null){
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_device, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }

            String device = mData.get(position).deviceName;
            holder = (ViewHolder)convertView.getTag();
            holder.device.setText(device);
            holder.device.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BusProvider.getInstance().post(new WifiConnectPeerEvent(mData.get(position)));
                }
            });

            return convertView;
        }

        private class ViewHolder{
            public TextView device;

            public ViewHolder(View view){
                device = (TextView)view.findViewById(R.id.tv_device);
            }
        }
    }
}