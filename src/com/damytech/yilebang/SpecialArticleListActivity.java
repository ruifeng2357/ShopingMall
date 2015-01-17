package com.damytech.yilebang;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.CommService.CommMgr;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STServiceData;
import com.damytech.STData.STSpecialArticleListInfo;
import com.damytech.STData.STSpecialArticleSubListInfo;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-20
 * Time: 下午3:27
 * To change this template use File | Settings | File Templates.
 */
public class SpecialArticleListActivity extends MyActivity {
    final int TEXT_FONT_SIZE = 18;
    final int TEXT_HEIGHT = 40;

    final int LAYOUT_BETWEEN_MARGIN = 20;
    final int LAYOUT_SUBITEM_TOP_MARGIN = 5;
    final int LAYOUT_SUBITEM_LEFT_MARGIN = 20;
    final int LAYOUT_SEPERATOR_MAGIN = 10;
    final int LAYOUT_SEPERATOR_HEIGHT = 10;

    private ArrayList<STSpecialArticleListInfo> mDatalist = new ArrayList<STSpecialArticleListInfo>(0);

    private JsonHttpResponseHandler handler;
    private ProgressDialog progDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specialarticlelist);

        // initialize
        initControls();
        callGetProductAllTypes();
    }

    /**
     * Initialize all controls
     */
    private void initControls()
    {
        ResolutionSet._instance.iterateChild(findViewById(R.id.rlSpecialArticleListMain));

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

    private void startFind()
    {
        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        // get keyword to be find
        TextView txtFind = (TextView) findViewById(R.id.txtFind);
        String keyword = txtFind.getText().toString();
        // show prefer list activity
        Intent intentDetail = new Intent(SpecialArticleListActivity.this, SpecialArticleActivity.class);
        intentDetail.putExtra("SearchMode", 0);  // search by keyword
        intentDetail.putExtra("Keyword", keyword);
        startActivity(intentDetail);
    }

    private void initMainMenu()
    {
//        RelativeLayout rlBottom_Home = (RelativeLayout) findViewById(R.id.rlBottom_Home);
//        rlBottom_Home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onImgbtnHomeClicked();
//            }
//        });

        RelativeLayout rlBootom_Back = (RelativeLayout) findViewById(R.id.rlBottom_BackArraw);
        rlBootom_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });

//        RelativeLayout rlBottom_MainMenu = (RelativeLayout) findViewById(R.id.rlBottom_MainMenu);
//        rlBottom_MainMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onImgbtnMainMenuClicked();
//            }
//        });

        RelativeLayout rlBottom_Packet = (RelativeLayout) findViewById(R.id.rlBottom_Packet);
        rlBottom_Packet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnCartClicked();
            }
        });

//        RelativeLayout rlBottom_PersonInfo = (RelativeLayout) findViewById(R.id.rlBottom_PersonInfo);
//        rlBottom_PersonInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onImgbtnAccountClicked();
//            }
//        });
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
        RelativeLayout rlMainLayout = (RelativeLayout)findViewById(R.id.rlMainPart);
        rlMainLayout.removeAllViews();
        AddItemsToView(mDatalist);
    }

    private void AddItemsToView(ArrayList<STSpecialArticleListInfo> itemDatas)
    {
        RelativeLayout rlMainLayout = (RelativeLayout)findViewById(R.id.rlMainPart);
        int nCurTop = LAYOUT_BETWEEN_MARGIN;
        for (int i = 0; i < itemDatas.size(); i++)
        {
            TextView txtCategory = new TextView(getApplicationContext());

            RelativeLayout.LayoutParams layout_txtcategory = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, TEXT_HEIGHT);
            layout_txtcategory.leftMargin = 5;
            layout_txtcategory.topMargin = nCurTop;
            txtCategory.setLayoutParams(layout_txtcategory);

            txtCategory.setTextColor(Color.parseColor("#FF000000"));
            txtCategory.setTextSize(TEXT_FONT_SIZE);
            txtCategory.setText(itemDatas.get(i).categoryName);

            txtCategory.setVisibility(View.VISIBLE);
            rlMainLayout.addView(txtCategory);
            nCurTop += TEXT_HEIGHT + LAYOUT_SUBITEM_TOP_MARGIN;

            RelativeLayout rlSubCategory = new RelativeLayout(getApplicationContext());

            RelativeLayout.LayoutParams layout_subcategory = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout_subcategory.leftMargin = 0;
            layout_subcategory.topMargin = nCurTop;

            rlSubCategory.setVisibility(View.VISIBLE);

            int nSubTop = LAYOUT_SUBITEM_TOP_MARGIN;
            for (int j = 0; j < itemDatas.get(i).subItemsList.size(); j++)
            {
                TextView txtSubItem = new TextView(getApplicationContext());

                RelativeLayout.LayoutParams layout_txtsubitem = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, TEXT_HEIGHT);
                layout_txtsubitem.leftMargin = LAYOUT_SUBITEM_LEFT_MARGIN;
                layout_txtsubitem.topMargin = nSubTop;
                txtSubItem.setLayoutParams(layout_txtsubitem);

                txtSubItem.setTextColor(Color.parseColor("#FF888888"));
                txtSubItem.setTextSize(TEXT_FONT_SIZE);
                txtSubItem.setText(itemDatas.get(i).subItemsList.get(j).name);

                txtSubItem.setVisibility(View.VISIBLE);
                rlSubCategory.addView(txtSubItem);

                // set sub menu id & menu name
                STSpecialArticleSubListInfo subList = itemDatas.get(i).subItemsList.get(j);
                Hashtable info = new Hashtable();
                info.put("PROD_ID", subList.id);
                info.put("MainCatName", itemDatas.get(i).categoryName);
                info.put("SubCatName", subList.name);
                txtSubItem.setTag(info);
                txtSubItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get sub menu tag
                        Hashtable info = (Hashtable) v.getTag();
                        // start product list activity
                        Intent detailIntent = new Intent(SpecialArticleListActivity.this, SpecialArticleActivity.class);
                        detailIntent.putExtra("PROD_ID", (Integer)info.get("PROD_ID"));
                        detailIntent.putExtra("SearchMode", 1);   // search by group id
                        detailIntent.putExtra("MainCatName", (String)info.get("MainCatName"));
                        detailIntent.putExtra("SubCatName", (String)info.get("SubCatName"));
                        startActivity(detailIntent);
                    }
                });

                nSubTop += TEXT_HEIGHT;

                if (j < itemDatas.get(i).subItemsList.size() - 1)
                {
                    RelativeLayout rlSeperator = new RelativeLayout(getApplicationContext());

                    RelativeLayout.LayoutParams layout_seperator = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, LAYOUT_SEPERATOR_HEIGHT);
                    layout_seperator.leftMargin = LAYOUT_SEPERATOR_MAGIN;
                    layout_seperator.rightMargin = LAYOUT_SEPERATOR_MAGIN;
                    layout_seperator.topMargin = nSubTop;

                    rlSeperator.setVisibility(View.VISIBLE);
                    rlSeperator.setLayoutParams(layout_seperator);
                    rlSeperator.setBackgroundResource(R.drawable.grayline);

                    rlSubCategory.addView(rlSeperator);
                    nSubTop += LAYOUT_SEPERATOR_HEIGHT;
                }
            }

            nSubTop += LAYOUT_SUBITEM_TOP_MARGIN;
            layout_subcategory.height = nSubTop;
            rlSubCategory.setLayoutParams(layout_subcategory);
            rlSubCategory.setBackgroundResource(R.drawable.frameback);

            rlMainLayout.addView(rlSubCategory);
            nCurTop += nSubTop + LAYOUT_BETWEEN_MARGIN;
        }
    }


    /////////////////////////////////////// Service Relation //////////////////////////////////////
    /**
     * Call GetProductAllTypes service
     */
    private void callGetProductAllTypes()
    {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseProductAllTypes(object, mDatalist);
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

                GlobalData.showToast(SpecialArticleListActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(SpecialArticleListActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                SpecialArticleListActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetProductAllTypes(handler);

        return;
    }
}
