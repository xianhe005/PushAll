package com.dongxl.pushdeme;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.dongxl.pushdeme.huawei.HuaweiPushRegister;
import com.dongxl.pushdeme.huawei.HMSSharedUtils;
import com.dongxl.pushdeme.oppo.OppoPushCallback;
import com.dongxl.pushdeme.utils.LogUtils;
import com.dongxl.pushdeme.utils.PhoneUtils;
import com.dongxl.pushdeme.utils.RomUtil;
import com.dongxl.pushdeme.vivo.VivoPushOperation;
import com.heytap.msp.push.HeytapPushManager;
import com.huawei.hms.aaid.HmsInstanceId;
import com.meizu.cloud.pushsdk.util.MzSystemUtils;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.vivo.push.util.NotifyAdapterUtil;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * 推送相关注册和操作
 * 小米: 所有设置只支持单一设置 不支持一次设置多个
 * 华为: 没有设置功能
 * 魅族:      其他是单一设置 tags 标签名称，多个逗号隔离，每个标签不能超过 20 个字符，限 100 个
 * oppo:  设置都支持多个，但方法都已经标签过时了
 * vivo: 所有设置只支持单一设置 不支持一次设置多个
 */
public class PushRegisterSet {
    private final static String TAG = PushRegisterSet.class.getSimpleName();

    /**
     * oppo 设置操作相关的回调
     */
    private static OppoPushCallback oppoPushCallback;

    private static void initOppoPushCallback(Context context) {
        if (null == oppoPushCallback) {
            oppoPushCallback = new OppoPushCallback(context.getApplicationContext());
        }
    }

    /**
     * application 初始化注册
     *
     * @param application
     */
    public static void applicationInit(Application application) {
        if (!PhoneUtils.isChinaCountry(application)) {
            return;
        }
        String platform = getSupportPushPlatform(application);
        LogUtils.i(PushConstants.XIAOMI_TAG, "==push applicationInit==platform:" + platform);
        switch (platform) {
            case PushConstants.PushPlatform.PLATFORM_XIAOMI:
                //不需要Application初始化
                break;
            case PushConstants.PushPlatform.PLATFORM_HUAWEI:
                huaweiRegisterInit(application);
                break;
            case PushConstants.PushPlatform.PLATFORM_OPPO:
                //不需要Application初始化
                break;
            case PushConstants.PushPlatform.PLATFORM_VIVO:
                //不需要Application初始化
                break;
            case PushConstants.PushPlatform.PLATFORM_FLYME:
                //不需要Application初始化
                break;
            case PushConstants.PushPlatform.PLATFORM_JPSUH:
                //极光 不需要Application初始化
                break;
            default:
                break;
        }
    }

    /**
     * activity初始化注册
     *
     * @param activity
     */
    public static void registerInitPush(Activity activity) {
        if (!PhoneUtils.isChinaCountry(activity)) {
            return;
        }
        switch (getSupportPushPlatform(activity)) {
            case PushConstants.PushPlatform.PLATFORM_XIAOMI:
                xiaomiRegisterInit(activity);
                break;
            case PushConstants.PushPlatform.PLATFORM_HUAWEI:
                huaweiRegisterConnect(activity);
                break;
            case PushConstants.PushPlatform.PLATFORM_OPPO:
                oppoRegisterInit(activity);
                break;
            case PushConstants.PushPlatform.PLATFORM_VIVO:
                vivoRegisterInit(activity);
                break;
            case PushConstants.PushPlatform.PLATFORM_FLYME:
                meizuRegisterInit(activity);
                break;
            case PushConstants.PushPlatform.PLATFORM_JPSUH:
                //极光
                jpushRegisterInit(activity);
                break;
            default:
                break;
        }
    }

    /**
     * 小米注册
     *
     * @param context
     */
    public static void xiaomiRegisterInit(Context context) {
        MiPushClient.registerPush(context, PushConstants.XIAOMI_APP_ID, PushConstants.XIAOMI_APP_KEY);
        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
//                LogUtils.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                LogUtils.d(PushConstants.XIAOMI_TAG, content);
            }
        };
        //SDCard/Android/data/app pkgname/files/MiPushLog
        Logger.setLogger(context, newLogger);
        if (!BuildConfig.DEBUG) {
            //关闭日志功能
            Logger.disablePushFileLog(context);
        }
    }

    /**
     * 华为注册
     * SDK连接HMS
     *
     * @param application
     */
    private static void huaweiRegisterInit(Application application) {
//        HMSAgentLog.setHMSAgentLogCallback(new HMSAgentLog.IHMSAgentLogCallback() {
//            @Override
//            public void logD(String tag, String log) {
//                LogUtils.d(TAG, log);
//            }
//
//            @Override
//            public void logV(String tag, String log) {
//                LogUtils.v(TAG, log);
//            }
//
//            @Override
//            public void logI(String tag, String log) {
//                LogUtils.i(TAG, log);
//            }
//
//            @Override
//            public void logW(String tag, String log) {
//                LogUtils.e(TAG, log);
//            }
//
//            @Override
//            public void logE(String tag, String log) {
//                LogUtils.e(TAG, log);
//            }
//        });
//        HMSAgent.init(application);
        HmsInstanceId inst = HmsInstanceId.getInstance(application);
    }

    /**
     * 华为注册
     * SDK连接HMS
     *
     * @param activity
     */
    private static void huaweiRegisterConnect(final Activity activity) {
        final Context context = activity.getApplicationContext();
//        HMSAgent.connect(activity, new ConnectHandler() {
//            @Override
//            public void onConnect(int rst) {
//                LogUtils.i(TAG, "HMS connect end:" + rst);
//                getHuaweiPushToken();
//            }
//        });
        getHuaweiPushToken(context);
    }

    /**
     * 获取华为的push token
     */
    private static void getHuaweiPushToken(final Context context) {
//        HMSAgent.Push.getToken(new GetTokenHandler() {
//            @Override
//            public void onResult(int rst) {
//                LogUtils.i(TAG, "get token: end" + rst);
//            }
//        });
        HuaweiPushRegister.getHuaweiPushToken(context);
    }

    /**
     * vivo 注册
     *
     * @param context
     */
    private static void vivoRegisterInit(Context context) {
        PushClient.getInstance(context.getApplicationContext()).initialize();
        PushClient.getInstance(context.getApplicationContext()).turnOnPush(new IPushActionListener() {
            @Override
            public void onStateChanged(int state) {
                LogUtils.i(TAG, "vivo 打开push getRegId: end state：" + state + " isSuc:" + (state == 0));
            }
        });
    }

    /**
     * oppo 注册
     *
     * @param context
     */
    private static void oppoRegisterInit(Context context) {
        initOppoPushCallback(context);
        try {
            HeytapPushManager.init(context.getApplicationContext(), true);
            HeytapPushManager.register(context.getApplicationContext(), PushConstants.OPPO_APP_KEY, PushConstants.OPPO_APP_SECRET, oppoPushCallback);//setPushCallback接口也可设置callback
            HeytapPushManager.requestNotificationPermission();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        getRegId(context);
    }

    /**
     * 魅族注册
     *
     * @param context
     */
    private static void meizuRegisterInit(Context context) {
        com.meizu.cloud.pushsdk.PushManager.register(context, PushConstants.MEIZU_APP_ID, PushConstants.MEIZU_APP_KEY);
    }

    /**
     * 极光推送注册
     *
     * @param context
     */
    private static void jpushRegisterInit(Context context) {
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(context);
    }

    /**
     * 获取 regId
     *
     * @return
     */
    public static String getRegId(Context context) {
        if (!PhoneUtils.isChinaCountry(context)) {
            return "";
        }
        String regId = "";
        switch (getSupportPushPlatform(context)) {
            case PushConstants.PushPlatform.PLATFORM_XIAOMI:
                regId = MiPushClient.getRegId(context);
                break;
            case PushConstants.PushPlatform.PLATFORM_HUAWEI:
                regId = HMSSharedUtils.getHuaweiToken(context);
                break;
            case PushConstants.PushPlatform.PLATFORM_OPPO:
                regId = HeytapPushManager.getRegisterID();
                break;
            case PushConstants.PushPlatform.PLATFORM_VIVO:
                regId = PushClient.getInstance(context.getApplicationContext()).getRegId();
                break;
            case PushConstants.PushPlatform.PLATFORM_FLYME:
                regId = com.meizu.cloud.pushsdk.PushManager.getPushId(context);
                break;
            case PushConstants.PushPlatform.PLATFORM_JPSUH:
                //极光
                regId = JPushInterface.getRegistrationID(context.getApplicationContext());
                break;
            default:
                break;
        }
        return regId;
    }

    /**
     * 设置别名
     *
     * @param context
     * @param alias
     */
    public static void setAlias(Context context, String alias) {
        if (!PhoneUtils.isChinaCountry(context)) {
            return;
        }
        switch (getSupportPushPlatform(context)) {
            case PushConstants.PushPlatform.PLATFORM_XIAOMI:
                MiPushClient.setAlias(context, alias, null);
                break;
            case PushConstants.PushPlatform.PLATFORM_HUAWEI:
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_OPPO:
//                initOppoPushCallback(context);
//                List<String> list = new ArrayList<>();
//                list.add(alias);
//                HeytapPushManager.setAliases(list);
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_VIVO:
                //unBindAlias 一天内最多调用 100 次，两次调用的间隔需大于 2s
                new VivoPushOperation().bindAlias(context, alias);
                break;
            case PushConstants.PushPlatform.PLATFORM_FLYME:
                com.meizu.cloud.pushsdk.PushManager.subScribeAlias(context, PushConstants.MEIZU_APP_ID, PushConstants.MEIZU_APP_KEY, com.meizu.cloud.pushsdk.PushManager.getPushId(context), alias);
                break;
            case PushConstants.PushPlatform.PLATFORM_JPSUH:
                //极光
                JPushInterface.setAlias(context, 1, alias);//也可以同时设置对个，具体看官网
                break;
            default:
                break;
        }
    }

    /**
     * 撤销别名
     *
     * @param context
     * @param alias
     */
    public static void unsetAlias(Context context, String alias) {
        if (!PhoneUtils.isChinaCountry(context)) {
            return;
        }
        switch (getSupportPushPlatform(context)) {
            case PushConstants.PushPlatform.PLATFORM_XIAOMI:
                MiPushClient.unsetAlias(context, alias, null);
                break;
            case PushConstants.PushPlatform.PLATFORM_HUAWEI:
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_OPPO:
//                initOppoPushCallback(context);
//                HeytapPushManager.unsetAlias(alias);
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_VIVO:
                //bindAlias 一天内最多调用 100 次，两次调用的间隔需大于 2s
                new VivoPushOperation().unBindAlias(context, alias);
                break;
            case PushConstants.PushPlatform.PLATFORM_FLYME:
                com.meizu.cloud.pushsdk.PushManager.unSubScribeAlias(context, PushConstants.MEIZU_APP_ID, PushConstants.MEIZU_APP_KEY, com.meizu.cloud.pushsdk.PushManager.getPushId(context), alias);
                break;
            case PushConstants.PushPlatform.PLATFORM_JPSUH:
                //极光
                JPushInterface.deleteAlias(context, 2);//也可以同时设置对个，具体看官网
                break;
            default:
                break;
        }
    }

    /**
     * 设置账号
     *
     * @param context
     * @param account
     */
    public static void setUserAccount(Context context, String account) {
        if (!PhoneUtils.isChinaCountry(context)) {
            return;
        }
        switch (getSupportPushPlatform(context)) {
            case PushConstants.PushPlatform.PLATFORM_XIAOMI:
                MiPushClient.setUserAccount(context, account, null);
                break;
            case PushConstants.PushPlatform.PLATFORM_HUAWEI:
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_OPPO:
//                initOppoPushCallback(context);
//                HeytapPushManager.setUserAccount(account);
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_VIVO:
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_FLYME:
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_JPSUH:
                //极光 不支持
                break;
            default:
                break;
        }
    }

    /**
     * 撤销账号
     *
     * @param context
     * @param account
     */
    public static void unsetUserAccount(Context context, String account) {
        if (!PhoneUtils.isChinaCountry(context)) {
            return;
        }
        switch (getSupportPushPlatform(context)) {
            case PushConstants.PushPlatform.PLATFORM_XIAOMI:
                MiPushClient.unsetUserAccount(context, account, null);
                break;
            case PushConstants.PushPlatform.PLATFORM_HUAWEI:
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_OPPO:
//                initOppoPushCallback(context);
//                List<String> list = new ArrayList<>();
//                list.add(account);
//                HeytapPushManager.unsetUserAccounts(list);
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_VIVO:
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_FLYME:
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_JPSUH:
                //极光 不支持
                break;
            default:
                break;
        }
    }

    /**
     * 设置标签
     *
     * @param context
     * @param topic   topic和topics必有一个为空 优先用topic
     * @param topics
     * @return 是否支持设置，true 支持
     */
    public static boolean setTopic(final Context context, final String topic/*, final List<String> topics*/) {
        if (!PhoneUtils.isChinaCountry(context)) {
            return false;
        }
        boolean isSupport = false;
        switch (getSupportPushPlatform(context)) {
            case PushConstants.PushPlatform.PLATFORM_XIAOMI:
                MiPushClient.subscribe(context, topic, null);
                isSupport = true;
                break;
            case PushConstants.PushPlatform.PLATFORM_HUAWEI:
                HuaweiPushRegister.subscribe(context, topic);
                isSupport = true;
                break;
            case PushConstants.PushPlatform.PLATFORM_OPPO:
//                initOppoPushCallback(context);
//                HeytapPushManager.setTags(getTopicKeys(topic/*, topics*/));
                isSupport = false;
                break;
            case PushConstants.PushPlatform.PLATFORM_VIVO:
                //delTopic 一天内最多调用 500 次，两次调用的间隔需大于 2s
                new VivoPushOperation().setTopic(context, topic);
                isSupport = true;
                break;
            case PushConstants.PushPlatform.PLATFORM_FLYME:
                String tags = !TextUtils.isEmpty(topic) ? topic : listToString(/*topics*/);
                com.meizu.cloud.pushsdk.PushManager.subScribeTags(context, PushConstants.MEIZU_APP_ID, PushConstants.MEIZU_APP_KEY, com.meizu.cloud.pushsdk.PushManager.getPushId(context), tags);
                isSupport = true;
                break;
            case PushConstants.PushPlatform.PLATFORM_JPSUH:
                //极光
                Set<String> jTags = new LinkedHashSet<>();
                jTags.add(topic);
//                JPushInterface.setTags(context, 4, jTags);
                JPushInterface.addTags(context, 3, jTags); //可以同时设置多个
                isSupport = true;
                break;
            default:
                break;
        }
        return isSupport;
    }

    private static String listToString(/*List<String> list*/) {
        /*int size = null == list ? 0 : list.size();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(list.get(i));
            if (i != size - 1) {
                sb.append(",");
            }
        }
        return sb.toString();*/
        return "";
    }

    /**
     * 获取添加的topic集合
     *
     * @param topic  topic和topics必有一个为空 优先用topic
     * @param topics
     * @return
     */
    private static Map<String, String> getTopicMap(String topic/*, List<String> topics*/) {
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(topic)) {
            map.put(topic, topic);
        }
//        else if (null != topics) {
//            for (int i = 0; i < topics.size(); i++) {
//                String value = topics.get(i);
//                map.put(value, value);
//            }
//        }
        return map;
    }

    /**
     * 撤销标签
     *
     * @param context
     * @param topic   topic和topics必有一个为空 优先用topic
     * @param topics
     * @return true 支持取消设置
     */
    public static boolean unsetTopic(final Context context, final String topic/*, final List<String> topics*/) {
        if (!PhoneUtils.isChinaCountry(context)) {
            return false;
        }
        boolean isSupport = false;
        switch (getSupportPushPlatform(context)) {
            case PushConstants.PushPlatform.PLATFORM_XIAOMI:
                MiPushClient.unsubscribe(context, topic, null);
                isSupport = true;
                break;
            case PushConstants.PushPlatform.PLATFORM_HUAWEI:
                HuaweiPushRegister.unsubscribe(context, topic);
                isSupport = true;
                break;
            case PushConstants.PushPlatform.PLATFORM_OPPO:
//                initOppoPushCallback(context);
//                HeytapPushManager.unsetTags(getTopicKeys(topic/*, topics*/));
                isSupport = false;
                break;
            case PushConstants.PushPlatform.PLATFORM_VIVO:
                //与setTopic 一天内最多调用 500 次，两次调用的间隔需大于 2s
                new VivoPushOperation().delTopic(context, topic);
                isSupport = true;
                break;
            case PushConstants.PushPlatform.PLATFORM_FLYME:
                String tags = !TextUtils.isEmpty(topic) ? topic : listToString(/*topics*/);
                com.meizu.cloud.pushsdk.PushManager.unSubScribeTags(context, PushConstants.MEIZU_APP_ID, PushConstants.MEIZU_APP_KEY, com.meizu.cloud.pushsdk.PushManager.getPushId(context), tags);
                isSupport = true;
                break;
            case PushConstants.PushPlatform.PLATFORM_JPSUH:
                //极光
                Set<String> jTags = new LinkedHashSet<>();
                jTags.add(topic);
                JPushInterface.deleteTags(context, 5, jTags);
//                JPushInterface.cleanTags(context, 6);
                isSupport = true;
                break;
            default:
                break;
        }
        return isSupport;
    }

    /**
     * 获取删除的topic key集合
     *
     * @param topic  topic和topics必有一个为空 优先用topic
     * @param topics
     * @return
     */
    private static List<String> getTopicKeys(String topic/*, List<String> topics*/) {
        List<String> keys = new ArrayList<>();
        if (!TextUtils.isEmpty(topic)) {
            keys.add(topic);
        }
        /*else if (null != topics) {
            keys.addAll(topics);
        }*/
        return keys;
    }

    /**
     * 设置接收时间
     *
     * @param context
     * @param startHour
     * @param startMin
     * @param endHour
     * @param endMin
     */
    public static void setAcceptTime(Context context, int startHour, int startMin, int endHour, int endMin) {
        if (!PhoneUtils.isChinaCountry(context)) {
            return;
        }
        switch (getSupportPushPlatform(context)) {
            case PushConstants.PushPlatform.PLATFORM_XIAOMI:
                MiPushClient.setAcceptTime(context, startHour, startMin, endHour, endMin, null);
                break;
            case PushConstants.PushPlatform.PLATFORM_HUAWEI:
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_OPPO:
                initOppoPushCallback(context);
                List<Integer> weekDays = new ArrayList<>();
                weekDays.add(0);//周日为0,周一为1,以此类推
                weekDays.add(1);
                weekDays.add(2);
                weekDays.add(3);
                weekDays.add(4);
                weekDays.add(5);
                weekDays.add(6);
                HeytapPushManager.setPushTime(weekDays, startHour, startMin, endHour, endMin);
                break;
            case PushConstants.PushPlatform.PLATFORM_VIVO:
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_FLYME:
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_JPSUH:
                //极光
                Set<Integer> days = new HashSet<Integer>();
                days.add(1);//周日为0,周一为1,以此类推
                days.add(2);
                days.add(3);
                days.add(4);
                days.add(5);
                JPushInterface.setPushTime(context, days, startHour, endHour);
                break;
            default:
                break;
        }
    }

    /**
     * 检查推送是否打开
     *
     * @param context
     * @return 默认返回true
     */
    public static boolean checkTurnOnOrOffPush(Context context) {
        if (!PhoneUtils.isChinaCountry(context)) {
            return false;
        }
        boolean isOn = false;
        switch (getSupportPushPlatform(context)) {
            case PushConstants.PushPlatform.PLATFORM_XIAOMI:
                isOn = MiPushClient.shouldUseMIUIPush(context);
                break;
            case PushConstants.PushPlatform.PLATFORM_HUAWEI:
                isOn = HuaweiPushRegister.checkTurnOnOrOffHuaweiPush(context);
                break;
            case PushConstants.PushPlatform.PLATFORM_OPPO:
                HeytapPushManager.getPushStatus();
                isOn = true;
                break;
            case PushConstants.PushPlatform.PLATFORM_VIVO:
                isOn = true;
                break;
            case PushConstants.PushPlatform.PLATFORM_FLYME:
                com.meizu.cloud.pushsdk.PushManager.checkPush(context, PushConstants.MEIZU_APP_ID, PushConstants.MEIZU_APP_KEY, com.meizu.cloud.pushsdk.PushManager.getPushId(context));
                isOn = true;
                break;
            case PushConstants.PushPlatform.PLATFORM_JPSUH:
                isOn = !JPushInterface.isPushStopped(context);
                break;
            default:
                isOn = false;
                break;
        }
        return isOn;
    }

    /**
     * 打开或者关闭推送
     *
     * @param context
     * @param isOn    true 打开
     */
    public static void setTurnOnOrOffPush(Context context, boolean isOn) {
        if (!PhoneUtils.isChinaCountry(context)) {
            return;
        }
        switch (getSupportPushPlatform(context)) {
            case PushConstants.PushPlatform.PLATFORM_XIAOMI:
                if (isOn) {
                    MiPushClient.resumePush(context, null);
                } else {
                    MiPushClient.pausePush(context, null);
                }
                break;
            case PushConstants.PushPlatform.PLATFORM_HUAWEI:
                HuaweiPushRegister.turnOnOrOffHuaweiPush(context, isOn);
                break;
            case PushConstants.PushPlatform.PLATFORM_OPPO:
                if (isOn) {
                    HeytapPushManager.resumePush();
                } else {
                    HeytapPushManager.pausePush();
                }
                break;
            case PushConstants.PushPlatform.PLATFORM_VIVO:
                if (isOn) {
                    new VivoPushOperation().turnOnPush(context);
                } else {
                    new VivoPushOperation().turnOffPush(context);
                }
                break;
            case PushConstants.PushPlatform.PLATFORM_FLYME:
                com.meizu.cloud.pushsdk.PushManager.switchPush(context, PushConstants.MEIZU_APP_ID, PushConstants.MEIZU_APP_KEY, com.meizu.cloud.pushsdk.PushManager.getPushId(context), isOn);
                break;
            case PushConstants.PushPlatform.PLATFORM_JPSUH:
                if (isOn) {
                    JPushInterface.resumePush(context);
                } else {
                    JPushInterface.stopPush(context);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 清空推送通知根据id
     *
     * @param context
     * @param platform
     * @param notifyId
     */
    public static void clearPushNotification(Context context, String platform, int notifyId) {
        switch (platform) {
            case PushConstants.PushPlatform.PLATFORM_XIAOMI:
                MiPushClient.clearNotification(context, notifyId);
                break;
            case PushConstants.PushPlatform.PLATFORM_HUAWEI:
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_OPPO:
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_VIVO:
                NotifyAdapterUtil.setNotifyId(notifyId);
                NotifyAdapterUtil.cancelNotify(context);
                break;
            case PushConstants.PushPlatform.PLATFORM_FLYME:
                com.meizu.cloud.pushsdk.PushManager.clearNotification(context, notifyId);
                break;
            case PushConstants.PushPlatform.PLATFORM_JPSUH:
                JPushInterface.clearNotificationById(context, notifyId);
                break;
            default:
                break;
        }
    }

    /**
     * 清空所有推送通知
     *
     * @param context
     * @param platform
     */
    public static void clearAllPushNotification(Context context, String platform) {
        switch (platform) {
            case PushConstants.PushPlatform.PLATFORM_XIAOMI:
                MiPushClient.clearNotification(context);
                break;
            case PushConstants.PushPlatform.PLATFORM_HUAWEI:
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_OPPO:
                //不支持
                break;
            case PushConstants.PushPlatform.PLATFORM_VIVO:
                NotifyAdapterUtil.cancelNotify(context);
                break;
            case PushConstants.PushPlatform.PLATFORM_FLYME:
                com.meizu.cloud.pushsdk.PushManager.clearNotification(context);
                break;
            case PushConstants.PushPlatform.PLATFORM_JPSUH:
                JPushInterface.clearAllNotifications(context);
                break;
            default:
                break;
        }
    }

    /**
     * 获取当前设备支持的推送类型
     *
     * @param context
     * @return push platform
     */
    private static String getSupportPushPlatform(Context context) {
        Context mContext = context.getApplicationContext();
        if (!TextUtils.isEmpty(PushConstants.XIAOMI_APP_ID) && RomUtil.isMiui() && MiPushClient.shouldUseMIUIPush(context)) {
            return PushConstants.PushPlatform.PLATFORM_XIAOMI;
        } else if (!TextUtils.isEmpty(PushConstants.HUAWEI_APP_ID) && RomUtil.isEmui()) {
            return PushConstants.PushPlatform.PLATFORM_HUAWEI;
        } else if (!TextUtils.isEmpty(PushConstants.VIVOPUSH_APPID) && PushClient.getInstance(mContext).isSupport()) {
            return PushConstants.PushPlatform.PLATFORM_VIVO;
        } else if (!TextUtils.isEmpty(PushConstants.OPPO_APP_ID) && RomUtil.isOppo() && HeytapPushManager.isSupportPush()) {
            return PushConstants.PushPlatform.PLATFORM_OPPO;
        } else if (!TextUtils.isEmpty(PushConstants.MEIZU_APP_ID) && MzSystemUtils.isMeizu(mContext)) {
            return PushConstants.PushPlatform.PLATFORM_FLYME;
        } else {
            if (!TextUtils.isEmpty(PushConstants.JPUSH_APPKEY)) {
                return PushConstants.PushPlatform.PLATFORM_JPSUH;
            } else {
                return PushConstants.PushPlatform.PLATFORM_OTHER;
            }
        }
    }
}
