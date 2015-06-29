package com.ymt.demo1.main;

/**
 * Created by Dan on 2015/6/10
 */
public class BaseURLUtil {
    public static final String EXPORT_ID = "export_id";
    public static final String NOW_USER_ID = "now_user_id";
    public static final String NOW_SESSION_ID = "now_session_id";
    public static final String XF_PUB_JZXF = "XF_PUB_JZXF";
    public static final String PUB_ZX_GJC = "PUB_ZX_GJC";
    public static final String PUB_ZX_ZY = "PUB_ZX_ZY";

    //登录
    private static final String SIGN_IN_BASE = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.LoginAction&loginname=";
    //注册
    private static final String SIGN_UP_BASE = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.RegAction";
    //关注、粉丝
    private static final String ATT_FANS_BASE = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.UserAction&t=app&SESSIONID=";
    //消息
    private static final String QQ_MSG_BASE = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.ChatAction&SESSIONID=";
    //分类
    private static final String TYPE_ACTION_BASE = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.TypeAction&t=app&m=st&type=";
    //分类内容列表
    private static final String TYPE_CONTENT_LIST_BASE = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.ArticleAction&t=app&m=chat&pagesize=";
    //知识平台（视频、标准规范、科研文献）
    private static final String KNOWLEDGE_ITEM_BASE = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.KnowAction&t=app&m=";
    //最近咨询
    private static final String RECENT_CONSULT = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.ArticleAction&t=app&m=lastest&start=";
    //热点咨询
    private static final String HOT_CONSULT = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.ArticleAction&t=app&m=hot&start=";
    //专家列表
    private static final String EXPERT_LIST = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.ExpertAction&t=app&m=list&pagesize=";
    //自动分配账号
    public static final String AUTO_CREATE_ACCOUNT = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.AutoRegAction&t=app";

    /**
     * 值日专家表
     */
    public static String doGetDutyExpert() {
        return "http://120.24.172.105:8000/fw?controller=com.xfsm.action.ExpertAction&m=duty";
    }

    /**
     * 专家列表
     */
    public static String doGetExpertList(int pageSize, int start, String searchWho) {
        return EXPERT_LIST + String.valueOf(pageSize) + "&start=" + String.valueOf(start) + "&jl=&kw=" + searchWho;
    }

    /**
     * 最近咨询
     */
    public static String doGetRecentConsult(int start, int pageSize) {
        return RECENT_CONSULT + String.valueOf(start) + "&pagesize=" + String.valueOf(pageSize);
    }

    /**
     * 热点咨询
     */
    public static String doGetHotConsult(int start, int pageSize) {
        return HOT_CONSULT + String.valueOf(start) + "&pagesize=" + String.valueOf(pageSize);
    }

    /**
     * 知识平台（视频、标准规范、科研文献）
     */
    public static String doGetKnowledgeAction(String knowledgeType, int pageSize, int start, String searchWhat) {
        return KNOWLEDGE_ITEM_BASE + knowledgeType + "&pagesize=" + String.valueOf(pageSize) + "&start=" + String.valueOf(start) + "&jl=" + "&kw=" + searchWhat;
    }

    /**
     * 分类
     */
    public static String doTypeAction(String type) {
        return TYPE_ACTION_BASE + type;
    }

    /**
     * 分类内容列表
     */
    public static String doTypeContentListAction(int pageSize, int start, String typeCode, String searchWhat) {
        return TYPE_CONTENT_LIST_BASE + String.valueOf(pageSize) + "&start=" + String.valueOf(start) + "&jl=" + typeCode + "&kw=" + searchWhat;
    }

    /**
     * 登录
     */
    public static String doSignIn(String admin, String pwd) {
        return SIGN_IN_BASE + admin + "&pwd=" + pwd + "&t=app";
    }

    /**
     * 注册
     */
    public static String doSignUp(String loginName, String pwd, String phone, String type) {
        return SIGN_UP_BASE + "&loginname=" + loginName + "&pwd=" + pwd + "&phone=" + phone + "&t=" + type;
    }

    /**
     * 关注、粉丝
     */
    public static String getAttFansUrl(String sessionId) {
        return ATT_FANS_BASE + sessionId;
    }

    /**
     * 根据sessionID获得消息（所有消息）
     */
    public static String getMyQQMsgs(String sessionID) {
        return QQ_MSG_BASE + sessionID + "&m=getMyQQs";
    }

    /**
     * 根据sessionID、qqID获得某QQ会话的未读消息
     */
    public static String getMyUnreadQQMsgUrl(String sessionID, String qq_id, String userId) {
        return QQ_MSG_BASE + sessionID + "&m=getQQUnreadMsg&qq_id=" + qq_id + "&userId=" + userId;
    }

    /**
     * 根据sessionID、qqID获得某一条QQ的所有消息
     */

    public static String getMyAllQQMsgUrl(String sessionID, String qq_id) {
        return QQ_MSG_BASE + sessionID + "&m=getQQAllMsg&qq_id=" + qq_id;
    }

    /**
     * 根据sessionID发起咨询（创建一个QQ会话） GET方式
     *
     * @param title    : 发起咨询的标题
     * @param content  ： 发起咨询的内容
     * @param expertId ： 对应专家的ID
     */
    public static String startQQChatUrlGET(String sessionID, String title, String content, String expertId) {
        return QQ_MSG_BASE + sessionID + "&t=app&m=startChat&title=" + title + "&content=" + content + "&expert=" + expertId;
    }

    /**
     * 根据sessionID发起咨询（创建一个QQ会话） POST方式
     */
    public static String startQQChatUrlPOST(String sessionID) {
        return QQ_MSG_BASE + sessionID + "&t=app&m=startChat";
    }

    /**
     * 注册
     */
    public static String sendQQMsgUrl(String sessionID, String content, String qq_id) {
        return QQ_MSG_BASE + sessionID + "&t=app&m=sendMsg&content=" + content + "&qq_id=" + qq_id;
    }

}
