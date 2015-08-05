package com.wyc.zhuanpainview;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	// ¶ºÄã×ª
		private Button btn_zhan;
		private ZhuanpanView zview;


		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		zview = (ZhuanpanView) this.findViewById(R.id.zview);
		btn_zhan = (Button) this.findViewById(R.id.btn_zhan);
		btn_zhan.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_zhan:
			if (zview.getState() == ZhuanpanView.NOMEAL) {
				zview.StartZhuan(1);
				btn_zhan.setBackgroundResource(R.drawable.icon_end);
			} else {
				if (zview.getState() == ZhuanpanView.ZHUANING) {
					zview.EndZhuan();
					btn_zhan.setBackgroundResource(R.drawable.icon_bengin);
				}
			}
			break;
		}

	}
	

}
