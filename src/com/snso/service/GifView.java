package com.snso.service;

import com.snso.heart.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.view.View;

public class GifView extends View {

	private long movieStart;
	private Movie movie;

	public GifView(Context context) {
		super(context);
		// ���ļ�����InputStream����ȡ��gifͼƬ��Դ
		movie = Movie.decodeStream(getResources().openRawResource(R.drawable.flow));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		 long curTime=android.os.SystemClock.uptimeMillis();

		//��һ�β���
		if (movieStart == 0){
			movieStart = curTime;
		}
		if (movie != null) {
			int duraction = movie.duration();
			int relTime = (int) ((curTime - movieStart) % duraction);
			movie.setTime(relTime);
			movie.draw(canvas, 0, 0);

			// ǿ���ػ�
			invalidate();

		}

		super.onDraw(canvas);
	}

}