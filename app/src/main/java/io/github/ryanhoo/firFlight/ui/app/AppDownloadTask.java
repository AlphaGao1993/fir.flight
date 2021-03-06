package io.github.ryanhoo.firFlight.ui.app;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.github.ryanhoo.firFlight.FlightApplication;
import rx.Observable;
import rx.Subscriber;

/**
 * Created with Android Studio. User: ryan.hoo.j@gmail.com Date: 8/22/16 Time: 12:12 AM Desc:
 * ObservableDownloadTask
 */

/* package */ class AppDownloadTask {

    private static final String TAG = "AppDownloadTask";
    private static final int BUFFER_SIZE = 64 * 1024;

    private String mUrl;
    private DownloadInfo mDownloadInfo;

    /* package */ AppDownloadTask(String url) {
        mUrl = url;
        mDownloadInfo = new DownloadInfo();
    }

    /* package */ Observable<DownloadInfo> downloadApk(final File fileDir) {
        return Observable.create(new Observable.OnSubscribe<DownloadInfo>() {
            @Override
            public void call(Subscriber<? super DownloadInfo> subscriber) {

                // Initiate download progress
                subscriber.onNext(mDownloadInfo);
                HttpURLConnection urlConnection = null;
                BufferedOutputStream outputStream = null;
                try {
                    URL url = new URL(mUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setInstanceFollowRedirects(true);
                    urlConnection.connect();
                    int contentLength = urlConnection.getContentLength();

                    Log.d(TAG, "Start downloading " + urlConnection.getURL());
                    Log.d(TAG, String.format("File size %.2f kb", (float) contentLength / 1024));

                    String fileName = getFileName(urlConnection);
                    File apkFile = new File(fileDir, fileName);
                    if (!apkFile.exists()) {
                        apkFile.createNewFile();
                    } else {
                        Boolean hasDownloadApkFile = isApkDownloaded(apkFile);
                        if (hasDownloadApkFile) {
                            mDownloadInfo.apkFile = apkFile;
                            mDownloadInfo.progress = 1F;
                            urlConnection.disconnect();
                            subscriber.onNext(mDownloadInfo);
                            subscriber.onCompleted();
                            return;
                        }
                    }
                    mDownloadInfo.apkFile = apkFile;
                    outputStream = new BufferedOutputStream(
                            new FileOutputStream(mDownloadInfo.apkFile));
                    Log.d(TAG, "Downloading apk into " + mDownloadInfo.apkFile);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int length;
                    int totalLength = 0;
                    InputStream in = urlConnection.getInputStream();
                    while ((length = in.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                        totalLength += length;
                        mDownloadInfo.progress =
                                (totalLength == 0f) ? 0f : (float) totalLength / (float) contentLength;
                        subscriber.onNext(mDownloadInfo);
                    }
                } catch (Exception e) {
                    Log.e(TAG, String.format("Download: %s, %s", mDownloadInfo.apkFile, mUrl), e);
                    subscriber.onError(e);
                } finally {
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            subscriber.onError(e);
                        }
                    }
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
                subscriber.onCompleted();
            }
        });
    }

    @NonNull
    private Boolean isApkDownloaded(File apkFile) throws NameNotFoundException {
        PackageManager pm = FlightApplication.getInstance().getPackageManager();
        PackageInfo packInfo = pm.getPackageArchiveInfo(apkFile.getPath(),
                PackageManager.GET_ACTIVITIES);

        String currentVersionName;
        if (packInfo.installLocation != -1) {
            currentVersionName = FlightApplication.getInstance()
                    .getPackageManager()
                    .getPackageInfo(packInfo.packageName, 0)
                    .versionName;
        } else {
            currentVersionName = "";
        }
        String version = packInfo.versionName;
        return !currentVersionName.equals(version);
    }

    private String getFileName(HttpURLConnection urlConnection) {
        Uri uri = Uri.parse(urlConnection.getURL().toString());
        // fir.im url: http://pkg.fir.im/27c81a3398038551ab67aa9335a4418f009c0655.apk
        // ?attname=bailu-2.6.6-16032001-160320142728-release.apk_2.6.6.apk
        // &e=1458475559&token=LOvmia8oXF4xnLh0IdH05XMYpH6ENHNpARlmPc-T:6N-VyCkN2bcr68ykQzhnDj3OkUE=
        String path = uri.toString();
        String apkUrl = path.substring(0, path.indexOf("?"));
        String fileName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1);
        if (TextUtils.isEmpty(fileName)) {
            // attachment; filename="bailu-2.6.6-16032001-160320142728-release.apk_2.6.6.apk"
            String attachment = urlConnection.getHeaderField("Content-Disposition");
            if (attachment != null) {
                String delimiter = "filename=\"";
                int index = attachment.indexOf(delimiter);
                if (index != -1) {
                    fileName = attachment
                            .substring(index + delimiter.length(), attachment.length() - 1);
                }
                if (TextUtils.isEmpty(fileName)) {
                    fileName = uri.getLastPathSegment();
                }
            }
        }
        return fileName;
    }

    /* package */ DownloadInfo getDownloadInfo() {
        return mDownloadInfo;
    }

    static class DownloadInfo {

        float progress;
        File apkFile;
    }
}
