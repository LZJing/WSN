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

public class NewDataSetActivity extends Activity implements OnClickListener{

	String nodeId = "";

	private EditText new_data_choseid;
	private Button ok;
	private Button cancle;
	private Button datePackerBtn;
	private Button timePackerBtn;
	
	private Calendar calendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_data_set);
		initData();
		initView();
	}
	

	private void initData() {
		calendar =Calendar.getInstance();
		calendar.setTimeInMillis(calendar.getTimeInMillis()-1000*60*60*24);
	}



	private void initView() {
		datePackerBtn = (Button) findViewById(R.id.new_datepicker);
		timePackerBtn = (Button) findViewById(R.id.new_timepicker);
		timePackerBtn.setOnClickListener(this);
		datePackerBtn.setOnClickListener(this);	
		new_data_choseid = (EditText) findViewById(R.id.new_data_choseid);
		ok=(Button) findViewById(R.id.new_data_set_bt_ok);
		cancle = (Button) findViewById(R.id.new_data_set_bt_cancel);
		ok.setOnClickListener(this);
		cancle.setOnClickListener(this);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    	datePackerBtn.setText(sdf1.format(calendar.getTime()));
    	SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm");
    	timePackerBtn.setText(sdf2.format(calendar.getTime()));	
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.new_datepicker:
			//�������ʾ������ѡ������Ĭ�ϵ�����Ϊcaledar����ǰ����
            DatePickerDialog startDatePicker = new DatePickerDialog(this,onStartDateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            startDatePicker.setTitle("��ָ����ѯ����ʼ����");
            startDatePicker.show();
			break;
		case R.id.new_timepicker:
			TimePickerDialog timePickerDialog = new TimePickerDialog(this,onStartTimeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
            timePickerDialog.setTitle("��ָ����ѯ����ʼʱ��");
            timePickerDialog.show();
			break;
		case R.id.new_data_set_bt_ok:
			boolean input_error = false;
			checkChoseId();
			for (int i = 0; i < nodeId.length(); i++) {
				char c = nodeId.charAt(i);
				if (!((c >= '0' && c <= '9') || (c == ' '))) {
					Toast.makeText(this, "��������,�����������ּ��ո�", Toast.LENGTH_SHORT)
							.show();
					input_error = true;
					break;
				}
			}
			if (nodeId.charAt(0) == ' ' && nodeId.charAt(1) == ' ') {
				Toast.makeText(this, "����ȫΪ�ո�", Toast.LENGTH_SHORT).show();
				break;
			}

			// ����һ��bug������ȫ�ǿո����
			if (input_error == true) {
				break;
			}
			String[] nodeIds = nodeId.split(" ");
			int[] nodeId_int = new int[nodeIds.length];
			for (int i = 0; i < nodeIds.length; i++) {
				if (0 <= Integer.parseInt(nodeIds[i]) && Integer.parseInt(nodeIds[i]) <= 50)
					nodeId_int[i] = Integer.parseInt(nodeIds[i]);
				else {
					Toast.makeText(this, "�������󣬽ڵ�ID��Χ1-50", Toast.LENGTH_SHORT).show();
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
			bundle.putLong("st", calendar.getTimeInMillis());
			bundle.putLong("et", System.currentTimeMillis());
			bundle.putString("nodeId", nodeId);
			intent.putExtras(bundle);
			setResult(2, intent);
			finish();
			break;
		case R.id.new_data_set_bt_cancel:
			finish();
			break;

		default:
			break;
		}
	}

	public void checkChoseId() {
		if (TextUtils.isEmpty(new_data_choseid.getText())) {
			for (int i = 1; i <= 50; i++) {
				nodeId += Integer.toString(i) + " ";
			}
		} else {
			nodeId = new_data_choseid.getText().toString();
		}
	}
	
	 //��ʼʱ��ѡ�����֮�󴥷�
    private DatePickerDialog.OnDateSetListener onStartDateSetListener = new DatePickerDialog.OnDateSetListener(){


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        	Toast.makeText(NewDataSetActivity.this, "OnDateSetListener", Toast.LENGTH_SHORT).show();
        	calendar.set(year, monthOfYear, dayOfMonth);
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	datePackerBtn.setText(sdf.format(calendar.getTime()));
        }
    };
    //��ʼʱ��ѡ�����֮�󴥷�
    private TimePickerDialog.OnTimeSetListener onStartTimeSetListener = new TimePickerDialog.OnTimeSetListener(){


    	 @Override
         public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
             calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);        //��������Сʱ��
             calendar.set(Calendar.MINUTE, minute);            //�������ӵķ�����
             SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
         	 timePackerBtn.setText(sdf.format(calendar.getTime()));
         }
    };
    
       
}
