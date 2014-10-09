package com.linchpin.drawable;

import java.util.HashMap;

import android.graphics.Color;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

public class DrawableUtil
{
	public static float[]	roundedCorner	= new float[]
											{
			0, 0, 0, 0, 0, 0, 0, 0
											};
	static ShapeDrawable	drawable;
	
	public static ShapeDrawable getShapeDrawable(int color)
	{
		
		drawable = new ShapeDrawable(new RoundRectShape(roundedCorner, null, null));
		
		drawable.getPaint().setStyle(Style.FILL);
		drawable.getPaint().setColor(color);
		return drawable;
	}
	
	public static ShapeDrawable getShapeDrawableWithStroke(int fillcolor, int stroakcolor, int stroakwidth)
	{
		RoundRectShape rectShape=new RoundRectShape(roundedCorner, null, null);
		
		drawable = new ShapeDrawable(rectShape);
		drawable.getPaint().setColor(fillcolor);
		drawable.getPaint().setStyle(Style.FILL);
		drawable.getPaint().setColor(stroakcolor);
		drawable.getPaint().setStyle(Style.STROKE);
	
		drawable.getPaint().setStrokeJoin(Join.ROUND);
		drawable.getPaint().setStrokeMiter(stroakwidth);
		
		drawable.getPaint().setStrokeWidth(stroakwidth);
		drawable.getPaint().setAntiAlias(true);
		return drawable;
	}
}
