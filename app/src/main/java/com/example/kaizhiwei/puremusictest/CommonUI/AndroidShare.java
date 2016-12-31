package com.example.kaizhiwei.puremusictest.CommonUI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.R;

public class AndroidShare extends Dialog implements AdapterView.OnItemClickListener {
	private TextView tvTitle;
	private GridView mGridView;
	private float mDensity;
	private String msgText = "分享了...哈哈";
	private String mImgPath;
	private int mScreenOrientation;
	private List<ShareItem> mListData;
	private Handler mHandler = new Handler();

	private Runnable work = new Runnable() {
		public void run() {
//			int orient = getScreenOrientation();
//			if (orient != mScreenOrientation) {
//				if (orient == 0)
//					mGridView.setNumColumns(4);
//				else {
//					mGridView.setNumColumns(6);
//				}
//				mScreenOrientation = orient;
//				((MyAdapter) mGridView.getAdapter()).notifyDataSetChanged();
//			}
//			mHandler.postDelayed(this, 1000L);
		}
	};

	public AndroidShare(Context context) {
		super(context, R.style.Dialog);
	}

	public AndroidShare(Context context, int theme, String msgText, final String imgUri) {
		super(context, theme);
		this.msgText = msgText;

		if (Patterns.WEB_URL.matcher(imgUri).matches())
			new Thread(new Runnable() {
				public void run() {
					try {
						mImgPath = getImagePath(imgUri, getFileCache());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		else
			this.mImgPath = imgUri;
	}

	public AndroidShare(Context context, String msgText, final String imgUri) {
		super(context, R.style.Dialog);
		this.msgText = msgText;

		if (Patterns.WEB_URL.matcher(imgUri).matches())
			new Thread(new Runnable() {
				public void run() {
					try {
						mImgPath = getImagePath(imgUri,getFileCache());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		else
			this.mImgPath = imgUri;
	}

	void init(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		this.mDensity = dm.density;
		this.mListData = new ArrayList<ShareItem>();
		this.mListData.add(new ShareItem("微信", R.drawable.bt_share_wechat_normal, R.drawable.bt_share_wechat_press,
				"com.tencent.mm.ui.tools.ShareImgUI", "com.tencent.mm"));
		this.mListData.add(new ShareItem("朋友圈", R.drawable.bt_share_circleoffriends_normal, R.drawable.bt_share_circleoffriends_press,
				"com.tencent.mm.ui.tools.ShareToTimeLineUI", "com.tencent.mm"));
		this.mListData.add(new ShareItem("QQ", R.drawable.bt_share_qq_normal, R.drawable.bt_share_qq_press,
				"com.tencent.mobileqq.activity.JumpActivity", "com.tencent.mobileqq"));
		this.mListData.add(new ShareItem("QQ空间", R.drawable.bt_share_qqzone_normal, R.drawable.bt_share_qqzone_press,
				"com.qzone.ui.operation.QZonePublishMoodActivity", "com.qzone"));
		this.mListData.add(new ShareItem("新浪微博", R.drawable.bt_share_sinaweibo_normal, R.drawable.bt_share_sinaweibo_press,
				"com.sina.weibo.EditActivity", "com.sina.weibo"));
		this.mListData.add(new ShareItem("其他", R.drawable.bt_share_wechat_normal, R.drawable.bt_share_wechat_press,
				"", ""));
	}

	public List<ComponentName> queryPackage() {
		List<ComponentName> cns = new ArrayList<ComponentName>();
		Intent i = new Intent("android.intent.action.SEND");
		i.setType("image/*");
		List<ResolveInfo> resolveInfo = getContext().getPackageManager().queryIntentActivities(i, 0);
		for (ResolveInfo info : resolveInfo) {
			ActivityInfo ac = info.activityInfo;
			ComponentName cn = new ComponentName(ac.packageName, ac.name);
			cns.add(cn);
		}
		return cns;
	}

	public boolean isAvilible(Context context, String packageName) {
		PackageManager packageManager = context.getPackageManager();

		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		for (int i = 0; i < pinfo.size(); i++) {
			if (((PackageInfo) pinfo.get(i)).packageName.equalsIgnoreCase(packageName))
				return true;
		}
		return false;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.share_dialog);
		mGridView = (GridView)this.findViewById(R.id.gvShare);
		tvTitle = (TextView)this.findViewById(R.id.tvTitle);

		init(getContext());
		this.mGridView.setAdapter(new MyAdapter());
		this.mGridView.setOnItemClickListener(this);

		this.mHandler.postDelayed(this.work, 1000L);

		Window window = this.getWindow();
		window.getDecorView().setPadding(0,0,0,0);
		window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
		window.setBackgroundDrawableResource(android.R.color.transparent);
		// 可以在此设置显示动画
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		wl.gravity = Gravity.BOTTOM;

		window.setAttributes(wl);

		setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				mHandler.removeCallbacks(work);
			}
		});
	}

	public void setTitle(String strTitle){
		if(tvTitle != null)
			tvTitle.setText(strTitle);
	}

	public void show() {
		super.show();
	}

	public int getScreenOrientation() {
		int landscape = 0;
		int portrait = 1;
		Point pt = new Point();
		getWindow().getWindowManager().getDefaultDisplay().getSize(pt);
		int width = pt.x;
		int height = pt.y;
		return width > height ? portrait : landscape;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ShareItem share = (ShareItem) this.mListData.get(position);
		shareMsg(getContext(), "分享到...", this.msgText, this.mImgPath, share);
	}

	private void shareMsg(Context context, String msgTitle, String msgText,
			String imgPath, ShareItem share) {
		if (!share.packageName.isEmpty() && !isAvilible(getContext(), share.packageName)) {
			Toast.makeText(getContext(), "请先安装" + share.title, Toast.LENGTH_SHORT).show();
			return;
		}

		Intent intent = new Intent("android.intent.action.SEND");
		if ((imgPath == null) || (imgPath.equals(""))) {
			intent.setType("text/plain");
		} else {
			File f = new File(imgPath);
			if ((f != null) && (f.exists()) && (f.isFile())) {
				intent.setType("image/png");
				intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
			}
		}

		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if(!share.packageName.isEmpty()) {
			intent.setComponent(new ComponentName(share.packageName,share.activityName));
			context.startActivity(intent);
		}
		else {
			context.startActivity(Intent.createChooser(intent, msgTitle));
		}
	}

	private File getFileCache() {
		File cache = null;

		if (Environment.getExternalStorageState().equals("mounted"))
			cache = new File(Environment.getExternalStorageDirectory() + "/." + getContext().getPackageName());
		else {
			cache = new File(getContext().getCacheDir().getAbsolutePath() + "/." + getContext().getPackageName());
		}
		if ((cache != null) && (!cache.exists())) {
			cache.mkdirs();
		}
		return cache;
	}

	public String getImagePath(String imageUrl, File cache) throws Exception {
		String name = imageUrl.hashCode() + imageUrl.substring(imageUrl.lastIndexOf("."));
		File file = new File(cache, name);

		if (file.exists()) {
			return file.getAbsolutePath();
		}

		URL url = new URL(imageUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		if (conn.getResponseCode() == 200) {
			InputStream is = conn.getInputStream();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			is.close();
			fos.close();

			return file.getAbsolutePath();
		}

		return null;
	}

	private final class MyAdapter extends BaseAdapter {
		private static final int image_id = 256;
		private static final int tv_id = 512;

		public MyAdapter() {
		}

		public int getCount() {
			return mListData.size();
		}

		public Object getItem(int position) {
			return mListData.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		private View getItemView() {
			LinearLayout item = new LinearLayout(getContext());
			item.setOrientation(LinearLayout.VERTICAL);
			int margin = (int) (5.0F * mDensity);
			AbsListView.LayoutParams param = new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			item.setLayoutParams(param);
			item.setGravity(Gravity.CENTER);

			MyImageView iv = new MyImageView(getContext());
			item.addView(iv);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(0,margin,0,margin);
			iv.setLayoutParams(layoutParams);
			iv.setId(image_id);

			TextView tv = new TextView(getContext());
			item.addView(tv);
			layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(margin,margin,margin,margin);
			tv.setLayoutParams(layoutParams);

			tv.setTextColor(AndroidShare.this.getContext().getResources().getColor(R.color.mainTextColor));
			tv.setTextSize(14.0F);
			tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
			tv.setId(tv_id);

			return item;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getItemView();
			}
			MyImageView iv = (MyImageView) convertView.findViewById(image_id);
			iv.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					ShareItem share = (ShareItem) AndroidShare.this.mListData.get(position);
					AndroidShare.this.shareMsg(getContext(), "分享到...", AndroidShare.this.msgText, AndroidShare.this.mImgPath, share);
				}
			});

			TextView tv = (TextView) convertView.findViewById(tv_id);
			ShareItem item = (ShareItem) mListData.get(position);
			iv.setResId(item.normalResId, item.pressedResId);
			tv.setText(item.title);
			return convertView;
		}
	}

	private class ShareItem {
		String title;
		int normalResId;
		int pressedResId;
		String activityName;
		String packageName;

		public ShareItem(String title, int normalResId, int pressedResId, String activityName, String packageName) {
			this.title = title;
			this.normalResId = normalResId;
			this.pressedResId = pressedResId;
			this.activityName = activityName;
			this.packageName = packageName;
		}
	}
}
