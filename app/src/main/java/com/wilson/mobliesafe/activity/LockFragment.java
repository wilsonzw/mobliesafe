package com.wilson.mobliesafe.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wilson.mobliesafe.R;
import com.wilson.mobliesafe.bean.AppInfo;
import com.wilson.mobliesafe.db.dao.AppLockDao;
import com.wilson.mobliesafe.engine.AppInfoParser;

import java.util.ArrayList;
import java.util.List;

public class LockFragment extends Fragment {
    private ListView list_view;
    private TextView tv_lock;
    private List<AppInfo> lockLists = new ArrayList<>();
    private AppLockDao dao;
    private LockAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_lock_fragment, null);
        list_view = (ListView) view.findViewById(R.id.list_view);
        tv_lock = (TextView) view.findViewById(R.id.tv_lock);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //拿到所有的应用程序
        List<AppInfo> appInfos = AppInfoParser.getAppInfos(getActivity());
        //初始化一个加锁的集合
        dao = new AppLockDao(getActivity());
        lockLists.clear();
        for (AppInfo appInfo : appInfos) {
            //如果能找到当前的包名说明在程序锁的数据库里面
            if (dao.find(appInfo.getApkPackageName())) {
                lockLists.add(appInfo);
            }
        }
        adapter = new LockAdapter();
        list_view.setAdapter(adapter);
        tv_lock.setText("已加锁(" + lockLists.size() + ")个");
    }


    private class LockAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return lockLists.size();
        }

        @Override
        public Object getItem(int position) {
            return lockLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_lock, null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.iv_lock = (ImageView) convertView.findViewById(R.id.iv_lock);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final View view = convertView;
            final AppInfo appInfo = lockLists.get(position);
            holder.iv_icon.setImageDrawable(appInfo.getIcon());
            holder.tv_name.setText(appInfo.getApkName());
            holder.iv_lock.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                    translateAnimation.setDuration(5000);
                    view.startAnimation(translateAnimation);
                    new Thread() {
                        public void run() {

                            SystemClock.sleep(5000);

                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    dao.delete(appInfo.getApkPackageName());
                                    lockLists.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }.start();
                }
            });
            return convertView;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            tv_lock.setText("已加锁(" + lockLists.size() + ")个");
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        ImageView iv_lock;
    }

}
