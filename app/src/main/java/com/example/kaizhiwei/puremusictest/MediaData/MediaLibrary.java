package com.example.kaizhiwei.puremusictest.MediaData;

import android.net.Uri;
import android.text.TextUtils;

import com.example.kaizhiwei.puremusictest.Util.DeviceUtil;
import com.example.kaizhiwei.puremusictest.Util.ExtensionUtil;
import com.example.kaizhiwei.puremusictest.Util.StringUtil;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by kaizhiwei on 16/11/6.
 */

public class MediaLibrary {

    public interface IMediaScanListener{
        public void onScanStart();
        public void onScaning(String fileInfo, float progress);
        public void onScanFinish();
    }

    private static MediaLibrary instance;
    private static final List<String> FOLDER_BLACKLIST;
    private List<MediaEntity> mItemList = new ArrayList<MediaEntity>();
    private ReadWriteLock mItemListLock = new ReentrantReadWriteLock();
    private boolean isScaning;
    private boolean isForceStop;
    private ExecutorService mPool;

    private Map<IMediaScanListener,Object> mMapListener;

    static {
        String[] folder_blacklist = {
                "/alarms",
                "/notifications",
                "/ringtones",
                "/media/alarms",
                "/media/notifications",
                "/media/ringtones",
                "/media/audio/alarms",
                "/media/audio/notifications",
                "/media/audio/ringtones",
                "/android/data",
                "/android/media"
        };

        FOLDER_BLACKLIST = Arrays.asList(folder_blacklist);
    }

    synchronized public static MediaLibrary getInstance(){
        if(instance == null){
            instance = new MediaLibrary();
        }
        return  instance;
    }

    private MediaLibrary(){
        mPool = Executors.newFixedThreadPool(1);
    }

    public List<MediaEntity> getAllMediaEntrty(){

        if(mItemList == null || mItemList.size() == 0){
            mItemListLock.writeLock().lock();
            List<MediaEntity> list = MediaDataBase.getInstance().queryAllMusicInfo();
            mItemList.addAll(list);
            mItemListLock.writeLock().unlock();
        }

        mItemListLock.readLock().lock();
        List<MediaEntity> newList = new ArrayList<>();
        newList.addAll(mItemList);
        mItemListLock.readLock().unlock();
        return newList;
    }

    public void startScan(){
        if(isScaning){
            isForceStop = true;
            isScaning = false;
        }

        isScaning = true;
        notifyScanStart();
        mPool.execute(scanRunnable);
        isForceStop = false;
    }

    public void stopScan(){
        isForceStop = false;
        isScaning = false;
        if(!mPool.isShutdown()){
            mPool.shutdown();
        }
    }

    public void notifyScanStart(){
        if(mMapListener == null)
            return ;

        for(IMediaScanListener key : mMapListener.keySet()){
            if(key != null){
                key.onScanStart();
            }
        }
    }

    public void notifyScaning(String fileInfo, float progress){
        if(mMapListener == null)
            return ;

        for(IMediaScanListener key : mMapListener.keySet()){
            if(key != null){
                key.onScaning(fileInfo, progress);
            }
        }
    }

    public void notifyScanFinish(){
        if(MediaDataBase.getInstance().isEmptyMusicInfo()){

        }
        else{
            MediaDataBase.getInstance().deleteAllMusicInfo();
        }
        for(int i = 0;i < mItemList.size();i++){
            MediaDataBase.getInstance().insertMusicInfo(mItemList.get(i));
        }

        if(mMapListener == null)
            return ;

        for(IMediaScanListener key : mMapListener.keySet()){
            if(key != null){
                key.onScanFinish();
            }
        }
        isScaning = false;
    }

    public void registerListener(IMediaScanListener listener){
        if(mMapListener == null){
            mMapListener = new HashMap<>();
        }

        mMapListener.put(listener,listener);
    }

    public void unregisterListener(IMediaScanListener listener){
        if(mMapListener == null){
            mMapListener = new HashMap<>();
        }

        if(mMapListener.containsKey(listener)){
            mMapListener.remove(listener);
        }
    }

    private static class MediaFileFilter implements FileFilter{

        @Override
        public boolean accept(File file) {
            boolean accept = false;
            if(file.isHidden() == false){
                if(file.isDirectory() && FOLDER_BLACKLIST.contains(file.getPath().toLowerCase())){
                    accept = true;
                }
                else{
                    String fileExtention = StringUtil.getExtention(file.getAbsolutePath());
                    if(ExtensionUtil.AUDIO.contains(fileExtention)  ||
                            ExtensionUtil.SUBTITLES.contains(fileExtention) ||
                            ExtensionUtil.PLAYLIST.contains(fileExtention)){
                        accept = true;
                    }
                }

            }
            return accept;
        }
    }

    private Runnable scanRunnable = new Runnable() {
        private final Stack<File> directoies = new Stack<File>();
        private List<String> scannedPath = new ArrayList<String>();

        @Override
        public void run() {
            mItemListLock.writeLock().lock();
            mItemList.clear();
            mItemListLock.writeLock().unlock();

            scannedPath.clear();
            directoies.clear();
            LibVLC libVlc = VLCInstance.getInstance();

            List<String> mediaDirs =  DeviceUtil.getStorageDirectories();
            for(int i = 0;i < mediaDirs.size();i++){
                File file = new File(mediaDirs.get(i));
                if(file.exists())
                    directoies.add(file);
            }

            LinkedList<File> mediaToScan = new LinkedList<File>();
            MediaFileFilter mediaFileFilter = new MediaFileFilter();
            List<String> ignoreList = new ArrayList<String>();
            while (directoies.isEmpty() == false){
                File dir = directoies.pop();
                String dirPath = dir.getAbsolutePath();
                //过滤系统目录
                if(dirPath.startsWith("/proc/") || dirPath.startsWith("/sys/") || dirPath.startsWith("/dev/"))
                    continue;

                if(scannedPath.contains(dirPath))
                    continue;

                scannedPath.add(dirPath);

                if(new File(dirPath + File.separator + ".nomedia").exists()){
                    ignoreList.add("file://" + dirPath);
                    continue;
                }

                String[] files = dir.list();
                if(files != null){
                    for(String fileName : files){
                        File subFile = new File(dirPath + File.separator + fileName);
                        if(subFile.isDirectory())
                        {
                            directoies.add(subFile);
                        }
                        else if(subFile.isFile()){
                            if(mediaFileFilter.accept(subFile)){
                                mediaToScan.add(subFile);
                                notifyScaning(fileName, 0);
                            }
                        }
                    }
                }

                if(isForceStop)
                    break;
            }

            if(isForceStop)
                return ;

            for(File file : mediaToScan){
                String fileUri = Uri.fromFile(file).toString();
                Media media = new Media(libVlc, Uri.parse(fileUri));
                media.parse();
                if(media.getDuration() <= 0 || (media.getTrackCount() != 0 && TextUtils.isEmpty(media.getTrack(0).codec))){
                    media.release();
                    continue;
                }

                MediaEntity entrty = new MediaEntity(media);
                media.release();
                mItemListLock.writeLock().lock();
                mItemList.add(entrty);
                mItemListLock.writeLock().unlock();
            }
            notifyScanFinish();
        }
    };


}
