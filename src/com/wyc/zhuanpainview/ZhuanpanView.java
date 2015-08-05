package com.wyc.zhuanpainview;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;



public class ZhuanpanView extends View implements OnGlobalLayoutListener {

	private int width; // 转盘宽度
	private int padding; // 转盘内边距
	private int radius;// 半径
	private RectF rect; // 圆形矩形区域
	private Paint paint; // 图形画笔
	private Paint painttext; // 文字画笔
	private float angle = 0; // 起始旋转角度
	private int count = 8;// 扇形个数
	private float shanxingangle; // 扇形角度
	private String[] jianzhi = { "谢谢参与", "4999", "200", "50", "500", "9999",
			"100", "500" }; // 价值
	private String[] jianzhimc = { "豆差一点儿", "流量豆", "经验值", "流量豆", "流量豆", "流量豆",
			"流量豆", "经验值" }; // 价值名称
	private float speed = 0; // 每次旋转角度
	private float decrease = 1;
	private int jg = 50;
	private Handler handler = new Handler();

	public static final int NOMEAL = 0; // 正常状态
	public static final int ZHUANING = 1; // 正在转
	public static final int ENDING = 2; // 正在停止
	private int state = NOMEAL;
	
	private Bitmap backbitmap=BitmapFactory.decodeResource(getResources(), R.drawable.zhuanpanwaiyuan);
	
 	private Runnable runnable = new Runnable() {

		@Override
		public void run() {

			ZhuanpanView.this.invalidate();
			handler.postDelayed(runnable, jg);
		}
	};

	public ZhuanpanView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		getViewTreeObserver().addOnGlobalLayoutListener(this);
		padding = getPaddingLeft();
		// 图形画笔
		paint = new Paint();
		paint.setAntiAlias(true); // 抗锯齿
		paint.setStyle(Paint.Style.FILL);
		paint.setARGB(255, 14 * 16 + 9, 14 * 16 + 9, 14 * 16 + 9);
		paint.setStrokeWidth(4);

		// 文字画笔
		painttext = new Paint();
		painttext.setAntiAlias(true); // 抗锯齿
		painttext.setTextSize(30);
		painttext.setStyle(Paint.Style.FILL);
		painttext.setARGB(255, 15 * 16 + 4, 6 * 16 + 12, 10);
		shanxingangle = 360.0f / 8;
	}

	public ZhuanpanView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ZhuanpanView(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = Math.min(getMeasuredHeight(), getMeasuredWidth());
		radius = (width - padding * 2) / 2;
		rect = new RectF(padding, padding, width - padding, width - padding);
		setMeasuredDimension(width, width);
	}

	public void StartZhuan(int index) {
		state = ZHUANING;
		// 计算速度
		// 每项角度大小
		float angle = (float) (360 /count);
		// 中奖角度范围（因为指针向上，所以水平第一项旋转到指针指向，需要旋转210-270；）
		float from = 270 - (index + 1) * angle;
		float to = from + angle;
		// 停下来时旋转的距离
		float targetFrom =  0* 360 + from;
		/**
		 * <pre>
		 *  (v1 + 0) * (v1+1) / 2 = target ;
		 *  v1*v1 + v1 - 2target = 0 ;
		 *  v1=-1+(1*1 + 8 *1 * target)/2;
		 * </pre>
		 */
		float v1 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetFrom) - 1) / 2;
		float targetTo = 0 * 360 + to;
		float v2 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetTo) - 1) / 2;

		speed = (float) (v1 + Math.random() * (v2 - v1));
		Log.v("speed",""+speed);
		//speed = 30;
	}

	public void EndZhuan() {
		
		state = ENDING;
		angle=0;
	}

	public int getState() {
		return state;
	}

	public boolean isZhan() {

		return speed != 0;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		angle += speed;
		if (angle >= 360) {
			angle -= 360;
		}
		// 点击停止
		if (state == ENDING) {

			speed -= decrease; // 速度的减少量

		}
		if (speed <= 0) {
			speed = 0;
			state = NOMEAL;
		}
        //绘制背景
		paint.setARGB(80, 13 * 16 + 12, 13 * 16 + 12, 13 * 16 + 12);
		
		//展示在屏幕上的区域
		canvas.drawBitmap(backbitmap, null, new Rect(padding / 2,
				padding / 2, getMeasuredWidth() - padding / 2,
				getMeasuredWidth() - padding / 2), null);
		//canvas.drawCircle(radius + padding, radius + padding, radius, paint);
		// 绘制扇形
		paint.setARGB(255, 13 * 16 + 12, 13 * 16 + 12, 13 * 16 + 12);
		paint.setStyle(Paint.Style.STROKE);
		float vOffset = 0;
		float hOffset = 0;
		float textwidth = 0;
		for (int i = 0; i < count; i++) {
			float startangle = i * shanxingangle + angle;
			canvas.drawArc(rect, startangle, shanxingangle, true, paint);
			// 绘制文本
			Path path = new Path();
			path.addArc(rect, startangle, shanxingangle);
			// 计算文字
			painttext.setTextSize(35);
			painttext.setStyle(Paint.Style.FILL);
			painttext.setARGB(255, 15 * 16 + 4, 6 * 16 + 12, 10);
			textwidth = painttext.measureText(jianzhi[i]);
			vOffset = radius / 5;
			hOffset = (float) (radius * Math.PI / count - textwidth / 2);
			canvas.drawTextOnPath(jianzhi[i], path, hOffset, vOffset, painttext);
			painttext.setTextSize(30);
			painttext.setStyle(Paint.Style.FILL);
			painttext.setARGB(255, 9 * 16 + 12, 9 * 16 + 12, 9 * 16 + 12);
			textwidth = painttext.measureText(jianzhimc[i]);
			hOffset = (float) (radius * Math.PI / count - textwidth / 2);
			canvas.drawTextOnPath(jianzhimc[i], path, hOffset, vOffset + 30,
					painttext);
		}
	}

	@Override
	public void onGlobalLayout() {
		// 如果整个布局完成启动handler
		getViewTreeObserver().removeGlobalOnLayoutListener(this);
		handler.postDelayed(runnable, jg);
	}
}
