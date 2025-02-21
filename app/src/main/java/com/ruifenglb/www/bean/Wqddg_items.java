package com.ruifenglb.www.bean;

import java.io.Serializable;
import java.util.List;


public class Wqddg_items implements Serializable {
    /**
     * code : 200
     * msg : success
     * data : {"sign":{"status":"1","reward":{"points":"5"},"title":"签到","info":"点击签到即可获得积分"},"vod_mark":{"status":"0","reward":{"points":"3"},"reward_num":"","title":"评分","info":"为视频评分即可获得积分"},"comment":{"status":"1","reward":{"points":"5"},"reward_num":"","title":"评论","info":"为视频即可获得积分"},"danmu":{"status":"0","reward":{"points":"2"},"reward_num":"","title":"弹幕","info":"发送弹幕即可获得积分"},"document":{"un_register":{"status":"1","title":"未注册弹窗","content":"未注册弹窗","type":"un_register"},"registerd":{"status":"1","title":"首页弹窗标题","content":"首页弹窗内容","type":"registerd"},"notice":{"status":"0","title":null,"content":"完成每日任务可获得积分奖励","type":"notice"},"roll_notice":{"status":"0","title":null,"content":"如长时间无法播放请点击换来源，如遇卡顿请点击修复，如没有来源可换，请联系客服进行添加播放来源！","type":"roll_notice"},"home_notice":{"status":"0","title":null,"content":"","type":"home_notice"},"game_notice":{"status":"0","title":null,"content":"","type":"game_notice"}},"ads":{"user_center":{"id":52,"typename":"个人中心","status":1,"sort":0,"tag":"user_center","description":"http://img-lvdoui.test.upcdn.net/fx.png","create_time":1560965440,"update_time":1593330630},"searcher":{"id":53,"typename":"搜索广告位","status":1,"sort":0,"tag":"searcher","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965460,"update_time":1630214161},"player_pause":{"id":54,"typename":"播放器暂停广告","status":0,"sort":0,"tag":"player_pause","description":"","create_time":1560965485,"update_time":1572960349},"player_down":{"id":55,"typename":"播放器下方广告","status":1,"sort":0,"tag":"player_down","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965505,"update_time":1630214155},"variety":{"id":56,"typename":"综艺广告位","status":1,"sort":0,"tag":"variety","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965569,"update_time":1630214149},"cartoon":{"id":57,"typename":"动漫广告位","status":1,"sort":0,"tag":"cartoon","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965583,"update_time":1630214144},"sitcom":{"id":58,"typename":"剧集广告位","status":1,"sort":0,"tag":"sitcom","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965601,"update_time":1630214137},"vod":{"id":59,"typename":"电影广告位","status":1,"sort":0,"tag":"vod","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965614,"update_time":1630214131},"index":{"id":60,"typename":"首页广告位","status":1,"sort":0,"tag":"index","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965629,"update_time":1630214125},"startup_adv":{"id":61,"typename":"启动广告位","status":1,"sort":0,"tag":"startup_adv","description":"<a href=\"\" target=\"_blank\"><img src=\"http://ossd.cn-sh2.ufileos.com/img/20201024133709.gif\"  /><\/a>","create_time":1560965647,"update_time":1630214059},"service_qq":{"id":62,"typename":"QQ客服","status":1,"sort":0,"tag":"service_qq","description":"5928050934","create_time":1560965677,"update_time":1635467804},"startup_config":{"status":1}},"player":{"app_logo":null},"payments":[{"name":"支付宝","payment":"epay","status":1},{"payment":"qqepay","name":"微信支付","status":1}],"share_url":"http://v.lvdoui.cn","share_logo":null,"search_hot":["暖阳之下","突围","好好生活","为你千千万万遍","燃烧大地","一生一世","中餐厅 第五季","冰血长津湖","怒火·重案","当爱情遇上科学家"]}
     */
    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * sign : {"status":"1","reward":{"points":"5"},"title":"签到","info":"点击签到即可获得积分"}
         * vod_mark : {"status":"0","reward":{"points":"3"},"reward_num":"","title":"评分","info":"为视频评分即可获得积分"}
         * comment : {"status":"1","reward":{"points":"5"},"reward_num":"","title":"评论","info":"为视频即可获得积分"}
         * danmu : {"status":"0","reward":{"points":"2"},"reward_num":"","title":"弹幕","info":"发送弹幕即可获得积分"}
         * document : {"un_register":{"status":"1","title":"未注册弹窗","content":"未注册弹窗","type":"un_register"},"registerd":{"status":"1","title":"首页弹窗标题","content":"首页弹窗内容","type":"registerd"},"notice":{"status":"0","title":null,"content":"完成每日任务可获得积分奖励","type":"notice"},"roll_notice":{"status":"0","title":null,"content":"如长时间无法播放请点击换来源，如遇卡顿请点击修复，如没有来源可换，请联系客服进行添加播放来源！","type":"roll_notice"},"home_notice":{"status":"0","title":null,"content":"","type":"home_notice"},"game_notice":{"status":"0","title":null,"content":"","type":"game_notice"}}
         * ads : {"user_center":{"id":52,"typename":"个人中心","status":1,"sort":0,"tag":"user_center","description":"http://img-lvdoui.test.upcdn.net/fx.png","create_time":1560965440,"update_time":1593330630},"searcher":{"id":53,"typename":"搜索广告位","status":1,"sort":0,"tag":"searcher","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965460,"update_time":1630214161},"player_pause":{"id":54,"typename":"播放器暂停广告","status":0,"sort":0,"tag":"player_pause","description":"","create_time":1560965485,"update_time":1572960349},"player_down":{"id":55,"typename":"播放器下方广告","status":1,"sort":0,"tag":"player_down","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965505,"update_time":1630214155},"variety":{"id":56,"typename":"综艺广告位","status":1,"sort":0,"tag":"variety","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965569,"update_time":1630214149},"cartoon":{"id":57,"typename":"动漫广告位","status":1,"sort":0,"tag":"cartoon","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965583,"update_time":1630214144},"sitcom":{"id":58,"typename":"剧集广告位","status":1,"sort":0,"tag":"sitcom","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965601,"update_time":1630214137},"vod":{"id":59,"typename":"电影广告位","status":1,"sort":0,"tag":"vod","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965614,"update_time":1630214131},"index":{"id":60,"typename":"首页广告位","status":1,"sort":0,"tag":"index","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965629,"update_time":1630214125},"startup_adv":{"id":61,"typename":"启动广告位","status":1,"sort":0,"tag":"startup_adv","description":"<a href=\"\" target=\"_blank\"><img src=\"http://ossd.cn-sh2.ufileos.com/img/20201024133709.gif\"  /><\/a>","create_time":1560965647,"update_time":1630214059},"service_qq":{"id":62,"typename":"QQ客服","status":1,"sort":0,"tag":"service_qq","description":"5928050934","create_time":1560965677,"update_time":1635467804},"startup_config":{"status":1}}
         * player : {"app_logo":null}
         * payments : [{"name":"支付宝","payment":"epay","status":1},{"payment":"qqepay","name":"微信支付","status":1}]
         * share_url : http://v.lvdoui.cn
         * share_logo : null
         * search_hot : ["暖阳之下","突围","好好生活","为你千千万万遍","燃烧大地","一生一世","中餐厅 第五季","冰血长津湖","怒火·重案","当爱情遇上科学家"]
         */

        private SignBean sign;
        private VodMarkBean vod_mark;
        private CommentBean comment;
        private DanmuBean danmu;
        private DocumentBean document;
        private AdsBean ads;
        private PlayerBean player;
        private String share_url;
        private Object share_logo;
        private List<PaymentsBean> payments;
        private List<String> search_hot;

        public SignBean getSign() {
            return sign;
        }

        public void setSign(SignBean sign) {
            this.sign = sign;
        }

        public VodMarkBean getVod_mark() {
            return vod_mark;
        }

        public void setVod_mark(VodMarkBean vod_mark) {
            this.vod_mark = vod_mark;
        }

        public CommentBean getComment() {
            return comment;
        }

        public void setComment(CommentBean comment) {
            this.comment = comment;
        }

        public DanmuBean getDanmu() {
            return danmu;
        }

        public void setDanmu(DanmuBean danmu) {
            this.danmu = danmu;
        }

        public DocumentBean getDocument() {
            return document;
        }

        public void setDocument(DocumentBean document) {
            this.document = document;
        }

        public AdsBean getAds() {
            return ads;
        }

        public void setAds(AdsBean ads) {
            this.ads = ads;
        }

        public PlayerBean getPlayer() {
            return player;
        }

        public void setPlayer(PlayerBean player) {
            this.player = player;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public Object getShare_logo() {
            return share_logo;
        }

        public void setShare_logo(Object share_logo) {
            this.share_logo = share_logo;
        }

        public List<PaymentsBean> getPayments() {
            return payments;
        }

        public void setPayments(List<PaymentsBean> payments) {
            this.payments = payments;
        }

        public List<String> getSearch_hot() {
            return search_hot;
        }

        public void setSearch_hot(List<String> search_hot) {
            this.search_hot = search_hot;
        }

        public static class SignBean implements Serializable {
            /**
             * status : 1
             * reward : {"points":"5"}
             * title : 签到
             * info : 点击签到即可获得积分
             */

            private String status;
            private RewardBean reward;
            private String title;
            private String info;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public RewardBean getReward() {
                return reward;
            }

            public void setReward(RewardBean reward) {
                this.reward = reward;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            public static class RewardBean implements Serializable {
                /**
                 * points : 5
                 */

                private String points;

                public String getPoints() {
                    return points;
                }

                public void setPoints(String points) {
                    this.points = points;
                }
            }
        }

        public static class VodMarkBean implements Serializable {
            /**
             * status : 0
             * reward : {"points":"3"}
             * reward_num :
             * title : 评分
             * info : 为视频评分即可获得积分
             */

            private String status;
            private RewardBeanX reward;
            private String reward_num;
            private String title;
            private String info;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public RewardBeanX getReward() {
                return reward;
            }

            public void setReward(RewardBeanX reward) {
                this.reward = reward;
            }

            public String getReward_num() {
                return reward_num;
            }

            public void setReward_num(String reward_num) {
                this.reward_num = reward_num;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            public static class RewardBeanX implements Serializable {
                public String getPoints() {
                    return points;
                }

                public void setPoints(String points) {
                    this.points = points;
                }

                /**
                 * points : 3
                 */

                private String points;
            }
        }

        public static class CommentBean implements Serializable {
            /**
             * status : 1
             * reward : {"points":"5"}
             * reward_num :
             * title : 评论
             * info : 为视频即可获得积分
             */

            private String status;
            private RewardBeanXX reward;
            private String reward_num;
            private String title;
            private String info;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public RewardBeanXX getReward() {
                return reward;
            }

            public void setReward(RewardBeanXX reward) {
                this.reward = reward;
            }

            public String getReward_num() {
                return reward_num;
            }

            public void setReward_num(String reward_num) {
                this.reward_num = reward_num;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            public static class RewardBeanXX implements Serializable {
                public String getPoints() {
                    return points;
                }

                public void setPoints(String points) {
                    this.points = points;
                }

                /**
                 * points : 5
                 */

                private String points;
            }
        }


        public static class DanmuBean implements Serializable {
            /**
             * status : 0
             * reward : {"points":"2"}
             * reward_num :
             * title : 弹幕
             * info : 发送弹幕即可获得积分
             */

            private String status;
            private RewardBeanXXX reward;
            private String reward_num;
            private String title;
            private String info;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public RewardBeanXXX getReward() {
                return reward;
            }

            public void setReward(RewardBeanXXX reward) {
                this.reward = reward;
            }

            public String getReward_num() {
                return reward_num;
            }

            public void setReward_num(String reward_num) {
                this.reward_num = reward_num;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }


            public static class RewardBeanXXX implements Serializable {
                public String getPoints() {
                    return points;
                }

                public void setPoints(String points) {
                    this.points = points;
                }

                /**
                 * points : 2
                 */

                private String points;
            }
        }


        public static class DocumentBean implements Serializable {
            /**
             * un_register : {"status":"1","title":"未注册弹窗","content":"未注册弹窗","type":"un_register"}
             * registerd : {"status":"1","title":"首页弹窗标题","content":"首页弹窗内容","type":"registerd"}
             * notice : {"status":"0","title":null,"content":"完成每日任务可获得积分奖励","type":"notice"}
             * roll_notice : {"status":"0","title":null,"content":"如长时间无法播放请点击换来源，如遇卡顿请点击修复，如没有来源可换，请联系客服进行添加播放来源！","type":"roll_notice"}
             * home_notice : {"status":"0","title":null,"content":"","type":"home_notice"}
             * game_notice : {"status":"0","title":null,"content":"","type":"game_notice"}
             */

            private UnRegisterBean un_register;
            private RegisterdBean registerd;
            private NoticeBean notice;
            private RollNoticeBean roll_notice;
            private HomeNoticeBean home_notice;
            private GameNoticeBean game_notice;

            public UnRegisterBean getUn_register() {
                return un_register;
            }

            public void setUn_register(UnRegisterBean un_register) {
                this.un_register = un_register;
            }

            public RegisterdBean getRegisterd() {
                return registerd;
            }

            public void setRegisterd(RegisterdBean registerd) {
                this.registerd = registerd;
            }

            public NoticeBean getNotice() {
                return notice;
            }

            public void setNotice(NoticeBean notice) {
                this.notice = notice;
            }

            public RollNoticeBean getRoll_notice() {
                return roll_notice;
            }

            public void setRoll_notice(RollNoticeBean roll_notice) {
                this.roll_notice = roll_notice;
            }

            public HomeNoticeBean getHome_notice() {
                return home_notice;
            }

            public void setHome_notice(HomeNoticeBean home_notice) {
                this.home_notice = home_notice;
            }

            public GameNoticeBean getGame_notice() {
                return game_notice;
            }

            public void setGame_notice(GameNoticeBean game_notice) {
                this.game_notice = game_notice;
            }


            public static class UnRegisterBean implements Serializable {
                /**
                 * status : 1
                 * title : 未注册弹窗
                 * content : 未注册弹窗
                 * type : un_register
                 */

                private String status;
                private String title;
                private String content;
                private String type;

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }


            public static class RegisterdBean implements Serializable {
                /**
                 * status : 1
                 * title : 首页弹窗标题
                 * content : 首页弹窗内容
                 * type : registerd
                 */

                private String status;
                private String title;
                private String content;
                private String type;

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }


            public static class NoticeBean implements Serializable {
                /**
                 * status : 0
                 * title : null
                 * content : 完成每日任务可获得积分奖励
                 * type : notice
                 */

                private String status;
                private Object title;
                private String content;
                private String type;

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public Object getTitle() {
                    return title;
                }

                public void setTitle(Object title) {
                    this.title = title;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }


            public static class RollNoticeBean implements Serializable {
                /**
                 * status : 0
                 * title : null
                 * content : 如长时间无法播放请点击换来源，如遇卡顿请点击修复，如没有来源可换，请联系客服进行添加播放来源！
                 * type : roll_notice
                 */

                private String status;
                private Object title;
                private String content;
                private String type;

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public Object getTitle() {
                    return title;
                }

                public void setTitle(Object title) {
                    this.title = title;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }

            public static class HomeNoticeBean implements Serializable {
                /**
                 * status : 0
                 * title : null
                 * content :
                 * type : home_notice
                 */

                private String status;
                private Object title;
                private String content;
                private String type;

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public Object getTitle() {
                    return title;
                }

                public void setTitle(Object title) {
                    this.title = title;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }


            public static class GameNoticeBean implements Serializable {
                /**
                 * status : 0
                 * title : null
                 * content :
                 * type : game_notice
                 */

                private String status;
                private Object title;
                private String content;
                private String type;

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public Object getTitle() {
                    return title;
                }

                public void setTitle(Object title) {
                    this.title = title;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }
        }

        public static class AdsBean implements Serializable {
            /**
             * user_center : {"id":52,"typename":"个人中心","status":1,"sort":0,"tag":"user_center","description":"http://img-lvdoui.test.upcdn.net/fx.png","create_time":1560965440,"update_time":1593330630}
             * searcher : {"id":53,"typename":"搜索广告位","status":1,"sort":0,"tag":"searcher","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965460,"update_time":1630214161}
             * player_pause : {"id":54,"typename":"播放器暂停广告","status":0,"sort":0,"tag":"player_pause","description":"","create_time":1560965485,"update_time":1572960349}
             * player_down : {"id":55,"typename":"播放器下方广告","status":1,"sort":0,"tag":"player_down","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965505,"update_time":1630214155}
             * variety : {"id":56,"typename":"综艺广告位","status":1,"sort":0,"tag":"variety","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965569,"update_time":1630214149}
             * cartoon : {"id":57,"typename":"动漫广告位","status":1,"sort":0,"tag":"cartoon","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965583,"update_time":1630214144}
             * sitcom : {"id":58,"typename":"剧集广告位","status":1,"sort":0,"tag":"sitcom","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965601,"update_time":1630214137}
             * vod : {"id":59,"typename":"电影广告位","status":1,"sort":0,"tag":"vod","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965614,"update_time":1630214131}
             * index : {"id":60,"typename":"首页广告位","status":1,"sort":0,"tag":"index","description":"<a href=\"\" target=\"_blank\"><img src=\"http://img-lvdoui.test.upcdn.net/fx.png\" width=\"100%\" height=\"100%\" border=\"0\" /><\/a> \r\n","create_time":1560965629,"update_time":1630214125}
             * startup_adv : {"id":61,"typename":"启动广告位","status":1,"sort":0,"tag":"startup_adv","description":"<a href=\"\" target=\"_blank\"><img src=\"http://ossd.cn-sh2.ufileos.com/img/20201024133709.gif\"  /><\/a>","create_time":1560965647,"update_time":1630214059}
             * service_qq : {"id":62,"typename":"QQ客服","status":1,"sort":0,"tag":"service_qq","description":"5928050934","create_time":1560965677,"update_time":1635467804}
             * startup_config : {"status":1}
             */

            private UserCenterBean user_center;
            private SearcherBean searcher;
            private PlayerPauseBean player_pause;
            private PlayerDownBean player_down;
            private VarietyBean variety;
            private CartoonBean cartoon;
            private SitcomBean sitcom;
            private VodBean vod;
            private IndexBean index;
            private StartupAdvBean startup_adv;
            private ServiceQqBean service_qq;
            private StartupConfigBean startup_config;

            public UserCenterBean getUser_center() {
                return user_center;
            }

            public void setUser_center(UserCenterBean user_center) {
                this.user_center = user_center;
            }

            public SearcherBean getSearcher() {
                return searcher;
            }

            public void setSearcher(SearcherBean searcher) {
                this.searcher = searcher;
            }

            public PlayerPauseBean getPlayer_pause() {
                return player_pause;
            }

            public void setPlayer_pause(PlayerPauseBean player_pause) {
                this.player_pause = player_pause;
            }

            public PlayerDownBean getPlayer_down() {
                return player_down;
            }

            public void setPlayer_down(PlayerDownBean player_down) {
                this.player_down = player_down;
            }

            public VarietyBean getVariety() {
                return variety;
            }

            public void setVariety(VarietyBean variety) {
                this.variety = variety;
            }

            public CartoonBean getCartoon() {
                return cartoon;
            }

            public void setCartoon(CartoonBean cartoon) {
                this.cartoon = cartoon;
            }

            public SitcomBean getSitcom() {
                return sitcom;
            }

            public void setSitcom(SitcomBean sitcom) {
                this.sitcom = sitcom;
            }

            public VodBean getVod() {
                return vod;
            }

            public void setVod(VodBean vod) {
                this.vod = vod;
            }

            public IndexBean getIndex() {
                return index;
            }

            public void setIndex(IndexBean index) {
                this.index = index;
            }

            public StartupAdvBean getStartup_adv() {
                return startup_adv;
            }

            public void setStartup_adv(StartupAdvBean startup_adv) {
                this.startup_adv = startup_adv;
            }

            public ServiceQqBean getService_qq() {
                return service_qq;
            }

            public void setService_qq(ServiceQqBean service_qq) {
                this.service_qq = service_qq;
            }

            public StartupConfigBean getStartup_config() {
                return startup_config;
            }

            public void setStartup_config(StartupConfigBean startup_config) {
                this.startup_config = startup_config;
            }


            public static class UserCenterBean implements Serializable {
                /**
                 * id : 52
                 * typename : 个人中心
                 * status : 1
                 * sort : 0
                 * tag : user_center
                 * description : http://img-lvdoui.test.upcdn.net/fx.png
                 * create_time : 1560965440
                 * update_time : 1593330630
                 */

                private int id;
                private String typename;
                private int status;
                private int sort;
                private String tag;
                private String description;
                private int create_time;
                private int update_time;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTypename() {
                    return typename;
                }

                public void setTypename(String typename) {
                    this.typename = typename;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public int getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(int create_time) {
                    this.create_time = create_time;
                }

                public int getUpdate_time() {
                    return update_time;
                }

                public void setUpdate_time(int update_time) {
                    this.update_time = update_time;
                }
            }

            public static class SearcherBean implements Serializable {
                /**
                 * id : 53
                 * typename : 搜索广告位
                 * status : 1
                 * sort : 0
                 * tag : searcher
                 * description : <a href="" target="_blank"><img src="http://img-lvdoui.test.upcdn.net/fx.png" width="100%" height="100%" border="0" /></a>
                 * create_time : 1560965460
                 * update_time : 1630214161
                 */

                private int id;
                private String typename;
                private int status;
                private int sort;
                private String tag;
                private String description;
                private int create_time;
                private int update_time;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTypename() {
                    return typename;
                }

                public void setTypename(String typename) {
                    this.typename = typename;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public int getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(int create_time) {
                    this.create_time = create_time;
                }

                public int getUpdate_time() {
                    return update_time;
                }

                public void setUpdate_time(int update_time) {
                    this.update_time = update_time;
                }
            }


            public static class PlayerPauseBean implements Serializable {
                /**
                 * id : 54
                 * typename : 播放器暂停广告
                 * status : 0
                 * sort : 0
                 * tag : player_pause
                 * description :
                 * create_time : 1560965485
                 * update_time : 1572960349
                 */

                private int id;
                private String typename;
                private int status;
                private int sort;
                private String tag;
                private String description;
                private int create_time;
                private int update_time;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTypename() {
                    return typename;
                }

                public void setTypename(String typename) {
                    this.typename = typename;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public int getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(int create_time) {
                    this.create_time = create_time;
                }

                public int getUpdate_time() {
                    return update_time;
                }

                public void setUpdate_time(int update_time) {
                    this.update_time = update_time;
                }
            }

            public static class PlayerDownBean implements Serializable {
                /**
                 * id : 55
                 * typename : 播放器下方广告
                 * status : 1
                 * sort : 0
                 * tag : player_down
                 * description : <a href="" target="_blank"><img src="http://img-lvdoui.test.upcdn.net/fx.png" width="100%" height="100%" border="0" /></a>
                 * create_time : 1560965505
                 * update_time : 1630214155
                 */

                private int id;
                private String typename;
                private int status;
                private int sort;
                private String tag;
                private String description;
                private int create_time;
                private int update_time;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTypename() {
                    return typename;
                }

                public void setTypename(String typename) {
                    this.typename = typename;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public int getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(int create_time) {
                    this.create_time = create_time;
                }

                public int getUpdate_time() {
                    return update_time;
                }

                public void setUpdate_time(int update_time) {
                    this.update_time = update_time;
                }
            }

            public static class VarietyBean implements Serializable {
                /**
                 * id : 56
                 * typename : 综艺广告位
                 * status : 1
                 * sort : 0
                 * tag : variety
                 * description : <a href="" target="_blank"><img src="http://img-lvdoui.test.upcdn.net/fx.png" width="100%" height="100%" border="0" /></a>
                 * create_time : 1560965569
                 * update_time : 1630214149
                 */

                private int id;
                private String typename;
                private int status;
                private int sort;
                private String tag;
                private String description;
                private int create_time;
                private int update_time;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTypename() {
                    return typename;
                }

                public void setTypename(String typename) {
                    this.typename = typename;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public int getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(int create_time) {
                    this.create_time = create_time;
                }

                public int getUpdate_time() {
                    return update_time;
                }

                public void setUpdate_time(int update_time) {
                    this.update_time = update_time;
                }
            }


            public static class CartoonBean implements Serializable {
                /**
                 * id : 57
                 * typename : 动漫广告位
                 * status : 1
                 * sort : 0
                 * tag : cartoon
                 * description : <a href="" target="_blank"><img src="http://img-lvdoui.test.upcdn.net/fx.png" width="100%" height="100%" border="0" /></a>
                 * create_time : 1560965583
                 * update_time : 1630214144
                 */

                private int id;
                private String typename;
                private int status;
                private int sort;
                private String tag;
                private String description;
                private int create_time;
                private int update_time;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTypename() {
                    return typename;
                }

                public void setTypename(String typename) {
                    this.typename = typename;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public int getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(int create_time) {
                    this.create_time = create_time;
                }

                public int getUpdate_time() {
                    return update_time;
                }

                public void setUpdate_time(int update_time) {
                    this.update_time = update_time;
                }
            }


            public static class SitcomBean implements Serializable {
                /**
                 * id : 58
                 * typename : 剧集广告位
                 * status : 1
                 * sort : 0
                 * tag : sitcom
                 * description : <a href="" target="_blank"><img src="http://img-lvdoui.test.upcdn.net/fx.png" width="100%" height="100%" border="0" /></a>
                 * create_time : 1560965601
                 * update_time : 1630214137
                 */

                private int id;
                private String typename;
                private int status;
                private int sort;
                private String tag;
                private String description;
                private int create_time;
                private int update_time;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTypename() {
                    return typename;
                }

                public void setTypename(String typename) {
                    this.typename = typename;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public int getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(int create_time) {
                    this.create_time = create_time;
                }

                public int getUpdate_time() {
                    return update_time;
                }

                public void setUpdate_time(int update_time) {
                    this.update_time = update_time;
                }
            }


            public static class VodBean implements Serializable {
                /**
                 * id : 59
                 * typename : 电影广告位
                 * status : 1
                 * sort : 0
                 * tag : vod
                 * description : <a href="" target="_blank"><img src="http://img-lvdoui.test.upcdn.net/fx.png" width="100%" height="100%" border="0" /></a>
                 * create_time : 1560965614
                 * update_time : 1630214131
                 */

                private int id;
                private String typename;
                private int status;
                private int sort;
                private String tag;
                private String description;
                private int create_time;
                private int update_time;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTypename() {
                    return typename;
                }

                public void setTypename(String typename) {
                    this.typename = typename;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public int getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(int create_time) {
                    this.create_time = create_time;
                }

                public int getUpdate_time() {
                    return update_time;
                }

                public void setUpdate_time(int update_time) {
                    this.update_time = update_time;
                }
            }


            public static class IndexBean implements Serializable {
                /**
                 * id : 60
                 * typename : 首页广告位
                 * status : 1
                 * sort : 0
                 * tag : index
                 * description : <a href="" target="_blank"><img src="http://img-lvdoui.test.upcdn.net/fx.png" width="100%" height="100%" border="0" /></a>
                 * create_time : 1560965629
                 * update_time : 1630214125
                 */

                private int id;
                private String typename;
                private int status;
                private int sort;
                private String tag;
                private String description;
                private int create_time;
                private int update_time;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTypename() {
                    return typename;
                }

                public void setTypename(String typename) {
                    this.typename = typename;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public int getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(int create_time) {
                    this.create_time = create_time;
                }

                public int getUpdate_time() {
                    return update_time;
                }

                public void setUpdate_time(int update_time) {
                    this.update_time = update_time;
                }
            }


            public static class StartupAdvBean implements Serializable {
                /**
                 * id : 61
                 * typename : 启动广告位
                 * status : 1
                 * sort : 0
                 * tag : startup_adv
                 * description : <a href="" target="_blank"><img src="http://ossd.cn-sh2.ufileos.com/img/20201024133709.gif"  /></a>
                 * create_time : 1560965647
                 * update_time : 1630214059
                 */

                private int id;
                private String typename;
                private int status;
                private int sort;
                private String tag;
                private String description;
                private int create_time;
                private int update_time;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTypename() {
                    return typename;
                }

                public void setTypename(String typename) {
                    this.typename = typename;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public int getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(int create_time) {
                    this.create_time = create_time;
                }

                public int getUpdate_time() {
                    return update_time;
                }

                public void setUpdate_time(int update_time) {
                    this.update_time = update_time;
                }
            }


            public static class ServiceQqBean implements Serializable {
                /**
                 * id : 62
                 * typename : QQ客服
                 * status : 1
                 * sort : 0
                 * tag : service_qq
                 * description : 5928050934
                 * create_time : 1560965677
                 * update_time : 1635467804
                 */

                private int id;
                private String typename;
                private int status;
                private int sort;
                private String tag;
                private String description;
                private int create_time;
                private int update_time;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTypename() {
                    return typename;
                }

                public void setTypename(String typename) {
                    this.typename = typename;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public int getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(int create_time) {
                    this.create_time = create_time;
                }

                public int getUpdate_time() {
                    return update_time;
                }

                public void setUpdate_time(int update_time) {
                    this.update_time = update_time;
                }
            }


            public static class StartupConfigBean implements Serializable {
                /**
                 * status : 1
                 */

                private int status;

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }
            }
        }


        public static class PlayerBean implements Serializable {
            /**
             * app_logo : null
             */

            private Object app_logo;

            public Object getApp_logo() {
                return app_logo;
            }

            public void setApp_logo(Object app_logo) {
                this.app_logo = app_logo;
            }
        }


        public static class PaymentsBean implements Serializable {
            /**
             * name : 支付宝
             * payment : epay
             * status : 1
             */

            private String name;
            private String payment;
            private int status;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPayment() {
                return payment;
            }

            public void setPayment(String payment) {
                this.payment = payment;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
