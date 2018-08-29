package com.wiser.library.shadow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;

import com.wiser.library.R;

/**
 * @author Wiser
 *         <p>
 *         阴影布局
 */
public class ShadowLayout extends FrameLayout {

	private Paint	mPaint;

	/**
	 * 阴影的颜色
	 */
	private int		mShadowColor	= Color.GRAY;

	/**
	 * 阴影弧度 以及 边距
	 */
	private float	mShadowRadiusEdge;

	/**
	 * y轴偏移量
	 */
	private float	mShadowDy;

	/**
	 * X轴偏移量
	 */
	private float	mShadowDx;

	private RectF	mRectF			= new RectF();

	public ShadowLayout(@NonNull Context context) {
		super(context);
	}

	public ShadowLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	/**
	 * 读取设置的阴影的属性
	 *
	 * @param attrs
	 *            从其中获取设置的值
	 */
	private void init(AttributeSet attrs) {
		setLayerType(View.LAYER_TYPE_SOFTWARE, null); // 关闭硬件加速
		this.setWillNotDraw(false); // 调用此方法后，才会执行 onDraw(Canvas) 方法

		@SuppressLint("CustomViewStyleable")
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShadowView);
		if (typedArray != null) {
			mShadowColor = typedArray.getColor(R.styleable.ShadowView_shadowLayoutColor, getContext().getResources().getColor(android.R.color.darker_gray));
			mShadowRadiusEdge = typedArray.getDimension(R.styleable.ShadowView_shadowLayoutRadiusEdge, dip2px());
			mShadowDx = typedArray.getDimension(R.styleable.ShadowView_shadowLayoutDx, dip2px());
			mShadowDy = typedArray.getDimension(R.styleable.ShadowView_shadowLayoutDy, dip2px());
			typedArray.recycle();
		}
		initPaint();
	}

	/**
	 * 初始化画笔
	 */
	private void initPaint() {
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(mShadowColor);
		mPaint.setStrokeWidth(0);
		this.setWillNotDraw(false); // 调用此方法后，才会执行 onDraw(Canvas) 方法
		mPaint.setAlpha(255);
	}

	@Override protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint.setColor(mShadowColor);
		// 设置阴影属性
		mPaint.setShadowLayer(mShadowRadiusEdge, mShadowDx, mShadowDy, mShadowColor);// 设置光晕效果（mShadowRadiusEdge是光晕半径,第二个参数是x轴偏移量，第三个是Y轴偏移量，最后一个是颜色）
		// 画阴影矩形背景
		canvas.drawRoundRect(mRectF, mShadowRadiusEdge, mShadowRadiusEdge, mPaint);// 第二个参数是x半径，第三个参数是y半径
	}

	@Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (this.getChildCount() == 1) {
			View view = this.getChildAt(0);
			mRectF.right = this.getMeasuredWidth() - mShadowRadiusEdge;
			mRectF.bottom = this.getMeasuredHeight() - mShadowRadiusEdge;
			mRectF.left = mShadowRadiusEdge;
			mRectF.top = mShadowRadiusEdge;
			MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
			params.leftMargin = (int) mShadowRadiusEdge;
			params.rightMargin = (int) mShadowRadiusEdge;
			params.topMargin = (int) mShadowRadiusEdge;
			params.bottomMargin = (int) mShadowRadiusEdge;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * dip2px dp 值转 px 值
	 *
	 * @return px 值
	 */
	private float dip2px() {
		DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
		float scale = dm.density;
		return ((float) 0 * scale + 0.5F);
	}
}
