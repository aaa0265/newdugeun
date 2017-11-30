package com.example.kmucs.dugeun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MemoListItemView extends LinearLayout {

	private ImageView itemPhoto;

	private TextView itemDate;

	private TextView itemText;

    Bitmap bitmap;				//사진 크기 조절

	public MemoListItemView(Context context) {
		super(context);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//메모 리스트 뷰 layout가져오기
		inflater.inflate(R.layout.memo_listitem, this, true);

		// 사진, 날짜, 내용
		itemPhoto = (ImageView) findViewById(R.id.itemPhoto);
		itemDate = (TextView) findViewById(R.id.itemDate);
		itemText = (TextView) findViewById(R.id.itemText);

	}

	public void setContents(int index, String data) {
		if (index == 0) {
			itemDate.setText(data);
		}
		else if (index == 1) {
			itemText.setText(data);
		}
		else if (index == 2) {

		}
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
