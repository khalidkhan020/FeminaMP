package com.linchpin.periodtracker.utlity;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;

public class CustomAnimation extends Animation {

	
	boolean toggle = false;
	View flip;
	AnimatorSet animatorSet = new AnimatorSet();
	ObjectAnimator animation1 = null;
	ObjectAnimator animation3 = null;
	float translateValueX;
	float translateValueY;
	
	float startX;
	float startY;
	
	float xRange = 5;
	float yRange = 5;
	
	public CustomAnimation(View flip) {
		// TODO Auto-generated constructor stub
		this.flip=flip;
		startX=flip.getX();
		startY=flip.getY();
		

	}

	public void startAnim() {

		
		
		// animation1.ofFloat(flip,
		// "translationY", 100);
		long time = 100;
		for(;time < 1000;){
			time = (long)(Math.random()*5000);
		}
		
		 translateValueX = (float)(Math.random()*20);
		 translateValueY = (float)(Math.random()*20);
		
		for (; (translateValueX + flip.getX() > startX + xRange && (int) translateValueX % 2 == 0)
				|| (flip.getX() - translateValueX < startX - xRange && (int) translateValueX % 2 == 1
				) ;) {
			translateValueX = (float) (Math.random() * 20);
		}

		for (
				;(translateValueY + flip.getY() > startY + yRange && (int)translateValueY % 2 == 1) || (flip
						.getY() - translateValueY < startY - yRange && (int)translateValueY % 2 == 0);) {
			translateValueY = (float) (Math.random() * 20);
		}

		if (animation1 != null) {
			animation1.end();
			animation1=null;
		}
		if(animation3!=null)
		{
			animation3.end();
			animation3=null;
		}
		
		
		
		
		if((int)translateValueX % 2 == 0) {
			animation1 = ObjectAnimator.ofFloat(flip, "x",
				flip.getX() + translateValueX);
		}
		else{
			animation1 = ObjectAnimator.ofFloat(flip, "x",
					flip.getX() - translateValueX);
		}
		
		if((int)translateValueY % 2 == 1) {
			animation3 = ObjectAnimator.ofFloat(flip, "y",
				flip.getY() + translateValueY);
		}
		else{
			animation3 = ObjectAnimator.ofFloat(flip, "y",
					flip.getY() - translateValueY);
		}
		animation1.setDuration(time);
		animation3.setDuration(time);
		// animation1.start();


		
		animatorSet.play(animation1).with(animation3);
					
		if(animatorSet.getListeners()==null || animatorSet.getListeners().size()==0)
		{
			animatorSet.addListener(new AnimListener());
		}
		animatorSet.start();
		
		
//		 AnimationSet animSet = new AnimationSet(true);
//		 animSet.addAnimation(animation1);
		
	}
	
	
	
	
	class AnimListener implements AnimatorListener{

		@Override
		public void onAnimationCancel(Animator animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			// TODO Auto-generated method stub
			animatorSet=null;
			animatorSet=new AnimatorSet();
			startAnim();
			
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animator animation) {
			// TODO Auto-generated method stub
			
		}
		
		
	}

}
