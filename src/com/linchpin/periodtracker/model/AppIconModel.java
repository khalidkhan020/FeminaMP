package com.linchpin.periodtracker.model;

public class AppIconModel {

	String appName, appIcon, activityAlias;

	/**
	 * @return the activityAlias
	 */
	public String getActivityAlias() {
		return activityAlias;
	}

	/**
	 * @param activityAlias
	 *            the activityAlias to set
	 */
	public void setActivityAlias(String activityAlias) {
		this.activityAlias = activityAlias;
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName
	 *            the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @return the appIcon
	 */
	public String getAppIcon() {
		return appIcon;
	}

	/**
	 * @param appIcon
	 *            the appIcon to set
	 */
	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}

	public AppIconModel(String activityAlias, String appName, String appIcon) {

		this.appName = appName;
		this.appIcon = appIcon;
		this.activityAlias = activityAlias;
	}

}
