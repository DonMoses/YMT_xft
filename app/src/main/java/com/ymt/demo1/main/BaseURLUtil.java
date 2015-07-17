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
    public static final String BASE_URL = "http://120.24.172.105:8000";

    //学习资料
    public static final String STUDY_DATUM = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.ExamAction&m=book&order=new&start=";

    //最新考试时间
    public static final String EARLIEST_EXAM_INFO = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.TypeAction&t=app&m=param&p=%E6%95%99%E8%82%B2%E5%B9%B3%E5%8F%B0%E6%9C%80%E6%96%B0%E8%80%83%E8%AF%95%E5%90%8D%E7%A7%B0,%E6%95%99%E8%82%B2%E5%B9%B3%E5%8F%B0%E6%9C%80%E6%96%B0%E8%80%83%E8%AF%95%E6%97%B6%E9%97%B4";

    //pdf
    public static final String PDF_BASE = "http://120.24.172.105:8000/public/pub/upload/down.jsp?id=";

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
    public static final String AUTO_CREATE_ACCOUNT = "http://120.24.172.105:8000/fw?t=app&controller=com.xfsm.action.AutoRegAction&method=distributionAccount";
    //论坛基本接口
    private static final String HUB_BASE = "http://120.24.172.105:8000/xxfintf/";
    //修改密码
    private static final String CHANGE_PSW = "http://120.24.172.105:8000/fw?t=app&controller=com.xfsm.action.PwdAction&loginname=";
    //退出账号
    private static final String SIGN_OUT = "http://120.24.172.105:8000/exit.jsp?sId=";
    //关注的专家列表
    private static final String FOLLOWED_EXPERT_LIST = "http://120.24.172.105:8000/fw?t=app&controller=com.xfsm.action.ExpertAction&m=myFcous&start=";
    //取消关注某专家
    private static final String FOLLOW_UN_FOLLOW_EXPERT = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.PersonalAction&sId=";
    //id获得基本信息
    private static final String INFO_BY_ID = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.UserInfoAction&uid=";
    //历年真题
    private static final String PAST_EXAMS = "http://120.24.172.105:8000/fw?controller=com.xfsm.action.ExamAction&m=histroy&order=new&start=";
    //模拟试题
    private static final String MOCK_EXAMS = "http://120.24.172.105:8000/fw?t=app&controller=com.xfsm.action.ExamAction&m=exams&jl=";


    /**
     * 模拟试题（考试类型、时间）
     */
    public static String getMockExams(int start, String examType, int dateYear, String searchWhat) {
        if (examType.equals("001") || examType.equals("002") || examType.equals("003") || examType.equals("004")) {
            if (String.valueOf(dateYear).length() != 4) {
                return MOCK_EXAMS + "type_" + examType + "&start=" + String.valueOf(start) + "&kw=" + searchWhat;
            } else {
                return MOCK_EXAMS + "type_" + examType + ",bookdate_" + String.valueOf(dateYear) + "&start=" + String.valueOf(start) + "&kw=" + searchWhat;
            }
        } else {
            if (String.valueOf(dateYear).length() != 4) {
                return MOCK_EXAMS + "&start=" + String.valueOf(start) + "&kw=" + searchWhat;
            } else {
                return MOCK_EXAMS + "bookdate_" + String.valueOf(dateYear) + "&start=" + String.valueOf(start) + "&kw=" + searchWhat;
            }
        }

    }

    /**
     * 历年真题(按年份)
     */
    public static String getPastExamsByYear(int start, int dateYear, String searchWhat) {
        if (String.valueOf(dateYear).length() != 4) {
            return PAST_EXAMS + String.valueOf(start) + "&kw=" + searchWhat;
        } else {
            return PAST_EXAMS + String.valueOf(start) + "&kw=" + searchWhat + "&jl=hisdate_" + String.valueOf(dateYear);
        }
    }

    /**
     * 历年真题(按level)
     */
    public static String getPastExamsByLevel(int start, String level, String searchWhat) {
        return PAST_EXAMS + String.valueOf(start) + "&kw=" + searchWhat + "&jl=type_" + level;
    }

    /**
     * 根据id获得基本信息
     */
    public static String getInfoById(String id) {
        return INFO_BY_ID + id;
    }

    /**
     * 学习资料
     */
    public static String getStudyDatum(int start, String searchWhat) {
        return STUDY_DATUM + String.valueOf(start) + "&kw=" + searchWhat;
    }

    /**
     * 关注某专家
     */
    public static String followExpert(String sId, String expertId) {
        return FOLLOW_UN_FOLLOW_EXPERT + sId + "&expertID=" + expertId + "&method=queryExpertList";

    }

    /**
     * 取消关注某专家
     */
    public static String unfollowedExpert(String sId, String expertId, String userId) {
        return FOLLOW_UN_FOLLOW_EXPERT + sId + "&expertID=" + expertId + "&user_id=" + userId + "&method=cancel";
    }

    /**
     * 关注的专家列表
     */
    public static String followedExpertList(int start, int pageSize, String sId) {
        return FOLLOWED_EXPERT_LIST + start + "&pagesize=" + pageSize + "&sId=" + sId;
    }

    /**
     * 退出账号
     */
    public static String signOutAct(String sId) {
        return SIGN_OUT + sId;
    }

    /**
     * 修改密码
     */
    public static String getChangePswUrl(String loginName, String oldPsw, String newPsw) {
        //&old_pwd=222222&new_pwd=111111
//        Log.e("TAG",">>>>>>>>>>url>>>>>"+CHANGE_PSW + loginName + "&old_pwd=" + oldPsw + "&new_pwd=" + newPsw);
        return CHANGE_PSW + loginName + "&old_pwd=" + oldPsw + "&new_pwd=" + newPsw;
    }

    /**
     * 根据tid 获取帖子内容
     */
    public static String getPostContentUrl(int tid) {
        return HUB_BASE + "bbs/getPostListByTid?tid=" + String.valueOf(tid);
    }

    /**
     * 回复帖子
     */
    public static String getReplyPostUrl(int tid, String msg, String userName) {
        return HUB_BASE + "bbs/wirtePost?tid=" + String.valueOf(tid) + "&user=" + userName + "&msg=" + msg;
    }

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
        return KNOWLEDGE_ITEM_BASE + knowledgeType + "&pagesize=" + String.valueOf(pageSize) + "&start=" + String.valueOf(start) + "&jl=&kw=" + searchWhat;
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
    public static String getMyQQMsgs(String sessionID, int start) {
        return QQ_MSG_BASE + sessionID + "&m=getMyQQs&start=" + String.valueOf(start);
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
