package com.lixinyuyin.monosyllabicdetect.activity.pure;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lixinyuyin.monosyllabicdetect.R;
import com.lixinyuyin.monosyllabicdetect.database.graphdata.GraphDataDao;
import com.lixinyuyin.monosyllabicdetect.util.StatusBarUtil;
import com.lixinyuyin.monosyllabicdetect.view.GraphData;
import com.lixinyuyin.monosyllabicdetect.view.PureToneGraph;
import com.lixinyuyin.monosyllabicdetect.view.MaterialDialog;

/**
 * Created by Administrator on 2015/7/30.
 */
public class HistoryDetailsActivity extends Activity {

    PureToneGraph pureToneGraph;
    TextView dateTextView;
    Button backButton;
    ImageView deleteImageView;
    public static GraphData mGraphData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_record_details_activity);
        StatusBarUtil.setStatusBarColor(this);
        initView();
    }

    private void initView() {
        backButton = (Button) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pureToneGraph = (PureToneGraph) findViewById(R.id.gridgraph);
        dateTextView = (TextView) findViewById(R.id.textview_date);
        if (null != mGraphData) {
            pureToneGraph.setGraph(mGraphData);
            dateTextView.setText(mGraphData.getDateString() + "by" + mGraphData.getName());
        }
        deleteImageView = (ImageView) findViewById(R.id.imageview_delete);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialDialog dialog = new MaterialDialog(HistoryDetailsActivity.this);
                dialog.show();
                dialog.setTitle(R.string.delete);
                dialog.setMessage(R.string.cancel_message);
                dialog.setCancelButton(R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setSureButton(R.string.sure, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GraphDataDao graphDataDao = new GraphDataDao(HistoryDetailsActivity.this);
                        graphDataDao.deleteByDate(String.valueOf(mGraphData.getDate()));
                        Toast.makeText(HistoryDetailsActivity.this, "Delete Succeed!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

    public static void start(Context context, GraphData graphData) {
        mGraphData = graphData;
        Intent intent = new Intent(context, HistoryDetailsActivity.class);
        context.startActivity(intent);
    }
}
