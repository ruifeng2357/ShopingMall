package com.damytech.yilebang;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
 * Date: 13-11-18
 * Time: 下午11:46
 * To change this template use File | Settings | File Templates.
 */
public class PreferentialListMainActivity extends MyActivity {
    private PreferentialListAdapter mAdapter;
    private ListView listPrefer;

    private ArrayList<STPreferListItemInfo> mPreferDatalist = new ArrayList<STPreferListItemInfo>(0);

    private JsonHttpResponseHandler handler;
    private ProgressDialog progDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferentiallistmain);

        // initialize
        initControls();
        // call service
        callGetCourtesyParentTypes();
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
                startFind();
            }
        });

        initCartBadge();
        initMainMenu();
        initFindTextView();
    }

    private void startFind()
    {
        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        // get keyword to be find
        TextView txtFind = (TextView) findViewById(R.id.txtFind);
        String keyword = txtFind.getText().toString();
        // show prefer list activity
        Intent intentDetail = new Intent(PreferentialListMainActivity.this, PreferentialListInfoActivity.class);
        intentDetail.putExtra("SearchMode", 0);  // search by keyword
        intentDetail.putExtra("Keyword", keyword);
        startActivity(intentDetail);
    }

    private void initFindTextView()
    {
        // get keyword to be find
        TextView editText = (TextView) findViewById(R.id.txtFind);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startFind();
                    return true;
                }
                return false;
            }
        });
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
        mAdapter = new PreferentialListAdapter(PreferentialListMainActivity.this, this.getApplicationContext(), true);
        mAdapter.setData(mPreferDatalist);

        listPrefer.setAdapter(mAdapter);
    }

    /////////////////////////////////////// Service Relation //////////////////////////////////////
    /**
     * Call login service
     */
    private void callGetCourtesyParentTypes()
    {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseCourtesyParentTypes(object, mPreferDatalist);
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

                GlobalData.showToast(PreferentialListMainActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(PreferentialListMainActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                PreferentialListMainActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetCourtesyParentTypes(handler);

        return;
    }
}