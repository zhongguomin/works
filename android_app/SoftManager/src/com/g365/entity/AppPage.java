package com.g365.entity;

import java.io.Serializable;

/**
 * 页数实体类
 * @author nova
 * 日期 2013年1月5日15:48:26
 */
public class AppPage implements Comparable<AppPage>, Serializable {

	/** 当前页 */
	public int CurrentPage=0;
	/** 一共多少页 */
	public int AllPage=0;
	/** 总共数目 */
	public int TotalSum=0;
	
	public int getCurrentPage() {
		return CurrentPage;
	}
	public void setCurrentPage(int currentPage) {
		CurrentPage = currentPage;
	}
	public int getAllPage() {
		return AllPage;
	}
	public void setAllPage(int allPage) {
		AllPage = allPage;
	}
	public int getTotalSum() {
		return TotalSum;
	}
	public void setTotalSum(int totalSum) {
		TotalSum = totalSum;
	}

	public AppPage(){
		
	}
	public AppPage(int currentPage, int allPage, int totalSum) {
		super();
		CurrentPage = currentPage;
		AllPage = allPage;
		TotalSum = totalSum;
	}
	
	@Override
	public String toString() {
		return "AppPage [CurrentPage=" + CurrentPage + ", AllPage=" + AllPage
				+ ", TotalSum=" + TotalSum + "]";
	}
	public int compareTo(AppPage another) {
		// TODO Auto-generated method stub
		return 0;
	}

}
