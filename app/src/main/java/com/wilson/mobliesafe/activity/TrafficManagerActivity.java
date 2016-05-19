package com.wilson.mobliesafe.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wilson.mobliesafe.R;
import com.wilson.mobliesafe.bean.TrafficInfo;
import com.wilson.mobliesafe.engine.TrafficManagerParser;
import com.wilson.mobliesafe.utils.TextFormat;

public class TrafficManagerActivity extends Activity {
    protected static final int SUCCESS_GET_TRAFFICINFO = 0;
    protected static final int UPDATE_DISPLAY = 1;
    private ListView lv_traffic_manager_content;
    private TrafficManagerParser trafficManagerParser;
    private List<TrafficInfo> realTrafficInfos;
    private TrafficManagerAdapter mAdapter;
    private Timer timer = null;
    private TimerTask task = new TimerTask() {

        @Override
        public void run() {
            Message msg = new Message();
            msg.what = UPDATE_DISPLAY;
            mHandler.sendMessage(msg);
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SUCCESS_GET_TRAFFICINFO:
                    mAdapter = new TrafficManagerAdapter(getApplicationContext());
                    lv_traffic_manager_content.setAdapter(mAdapter);
                    timer = new Timer();
                    timer.schedule(task, 0, 2000);
                    break;
                case UPDATE_DISPLAY:
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic_manager);

/*        2g3g 接收流量
        TrafficStats.getMobileRxBytes();
        //2g/3g 接收的包
        TrafficStats.getMobileRxPackets();
        //2g/3g 上传的流量
        TrafficStats.getMobileTxBytes();
        //2g/3g 上传的包
        TrafficStats.getMobileTxPackets();
        // 手机总共接收的流量
        TrafficStats.getTotalRxBytes();
        // 手机总共上传的流量
        TrafficStats.getTotalTxBytes();
        //得到么个应用程序接收的流量
        TrafficStats.getUidRxBytes(uid);
        TrafficStats.getUidTxBytes(uid);*/

        TextView tv_traffic_manager_mobile = (TextView) findViewById(R.id.tv_traffic_manager_mobile);
        TextView tv_traffic_manager_wifi = (TextView) findViewById(R.id.tv_traffic_manager_wifi);
        lv_traffic_manager_content = (ListView) findViewById(R.id.lv_traffic_manager_content);

        tv_traffic_manager_mobile.setText(TextFormat.formatByte(getMobileTotal()));
        tv_traffic_manager_wifi.setText(TextFormat.formatByte(getWifiTotal()));

        trafficManagerParser = new TrafficManagerParser(this);

        new Thread() {
            public void run() {
//        		trafficInfos = trafficManagerService.getLauncherTrafficInfos();
                realTrafficInfos = trafficManagerParser.getInternetTrafficInfos();
/*        		realTrafficInfos = new ArrayList<TrafficInfo>();
                for(TrafficInfo info:trafficInfos){
        			if(TrafficStats.getUidRxBytes(info.getUid()) == -1 && TrafficStats.getUidTxBytes(info.getUid()) == -1){
        			}else{
        				realTrafficInfos.add(info);
        			}
        		}*/
                Message msg = new Message();
                msg.what = SUCCESS_GET_TRAFFICINFO;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 2g/3g的总流量
     *
     * @return 2g/3g的总流量
     */
    private long getMobileTotal() {
        long mobile_rx = TrafficStats.getMobileRxBytes();
        long mobile_tx = TrafficStats.getMobileTxBytes();
        return mobile_rx + mobile_tx;
    }

    /**
     * 得到手机的总流量
     *
     * @return 手机的总流量
     */
    private long getTotal() {
        return TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
    }

    /**
     * 得到wifi的总流量
     *
     * @return wifi的总流量
     */
    private long getWifiTotal() {
        return getTotal() - getMobileTotal();
    }


    static class ViewHolder {
        ImageView iv_appicon;
        TextView tv_appname;
        TextView tv_apptraffic;
        TextView tv_apptx;
        TextView tv_apprx;
    }

    private final class TrafficManagerAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public TrafficManagerAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return realTrafficInfos.size();
        }

        public Object getItem(int position) {
            return realTrafficInfos.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = mInflater.inflate(R.layout.traffic_manager_item, null);
                holder = new ViewHolder();
                holder.iv_appicon = (ImageView) convertView.findViewById(R.id.iv_appicon);
                holder.tv_appname = (TextView) convertView.findViewById(R.id.tv_appname);
                holder.tv_apptraffic = (TextView) convertView.findViewById(R.id.tv_apptarffic);
                holder.tv_apptx = (TextView) convertView.findViewById(R.id.tv_apptx);
                holder.tv_apprx = (TextView) convertView.findViewById(R.id.tv_apprx);
                convertView.setTag(holder);
            }
            TrafficInfo trafficInfo = realTrafficInfos.get(position);
            holder.iv_appicon.setImageDrawable(trafficInfo.getAppicon());
            String name = trafficInfo.getAppname();
            String realname;
            if (name.length() > 8) {
                realname = name.substring(0, 7) + "...";
            } else {
                realname = name;
            }
            holder.tv_appname.setText(realname);
            int uid = trafficInfo.getUid();
            long tx = TrafficStats.getUidTxBytes(uid);
            if (tx == -1) {
                tx = 0;
            }
            long rx = TrafficStats.getUidRxBytes(uid);
            if (rx == -1) {
                rx = 0;
            }
            long total = tx + rx;
            holder.tv_apptraffic.setText(TextFormat.formatByte(total));
            holder.tv_apptx.setText("上传：" + TextFormat.formatByte(tx));
            holder.tv_apprx.setText("下载：" + TextFormat.formatByte(rx));
            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            task = null;
        }
    }
}