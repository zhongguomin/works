package com.g365.entity;

import java.io.Serializable;

/**
 * ҳ��ʵ����
 * @author nova
 * ���� 2013��1��5��15:48:26
 */
public class AppPage implements Comparable<AppPage>, Serializable {

	/** ��ǰҳ */
	public int CurrentPage=0;
	/** һ������ҳ */
	public int AllPage=0;
	/** �ܹ���Ŀ */
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
