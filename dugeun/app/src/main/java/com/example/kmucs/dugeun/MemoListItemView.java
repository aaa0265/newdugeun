package com.example.kmucs.dugeun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/** 리스트 화면 구성
 *  작게보이는 사진, 내용, yyyy-mm-dd 날짜 */


public class MemoListItemView extends LinearLayout {

	private ImageView itemPhoto;			// 사진이 작게 보이는 view
	private TextView itemDate;				// 날짜가 보이는 view
	private TextView itemText;				// 메모 내용 보이는 view

    Bitmap bitmap;				//사진 크기 조절

	// list가 뜨는 view
	public MemoListItemView(Context context) {
		super(context);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//메모 리스트 뷰 layout가져오기
		inflater.inflate(R.layout.memo_listitem, this, true);

		// 사진, 날짜, 내용 입력 버튼, 아이디
		itemPhoto = (ImageView) findViewById(R.id.itemPhoto);
		itemDate = (TextView) findViewById(R.id.itemDate);
		itemText = (TextView) findViewById(R.id.itemText);

	}

	//memolistadapter 에 있는 함수와 연결됨.
	public void setContents(int index, String data) {
		// 인덱스 0은 날짜
		if (index == 0) {
			itemDate.setText(data);
		}

		// 인덱스 1은 내용
		else if (index == 1) {
			itemText.setText(data);
		}

		// 인덱스 2는 사진
		else if (index == 2) {

		}

		// 인덱스 3은 사진 uri
		else if (index == 3) {
			//사진이 없을 때,
			if (data == null || data.equals("-1") || data.equals("")) {
				//기본 사진으로 설정
				itemPhoto.setImageResource(R.drawable.person);
			}
			//사진을 찍었을 때,
			else {
                if (bitmap != null) {
                    bitmap.recycle();
                }

                BitmapFactory.Options options = new BitmapFactory.Options();
                // 8개의 픽셀을 하나로 만들어라 = 1/8 크기로 만들어라
				options.inSampleSize = 8;
				//파일을 그대로 읽어온다.
                bitmap = BitmapFactory.decodeFile(BasicInfo.FOLDER_PHOTO + data, options);

                itemPhoto.setImageBitmap(bitmap);

            }
		} else {
			throw new IllegalArgumentException();
		}
	}

}
