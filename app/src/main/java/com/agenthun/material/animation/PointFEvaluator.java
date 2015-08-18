package com.agenthun.material.animation;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

public class PointFEvaluator implements TypeEvaluator<Object> {

	@Override
	public Object evaluate(float fraction, Object startValue, Object endValue) {
		// TODO Auto-generated method stub
		PointF f1 = (PointF) startValue;
		PointF f2 = (PointF) endValue;
		return new PointF(f1.x + fraction * (f2.x - f1.x), f1.y + fraction
				* (f2.y - f1.y));
	}
}