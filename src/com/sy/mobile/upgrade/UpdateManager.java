package com.sy.mobile.upgrade;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.tools.R;
import com.sy.mobile.control.MyDialog;
import com.sy.mobile.net.HttpDream;
import com.sy.mobile.net.HttpDream.Cont;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * app自动更新
 *
 * @author coolszy
 * @date 2012-4-26
 */

public class UpdateManager {
    // HttpDream http = new HttpDream();
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;

    private static int IS_DOWN = 2;
    /* 保存解析的XML信息 */
    HashMap<String, String> mHashMap;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;
    //当前进度
    int count = 0;
    //总大小
    int length = 0;
    private Dialog mDownloadDialog;

    private Activity mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private TextView tvNumber;
    boolean isx = false;

    HttpDream http;

    String titleString, contentString;
    int ioc;

    NotificationManager nm;
    Notification.Builder builder;

    int type = 1;

    int diobg = 0;
    /**
     * 是否显示通知栏
     */
    boolean isNo;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @SuppressLint("NewApi")
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    mProgress.setProgress(progress);
                    tvNumber.setText(count + "/" + length);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    private Handler mHandler2 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 显示提示对话框
                    showNoticeDialog(new HashMap<String, String>());
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    public UpdateManager(Activity context) {
        this.mContext = context;
    }

    /**
     * 检测软件更新
     */
    private void checkUpdate() {
        // TODO Auto-generated method stub
        if (isUpdate()) {
            mHandler2.sendEmptyMessage(0);
        } else {
            if (isx)
                Toast.makeText(mContext, "当前已是最新版本", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 不显示通知栏
     */
    public void setIsNo() {
        isNo = true;
    }

    /**
     * 自定义
     */
    public void setDiobg(int diobg) {
        this.diobg = diobg;
    }

    /**
     * 设置标题
     *
     * @param titleString
     * @param contentString
     */
    public void setStringTitle(String titleString, String contentString) {
        this.titleString = titleString;
        this.contentString = contentString;
    }

    /**
     * 设置下载图标
     *
     * @param ioc
     */
    public void setIoc(int ioc) {
        this.ioc = ioc;
    }

    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
    private boolean isUpdate() {
        // 获取当前软件版本
        int versionCode = getVersionCode();
        if (null != mHashMap) {
            int serviceCode = Integer.valueOf(mHashMap.get("version"));
            // 版本判断
            if (serviceCode > versionCode) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取软件版本号
     *
     * @return
     */
    public int getVersionCode() {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取软件版本号名字
     *
     * @return
     */
    public String getVersionName() {
        String versionName = "";
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionName = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 显示软件更新对话框
     */
    public void showNoticeDialog(HashMap<String, String> map) {
        mHashMap = map;
        MyDialog.closeDialog();
        MyDialog.createChoiceDialog(mContext, "软件更新\n检测到新版本，立即更新吗", "更新", "退出程序",
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        MyDialog.closeDialog();
                        // 显示下载对话框
                        showDownloadDialog();

                    }
                }, new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        MyDialog.closeDialog();
                        mContext.finish();
                    }
                });
        MyDialog.isRtu();
    }

    /**
     * 显示软件更新对话框
     */
    public void showNoticeNotDialog(HashMap<String, String> map) {
        if (diobg != 0)
            MyDialog.setChoiceDialogCustom(diobg);
        showNoticeNotDialog(map, true);
    }

    /**
     * 显示软件更新对话框
     */
    public void showNoticeNotDialog(HashMap<String, String> map, boolean isRtu) {
        mHashMap = map;
        MyDialog.closeDialog();
        //是否有标题
        String content = "";
        if (titleString != null && titleString.length() > 0) {
            MyDialog.setTitleString(titleString);
            content = contentString;
        } else {
            content = mContext.getString(R.string.soft_update_info);
        }
        MyDialog.createChoiceOneDialog(mContext, content, mContext.getString(R.string.soft_update_updatebtn),
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        MyDialog.closeDialog();
                        // 显示下载对话框
                        showDownloadDialog();
                    }
                });
        if (isRtu)
            MyDialog.isRtu();
        if (diobg != 0)
            MyDialog.setChoiceDialogCustom(0);
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        if (IS_DOWN == DOWNLOAD) {
            MyDialog.showTextToast("正在更新中..", mContext);
            return;
        }
        //判断android 如果是16以下下显示原来的进度条
        if (Build.VERSION.SDK_INT < 16 || isNo) {
            AlertDialog.Builder builder = new Builder(mContext);
            builder.setTitle(R.string.soft_updating);
            // 给下载对话框增加进度条
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            View v = inflater.inflate(R.layout.softupdate_progress, null);
            mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
            tvNumber = v.findViewById(R.id.tv_number);
            builder.setView(v);
            // 取消更新
            mDownloadDialog = builder.create();
            mDownloadDialog.setCancelable(false);
            mDownloadDialog.show();
        } else {
            nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            //构建一个Notifaction的Builder对象
            builder = new Notification.Builder(mContext);
            builder.setTicker("文件开始下载");
            builder.setSmallIcon(ioc != 0 ? ioc : R.drawable.ic_launcher);
            builder.setContentTitle("文件下载");
            builder.setContentText("文件正在下载中......");
            builder.setWhen(System.currentTimeMillis());
            builder.setDefaults(Notification.FLAG_NO_CLEAR);//消息提示模式
            builder.setProgress(100, 0, false);
            nm.notify(1, builder.build());
        }
        // 下载文件
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载路径
     *
     * @param mSavePath
     */
    public void setDown(String mSavePath) {
        this.mSavePath = mSavePath;
    }

    /**
     * 下载文件线程
     *
     * @author coolszy
     * @date 2012-4-26
     * @blog http://blog.92coding.com
     */
    private class downloadApkThread extends Thread {
        @SuppressLint("NewApi")
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    if (mSavePath == null || mSavePath.length() == 0) {
                        // 获得存储卡的路径
                        String sdpath = Environment.getExternalStorageDirectory()
                                + "/";
                        mSavePath = sdpath + "download";
                    }
                    URL url = new URL(mHashMap.get("url"));
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.connect();
                    // 获取文件大小
                    length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, mHashMap.get("name"));
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        if (builder != null && nm != null) {
                            builder.setProgress(100, progress, false);
                            nm.notify(1, builder.build());
                        }
                        IS_DOWN = DOWNLOAD;
                        //有进度条时修改进度条
                        if (mDownloadDialog != null)
                            mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            if (builder != null && nm != null) {
                                builder.setContentText("文件下载完毕!");
                                nm.cancel(1);
                            }
                            IS_DOWN = DOWNLOAD_FINISH;
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IS_DOWN = DOWNLOAD_FINISH;
            }
            // 取消下载对话框显示
            if (mDownloadDialog != null)
                mDownloadDialog.dismiss();
        }
    }

    /**
     * 安装APK文件
     */
    public void installApk() {
        File apkfile = new File(mSavePath, mHashMap.get("name"));
//        File apkfile = new File(Environment.getExternalStorageDirectory() + "/download", "Education.apk");
        if (!apkfile.exists()) {
            return;
        }
        try {
            Intent intent = new Intent();
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            //设置intent的data和Type属性。android 7.0以上crash,改用provider
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri fileUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", apkfile);//android 7.0以上
                intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                grantUriPermission(mContext, fileUri, intent);
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                        "application/vnd.android.package-archive");
            }
            //跳转
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void grantUriPermission(Context context, Uri fileUri, Intent intent) {
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    /**
     * 将String转换成InputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static InputStream StringTOInputStream(String in) throws Exception {

        ByteArrayInputStream is = new ByteArrayInputStream(
                in.getBytes("ISO-8859-1"));
        return is;
    }

    /**
     * make true current connect service is wifi
     *
     * @param mContext
     * @return
     */
    private boolean isWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

}
