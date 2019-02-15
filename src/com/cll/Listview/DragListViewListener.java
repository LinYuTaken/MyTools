package com.cll.Listview;
/**
 * 
 * TODO 包含刷新和加载更多地接口方法
 */
public interface DragListViewListener {
	/**
	 * 刷新
	 */
	public void onRefresh();
	/**
	 * 加载更多
	 */
	public void onLoadMore();
}
