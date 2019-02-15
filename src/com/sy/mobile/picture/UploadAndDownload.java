package com.sy.mobile.picture;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONException;

import com.sy.moblie.json.JsonAnalytical;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 下载
 */
public class UploadAndDownload {
    private String SERVER_URL = "";
    private String end = "\r\n";
    private String twoHyphens = "--";
    private String boundary = "*****";
    private boolean isCom = false;
    String ide;
    private Dialog pdialog;
    private Map<String, Boolean> map = new HashMap<String, Boolean>();
    JsonAnalytical jso = new JsonAnalytical();
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    String fileName;

    public UploadAndDownload(String server_url) {
        SERVER_URL = server_url;
    }

    public void setUpload(String server_url) {
        SERVER_URL = server_url;
    }

    public interface UploadCont {
        /**
         * @param 返回数据
         */
        public void content(Object content);
    }

    UploadCont callback;

    /**
     * 设置是否压缩默认不需要压缩
     */
    public void setIsCom(boolean isCom) {
        this.isCom = isCom;
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 下载中进度
//				mProgress.setProgress(progress);
                    Log.i("jindu", progress + "");
                    break;
                case DOWNLOAD_FINISH:
                    // 下载完成
//				installApk();
                    if (callback != null)
                        callback.content(msg.obj);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    /**
     * 上传
     */
    public void upload(String identification, Dialog dialog, final HashMap<String, File> fileMap, final Map<String, String> params) {
        if (identification != null && dialog != null) {
            showDialog(dialog, identification);
            ide = identification;
        }
        new Thread() {
            public void run() {
                String list = "";
                list = getData(fileMap, params);
                try {
                    Message mess = new Message();
                    mess.what = 0;
                    mess.obj = jso.JsonRe(list);
                    han.sendMessage(mess);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Message mess = new Message();
                    mess.what = -1;
                    mess.obj = "获取数据失败";
                    han.sendMessage(mess);
                }
            }

            ;
        }.start();
    }

    Handler han = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (cloDialog(ide)) {
                switch (msg.what) {
                    case 0:
                        // showToast("获取成功");
                        if (callback != null)
                            callback.content(msg.obj);
                        break;
                    default:
                        // showToast((String)msg.obj);
                        if (callback != null)
                            callback.content(null);
                        break;
                }
            }
            return false;
        }
    });

    public String getData(HashMap<String, File> fileMap, Map<String, String> params) {
        String data = "";
        try {
            URL url = new URL(SERVER_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
			/* 设置传送的method=POST */
            con.setRequestMethod("POST");
            con.setConnectTimeout(60000);//连接超时 单位毫秒
            con.setReadTimeout(60000);
			/* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
			/* 设置DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());

            writeFileParams(fileMap, ds);

            writeStringParams(params, ds);

            paramsEnd(ds);

            ds.flush();
			/* 取得Response内容 */
            Log.i("httpto", SERVER_URL + ds.toString());
            System.out.println(con.getResponseCode());
            InputStream is = con.getInputStream();

            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
			/* 将Response显示于Dialog */
            // showDialog("上传成功" + b.toString().trim());
			/*
			 * // 关闭DataOutputStream
			 */
            data = b.toString().trim();
            Log.i("httpby", data);
            ds.close();
        } catch (Exception e) {
            // showDialog("上传失败" + e);
            Log.i("httpby", e.toString());
        }
        return data;
    }

    /**
     * 添加参数数据
     *
     * @param params
     * @param ds
     * @throws Exception
     */
    private void writeStringParams(Map<String, String> params,
                                   DataOutputStream ds) throws Exception {
        Set<String> keySet = params.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            String name = it.next();
            String value = params.get(name);
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; name=\"" + name
                    + "\"" + end);
            ds.writeBytes(end);
            ds.writeBytes(URLEncoder.encode(value) + end);
        }
    }

    /**
     * 添加文件数据
     *
     * @param params
     * @param ds
     * @throws Exception
     */
    private void writeFileParams(Map<String, File> params, DataOutputStream ds)
            throws Exception {
        Set<String> keySet = params.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
            String name = it.next();
            File value = params.get(name);
            //如果isCom是真，需要压缩图片
            if (isCom) {
                String fileto = value.toString();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeFile(fileto,options);
//				int hhe = getBitmapSize(bitmap);
                File file = new File((Environment.getExternalStorageDirectory().toString()
                        + "/media/"+value.getName()).replace(".", "(2)."));// 将要保存图片的路径
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(
                            new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                    bitmap.recycle();
                    value = file;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; name=\"" + name
                    + "\"; filename=\"" + URLEncoder.encode(value.getName())
                    + "\"" + end);
            ds.writeBytes("Content-Type: " + getContentType(value) + end);
            ds.writeBytes(end);
            ds.write(getBytes(value));
            ds.writeBytes(end);
        }
    }

    // 获取文件的上传类型，图片格式为image/png,image/jpg等。非图片为application/octet-stream
    private String getContentType(File f) throws Exception {
        return "application/octet-stream";
    }

    // 把文件转换成字节数组
    private byte[] getBytes(File f) throws Exception {
        FileInputStream in = new FileInputStream(f);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = in.read(b)) != -1) {
            out.write(b, 0, n);
        }
        in.close();
        return out.toByteArray();
    }

    // 添加结尾数据
    private void paramsEnd(DataOutputStream ds) throws Exception {
        ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
        ds.writeBytes(end);
    }

    /**
     * 关闭等待窗口
     *
     * @param lei 标识
     * @return
     */
    public boolean cloDialog(String lei) {
        if (lei == null)
            return true;
        if (map.get(lei)) {
            return false;
        } else {
            if (pdialog != null)
                pdialog.dismiss();

            return true;
        }
    }

    /**
     * 显示等待窗口
     *
     * @param te  显示的内容
     * @param lei 标识
     */
    public void showDialog(Dialog dialog, final String lei) {
        pdialog = dialog;
        pdialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface arg0) {
                // TODO Auto-generated method stub
                // showToast("关闭");
                map.put(lei, true);
            }
        });
        pdialog.setCancelable(true);
        map.put(lei, false);
    }

    public void setOnReturn(UploadCont callback) {
        this.callback = callback;
    }

    /**
     * 下载文件（默认保存在download文件夹下）
     *
     * @param fileName 文件名
     */
    public void downFile(String fileName) {
        this.fileName = fileName;

        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    public void setSavePath(String mSavePath) {
        this.mSavePath = mSavePath;
    }

    /**
     * 下载文件线程
     *
     * @author coolszy
     * @date 2012-4-26
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    //如果有路径，就不用默认的
                    if (mSavePath == null || mSavePath.length() == 0) {
                        // 获得存储卡的路径
                        String sdpath = Environment.getExternalStorageDirectory()
                                + "/";
                        mSavePath = sdpath + "download";
                    }
                    URL url = new URL(SERVER_URL);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, fileName);
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
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
            }
            // 取消下载对话框显示
//			mDownloadDialog.dismiss();
        }
    }

    ;
}
