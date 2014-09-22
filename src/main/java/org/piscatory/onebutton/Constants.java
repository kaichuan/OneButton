package org.piscatory.onebutton;

/**
 * Created by kc on 9/11/14.
 *
 */
public final class Constants {
    public static final String UAC = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36";
    public static final String UAA = "Dalvik/1.6.0 (Linux; U; Android 4.4.4; Nexus 5 Build/KTU84P)";
    public static final String UAI = "MTV-EMAs/6.0 (iPad; iOS 7.1.2; Scale/2.00)";
    public static final String UAIE = "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)";

    public static final String URLC = "http://funnel.mtvnservices.com/api/v2/mtvema.com/collections/ema2014-vote/entries";
    public static final String URLA = "http://funnel.mtvnservices.com/api/v2/mtvema.com/collections/ema2014-vote-app-android/entries";
    public static final String URLI = "http://funnel.mtvnservices.com/api/v2/mtvema.com/collections/ema2014-vote-app/entries";

    public static final String VOTE_RESULT_ACTION = "org.piscatory.onebutton.vote.success.BROADCAST";
    public static final String VOTE_FAILED_ACTION = "org.piscatory.onebutton.vote.failed.BROADCAST";
    public static final String VOTE_STATUS_UPDATE_ACTION = "org.piscatory.onebutton.vote.status.BROADCAST";
    public static final String VOTE_STATUS_KEY = "org.piscatory.onebutton.vote.status.timestamp.KEY";
    public static final String VOTE_COUNT_KEY = "org.piscatory.onebutton.vote.count.KEY";
    public static final String NETWORK_STATUS_UPDATE_ACTION = "org.piscatory.onebutton.network.status.ACTION";
    public static final String NETWORK_STATUS_KEY = "org.piscatory.onebutton.network.status.KEY";
    public static final String SERVER_RESPONSE_KEY = "org.piscatory.onebutton.vote.server.response.KEY";
    public static final String NETWORK_LOCAL_IP_KEY = "org.piscatory.onebutton.network.localip.KEY";
    public static final String NETWORK_IP_KEY = "org.piscatory.onebutton.network.ip.KEY";
    public static final String VOTE_COUNT_TOTAL_KEY = "org.piscatory.onebutton.vote.count.total.KEY";
    public static final String VOTE_TIME_CHANGE_ACTION = "org.piscatory.onebutton.vote.invalid.BROADCAST";
    public static final String NETWORK_SLEEP_TIME_KEY = "org.piscatory.onebutton.network.sleeptime.KEY";
    public static final String VOTE_SUCCESS_TOTAL_KEY = "org.piscatory.onebutton.vote.success.total.KEY";
    public static final String VOTE_SUCCESS_KEY = "org.piscatory.onebutton.vote.success.KEY";

    public static final String VOTE_SUCCESS_RATE = "org.piscatory.onebutton.vote.success.rate.KEY";
    public static final String VOTE_SUCCESS_SPEED = "org.piscatory.onebutton.vote.success.speed.KEY";
}
