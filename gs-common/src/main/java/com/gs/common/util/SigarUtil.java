//package com.gs.common.util;
//
//import com.gs.common.define.Constants;
//import com.gs.common.util.logger.LoggerUtil;
//import org.hyperic.sigar.*;
//
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 系统监控工具类，单例模式
// */
//public class SigarUtil {
//	public static SigarUtil sigarUtil;
//	public static Sigar sigar;
//
//	/**
//	 * 获取实例
//	 *
//	 * @return
//	 */
//	public synchronized static SigarUtil getInstance() {
//		if (sigar == null) {
//			// 获取系统变量的值
//			String path = System.getProperty("java.library.path");
//			// 添加系统变量：libsigar-amd64-linux.so放在nativelib文件夹，将路径添加到系统变量
//			if (!path.contains(Constants.NATIVELIB_PATH)) {
//				System.setProperty("java.library.path", path + ":" + Constants.NATIVELIB_PATH);
//			}
//			sigar = new Sigar();
//		}
//
//		if (sigarUtil == null) {
//			sigarUtil = new SigarUtil();
//		}
//
//		return sigarUtil;
//	}
//
//	/**
//	 * 关闭
//	 */
//	private synchronized void close() {
//		if (sigar != null) {
//			sigar.close();
//		}
//		sigar = null;
//	}
//
//	/**
//	 * 获取系统数据：硬盘、CPU、内存、交换区等信息
//	 *
//	 * @return
//	 */
//	public Map<String, Object> getSystemData() {
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		try {
//			FileSystemUsage usage = null;
//			List<String> nameList = new ArrayList<>();
//			List<Long> totalList = new ArrayList<>();
//			List<Long> usedList = new ArrayList<>();
//
//			// 1.磁盘信息
//			FileSystem[] fsList = sigar.getFileSystemList();
//			for (int i = 0; i < fsList.length; i++) {
//				FileSystem fs = fsList[i];
//				String name = fs.getDevName();
//				try {
//					usage = sigar.getFileSystemUsage(fs.getDirName());
//				} catch (Exception e) {
//					continue;
//				}
//				long total = usage.getTotal() / 1024L;
//				if (total > 0) {
//					nameList.add(name);
//					totalList.add(usage.getTotal() / 1024L);
//					usedList.add(usage.getUsed() / 1024L);
//				}
//			}
//
//			// 2.cpu 信息
//			CpuPerc cpu = sigar.getCpuPerc();
//			DecimalFormat df = new DecimalFormat("######0.00");
//
//			Mem mem = sigar.getMem(); // 3.内存信息
//			Swap swap = sigar.getSwap(); // 4.交换区信息
//
//			// 封装结果
//			resultMap.put("nameList", nameList);
//			resultMap.put("totalList", totalList);
//			resultMap.put("usedList", usedList);
//
//			resultMap.put("cpu_sys", df.format(cpu.getSys() * 100));
//			resultMap.put("cpu_use", df.format(cpu.getUser() * 100));
//
//			resultMap.put("mem_Tused", mem.getActualUsed() / 1024L / 1024L);
//			resultMap.put("mem_Tfree", mem.getActualFree() / 1024L / 1024L);
//			resultMap.put("mem_Sused", swap.getUsed() / 1024L / 1024L);
//			resultMap.put("mem_Sfree", swap.getFree() / 1024L / 1024L);
//
//		} catch (SigarException e) {
//			LoggerUtil.errorLog("getSystemData error", e);
//			close();
//		}
//
//		return resultMap;
//	}
//
//	public static void main(String[] args) {
//		Map<String, Object> systemData = SigarUtil.getInstance().getSystemData();
//		for(String key : systemData.keySet()){
//			Object value = systemData.get(key);
//			System.out.println(key + ":" + value);
//		}
//	}
//}
