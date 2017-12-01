package com.example.kmucs.dugeun;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class MemoListAdapter extends BaseAdapter {

	private Context mContext;

	//List 이름 mItems
	private List<MemoListItem> mItems = new ArrayList<MemoListItem>();

	public MemoListAdapter(Context context) {
		mContext = context;
	}

	// 리스트 비우기
	public void clear() {
		mItems.clear();
	}

	// 리스트 메모 추가해주기
	public void addItem(MemoListItem it) {
		mItems.add(it);
	}

	public void setListItems(List<MemoListItem> lit) {
		mItems = lit;
	}

	// 리트스 원소 갯수
	public int getCount() {
		return mItems.size();
	}

	// position인덱스에 있는 아이템 가져오기
	public Object getItem(int position) {
		return mItems.get(position);
	}

	public boolean areAllItemsSelectable() {
		return false;
	}

	// position인덱스에 있는 부분의 아이템 선택가능한가?
	public boolean isSelectable(int position) {
		try {
			return mItems.get(position).isSelectable();
		} catch (IndexOutOfBoundsException ex) {
			return false;
		}
	}


	public long getItemId(int position) {
		return position;
	}

	// view 에 보일 아이템 가져오기
	public View getView(int position, View convertView, ViewGroup parent) {
		MemoListItemView itemView;
		if (convertView == null) {
			itemView = new MemoListItemView(mContext);
		}
		else {
			itemView = (MemoListItemView) convertView;
		}

		// 현재 아이템 불러오기
		itemView.setContents(0, ((String) mItems.get(position).getData(0)));
		itemView.setContents(1, ((String) mItems.get(position).getData(1)));
		itemView.setContents(3, ((String) mItems.get(position).getData(3)));


		return itemView;
	}

}
