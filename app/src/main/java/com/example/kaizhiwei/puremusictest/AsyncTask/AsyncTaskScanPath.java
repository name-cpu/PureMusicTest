package com.example.kaizhiwei.puremusictest.AsyncTask;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.MainThread;
import android.text.TextUtils;
import android.util.Log;

import com.example.kaizhiwei.puremusictest.MediaData.VLCInstance;
import com.example.kaizhiwei.puremusictest.Util.DeviceUtil;

import org.videolan.libvlc.Media;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kaizhiwei on 16/10/30.
 */
public class AsyncTaskScanPath extends AsyncTask<HashMap<String,String>, Integer, HashMap<String, String>> {
    public interface ScanResultListener{
        public void onScanStart();
        public void onScaning(int process, String strFilePath, boolean bAudioFile);
        public void onScanCompleted(HashMap<String, String>  mapResult, int filterNum);
    }

    private static final String TAG = "AsyncTaskScanPath";
    private List<String> mListScanPath;
    private List<String> mListSupportAudioFormat;
    private HashMap<String, String> mMapScanResult;
    private long mScanFileSize;
    private long mAvailableSize;
    private ScanResultListener mListener;
    private int mMinMediaDuration = 0;
    private List<String> mListFiletFolderName;
    private boolean mIsCancel = false;
    private int mFilterMediaNum = 0;

    public AsyncTaskScanPath(ScanResultListener listener, List<String> listScanPath){
        mListener = listener;
        mListScanPath = listScanPath;
    }

    public void setMinMediaDuration(int minDuration) {
        mMinMediaDuration = minDuration;
    }

    public void setFilterFolderPath(List<String> list){
        mListFiletFolderName = list;
    }

    public void cancel(){
        mIsCancel = true;
    }

    @MainThread
    protected void onPreExecute() {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

        }else{
            Log.e(TAG, "sdcard not mounted.");
        }

        mMapScanResult = new HashMap<String, String>();
        mListSupportAudioFormat = new ArrayList<String>();
        mListSupportAudioFormat.add(".aac");
        mListSupportAudioFormat.add(".MP3");
        mListSupportAudioFormat.add(".AMR");
        mListSupportAudioFormat.add(".Ogg");
        mListSupportAudioFormat.add(".PCM");

        if(mListScanPath == null || mListScanPath.size() == 0){
            mListScanPath = new ArrayList<>();
            mListScanPath.add(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator);
        }

        for(int i = 0;i < mListScanPath.size();i++){
            mAvailableSize += DeviceUtil.getFolderSize(new File(mListScanPath.get(i))); //new File(mListScanPath.get(i)).getUsableSpace(); //getPathTotalSize(mListScanPath.get(i)) - getPathAvailableSize(mListScanPath.get(i));
        }

        if(mListener != null){
            mListener.onScanStart();
        }
    }

    @Override
    protected HashMap<String, String> doInBackground(HashMap<String, String>... params) {

        for(int i = 0; i < mListScanPath.size();i++){
            getDirFile(mListScanPath.get(i));
        }

        return mMapScanResult;
    }

    @MainThread
    protected void onPostExecute(HashMap<String, String>  result) {
        if(mListener != null){
            mListener.onScanCompleted(result, mFilterMediaNum);
        }
    }

    /**
     * Runs on the UI thread after {@link #publishProgress} is invoked.
     * The specified values are the values passed to {@link #publishProgress}.
     *
     * @param values The values indicating progress.
     *
     * @see #publishProgress
     * @see #doInBackground
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @MainThread
    protected void onProgressUpdate(int... values) {
    }

    // 获得SD卡总大小
    private long getPathTotalSize(String path) {
        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return blockSize * totalBlocks;
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    private long getPathAvailableSize(String path) {
        File file = new File(path);
        if(file.isDirectory() == false)
            return 0;

        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    private void getSDFile(String path){
        if(path == null)
            return;

        File root = new File(path);
        //Log.e("weikaizhi", "path: " + path + "###" + root.getAbsolutePath() + " threadId: " + Thread.currentThread().getName());
        //非utf8文件名称编码会崩溃
        String files[] = root.list();
        if(files != null){
            for(String f: files){
                File file = new File(f);
                if(file.isDirectory()){
                    getSDFile(file.getAbsolutePath());
                }
                else{
                    String strFilePath = file.getPath();
                    mScanFileSize += f.length();
                    int progress = (int)(mScanFileSize * 100.0 / mAvailableSize);
                    Log.e("weikaizhi", "mScanFileSize: " + mScanFileSize + ",mAvailableSize: " + mAvailableSize);
                    publishProgress(progress);

                    //过滤不需要扫描的文件夹
                    boolean isContinue = false;
                    if(mListScanPath != null){
                        for(int i = 0;i < mListScanPath.size();i++){
                            if(strFilePath.contains(mListScanPath.get(i))){
                                isContinue = true;
                                break;
                            }
                        }
                    }
                    if(isContinue)
                        continue;

                    int index = strFilePath.lastIndexOf(".");
                    String strFileFormat = strFilePath.substring(index, strFilePath.length()-1);
                    //Log.e("weikaizhi", "strFileFormat:" + strFileFormat);
                    for(int i = 0;i < mListSupportAudioFormat.size();i++) {
                        if (mListSupportAudioFormat.get(i).compareToIgnoreCase(strFileFormat) == 0) {
                            Media media = new Media(VLCInstance.getInstance(), Uri.parse(strFilePath));
                            media.parse();
                            if(media.getDuration() <= mMinMediaDuration){
                                media.release();
                                continue;
                            }

                            mMapScanResult.put(strFilePath, strFileFormat);
                        }
                    }

                    if(mListener != null){
                        mListener.onScaning(progress, strFilePath, false);
                    }
                }
            }
        }
    }

    public void getDirFile(String path){
        if(TextUtils.isEmpty(path))
            return ;

        File parentFile = new File(path);
        if(!parentFile.exists())
            return;

        if(mIsCancel)
            return;

        //String strSubFile = execCommand("cd " + path + "\n" + "ls\n");
        List<File> listSubFile = Arrays.asList(parentFile.listFiles()); //Arrays.asList(strSubFile.split("\n"));
        for(int i = 0;i < listSubFile.size();i++){
            File file =  listSubFile.get(i);
            if(file == null)
                continue;

            if(file.isHidden())
                continue;

            if(mIsCancel)
                return;

            Log.e("weikaizhi", "scaning: " + file.getAbsolutePath());
            if(file.isDirectory()){
                getDirFile(file.getAbsolutePath());
            }
            else{
                String strFilePath = file.getAbsolutePath();
                mScanFileSize += file.length();
                int progress =  (int)(mScanFileSize / mAvailableSize);
                Log.e("weikaizhi", "mScanFileSize: " + mScanFileSize + ",mAvailableSize: " + mAvailableSize);
                publishProgress(progress);

                String strFileName = file.getName();
                boolean bAudioFile = false;
                int index = strFileName.lastIndexOf(".");
                if(index < 0)
                    continue ;

                String strFileFormat = strFileName.substring(index, strFileName.length());
                //Log.e("weikaizhi", "strFileName:" + strFileName + "###strFileFormat:" + strFileFormat + "mScanFileSize:"+mScanFileSize+ "###mAvailableSize:"+mAvailableSize);
                for(int j = 0;j < mListSupportAudioFormat.size();j++) {
                    if (mListSupportAudioFormat.get(j).compareToIgnoreCase(strFileFormat) == 0) {
                        Media media = new Media(VLCInstance.getInstance(), Uri.parse(strFilePath));
                        media.parse();
                        if(media.getDuration() <= mMinMediaDuration * 1000){
                            mFilterMediaNum++;
                            continue;
                        }
                        else{
                            mMapScanResult.put(strFilePath, strFileFormat);
                            bAudioFile = true;
                        }
                        media.release();
                    }
                }

                if(mListener != null){
                    mListener.onScaning(progress, strFilePath, bAudioFile);
                }
            }
        }
    }

    public String execCommand(String command) throws IOException {
        // start the ls command running
        //String[] args =  new String[]{"sh", "-c", command};
        Runtime runtime = Runtime.getRuntime();
        String[] newCommond = new String[]{"/system/bin/sh", "-c", command};
        Process proc = runtime.exec(newCommond);        //这句话就是shell与高级语言间的调用
        //如果有参数的话可以用另外一个被重载的exec方法
        //实际上这样执行时启动了一个子进程,它没有父进程的控制台
        //也就看不到输出,所以我们需要用输出流来得到shell执行后的输出
        InputStream inputstream = proc.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
        // read the ls output
        String line = "";
        StringBuilder sb = new StringBuilder(line);
        while ((line = bufferedreader.readLine()) != null) {
            //System.out.println(line);
            sb.append(line);
            sb.append('\n');
        }
        //tv.setText(sb.toString());
        //使用exec执行不会等执行成功以后才返回,它会立即返回
        //所以在某些情况下是很要命的(比如复制文件的时候)
        //使用wairFor()可以等待命令执行完成以后才返回

        if(proc != null){
            proc.destroy();
        }
        return sb.toString();
    }

    public String exec(String[] args) {
        String result = "";
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        InputStream is = null;
        try {
            process = processBuilder.start();
            is = process.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            while ((read = is.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            result = new String(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }
}
