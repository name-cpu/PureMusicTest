package com.example.kaizhiwei.puremusictest.AsyncTask;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.MainThread;
import android.text.TextUtils;
import android.util.Log;

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
        public void onScanCompleted(HashMap<String, String>  mapResult);
    }

    private static final String TAG = "AsyncTaskScanPath";
    private List<String> mListScanPath;
    private List<String> mListSupportAudioFormat;
    private HashMap<String, String> mMapScanResult;
    private long mScanFileSize;
    private long mAvailableSize;
    private ScanResultListener mListener;

    public AsyncTaskScanPath(ScanResultListener listener, List<String> listScanPath){
        mListener = listener;
        mListScanPath = listScanPath;
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
            mAvailableSize += getPathTotalSize(mListScanPath.get(i)) - getPathAvailableSize(mListScanPath.get(i));
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
            mListener.onScanCompleted(result);
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
        Log.e("weikaizhi", "path: " + path + "###" + root.getAbsolutePath() + " threadId: " + Thread.currentThread().getName());
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
                    int progress =  (int)(mScanFileSize / mAvailableSize);
                    publishProgress(progress);


                    int index = strFilePath.lastIndexOf(".");
                    String strFileFormat = strFilePath.substring(index, strFilePath.length()-1);
                    Log.e("weikaizhi", "strFileFormat:" + strFileFormat);
                    for(int i = 0;i < mListSupportAudioFormat.size();i++) {
                        if (mListSupportAudioFormat.get(i).compareToIgnoreCase(strFileFormat) == 0) {
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

        try {
            String strSubFile = execCommand("cd " + path + "\n" + "ls\n");
            List<String> listSubFile = Arrays.asList(strSubFile.split("\n"));
            for(int i = 0;i < listSubFile.size();i++){
                String strItem = listSubFile.get(i);
                if(TextUtils.isEmpty(strItem))
                    continue;

                Log.e("weikaizhi", "scaning: " + path + File.separator + strItem);
                File file = new File(path + File.separator + strItem);
                if(file.isDirectory()){
                    getDirFile(file.getCanonicalPath());
                }
                else{
                    String strFilePath = file.getCanonicalPath();
                    mScanFileSize += file.length();
                    int progress =  (int)(mScanFileSize / mAvailableSize);
                    publishProgress(progress);

                    int index = strFilePath.lastIndexOf(File.separator);
                    if(index < 0)
                        return ;

                    String strFileName = strFilePath.substring(index+1, strFilePath.length());
                    index = strFileName.lastIndexOf(".");
                    if(index < 0)
                        return ;

                    boolean bAudioFile = false;
                    String strFileFormat = strFileName.substring(index, strFileName.length());
                    Log.e("weikaizhi", "strFileName:" + strFileName + "###strFileFormat:" + strFileFormat + "mScanFileSize:"+mScanFileSize+ "###mAvailableSize:"+mAvailableSize);
                    for(int j = 0;j < mListSupportAudioFormat.size();j++) {
                        if (mListSupportAudioFormat.get(j).compareToIgnoreCase(strFileFormat) == 0) {
                            mMapScanResult.put(strFilePath, strFileFormat);
                            bAudioFile = true;
                        }
                    }

                    if(mListener != null){
                        mListener.onScaning(progress, strFilePath, bAudioFile);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
