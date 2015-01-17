package com.damytech.Utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.damytech.yilebang.R;


public class ComboBox extends Button {

	private final static 	String TAG = "ComboBox";
    
	private ListViewItemClickListener	m_listener;
	
    private View	 		m_view;
    private ListView 		m_listView;
    private PopupWindow 	m_popupwindow;
    private ListViewAdapter m_adapter_listview;
	private String[]		m_data; 
	private Context			m_context;
    
	public ComboBox(Context context) {
		super(context);
		m_context = context;
		setListeners();
		
		init();
	}

	public ComboBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		m_context = context;
		setListeners();
		
		init();
	}
	
	public ComboBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		m_context = context;
		setListeners();
		
		init();
	}
	
	private void init(){

        m_adapter_listview = new ListViewAdapter(m_context);
    	m_view = LayoutInflater.from(m_context).inflate(R.layout.combobox_listview, null);

    	m_listView =  (ListView)m_view.findViewById(R.id.id_listview);
    	m_listView.setAdapter(m_adapter_listview);
    	m_listView.setClickable(true);
    	m_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				m_popupwindow.dismiss();
				ComboBox.this.setText(m_data[position]);
				
				if (m_listener != null){
					m_listener.onItemClick(position);
				}
			}
		});
    	
		Drawable drawable = m_context.getResources().getDrawable(R.drawable.icon_dropdown);
		
		drawable.setBounds(0,  0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		this.setCompoundDrawables(null, null, drawable, null);
		//this.setBackgroundColor(Color.WHITE);

//        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));
    }
	
	public void setData(String[] data){
		if (null == data || data.length <= 0){
			return ;
		}
		
		m_data = data;
		this.setText(data[0]);
	}

	public void setListViewOnClickListener(ListViewItemClickListener listener){
		m_listener = listener;
	}
	
	private void setListeners() {
		
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.d(TAG, "Touch......");
				return false;
			}
		});
		
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "Click......");
				 if(m_popupwindow == null){
					 m_popupwindow = new PopupWindow(m_view, ComboBox.this.getWidth(), 300);//LayoutParams.WRAP_CONTENT);

                     m_popupwindow.setBackgroundDrawable(new BitmapDrawable());
					 
					 //pop.setFocusable(true)
					 m_popupwindow.setFocusable(true);  
					 m_popupwindow.setOutsideTouchable(true);
					 m_popupwindow.showAsDropDown(ComboBox.this, 0, 0);

					 }else if(m_popupwindow.isShowing()){
	                	m_popupwindow.dismiss();
					 }else{
	                	m_popupwindow.showAsDropDown(ComboBox.this);
				}
			}
			
		});
	}	
	
	 class ListViewAdapter extends BaseAdapter {
        private LayoutInflater 	m_inflate;
        
        public ListViewAdapter(Context context) {        	
            // TODO Auto-generated constructor stub
        	m_inflate = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return m_data == null ? 0 : m_data.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
            TextView textview = null;
            
            if(convertView==null){
                convertView		= m_inflate.inflate(R.layout.combobox_item, null);
                textview		= (TextView)convertView.findViewById(R.id.id_txt);

                ResolutionSet._instance.iterateChild(convertView);

                convertView.setTag(textview);
            }else{
            	textview = (TextView) convertView.getTag();
            }

            textview.setText(m_data[position]);
             
            return convertView;
		}
    }
	
	 
	public interface ListViewItemClickListener{
		void onItemClick(int position);
	}
}
