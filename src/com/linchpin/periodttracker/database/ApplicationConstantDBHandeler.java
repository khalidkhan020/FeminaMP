package com.linchpin.periodttracker.database;

import java.util.List;

import android.content.Context;

import com.linchpin.periodtracker.model.ApplicationConstants;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;

public class ApplicationConstantDBHandeler extends PeriodTrackerDBHandler {

	public ApplicationConstantDBHandeler(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	public List<PeriodTrackerModelInterface>  getApplicationconstant(){
		
		String Sql = " Select * FROM "+ApplicationConstants.APPLICATION_CONSTANTS;
		return this.selectMulipleRecord(ApplicationConstants.class, Sql);
		
		
	}
	

	
	public PeriodTrackerModelInterface  getParticularApplicationconstant(String id){
		
		String Sql = " Select * FROM "+ApplicationConstants.APPLICATION_CONSTANTS + " Where "+ApplicationConstants.APPLICATION_CONSTANT_ID+  "="+ "'"+ id +"'";
		return this.selectRecord(ApplicationConstants.class, Sql);
		
		
	}
	
}
