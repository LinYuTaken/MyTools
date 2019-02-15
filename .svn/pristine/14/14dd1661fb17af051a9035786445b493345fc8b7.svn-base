package com.sy.mobile.picture;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tools.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.sy.mobile.control.MyDialog;
import com.wxample.data.MyData;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 图片详情展示
 */
public class ImageDetailFragment extends Fragment {
    private String mImageUrl;
    private ImageView mImageView;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;
    boolean isdw = false;
    boolean isShow = false;
    private boolean mHasLoadedOnce = false;

    public Fragment setUrl(String imageUrl, boolean isShow) {
//		final ImageDetailFragment f = new ImageDetailFragment();
//		final Bundle args = new Bundle();
//		args.putString("url", imageUrl);
//		f.setArguments(args);
        this.isShow = isShow;
        this.mImageUrl = imageUrl;
        return this;
    }

//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
//	}

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
                // 页面可见时
                if (!mHasLoadedOnce) {
                    mHasLoadedOnce = true;
                    if (!isShow)
                        showView();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mImageView = (ImageView) v.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);

        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }
        });
        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {

            @Override
            public void onViewTap(View view, float x, float y) {
                // TODO Auto-generated method stub
                getActivity().finish();
            }
        });
        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        v.findViewById(R.id.dwon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isdw) {
                    mImageView.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(mImageView.getDrawingCache());
                    mImageView.setDrawingCacheEnabled(false);
                    MyData.saveBitmap(null, null, bitmap, getActivity());
                } else {
                    MyDialog.showTextToast("图片未下载完成,请重试", getActivity());
                }
            }
        });
        return v;
    }

    /**
     * 显示图片
     */
    private void showView() {
        ImageLoader.getInstance().displayImage(mImageUrl, mImageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "下载错误";
                        break;
                    case DECODING_ERROR:
                        message = "图片无法显示";
                        break;
                    case NETWORK_DENIED:
                        message = "网络有问题，无法下载";
                        break;
                    case OUT_OF_MEMORY:
                        message = "图片太大无法显示";
                        break;
                    case UNKNOWN:
                        message = "未知的错误";
                        break;
                }
//				Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                isdw = true;
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                isdw = true;
                progressBar.setVisibility(View.GONE);
                mAttacher.update();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isShow) {
            showView();
        }
    }
}
