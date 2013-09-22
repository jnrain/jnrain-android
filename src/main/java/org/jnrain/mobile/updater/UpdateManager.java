/*
 * Copyright 2013 JNRain
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.jnrain.mobile.updater;

import java.text.MessageFormat;

import org.jnrain.mobile.R;
import org.jnrain.mobile.config.ConfigHub;
import org.jnrain.mobile.config.UpdaterConfigUtil;
import org.jnrain.mobile.network.listeners.NotifyingCheckUpdateRequestListener;
import org.jnrain.mobile.network.requests.CheckUpdateRequest;
import org.jnrain.mobile.util.JNRainAccountAuthenticatorActivity;
import org.jnrain.mobile.util.JNRainActivity;
import org.jnrain.mobile.util.JNRainFragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;


public class UpdateManager {
    /**
     * Filename of update info cache. 升级信息缓存的文件名.
     */
    public static final String UPDATE_INFO_CACHE_FILENAME = "updates.json";

    /**
     * Base URL for update packages. 升级包的 URL 基准.
     */
    public static final String UPDATE_URL_BASE = "http://dl.jnrain.com/android/";

    /**
     * Format string for APK filename. APK 文件名的格式.
     */
    public static final String UPDATE_APK_NAME = "jnrain-android-{0}.apk";

    /**
     * Returns the canonical filename of this app's official download.
     * 返回本应用官方下载使用的标准文件名.
     * 
     * @param version
     *            app version requested
     * @return a String of canonical apk filename containing version
     */
    public static String getApkNameForVersion(String version) {
        return MessageFormat.format(UPDATE_APK_NAME, version);
    }

    /**
     * Returns the canonical filename of this app's official download.
     * 返回本应用官方下载使用的标准文件名.
     * 
     * @param version
     *            VersionInfo object specifying requested version
     * @return a String of canonical apk filename containing version
     */
    public static String getApkNameForVersion(VersionInfo version) {
        return getApkNameForVersion(version.getName());
    }

    /**
     * Format a URL that points to a specific version of this app.
     * 格式化一个指向本应用某特定版本的 URL.
     * 
     * @param version
     *            VersionInfo object specifying requested version
     * @return URL for the .apk file
     */
    public static String getApkURLForVersion(String version) {
        return UPDATE_URL_BASE + getApkNameForVersion(version);
    }

    /**
     * Format a URL that points to a specific version of this app.
     * 格式化一个指向本应用某特定版本的 URL.
     * 
     * @param version
     *            app version requested
     * @return URL for the .apk file
     */
    public static String getApkURLForVersion(VersionInfo version) {
        return UPDATE_URL_BASE + getApkNameForVersion(version);
    }

    /**
     * Open in the browser the .apk file for the specified app version.
     * 在浏览器中打开指定版本的本应用 .apk 文件.
     * 
     * @param ctx
     *            Android context to execute in
     * @param version
     *            app version requested
     */
    public static void openApkInBrowser(Context ctx, String version) {
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getApkURLForVersion(version)));
        ctx.startActivity(intent);
    }

    /**
     * Open in the browser the .apk file for the specified app version.
     * 在浏览器中打开指定版本的本应用 .apk 文件.
     * 
     * @param ctx
     *            Android context to execute in
     * @param version
     *            VersionInfo object specifying requested version
     */
    public static void openApkInBrowser(Context ctx, VersionInfo version) {
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getApkURLForVersion(version)));
        ctx.startActivity(intent);
    }

    /**
     * Called to see if auto check of updates should be performed at the
     * moment.
     * 
     * 检查此时此刻是否应该自动检查更新.
     * 
     * @param ctx
     *            Android context to execute in
     * @return true, if current time minus last check time is greater than
     *         specified interval
     */
    public static boolean shouldAutoCheck(Context ctx) {
        UpdaterConfigUtil updUtil = ConfigHub.getUpdaterUtil(ctx);

        if (!updUtil.isAutoCheckEnabled()) {
            return false;
        }

        long now = System.currentTimeMillis();
        long lastCheck = updUtil.getLastCheckTime().getTime();
        long interval = updUtil.getCheckFreq() * 86400000;

        return now - lastCheck >= interval;
    }

    /**
     * Issue network request for auto checking updates.
     * 
     * 发送自动检查更新的网络请求.
     * 
     * @param activity
     *            caller activity for invoking RoboSpice machinery as
     */
    @SuppressWarnings({
            "rawtypes",
            "unchecked"
    })
    public static void issueAutoCheckRequest(JNRainActivity activity) {
        activity.makeSpiceRequest(
                new CheckUpdateRequest(),
                new NotifyingCheckUpdateRequestListener(activity));
    }

    /**
     * Issue network request for auto checking updates.
     * 
     * 发送自动检查更新的网络请求.
     * 
     * @param activity
     *            caller activity for invoking RoboSpice machinery as
     */
    @SuppressWarnings({
            "rawtypes",
            "unchecked"
    })
    public static void issueAutoCheckRequest(JNRainFragmentActivity activity) {
        activity.makeSpiceRequest(
                new CheckUpdateRequest(),
                new NotifyingCheckUpdateRequestListener(activity));
    }

    /**
     * Issue network request for auto checking updates.
     * 
     * 发送自动检查更新的网络请求.
     * 
     * @param activity
     *            caller activity for invoking RoboSpice machinery as
     */
    @SuppressWarnings({
            "rawtypes",
            "unchecked"
    })
    public static void issueAutoCheckRequest(
            JNRainAccountAuthenticatorActivity activity) {
        activity.makeSpiceRequest(
                new CheckUpdateRequest(),
                new NotifyingCheckUpdateRequestListener(activity));
    }

    /**
     * Show a dialog notifying the user of an available update.
     * 
     * 显示提醒用户更新可用的对话框.
     * 
     * @param ctx
     *            Android context to execute in
     * @param version
     *            VersionInfo of the version presented to user
     */
    public static void showUpdateNotifyDialog(
            final Context ctx,
            final VersionInfo version) {
        String dialogTitle = MessageFormat.format(
                ctx.getString(R.string.new_version_available),
                version.getName());
        String versionName;
        String desc;
        CharSequence dialogMsg;

        // version name, different if version has a codename
        if (version.getCodeName().length() > 0) {
            // has codename
            versionName = MessageFormat.format(
                    ctx.getString(R.string.new_version_number_w_codename),
                    version.getName(),
                    version.getCodeName());
        } else {
            // without codename
            versionName = MessageFormat.format(
                    ctx.getString(R.string.new_version_number),
                    version.getName());
        }

        // description
        desc = version.getDesc();
        if (desc.length() == 0) {
            desc = ctx.getString(R.string.new_version_desc_empty);
        }

        // dialog message body
        dialogMsg = Html.fromHtml(MessageFormat.format(
                ctx.getString(R.string.new_version_message),
                versionName,
                desc));

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogMsg);
        builder.setPositiveButton(
                R.string.new_version_ok_btn,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UpdateManager.openApkInBrowser(ctx, version);
                    }
                });
        builder.setNegativeButton(R.string.new_version_cancel_btn, null);

        builder.show();
    }
}
