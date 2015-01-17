package com.damytech.yilebang;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.CommService.CommMgr;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STPreferListItemInfo;
import com.damytech.STData.STServiceData;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-19
 * Time: 下午2:42
 * To change this template use File | Settings | File Templates.
 */
public class PreferentialListDetailActivity extends MyActivity {
    private PreferentialListAdapter mAdapter;
    private ListView listPrefer;

    private int mListId = 0;
    private ArrayList<STPreferListItemInfo> mPreferDatalist = new ArrayList<STPreferListItemInfo>(0);

    private JsonHttpResponseHandler handler;
    private ProgressDialog progDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferentiallistmain);

        mListId = getIntent().getExtras().getInt("ItemId");

        // initialize
        initControls();
        callGetCourtesyChildTypes(mListId);
    }

    /**
     * Initialize all controls
     */
    private void initControls()
    {
        ResolutionSet._instance.iterateChild(findViewById(R.id.rlPreferListMain));

        listPrefer = (ListView)findViewById(R.id.listMainItems);

        ImageView findView = (ImageView) findViewById(R.id.imageView);
        findView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get keyword to be find
                TextView txtFind = (TextView) findViewById(R.id.txtFind);
                String keyword = txtFind.getText().toString();
                // show prefer list activity
                Intent intentDetail = new Intent(PreferentialListDetailActivity.this, PreferentialListInfoActivity.class);
                intentDetail.putExtra("SearchMode", 0);  // search by keyword
                intentDetail.putExtra("Keyword", keyword);
                startActivity(intentDetail);
            }
        });

        initCartBadge();
        initMainMenu();
    }


    private void initMainMenu()
    {
        RelativeLayout rlBottom_Home = (RelativeLayout) findViewById(R.id.rlBottom_Home);
        rlBottom_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnHomeClicked();
            }
        });

        RelativeLayout rlBootom_Back = (RelativeLayout) findViewById(R.id.rlBottom_BackArraw);
        rlBootom_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });

        RelativeLayout rlBottom_MainMenu = (RelativeLayout) findViewById(R.id.rlBottom_MainMenu);
        rlBottom_MainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnMainMenuClicked();
            }
        });

        RelativeLayout rlBottom_Packet = (RelativeLayout) findViewById(R.id.rlBottom_Packet);
        rlBottom_Packet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnCartClicked();
            }
        });

        RelativeLayout rlBottom_PersonInfo = (RelativeLayout) findViewById(R.id.rlBottom_PersonInfo);
        rlBottom_PersonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnAccountClicked();
            }
        });
    }

    /**
     * Initialize badge icon of shopping cart
     */
    private void initCartBadge()
    {
        ImageView imgPacket = (ImageView) findViewById(R.id.imgBottom_Packet);
        this.setBadgeParent(imgPacket);
    }

    /**
     * Update UI using service data
     */
    private void updateUI()
    {
        listPrefer.setDivider(new ColorDrawable(Color.LTGRAY));
        listPrefer.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
        listPrefer.setDividerHeight(2);
        mAdapter = new PreferentialListAdapter(PreferentialListDetailActivity.this, this.getApplicationContext(), false);
        mAdapter.setData(mPreferDatalist);

        listPrefer.setAdapter(mAdapter);
    }


    /////////////////////////////////////// Service Relation //////////////////////////////////////
    /**
     * Call GetCourtesyChildTypes service
     */
    private void callGetCourtesyChildTypes(int parentId)
    {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseCourtesyChildTypes(object, mPreferDatalist);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    // update ui
                    updateUI();
                    return;
                }
                else
                {
                    progDialog.dismiss();
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(PreferentialListDetailActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(PreferentialListDetailActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                PreferentialListDetailActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetCourtesyChildTypes(parentId, handler);

        return;
    }
}
