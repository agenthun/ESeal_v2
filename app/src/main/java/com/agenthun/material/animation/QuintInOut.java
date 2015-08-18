package com.agenthun.material.animation;

import android.view.animation.Interpolator;

public class QuintInOut implements Interpolator {

	@Override
	public float getInterpolation(float input) {
		// TODO Auto-generated method stub
		float f1 = input * 2.0f;
		if (f1 < 1.0f) {
			return f1 * (f1 * (f1 * (f1 * (0.5f * f1))));
		} else {
			float f2 = f1 - 2.0f;
			return 0.5f * (2.0f + f2 * (f2 * (f2 * (f2 * f2))));
		}
	}
}