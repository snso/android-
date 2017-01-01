package com.snso.heart;

import java.lang.reflect.Method;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;

import com.snso.service.MusicService;



public class MainActivity extends Activity implements OnClickListener {
	
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mLayout;
	private DesktopLayout mDesktopLayout;
	private long startTime;
	// 声明屏幕的宽高
	float x, y;
	int top;
	
	
	private Button btn_start;
	private Button btn_stop;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heart);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        
        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        
        
        openPermission(); 
        
        createWindowManager();
		createDesktopLayout();
        
		
		creatMusic();
		
		showDesk();
		
//		finish();
       
        
    }



	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_start:
			showDesk();
			break;
			
		case R.id.btn_stop:

			break;

		default:
			break;
		}
	}

	private void openPermission() {
		Log.i("SNSO", getAppOps(this)+"------");
	} 
	
    /** 
     * 判断 悬浮窗口权限是否打开 
     * @return true 允许  false禁止 
     */  
    public static boolean getAppOps(Context context) {  
        try {  
            Object object = context.getSystemService("appops");  
            if (object == null) {  
                return false;  
            }  
            Class localClass = object.getClass();  
            Class[] arrayOfClass = new Class[3];  
            arrayOfClass[0] = Integer.TYPE;  
            arrayOfClass[1] = Integer.TYPE;  
            arrayOfClass[2] = String.class;  
            Method method = localClass.getMethod("checkOp", arrayOfClass);  
            if (method == null) {  
                return false;  
            }  
            Object[] arrayOfObject1 = new Object[3];  
            arrayOfObject1[0] = Integer.valueOf(24);  
            arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());  
            arrayOfObject1[2] = context.getPackageName();  
            int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();  
            return m == AppOpsManager.MODE_ALLOWED;  
        } catch (Exception ex) {  
   
        }  
        return false;  
    }  
	
	
	
	
	/**
	 * 创建悬浮窗体
	 */
	private void createDesktopLayout() {
		mDesktopLayout = new DesktopLayout(this);
		mDesktopLayout.setOnTouchListener(new OnTouchListener() {
			float mTouchStartX;
			float mTouchStartY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 获取相对屏幕的坐标，即以屏幕左上角为原点
				x = event.getRawX();
				y = event.getRawY() - top; // 25是系统状态栏的高度
				
				mLayout.gravity = Gravity.TOP | Gravity.LEFT; 
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 获取相对View的坐标，即以此View左上角为原点
					mTouchStartX = event.getX();
					mTouchStartY = event.getY();
					Log.i("startP", "startX" + mTouchStartX + "====startY"
							+ mTouchStartY);
					long end = System.currentTimeMillis() - startTime;
					// 双击的间隔在 300ms以下
					if (end < 300) {
//						closeDesk();
					}
					startTime = System.currentTimeMillis();
					break;
				case MotionEvent.ACTION_MOVE:
					// 更新浮动窗口位置参数
					mLayout.x = (int) (x - mTouchStartX);
					mLayout.y = (int) (y - mTouchStartY);
					mWindowManager.updateViewLayout(v, mLayout);
					break;
				case MotionEvent.ACTION_UP:

					// 更新浮动窗口位置参数
					mLayout.x = (int) (x - mTouchStartX);
					mLayout.y = (int) (y - mTouchStartY);
					mWindowManager.updateViewLayout(v, mLayout);

					// 可以在此记录最后一次的位置

					mTouchStartX = mTouchStartY = 0;
					break;
				}
				return true;
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		Rect rect = new Rect();
		// /取得整个视图部分,注意，如果你要设置标题样式，这个必须出现在标题样式之后，否则会出错
		getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		top = rect.top;//状态栏的高度，所以rect.height,rect.width分别是系统的高度的宽度

		Log.i("top",""+top);
	}

	/**
	 * 显示DesktopLayout
	 */
	private void showDesk() {
		mWindowManager.addView(mDesktopLayout, mLayout);
		finish();
	}

	/**
	 * 关闭DesktopLayout
	 */
	private void closeDesk() {
		mWindowManager.removeView(mDesktopLayout);
		finish();
	}

	/**
	 * 设置WindowManager
	 */
	private void createWindowManager() {
		// 取得系统窗体
		mWindowManager = (WindowManager) getApplicationContext()
				.getSystemService("window");

		// 窗体的布局样式
		mLayout = new WindowManager.LayoutParams();

		// 设置窗体显示类型——TYPE_SYSTEM_ALERT(系统提示)
		mLayout.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

		// 设置窗体焦点及触摸：
		// FLAG_NOT_FOCUSABLE(不能获得按键输入焦点)
		mLayout.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		// 设置显示的模式
		mLayout.format = PixelFormat.RGBA_8888;

		// 设置对齐的方法
//		mLayout.gravity = Gravity.TOP | Gravity.LEFT;
		mLayout.gravity = Gravity.CENTER;
		
		// 透明度  
		mLayout.alpha=0.8f;  
		
		// 设置窗体宽度和高度
		mLayout.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayout.height = WindowManager.LayoutParams.WRAP_CONTENT;

	}


	private void creatMusic() {
		Intent intent = new Intent(this,MusicService.class);  
		startService(intent);  
		
	}
	

}
