package com.wilson.mobliesafe.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

import java.util.List;

/**
 * 服务状态工具类
 * 
 * @author wilson
 * 
 */
public class ServiceStatusUtils {

	/**
	 * 检测服务是否正在运行
	 * 
	 * @return 是否在运行
	 */
	public static boolean isServiceRunning(Context ctx, String serviceName) {
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);// 获取系统所有正在运行的服务,最多返回100个
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			String className = runningServiceInfo.service.getClassName();// 获取服务的名称
			if (className.equals(serviceName)) {// 服务存在
				return true;
			}
		}
		return false;
	}
}
