package com.buaa.sensory.wsn_dr.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.buaa.sensory.wsn_dr.R;
import com.buaa.sensory.wsn_dr.entity.DateAndTime;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class HisDataSetActivity extends Activity implements OnClickListener{

	String nodeId = "";
	@ViewInject(R.id.new_data_choseid)
	private EditText new_data_choseid;
	@ViewInject(R.id.new_data_set_bt_ok)
	private Button ok;
	@ViewInject(R.id.new_data_set_bt_cancel)
	private Button cancel;
	private Button stDatePackerBtn;
	private Button stTimePackerBtn;
	private Button etDatePackerBtn;
	private Button etTimePackerBtn;
	
	private Calendar calendar_st,calendar_et;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.his_data_set);
		ViewUtils.inject(this);
		initData();
		stDatePackerBtn = (Button) findViewById(R.id.his_st_datepicker);
		stTimePackerBtn = (Button) findViewById(R.id.his_st_timepicker);
		
		etDatePackerBtn = (Button) findViewById(R.id.his_et_datepicker);
		etTimePackerBtn = (Button) findViewById(R.id.his_et_timepicker);
		
		stDatePackerBtn.setOnClickListener(this);
		stTimePackerBtn.setOnClickListener(this);
		etDatePackerBtn.setOnClickListener(this);
		etTimePackerBtn.setOnClickListener(this);
		
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        etTimePackerBtn.setText(sdf.format(calendar_et.getTime()));
        stTimePackerBtn.setText(sdf.format(calendar_st.getTime()));
        
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    	stDatePackerBtn.setText(sdf2.format(calendar_st.getTime()));
    	etDatePackerBtn.setText(sdf2.format(calendar_et.getTime()));

	}
	private void initData() {
		calendar_st =Calendar.getInstance();
		
		calendar_st.setTimeInMillis(calendar_st.getTimeInMillis()-1000*60*60*24);
		calendar_et = Calendar.getInstance();
	}

	@OnClick({ R.id.new_data_set_bt_ok, R.id.new_data_set_bt_cancel })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.his_st_datepicker:
			//点击后显示出日期选择器，默认的日期为caledar，当前日期
            DatePickerDialog startDatePicker = new DatePickerDialog(this,onStartDateSetListener,
                    calendar_st.get(Calendar.YEAR),
                    calendar_st.get(Calendar.MONTH),
                    calendar_st.get(Calendar.DAY_OF_MONTH));
            startDatePicker.setTitle("请指定查询的起始日期");
            startDatePicker.show();
			break;
		case R.id.his_st_timepicker:
			TimePickerDialog timePickerDialog = new TimePickerDialog(this,onStartTimeSetListener,calendar_st.get(Calendar.HOUR_OF_DAY),calendar_st.get(Calendar.MINUTE),false);
            timePickerDialog.setTitle("请指定查询的起始时间");
            timePickerDialog.show();
			break;
		case R.id.his_et_datepicker:
			//点击后显示出日期选择器，默认的日期为caledar，当前日期
            DatePickerDialog endDatePicker = new DatePickerDialog(this,onEndDateSetListener,
                    calendar_et.get(Calendar.YEAR),
                    calendar_et.get(Calendar.MONTH),
                    calendar_et.get(Calendar.DAY_OF_MONTH));
            endDatePicker.setTitle("请指定查询的起始日期");
            endDatePicker.show();
			break;
		case R.id.his_et_timepicker:
			TimePickerDialog endTimePicker = new TimePickerDialog(this,onEndTimeSetListener,calendar_et.get(Calendar.HOUR_OF_DAY),calendar_et.get(Calendar.MINUTE),false);
			endTimePicker.setTitle("请指定查询的起始时间");
			endTimePicker.show();
			break;
		case R.id.new_data_set_bt_ok:
			boolean input_error = false;
			input_error = checkChoseId();
			if (input_error == true) {
				break;
			}
			for (int i = 0; i < nodeId.length(); i++) {
				char c = nodeId.charAt(i);
				if (!((c >= '0' && c <= '9') || (c == ' '))) {
					Toast.makeText(this, "输入有误,仅可输入数字及空格", Toast.LENGTH_SHORT)
							.show();
					input_error = true;
					break;
				}
			}
			if (nodeId.charAt(0) == ' ' && nodeId.charAt(1) == ' ') {
				Toast.makeText(this, "不能全为空格", Toast.LENGTH_SHORT).show();
				break;
			}
			// 留有一个bug，输入全是空格出错。
			if (input_error == true) {
				break;
			}
			String[] nodeIds = nodeId.split(" ");
			int[] nodeId_int = new int[nodeIds.length];
			if (nodeIds.length > 3) {

				Toast.makeText(this, "最多可输入3个节点", Toast.LENGTH_SHORT).show();
				break;
			}
			for (int i = 0; i < nodeIds.length; i++) {
				if (0 <= Integer.parseInt(nodeIds[i])
						&& Integer.parseInt(nodeIds[i]) <= 50)
					nodeId_int[i] = Integer.parseInt(nodeIds[i]);
				else {
					Toast.makeText(this, "输入有误，节点ID范围1-50", Toast.LENGTH_SHORT)
							.show();
					input_error = true;
					break;
				}
			}
			if (input_error == true) {
				break;
			}
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putIntArray("nodeId_int", nodeId_int);
			bundle.putLong("st", calendar_st.getTimeInMillis());
			bundle.putLong("et", calendar_et.getTimeInMillis());
			bundle.putString("nodeId", nodeId);
			intent.putExtras(bundle);
			setResult(3, intent);
			finish();
			break;
		case R.id.new_data_set_bt_cancel:
			finish();
			break;

		default:
			break;
		}
	}

	public boolean checkChoseId() {
		if (TextUtils.isEmpty(new_data_choseid.getText())) {
			Toast.makeText(this, "请输入查询节点", Toast.LENGTH_SHORT).show();
			return true;
		} else {
			nodeId = new_data_choseid.getText().toString();
			return false;
		}
	}
	
    private DatePickerDialog.OnDateSetListener onStartDateSetListener = new DatePickerDialog.OnDateSetListener(){


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        	Toast.makeText(HisDataSetActivity.this, "OnDateSetListener", Toast.LENGTH_SHORT).show();
        	calendar_st.set(year, monthOfYear, dayOfMonth);
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	stDatePackerBtn.setText(sdf.format(calendar_st.getTime()));
        }
    };
    private TimePickerDialog.OnTimeSetListener onStartTimeSetListener = new TimePickerDialog.OnTimeSetListener(){

    	 @Override
         public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
             calendar_st.set(Calendar.HOUR_OF_DAY, hourOfDay);        //设置闹钟小时数
             calendar_st.set(Calendar.MINUTE, minute);            //设置闹钟的分钟数
             SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
             stTimePackerBtn.setText(sdf.format(calendar_st.getTime()));
         }
    };
    private DatePickerDialog.OnDateSetListener onEndDateSetListener = new DatePickerDialog.OnDateSetListener(){


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        	Toast.makeText(HisDataSetActivity.this, "OnDateSetListener", Toast.LENGTH_SHORT).show();
        	calendar_et.set(year, monthOfYear, dayOfMonth);
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	etDatePackerBtn.setText(sdf.format(calendar_st.getTime()));
        }
    };
    private TimePickerDialog.OnTimeSetListener onEndTimeSetListener = new TimePickerDialog.OnTimeSetListener(){

    	 @Override
         public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
             calendar_et.set(Calendar.HOUR_OF_DAY, hourOfDay);        //设置闹钟小时数
             calendar_et.set(Calendar.MINUTE, minute);            //设置闹钟的分钟数
             SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
             etTimePackerBtn.setText(sdf.format(calendar_et.getTime()));
         }
    };
}
