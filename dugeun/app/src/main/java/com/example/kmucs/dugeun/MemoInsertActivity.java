package com.example.kmucs.dugeun;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.kmucs.dugeun.db.MemoDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 새 메모 / 메모 보기 액티비티
 */

public class MemoInsertActivity extends Activity {

	public static final String TAG = "MemoInsertActivity";

	// 변수, 객체 설정

	EditText mMemoEdit;
	ImageView mPhoto;

	String mMemoMode;
	String mMemoId;

	String mMediaPhotoId;
	String mMediaPhotoUri;

	String tempPhotoUri;

	String mDateStr;
	String mMemoStr;

	Bitmap resultPhotoBitmap;

	boolean isPhotoCaptured;

	boolean isPhotoFileSaved;

	boolean isPhotoCanceled;

	Calendar mCalendar = Calendar.getInstance();
	Button insertDateButton;

	int mSelectdContentArray;
	int mChoicedArrayItem;

	Button titleBackgroundBtn;
	Button insertSaveBtn;
	Button delete_Btn;
	Button insertCancelBtn;

	EditText insert_memoEdit;

	Animation translateLeftAnim;
	Animation translateRightAnim;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 메모 입력 layout 출력, 버튼 설정.
		setContentView(R.layout.memo_insert_activity);
		titleBackgroundBtn = (Button)findViewById(R.id.titleBackgroundBtn);
		mPhoto = (ImageView)findViewById(R.id.insert_photo);
    	mMemoEdit = (EditText) findViewById(R.id.insert_memoEdit);
		delete_Btn = (Button) findViewById(R.id.delete_Btn);

    	insert_memoEdit = (EditText)findViewById(R.id.insert_memoEdit);

		//애니메이션 객체(사진 미리보기화면에서 좌우로 넘겨보기-슬라이딩)
    	translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.translate_left);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_right);

        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        translateLeftAnim.setAnimationListener(animListener);
        translateRightAnim.setAnimationListener(animListener);

		// 사진 추가 화면을 눌렀을 때
    	mPhoto.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//찍을건지, 선택할건지.
				if(isPhotoCaptured || isPhotoFileSaved) {
					showDialog(BasicInfo.CONTENT_PHOTO_EX);
				}
				//------------
				else {
					showDialog(BasicInfo.CONTENT_PHOTO);
				}
			}
		});

		//메모 삭제 버튼을 눌렀을 때
		delete_Btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//삭제 여부 묻는 다이얼로그 띄움
				showDialog(BasicInfo.CONFIRM_DELETE);
			}
		});

		setBottomButtons();

		setMediaLayout();

		setCalendar();

		Intent intent = getIntent();
		mMemoMode = intent.getStringExtra(BasicInfo.KEY_MEMO_MODE);
		//기존 메모를 눌렀을 때(= 수정도 가능, 그냥 보는 것도 가능)
		if(mMemoMode.equals(BasicInfo.MODE_MODIFY) || mMemoMode.equals(BasicInfo.MODE_VIEW)) {
			processIntent(intent);
			titleBackgroundBtn.setText("메모 보기");
			insertSaveBtn.setText("수정");  					//사진으로 해놨음. 겹쳐보임.
			delete_Btn.setVisibility(View.VISIBLE);				//이때만 삭제버튼 보임
		}
		// 새로운 메모를 작상할 때
		else {
			titleBackgroundBtn.setText("새 메모");
			insertSaveBtn.setText("저장");
			delete_Btn.setVisibility(View.GONE);				//이때는 보이지 않음.
		}
	}

	//슬라이딩 애니메이션
    private class SlidingPageAnimationListener implements AnimationListener {

		public void onAnimationEnd(Animation animation) {

		}

		public void onAnimationRepeat(Animation animation) {

		}

		public void onAnimationStart(Animation animation) {

		}

    }

//-----------------------------------------------------------------------------------------------------보류
	public void processIntent(Intent intent) {
		mMemoId = intent.getStringExtra(BasicInfo.KEY_MEMO_ID);
		mMemoEdit.setText(intent.getStringExtra(BasicInfo.KEY_MEMO_TEXT));
		mMediaPhotoId = intent.getStringExtra(BasicInfo.KEY_ID_PHOTO);
		mMediaPhotoUri = intent.getStringExtra(BasicInfo.KEY_URI_PHOTO);

		setMediaImage(mMediaPhotoId, mMediaPhotoUri);	//둘 다 string (밑에 있음)
    }


    public void setMediaImage(String photoId, String photoUri) {
    	Log.d(TAG, "photoId : " + photoId + ", photoUri : " + photoUri);

    	if(photoId.equals("") || photoId.equals("-1")) {
    		mPhoto.setImageResource(R.drawable.person_add);
    	}
    	else {
    		isPhotoFileSaved = true;
    		mPhoto.setImageURI(Uri.parse(BasicInfo.FOLDER_PHOTO + photoUri));
    	}

    }

	/**
	 * 하단 메뉴 버튼 설정
	 */

    public void setBottomButtons() {
		//메모 입력시, 저장과 닫기 버튼 설정
    	insertSaveBtn = (Button)findViewById(R.id.insert_saveBtn);
    	insertCancelBtn = (Button)findViewById(R.id.insert_cancelBtn);
    	// 저장 버튼
    	insertSaveBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean isParsed = parseValues();
                if (isParsed) {
                	if(mMemoMode.equals(BasicInfo.MODE_INSERT)) {
                		saveInput();
                	} else if(mMemoMode.equals(BasicInfo.MODE_MODIFY) || mMemoMode.equals(BasicInfo.MODE_VIEW)) {
                		modifyInput();
                	}
                }
			}
		});

    	// 닫기 버튼
    	insertCancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
//---------------------------------------------------------------------------------------------


	/**
     * 데이터베이스에 레코드 추가
     */
    private void saveInput() {

		// 사진 파일의 이름을 받아옴.
    	String photoFilename = insertPhoto();
    	int photoId = -1;

    	String SQL = null;

		//사진이 존재
    	if (photoFilename != null) {
	    	// query picture id
	    	SQL = "select _ID from " + MemoDatabase.TABLE_PHOTO + " where URI = '" + photoFilename + "'";
	    	Log.d(TAG, "SQL : " + SQL);
	    	if (countrymemo.mDatabase != null) {
	    		Cursor cursor = countrymemo.mDatabase.rawQuery(SQL);
	    		if (cursor.moveToNext()) {
	    			photoId = cursor.getInt(0);
	    		}
	    		cursor.close();
	    	}
    	}

    	// 형식 지정
		SQL = "insert into " + MemoDatabase.TABLE_MEMO +
				"(INPUT_DATE, CONTENT_TEXT, ID_PHOTO) values(" +
				"DATETIME('" + mDateStr + "'), " +
				"'"+ mMemoStr + "', " +
				"'"+ photoId + "')";

    	Log.d(TAG, "SQL : " + SQL);
    	if (countrymemo.mDatabase != null) {
			countrymemo.mDatabase.execSQL(SQL);
    	}

    	Intent intent = getIntent();
    	setResult(RESULT_OK, intent);
    	finish();

    }

    /**
     * 데이터베이스 레코드 수정
     */
    private void modifyInput() {

    	Intent intent = getIntent();

    	String photoFilename = insertPhoto();
    	int photoId = -1;

    	String SQL = null;

    	if (photoFilename != null) {
	    	// query picture id
	    	SQL = "select _ID from " + MemoDatabase.TABLE_PHOTO + " where URI = '" + photoFilename + "'";
	    	Log.d(TAG, "SQL : " + SQL);
	    	if (countrymemo.mDatabase != null) {
	    		Cursor cursor = countrymemo.mDatabase.rawQuery(SQL);
	    		if (cursor.moveToNext()) {
	    			photoId = cursor.getInt(0);
	    		}
	    		cursor.close();

	    		mMediaPhotoUri = photoFilename;

	    		SQL = "update " + MemoDatabase.TABLE_MEMO +
		    		" set " +
					" ID_PHOTO = '" + photoId + "'" +
					" where _id = '" + mMemoId + "'";

	    		if (countrymemo.mDatabase != null) {
					countrymemo.mDatabase.rawQuery(SQL);
	    		}

	    		mMediaPhotoId = String.valueOf(photoId);
	    	}
    	}
    	else if(isPhotoCanceled && isPhotoFileSaved) {
    		SQL = "delete from " + MemoDatabase.TABLE_PHOTO +
    			" where _ID = '" + mMediaPhotoId + "'";
			Log.d(TAG, "SQL : " + SQL);
			if (countrymemo.mDatabase != null) {
				countrymemo.mDatabase.execSQL(SQL);
			}

			File photoFile = new File(BasicInfo.FOLDER_PHOTO + mMediaPhotoUri);
			if (photoFile.exists()) {
				photoFile.delete();
			}

			SQL = "update " + MemoDatabase.TABLE_MEMO +
    		" set " +
			" ID_PHOTO = '" + photoId + "'" +
			" where _id = '" + mMemoId + "'";

			if (countrymemo.mDatabase != null) {
				countrymemo.mDatabase.rawQuery(SQL);
			}

			mMediaPhotoId = String.valueOf(photoId);
    	}

    	// update memo info
    	SQL = "update " + MemoDatabase.TABLE_MEMO +
    				" set " +
    				" INPUT_DATE = DATETIME('" + mDateStr + "'), " +
    				" CONTENT_TEXT = '" + mMemoStr + "'" +
    				" where _id = '" + mMemoId + "'";

    	Log.d(TAG, "SQL : " + SQL);
    	if (countrymemo.mDatabase != null) {
			countrymemo.mDatabase.execSQL(SQL);
    	}

    	intent.putExtra(BasicInfo.KEY_MEMO_TEXT, mMemoStr);
    	intent.putExtra(BasicInfo.KEY_ID_PHOTO, mMediaPhotoId);

    	intent.putExtra(BasicInfo.KEY_URI_PHOTO, mMediaPhotoUri);


    	setResult(RESULT_OK, intent);
    	finish();
    }

//-----------------------------------------------------------------------------------------
    /**
     * 앨범의 사진을 사진 폴더에 복사한 후, PICTURE 테이블에 사진 정보 추가
     * 이미지의 이름은 현재 시간을 기준으로 한 getTime() 값의 문자열 사용
     */
    private String insertPhoto() {
       	String photoName = null; //초기값

		//사진을 찍을 때
    	if (isPhotoCaptured) { // captured Bitmap
	    	try {
				// 메모 수정시
	    		if (mMemoMode != null && mMemoMode.equals(BasicInfo.MODE_MODIFY)) {
	    			Log.d(TAG, "previous photo is newly created for modify mode.");

	    			String SQL = "delete from " + MemoDatabase.TABLE_PHOTO +
    				" where _ID = '" + mMediaPhotoId + "'";
			    	Log.d(TAG, "SQL : " + SQL);
			    	if (countrymemo.mDatabase != null) {
						countrymemo.mDatabase.execSQL(SQL);
			    	}

	    			File previousFile = new File(BasicInfo.FOLDER_PHOTO + mMediaPhotoUri);
	    	    	if (previousFile.exists()) {
	    	    		previousFile.delete();
	    	    	}
	    		}

	    		File photoFolder = new File(BasicInfo.FOLDER_PHOTO);

				//폴더가 없다면 폴더를 생성한다.
				if(!photoFolder.isDirectory()){
					Log.d(TAG, "creating photo folder : " + photoFolder);
					photoFolder.mkdirs();
				}

				// 파일이름 설정
				photoName = createFilename();

				FileOutputStream outstream = new FileOutputStream(BasicInfo.FOLDER_PHOTO + photoName);
				resultPhotoBitmap.compress(CompressFormat.PNG, 100, outstream);
				outstream.close();


				if (photoName != null) {
					Log.d(TAG, "isCaptured: " +isPhotoCaptured);

			    	// INSERT PICTURE INFO
			    	String SQL = "insert into " + MemoDatabase.TABLE_PHOTO + "(URI) values(" + "'" + photoName + "')";
			    	if (countrymemo.mDatabase != null) {
						countrymemo.mDatabase.execSQL(SQL);
			    	}
				}

	    	} catch (IOException ex) {
	    		Log.d(TAG, "Exception in copying photo : " + ex.toString());
	    	}


    	}
    	return photoName;
    }

    //-------------------------------------------------------------------------------------

	private void deleteMemo() {

		// 포토 데이테 베이스 삭제
		Log.d(TAG, "사진기록과 파일 삭제 : " + mMediaPhotoId);
		String SQL = "delete from " + MemoDatabase.TABLE_PHOTO +
				" where _ID = '" + mMediaPhotoId + "'";

		Log.d(TAG, "SQL : " + SQL);

		if (countrymemo.mDatabase != null) {
			countrymemo.mDatabase.execSQL(SQL);
		}

		File photoFile = new File(BasicInfo.FOLDER_PHOTO + mMediaPhotoUri);
		if (photoFile.exists()) {
			photoFile.delete();
		}

		// 메모삭제
		Log.d(TAG, "메모기록 삭제 : " + mMemoId);
		SQL = "delete from " + MemoDatabase.TABLE_MEMO +
				" where _id = '" + mMemoId + "'";
		Log.d(TAG, "SQL : " + SQL);
		if (countrymemo.mDatabase != null) {
			countrymemo.mDatabase.execSQL(SQL);
		}

		setResult(RESULT_OK);

		finish();
	}

	//지금 시간으로 사진이름 정하기
    private String createFilename() {
    	Date curDate = new Date();
    	String curDateStr = String.valueOf(curDate.getTime());

    	return curDateStr;
	}

	// 사진 찍을때, true일때 사진찍기
    public void setMediaLayout() {
    	isPhotoCaptured = false;

    }

    //날짜 설정하기
    private void setCalendar(){
        //날짜 버튼 눌렀을때, 캘린더에서 날짜 선택
    	insertDateButton = (Button) findViewById(R.id.insert_dateBtn);
    	insertDateButton.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			String mDateStr = insertDateButton.getText().toString();
    			Calendar calendar = Calendar.getInstance();
    			Date date = new Date();
    			try {
    				date = BasicInfo.dateDayNameFormat.parse(mDateStr);
    			} catch(Exception ex) {
    				Log.d(TAG, "Exception in parsing date : " + date);
    			}

    			calendar.setTime(date);

    			new DatePickerDialog(
    					MemoInsertActivity.this,
    					dateSetListener,
    					calendar.get(Calendar.YEAR),
    					calendar.get(Calendar.MONTH),
    					calendar.get(Calendar.DAY_OF_MONTH)
    					).show();

    		}
    	});

    	Date curDate = new Date();
    	mCalendar.setTime(curDate);

		//연도, 월, 일
    	int year = mCalendar.get(Calendar.YEAR);
    	int monthOfYear = mCalendar.get(Calendar.MONTH);
    	int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

		//날짜 버튼위로 내가 선택한 날짜가 보인다
    	insertDateButton.setText(year + "년 " + (monthOfYear+1) + "월 " + dayOfMonth + "일");

    }


    /**
     * 날짜 설정 리스너
     */
    // 날짜 선택 버튼
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mCalendar.set(year, monthOfYear, dayOfMonth);
			insertDateButton.setText(year + "년 " + (monthOfYear+1) + "월 " + dayOfMonth + "일");
		}
	};


	/**
	 * 일자와 메모 확인
	 */
    private boolean parseValues() {
		//날짜 입력
    	String insertDateStr = insertDateButton.getText().toString();
		//입력된 날짜를 지정 형태로 저장 (위: 메모 추가시, 아래 : 메모 목록)
    	try {
    		Date insertDate = BasicInfo.dateDayNameFormat.parse(insertDateStr);
    		mDateStr = BasicInfo.dateDayFormat.format(insertDate);
    	} catch(ParseException ex) {
    		Log.e(TAG, "Exception in parsing date : " + insertDateStr);
    	}

    	// 내가 입력한 메모
    	String memotxt = mMemoEdit.getText().toString();
    	mMemoStr = memotxt;

		// 메모 입력값이 아무것도 없거나 공백(space)만 입력했을때, dialog 출력
    	if (mMemoStr.trim().length() < 1) {
    		showDialog(BasicInfo.CONFIRM_TEXT_INPUT);
    		return false;
    	}

    	return true;
    }

	//dialog 내용 switch문
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = null;
		switch(id) {
			//입력한 내용이 빈칸이거나 없을 때
			case BasicInfo.CONFIRM_TEXT_INPUT:
				builder = new AlertDialog.Builder(this);
				builder.setTitle("메모");
				builder.setMessage("텍스트를 입력하세요.");
				builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
	        	    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

				break;

			//사진 추가 화면을 눌렀을 때 dialog
			case BasicInfo.CONTENT_PHOTO:
				builder = new AlertDialog.Builder(this);
				// 선택사항 2개 = array_photo
				mSelectdContentArray = R.array.array_photo;
				builder.setTitle("선택하세요");

				//초기값은 0번째 = 사진촬영 (초기화만)
				builder.setSingleChoiceItems(mSelectdContentArray, 0, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	mChoicedArrayItem = whichButton;
	                }
	            });

				//선택에 따라 사진 촬영 화면 또는 선택 화면으로 넘어간다. (선택)
				builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
	        	    public void onClick(DialogInterface dialog, int whichButton) {
	        	    	if(mChoicedArrayItem == 0 ) {
	        	    		showPhotoCaptureActivity();
	        	    	} else if(mChoicedArrayItem == 1) {
	        	    		showPhotoSelectionActivity();
	        	    	}
	                }
	            });

				// 선택없이 취소버튼
				builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
		             public void onClick(DialogInterface dialog, int whichButton) {
		            	 Log.d(TAG, "whichButton3        ======        " + whichButton);
	                 }
	            });

				break;

			// 입력된 메모를 수정할때,
			case BasicInfo.CONTENT_PHOTO_EX:
				builder = new AlertDialog.Builder(this);
				//보기 3개 (촬영, 선택, 삭제)
				mSelectdContentArray = R.array.array_photo_ex;
				builder.setTitle("선택하세요");

				//선택 보기 초기값은 인덱스 0 = 사진 촬영
				builder.setSingleChoiceItems(mSelectdContentArray, 0, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	mChoicedArrayItem = whichButton;
	                }
	            });

				// 내가 보기 선택하는 함수
				builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
	        	    public void onClick(DialogInterface dialog, int whichButton) {
						// 사진 촬영
	        	    	if(mChoicedArrayItem == 0) {
	        	    		showPhotoCaptureActivity();
	        	    	}
	        	    	// 사진 선택(새로 불러오기)
	        	    	else if(mChoicedArrayItem == 1) {
	        	    		showPhotoSelectionActivity();
	        	    	}
	        	    	// 기존 사진 삭제
	        	    	else if(mChoicedArrayItem == 2) {
	        	    		isPhotoCanceled = true;
	        	    		isPhotoCaptured = false;

							// 사진을 삭제한 자리에 default값 설정
	        	    		mPhoto.setImageResource(R.drawable.person_add);
	        	    	}
	                }
	            });

				//보기 선택하지 않음.
				builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
		             public void onClick(DialogInterface dialog, int whichButton) {

	                 }
	             });

				break;

			// 메모 수정화면에서 삭제 버튼 누를 때
			case BasicInfo.CONFIRM_DELETE:
				builder = new AlertDialog.Builder(this);
				builder.setTitle("메모");
				builder.setMessage("메모를 삭제하시겠습니까?");
				//예
				builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						deleteMemo();
					}
				});
				//아니오.
				builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dismissDialog(BasicInfo.CONFIRM_DELETE);
					}
				});

				break;
			default:
				break;
		}

		return builder.create();
	}

	// 사진 촬영 화면으로 넘어간다.
	public void showPhotoCaptureActivity() {
		Intent intent = new Intent(getApplicationContext(), PhotoCaptureActivity.class);
		startActivityForResult(intent, BasicInfo.REQ_PHOTO_CAPTURE_ACTIVITY);
	}

	// 사진 선택 화면으로 넘어간다.
	public void showPhotoSelectionActivity() {
		Intent intent = new Intent(getApplicationContext(), PhotoSelectionActivity.class);
		startActivityForResult(intent, BasicInfo.REQ_PHOTO_SELECTION_ACTIVITY);
	}

    /**
     * 다른 액티비티로부터의 응답 처리
     */
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		switch(requestCode) {
			case BasicInfo.REQ_PHOTO_CAPTURE_ACTIVITY:  // 사진 찍는 경우
				Log.d(TAG, "onActivityResult() for REQ_PHOTO_CAPTURE_ACTIVITY.");

				if (resultCode == RESULT_OK) {
					Log.d(TAG, "resultCode : " + resultCode);

					//저장된 사진이다
					boolean isPhotoExists = checkCapturedPhotoFile();
			    	if (isPhotoExists) {
			    		Log.d(TAG, "image file exists : " + BasicInfo.FOLDER_PHOTO + "captured");

			    		resultPhotoBitmap = BitmapFactory.decodeFile(BasicInfo.FOLDER_PHOTO + "captured");

			    		tempPhotoUri = "captured";

			    		mPhoto.setImageBitmap(resultPhotoBitmap);
			            isPhotoCaptured = true;

			            mPhoto.invalidate();
			    	} else {
			    		Log.d(TAG, "image file doesn't exists : " + BasicInfo.FOLDER_PHOTO + "captured");
			    	}
				}

				break;

			case BasicInfo.REQ_PHOTO_SELECTION_ACTIVITY:  // 사진을 앨범에서 선택하는 경우
				Log.d(TAG, "onActivityResult() for REQ_PHOTO_LOADING_ACTIVITY.");

				if (resultCode == RESULT_OK) {
					Log.d(TAG, "resultCode : " + resultCode);

					Uri getPhotoUri = intent.getParcelableExtra(BasicInfo.KEY_URI_PHOTO);
					try {

	    				BitmapFactory.Options options = new BitmapFactory.Options();
	    				options.inSampleSize = 8;

						resultPhotoBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(getPhotoUri), null, options);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					mPhoto.setImageBitmap(resultPhotoBitmap);
		            isPhotoCaptured = true;

		            mPhoto.invalidate();
				}

				break;

		}
	}


	/**
     * 저장된 사진 파일 확인
     */
    private boolean checkCapturedPhotoFile() {
    	File file = new File(BasicInfo.FOLDER_PHOTO + "captured");
    	if(file.exists()) {
    		return true;
    	}

    	return false;
    }

}
