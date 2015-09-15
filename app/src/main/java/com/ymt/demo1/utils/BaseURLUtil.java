package com.ymt.demo1.utils;

import android.util.Log;

import java.net.URLEncoder;

/**
 * Created by Dan on 2015/6/10
 */
public class BaseURLUtil {
    public static final String PUB_ZX_JZ = "PUB_ZX_JZ";
    public static final String PUB_ZX_GJC = "PUB_ZX_GJC";
    public static final String PUB_ZX_ZY = "PUB_ZX_ZY";
    public static final String BASE_URL_test = "http://120.24.172.105";
    public static final String BASE_URL = "http://101.204.236.5";

    //学习资料
    public static final String STUDY_DATUM = BASE_URL + "/fw?controller=com.xfsm.action.ExamAction&m=book&order=new&start=";

    //最新考试时间
    public static final String EARLIEST_EXAM_INFO = BASE_URL + "/fw?controller=com.xfsm.action.TypeAction&t=app&m=param&p=" + "教育平台最新考试名称" + "," + "教育平台最新考试时间";

    //pdf
    public static final String PDF_BASE = BASE_URL + "/public/pub/upload/down.jsp?id=";

    //登录
    private static final String SIGN_IN_BASE = BASE_URL + "/fw?controller=com.xfsm.action.LoginAction&loginname=";
    //注册
    private static final String SIGN_UP_BASE = BASE_URL + "/fw?controller=com.xfsm.action.RegAction";
    //关注、粉丝
    private static final String ATT_FANS_BASE = BASE_URL + "/fw?controller=com.xfsm.action.UserAction&t=app&SESSIONID=";
    //消息
    private static final String QQ_MSG_BASE = BASE_URL + "/fw?controller=com.xfsm.action.ChatAction&SESSIONID=";
    //分类
    private static final String TYPE_ACTION_BASE = BASE_URL + "/fw?controller=com.xfsm.action.TypeAction&t=app&m=st&type=";
    //分类内容列表
    private static final String TYPE_CONTENT_LIST_BASE = BASE_URL + "/fw?controller=com.xfsm.action.ArticleAction&t=app&m=chat&pagesize=";
    //知识平台（视频、标准规范、科研文献）
    private static final String KNOWLEDGE_ITEM_BASE = BASE_URL + "/fw?controller=com.xfsm.action.KnowAction&t=app&m=";
    //最近咨询
    private static final String RECENT_CONSULT = BASE_URL + "/fw?controller=com.xfsm.action.ArticleAction&t=app&m=lastest&start=";
    //热点咨询
    private static final String HOT_CONSULT = BASE_URL + "/fw?controller=com.xfsm.action.ArticleAction&t=app&m=hot&start=";
    //专家列表
    private static final String EXPERT_LIST = BASE_URL + "/fw?controller=com.xfsm.action.ExpertAction&t=app&m=list&pagesize=";
    //自动分配账号
    public static final String AUTO_CREATE_ACCOUNT = BASE_URL + "/fw?t=app&controller=com.xfsm.action.AutoRegAction&method=distributionAccount";
    //    //论坛基本接口
//    private static final String HUB_BASE = BASE_URL + "/xxfintf/";
    //修改密码
    private static final String CHANGE_PSW = BASE_URL + "/fw?t=app&controller=com.xfsm.action.PwdAction&loginname=";
    //退出账号
    private static final String SIGN_OUT = BASE_URL + "/exit.jsp?sId=";
    //关注的专家列表
    private static final String FOLLOWED_EXPERT_LIST = BASE_URL + "/fw?t=app&controller=com.xfsm.action.ExpertAction&m=myFcous&start=";
    //取消关注某专家
    private static final String FOLLOW_UN_FOLLOW_EXPERT = BASE_URL + "/fw?controller=com.xfsm.action.PersonalAction&sId=";
    //id获得基本信息
    private static final String INFO_BY_ID = BASE_URL + "/fw?controller=com.xfsm.action.UserInfoAction&uid=";
    //历年真题
    private static final String PAST_EXAMS = BASE_URL + "/fw?controller=com.xfsm.action.ExamAction&m=histroy&order=new&start=";
    //模拟试题
    private static final String MOCK_EXAMS = BASE_URL + "/fw?t=app&controller=com.xfsm.action.ExamAction&m=exams&jl=";
    //试题信息
    private static final String PAPER_INFO = BASE_URL + "/fw?t=app&controller=com.xfsm.action.ExamAction&m=goExam&id=";
    //提交答案、试卷
    public static final String SUB_ANSWER = BASE_URL + "/fw?controller=com.xfsm.action.UserExamAction";
    //收藏
    private static final String COLLECT_BASE = BASE_URL + "/fw?controller=com.mingsokj.action.XfCollectAction&method=collect&table=";
    //全文检索-热门词
    public static final String HOT_KEY_WORDS = BASE_URL + "/webintf/search/getFullQueryHot?";
    //搜索历史
    private static final String HIS_KEY_WORDS = BASE_URL + "/webintf/search/getFullQueryHis?userId=";
    //全文检索
    private static final String SEARCH_BASE = BASE_URL + "/webintf/search/getFullQueryForKN";
    //建议
    private static final String ADVICE_BASE = BASE_URL + "/fw?controller=com.xfsm.action.AdviceAPIAction&m=advice&sId=";
    //论坛1
    public static final String PLATE_REQUEST_URL = BASE_URL + "/bbs/bbs/getForumList.do";
    private static final String SUBJECT_REQUEST_BASE_URL = BASE_URL + "/bbs/bbs/getSubjectListByFid.do";
    private static final String POST_URL = BASE_URL + "/bbs/bbs/wirteSubject.do";
    public static final String HUB_HOT_URL = BASE_URL + "/bbs/bbs/getHotPostList.do";
    public static final String HUB_NEW_URL = BASE_URL + "/bbs/bbs/getLastSubject.do";
    private static final String HUB_MY_REPLIES = BASE_URL + "/bbs/bbs/getPostListByUser.do";
    private static final String HUB_MY_POST = BASE_URL + "/bbs/bbs/getThreadListByUserName.do";
    private static final String HUB_SYS_INFO = BASE_URL + "/bbs/bbs/getNotifiListByUser.do";
    private static final String HUB_POST_CONTENT = BASE_URL + "/bbs/bbs/getPostListByTid.do";
    private static final String HUB_POST_REPLY = BASE_URL + "/bbs/bbs/wirtePost.do";

    /**
     * 根据tid 获取帖子内容
     */
    public static String getPostContentUrl(int tid, int index) {
        return HUB_POST_CONTENT + "?tid=" + String.valueOf(tid) + "&index=" + String.valueOf(index);
    }

    /**
     * 专家值班表
     */
    public static String getOnDutyExpert() {
        return BASE_URL + "/fw?controller=com.xfsm.action.ExpertAction&m=duty";
    }

    /**
     * 回复帖子
     */
    public static String getReplyPostUrl(int tid, String msg, String user, int reqType) {
        return HUB_POST_REPLY + "?tid=" + String.valueOf(tid) + "&user=" + user + "&msg=" + msg + "&reqType=" + String.valueOf(reqType);
    }

    /**
     * 论坛系统消息
     */
    public static String getHubSysInfo(String userName) {
        return HUB_SYS_INFO + "?user=" + userName;
    }

    /**
     * 我的回帖
     */
    public static String getHubMyReplies(String userName, int index) {
        return HUB_MY_REPLIES + "?user=" + userName + "&index=" + String.valueOf(index);
    }

    /**
     * 我的发帖
     */
    public static String getHubMyPost(String userName, int index) {
        return HUB_MY_POST + "?user=" + userName + "&index=" + String.valueOf(index);
    }

    /**
     * 发帖
     */
    public static String postHubSubject(String userName, int fid, String title, String content, int reqType) {
        return POST_URL + "?user=" + userName + "&fid=" + String.valueOf(fid) + "&subject=" + title + "&msg=" + content + "&reqType=" + String.valueOf(reqType);
    }

    /**
     * 论坛主题
     */
    public static String getSubjectsById(int fId) {
        return SUBJECT_REQUEST_BASE_URL + "?fid=" + fId;
    }

    /**
     * 建议
     */
    public static String doAdviceAction(String sId, String title, String content, String phoneNum) {
        return ADVICE_BASE + sId + "&title=" + title + "&content=" + content + "&phonenumber=" + phoneNum;
    }

    /**
     * 全文检索
     */
    public static String getQuerySearch(String user_id, int queryType, int start, int limit, String queryInfo) {
        return SEARCH_BASE + "?userId=" + user_id + "&queryWay=app&queryType=" + String.valueOf(queryType) + "&start=" + String.valueOf(start) + "&limit=" + String.valueOf(limit) + "&queryInfo=" + queryInfo;
    }

    /**
     * 历史搜索
     */
    public static String getHistoryKW(String user_id, int start, int limit) {
        return HIS_KEY_WORDS + user_id + "&start=" + String.valueOf(start) + "&limit=" + String.valueOf(limit);
    }

    /**
     * 热门搜索
     */
    public static String getHotKW(int start, int limit) {
        return HOT_KEY_WORDS + String.valueOf(start) + "&limit=" + String.valueOf(limit);
    }

    /**
     * 收藏
     */
    public static String doCollect(String type, String article_id) {
        return COLLECT_BASE + type + "&fk_article_id=" + article_id;
    }

    /**
     * 获取试卷内容
     */
    public static String getPaperContent(String paper_id) {
        return PAPER_INFO + paper_id + "&sId=" + AppContext.now_session_id;
    }

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
     * 值日专家表
     */
    public static String doGetDutyExpert() {
        return BASE_URL + "/fw?controller=com.xfsm.action.ExpertAction&m=duty";
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
//        String str = SIGN_IN_BASE + admin + "&pwd=" + pwd + "&t=app";
//        Log.e("TAG","..sign Str>>>>>>>>>>>"+str);
        return SIGN_IN_BASE + URLEncoder.encode(admin) + "&pwd=" + pwd + "&t=app";

    }

    /**
     * 注册
     */
    public static String doSignUp(String loginName, String pwd, String phone, String type) {
        return SIGN_UP_BASE + "&loginname=" + URLEncoder.encode(loginName) + "&pwd=" + pwd + "&phone=" + phone + "&t=" + type;
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
