package com.lixinyuyin.monosyllabicdetect.activity.pure;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lixinyuyin.monosyllabicdetect.R;
import com.lixinyuyin.monosyllabicdetect.database.graphdata.GraphDataDao;
import com.lixinyuyin.monosyllabicdetect.util.StatusBarUtil;
import com.lixinyuyin.monosyllabicdetect.view.GraphData;

import java.util.List;

/**
 * Created by Administrator on 2015/7/29.
 */
public class HistoryRecordActivity extends Activity implements AdapterView.OnItemClickListener {

    ListView mRecordListView;
    Button backButton;
    private RecordListAdapter mAdapter;
    private List<GraphData> mGraphDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_record_activity);
        StatusBarUtil.setStatusBarColor(this);
        initView();
        initData();
    }


    private void initView() {
        mRecordListView = (ListView) findViewById(R.id.listview);
        backButton = (Button) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        GraphDataDao mDataDao = new GraphDataDao(this);
        mGraphDataList = mDataDao.getAll(PureToneTestActivity.mUserName);
        mDataDao.close();
        mAdapter = new RecordListAdapter(this, mGraphDataList);
        mRecordListView.setAdapter(mAdapter);
        mRecordListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HistoryDetailsActivity.start(this, mGraphDataList.get(position));
    }

    @Override
    protected void onResume() {
        super.onResume();
        GraphDataDao mDataDao = new GraphDataDao(this);
        mGraphDataList.clear();
        mGraphDataList.addAll(mDataDao.getAll(PureToneTestActivity.mUserName));
        mAdapter.notifyDataSetChanged();
    }

    private class RecordListAdapter extends BaseAdapter {

        private List<GraphData> mList;
        private Context mContext;

        public RecordListAdapter(Context context, List<GraphData> list) {
            mList = list;
            mContext = context;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (null == convertView) {
                convertView = View.inflate(mContext, R.layout.listview_item_history_record, null);
                viewHolder = new ViewHolder();
                viewHolder.yearTextView = (TextView) convertView.findViewById(R.id.textview_year_info);
                viewHolder.dayTextView = (TextView) convertView.findViewById(R.id.textview_day_info);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.yearTextView.setText(mList.get(position).getYearString());
            viewHolder.dayTextView.setText(mList.get(position).getDayString());
            return convertView;
        }

        class ViewHolder {
            TextView yearTextView;
            TextView dayTextView;
        }
    }


    /**
     * Start this activity
     */
    public static void start(Context context) {
        Intent intent = new Intent(context, HistoryRecordActivity.class);
        context.startActivity(intent);
    }
}
