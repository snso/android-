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
	// ������Ļ�Ŀ��
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
     * �ж� ��������Ȩ���Ƿ�� 
     * @return true ����  false��ֹ 
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
	 * ������������
	 */
	private void createDesktopLayout() {
		mDesktopLayout = new DesktopLayout(this);
		mDesktopLayout.setOnTouchListener(new OnTouchListener() {
			float mTouchStartX;
			float mTouchStartY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// ��ȡ�����Ļ�����꣬������Ļ���Ͻ�Ϊԭ��
				x = event.getRawX();
				y = event.getRawY() - top; // 25��ϵͳ״̬���ĸ߶�
				
				mLayout.gravity = Gravity.TOP | Gravity.LEFT; 
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// ��ȡ���View�����꣬���Դ�View���Ͻ�Ϊԭ��
					mTouchStartX = event.getX();
					mTouchStartY = event.getY();
					Log.i("startP", "startX" + mTouchStartX + "====startY"
							+ mTouchStartY);
					long end = System.currentTimeMillis() - startTime;
					// ˫���ļ���� 300ms����
					if (end < 300) {
//						closeDesk();
					}
					startTime = System.currentTimeMillis();
					break;
				case MotionEvent.ACTION_MOVE:
					// ���¸�������λ�ò���
					mLayout.x = (int) (x - mTouchStartX);
					mLayout.y = (int) (y - mTouchStartY);
					mWindowManager.updateViewLayout(v, mLayout);
					break;
				case MotionEvent.ACTION_UP:

					// ���¸�������λ�ò���
					mLayout.x = (int) (x - mTouchStartX);
					mLayout.y = (int) (y - mTouchStartY);
					mWindowManager.updateViewLayout(v, mLayout);

					// �����ڴ˼�¼���һ�ε�λ��

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
		// /ȡ��������ͼ����,ע�⣬�����Ҫ���ñ�����ʽ�������������ڱ�����ʽ֮�󣬷�������
		getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		top = rect.top;//״̬���ĸ߶ȣ�����rect.height,rect.width�ֱ���ϵͳ�ĸ߶ȵĿ��

		Log.i("top",""+top);
	}

	/**
	 * ��ʾDesktopLayout
	 */
	private void showDesk() {
		mWindowManager.addView(mDesktopLayout, mLayout);
		finish();
	}

	/**
	 * �ر�DesktopLayout
	 */
	private void closeDesk() {
		mWindowManager.removeView(mDesktopLayout);
		finish();
	}

	/**
	 * ����WindowManager
	 */
	private void createWindowManager() {
		// ȡ��ϵͳ����
		mWindowManager = (WindowManager) getApplicationContext()
				.getSystemService("window");

		// ����Ĳ�����ʽ
		mLayout = new WindowManager.LayoutParams();

		// ���ô�����ʾ���͡���TYPE_SYSTEM_ALERT(ϵͳ��ʾ)
		mLayout.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

		// ���ô��役�㼰������
		// FLAG_NOT_FOCUSABLE(���ܻ�ð������뽹��)
		mLayout.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		// ������ʾ��ģʽ
		mLayout.format = PixelFormat.RGBA_8888;

		// ���ö���ķ���
//		mLayout.gravity = Gravity.TOP | Gravity.LEFT;
		mLayout.gravity = Gravity.CENTER;
		
		// ͸����  
		mLayout.alpha=0.8f;  
		
		// ���ô����Ⱥ͸߶�
		mLayout.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayout.height = WindowManager.LayoutParams.WRAP_CONTENT;

	}


	private void creatMusic() {
		Intent intent = new Intent(this,MusicService.class);  
		startService(intent);  
		
	}
	

}
