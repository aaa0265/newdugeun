package com.example.kmucs.dugeun;


/** 메모 리스트 관리 */

public class MemoListItem {

	private String[] mData; 			// Data 배열 (메모번호, 메모 입력 내용 [날짜, 내용, 사진이름, 사진uri]
	private String mId; 				// item id
	private boolean mSelectable = true; // 이 아이템이 선택가능 할 경우 true

	public MemoListItem(String itemId, String[] obj) {
		mId = itemId;
		mData = obj;
	}

	public MemoListItem(String memoId, String memoDate, String memoText,
						String id_photo, String uri_photo)
	{
		mId = memoId;
		mData = new String[4];
		mData[0] = memoDate;
		mData[1] = memoText;
		mData[2] = id_photo; //4
		mData[3] = uri_photo; // 5
	}

	/**
	 * 아이템이 선택 가능하다.
	 */
	public boolean isSelectable() {
		return mSelectable;
	}

	/**
	 * Set selectable flag
	 */
	public void setSelectable(boolean selectable) {
		mSelectable = selectable;
	}

	public String getId() {
		return mId;
	}

	public void setId(String itemId) {
		mId = itemId;
	}


	/**
	 *  메모 내용이 담긴 array 리턴
	 */
	public String[] getData() {
		return mData;
	}

	/**
	 * index 위치에 있는 내용 가져오기
	 */
	public String getData(int index) {
		if (mData == null || index >= mData.length) {
			return null;
		}

		return mData[index];
	}

	/**
	 * Set data array
	 *
	 * @param obj
	 */
	public void setData(String[] obj) {
		mData = obj;
	}


	/**
	 * Compare with the input object
	 *
	 */
	public int compareTo(MemoListItem other) {
		if (mData != null) {
			Object[] otherData = other.getData();
			if (mData.length == otherData.length) {
				for (int i = 0; i < mData.length; i++) {
					if (!mData[i].equals(otherData[i])) {
						return -1;
					}
				}
			} else {
				return -1;
			}
		} else {
			throw new IllegalArgumentException();
		}

		return 0;
	}

}
