package com.ruifenglb.www;

public class ApiConfig {

    public static final String HTTP_URL="http://47.109.16.150:777/apk/getapp.txt"; //动态域名，推荐七牛储存免费10G基本够用

    public static  String MOGAI_BASE_URL = "https://jzsjw.top";//可以是http也可以是https结尾不能带/


    public static final String getStart = "/ikumiao_api.php/v1.main/startup";
    public static final String getTypeList = "/ikumiao_api.php/v1.vod/types";
    public static final String getBannerList = "/ikumiao_api.php/v1.vod";

    //专题
    public static final String getTopicList = "/ikumiao_api.php/v1.topic/topicList";
    //专题详情
    public static final String getTopicDetail = "/ikumiao_api.php/v1.topic/topicDetail";
    //游戏
    public static final String getGameList = "/ikumiao_api.php/v1.youxi/index";
    //添加视频播放记录
    public static final String addPlayLog = "/ikumiao_api.php/v1.name/addViewLog";
    //上报观影时长
    public static final String watchTimeLong = "/ikumiao_api.php/v1.name/viewSeconds";
    //获取视频播放记录
    public static final String getPlayLogList = "/ikumiao_api.php/v1.name/viewLog";
    //获取视频播放进度
    public static final String getVideoProgress = "/ikumiao_api.php/v1.vod/videoProgress";
    //删除播放记录
    public static final String dleltePlayLogList = "/ikumiao_api.php/v1.name/delVlog";
    //直播列表
    public static final String getLiveList = "/ikumiao_api.php/v1.zhibo";
    //直播详情
    public static final String getLiveDetail = "/ikumiao_api.php/v1.zhibo/detail";
    //播放器开始播放广告
	    public static final String getLbconfig = "/ikumiao_api.php/v1.Vodad";  //新版本开始使用这个接口
    //public static final String getLbconfig = "/application/api/controller/v1/mogai_ad.php";  //老版本使用这个接口

    //找回密码短信发送
    public static final String findpasssms = "/ikumiao_api.php/v1.auth/findpasssms";
    //找回密码
    public static final String findpass = "/ikumiao_api.php/v1.auth/findpass";



    public static final String getTopList = "/ikumiao_api.php/v1.vod";
    public static final String getCardList = "/ikumiao_api.php/v1.main/category";
    public static final String getRecommendList = "/ikumiao_api.php/v1.vod/vodPhbAll";
    public static final String getCardListByType = "/ikumiao_api.php/v1.vod/type";
    public static final String getVodList = "/ikumiao_api.php/v1.vod";
    public static final String getVod = "/ikumiao_api.php/v1.vod/detail";

    public static final String COMMENT = "/ikumiao_api.php/v1.comment";

    public static final String USER_INFO = "/ikumiao_api.php/v1.name/detailUVE3MjQyNDg0";
    public static final String LOGIN = "/ikumiao_api.php/v1.auth/login";
    public static final String LOGOUT = "/ikumiao_api.php/v1.auth/logout";
    public static final String REGISTER = "/ikumiao_api.php/v1.auth/register";
    public static final String VERIFY_CODE = "/ikumiao_api.php/v1.auth/registerSms";//发送注册验证码
    public static final String VERIFY_CODE_FIND = "/ikumiao_api.php/v1.auth/findpasssms";//发送找回密码验证码
    public static final String OPEN_REGISTER = "/ikumiao_api.php/v1.name/phoneReg";
    public static final String SIGN = "/ikumiao_api.php/v1.sign";
    public static final String GROUP_CHAT = "/ikumiao_api.php/v1.groupchat";
    public static final String CARD_BUY = "/ikumiao_api.php/v1.name/buy";
    public static final String UPGRADE_GROUP = "/ikumiao_api.php/v1.name/group";
    public static final String SCORE_LIST = "/ikumiao_api.php/v1.name/groups";
    public static final String CHANGE_AGENTS = "/ikumiao_api.php/v1.name/changeAgents";
    public static final String AGENTS_SCORE = "/ikumiao_api.php/v1.name/agentsScore";
    public static final String POINT_PURCHASE = "/ikumiao_api.php/v1.name/order";
    public static final String CHANGE_NICKNAME = "/ikumiao_api.php/v1.name";
    public static final String CHANGE_AVATOR = "/ikumiao_api.php/v1.upload/user";
    public static final String GOLD_WITHDRAW = "/ikumiao_api.php/v1.name/goldWithdrawApply";
    public static final String PAY_TIP = "/ikumiao_api.php/v1.name/payTip";
    public static final String GOLD_TIP = "/ikumiao_api.php/v1.name/goldTip";
    public static final String FEEDBACK = "/ikumiao_api.php/v1.gbook";
    public static final String COLLECTION_LIST = "/ikumiao_api.php/v1.name/favs";
    public static final String COLLECTION = "/ikumiao_api.php/v1.name/ulog";
    public static final String SHARE_SCORE = "/ikumiao_api.php/v1.name/shareScore";
    public static final String TASK_LIST = "/ikumiao_api.php/v1.name/task";
    public static final String MSG_LIST = "/ikumiao_api.php/v1.message/index";
    public static final String MSG_DETAIL = "/ikumiao_api.php/v1.message/detail";
    public static final String EXPAND_CENTER = "/ikumiao_api.php/v1.name/userLevelConfig";
    public static final String MY_EXPAND = "/ikumiao_api.php/v1.name/subUsers";
    public static final String SEND_DANMU = "/ikumiao_api.php/v1.danmu";
    public static final String SCORE = "/ikumiao_api.php/v1.vod/score";
    public static final String CHECK_VOD_TRYSEE = "/ikumiao_api.php/v1.name/checkVodTrySee";
    public static final String BUY_VIDEO = "/ikumiao_api.php/v1.name/buypopedom";
    public static final String CHECK_VERSION = "/ikumiao_api.php/v1.main/version";
    public static final String PAY = "/ikumiao_api.php/v1.name/pay";
    public static final String ORDER = "/ikumiao_api.php/v1.name/order";
    public static final String APP_CONFIG = "/ikumiao_api.php/v1.name/appConfig";
    public static final String SHARE_INFO = "/ikumiao_api.php/v1.name/shareInfo";
    public static final String video_count = "/ikumiao_api.php/v1.vod/videoViewRecode";
    public static final String tabFourInfo = "/ikumiao_api.php/v1.youxi/index";
    public static final String tabThreeName = "/ikumiao_api.php/v1.zhibo/thirdUiName";
    public static final String getRankList = "/ikumiao_api.php/v1.vod/vodphb";
    public static final String ADD_GROUP = "/ikumiao_api.php/v1.name/addGroup";
		public static final String getDanMuList = "/ikumiao_api.php/v1.danmu/index";//弹幕列表

    public static void main() {

    }

}
