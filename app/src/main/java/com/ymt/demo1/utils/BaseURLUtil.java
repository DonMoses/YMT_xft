package com.ymt.demo1.utils;

import android.text.TextUtils;

import com.ymt.demo1.plates.eduPlane.mockExams.MockExamsListActivity;

import java.net.URLEncoder;

/**
 * Created by Dan on 2015/6/10
 */
public class BaseURLUtil {
    public static final String BASE_URL = "http://www.xxf3.com";
    //    public static final String BASE_URL = "http://192.168.1.132";
    public static final String HUB_BASE = "http://bbs.xxf3.com";
    public static final String EXPERT_BASE = "http://con.xxf3.com";
    public static final String EDU_BASE = "http://edu.xxf3.com";
    public static final String KNO_BASE = "http://know.xxf3.com";

    //学习资料
    public static final String STUDY_DATUM = EDU_BASE + "/xxf/searchShows.do?";
    //最新考试时间
    public static final String EARLIEST_EXAM_INFO = EDU_BASE + "/test/loadXxfEduExam.do?";
    //注册
    private static final String SIGN_IN_BASE = BASE_URL + "/uc/login.do?loginName=";
    //登录
    private static final String SIGN_UP_BASE = BASE_URL + "/uc/regster.do?loginName=";
    //消息
    private static final String QQ_MSG_BASE = EXPERT_BASE + "/cs/ac.do?";
    //发送一条消息
    private static final String QQ_SEND_MSG = EXPERT_BASE + "/crc/acc.do?";
    //获取一个QQ回话的所有消息
    private static final String QQ_CHAT_MSGS = EXPERT_BASE + "/crc/fcrbc.do?cid=";
    //我的QQ消息条目
    private static final String MY_QQ_INFO = BASE_URL + "/xxf/participateConsultation.do?sId=";
    //分类
    private static final String CONSULT_CATO_BASE = BASE_URL + "/cc/fcbt.do?typeId=";
    //新闻
    private static final String NEWS_BASE = BASE_URL + "/news/getNews.do?";
    //公告
    private static final String NOTICE_BASE = BASE_URL + "/news/getNotice.do?";
    //新闻/公告具体内容
    private static final String NEWS_NOTICES_CONTENT_BASE = BASE_URL + "/hc/findNews.do?";
    //分类内容列表
    private static final String TYPE_CONTENT_LIST_BASE = EXPERT_BASE + "/cs/faci.do?";
    //最近咨询
    private static final String RECENT_CONSULT = EXPERT_BASE + "/cs/fnc.do?";
    //热点咨询
    private static final String HOT_CONSULT = EXPERT_BASE + "/cs/fhc.do?";
    //咨询的具体内容
    private static final String CONSULT_CONTENT = EXPERT_BASE + "/cs/fci.do?cid=";
    //专家列表
    private static final String EXPERT_LIST = EXPERT_BASE + "/ec/fae.do?";
    //专家详情
    private static final String EXPERT_INFO = EXPERT_BASE + "/ec/mi.do?exId=";
    //自动分配账号
    public static final String AUTO_CREATE_ACCOUNT = BASE_URL + "/uc/regster.do?userType=000";
    //修改密码
    private static final String CHANGE_PSW = BASE_URL + "/xxf/amendByPwd.do?";
    //退出账号
    private static final String SIGN_OUT = BASE_URL + "/uc/exitLogin.do?";
    //取消关注某专家
    private static final String FOLLOW_UN_FOLLOW_EXPERT = EXPERT_BASE + "/ec/cxgz.do?";
    //关注某专家
    private static final String FOLLOW_FOLLOW_EXPERT = EXPERT_BASE + "/ec/cce.do?";
    //历年真题列表
    private static final String PAST_EXAMS_LIST = EDU_BASE + "/xxf/searchShow.do?";
    //易错题列表
    private static final String EASY_WRONG_LIST = EDU_BASE + "/xxf/xxfEduTopicByError.do?";
    //易错题详情
    private static final String EASY_WRONG_DETAIL = EDU_BASE + "/xxf/byWrong.do?bankId=";
    //历年真题详情
    private static final String PAST_EXAM_DETAIL = EDU_BASE + "/xxf/xxfEduByHXxfEduByHistoryId.do?historyId=";
    //模拟试题列表
    private static final String MOCK_EXAMS_LIST = EDU_BASE + "/xxf/xxfEduSubByLevelAndXxfEduExa.do?";
    //我的学习列表
    private static final String MOCK_MY_EXAMS_LIST = EDU_BASE + "/xxf/xxfEduRecordsAndUser.do?";
    //生成模拟试题
    private static final String MOCK_EXAM_TTT_INFO = EDU_BASE + "/test/loadByPrimaryKey.do?";
    //模拟试题题目信息
    private static final String MOCK_EXAM_TTT_CONTENT = EDU_BASE + "/test/loadStartQuestions.do?";
    //提交一个试题
    private static final String MOCK_SUBMIT_TTT = EDU_BASE + "/test/updateByUserAnswer.do?";
    //提交试卷
    private static final String MOCK_SUBMIT_PAPER = EDU_BASE + "/test/loadResultScore.do?";
    //我的考试
    private static final String MY_EXAM = EDU_BASE + "/xxf/xxfEduRecordsAndUser.do?";
    //收藏真题
    private static final String COLLECT_PAST_EXAM = EDU_BASE + "/xxf/collectionAndXxfEdu.do?";
    //收藏咨询
    private static final String COLLECT_CONSULT = EXPERT_BASE + "/cs/cc.do?";
    //取消收藏咨询
    private static final String DE_COLLECT_CONSULT = EXPERT_BASE + "/cs/dc.do?";
    //收藏知识【取消收藏知识也是该接口，type =1，收藏； type =2，取消收藏】
    private static final String COLLECT_KNOWLEDGE = KNO_BASE + "/xxfKnow/collect.do?";
    //知识平台详情
    private static final String KNOWLEDGE_DETAIL = KNO_BASE + "/xxfKnow/getKnowInfo.do?knowId=";
    //全文检索-热门词
    public static final String HOT_KEY_WORDS = BASE_URL + "/webintf/search/getFullQueryHot?";
    //搜索历史
    private static final String HIS_KEY_WORDS = BASE_URL + "/webintf/search/getFullQueryHis?userId=";
    //建议
    private static final String ADVICE_BASE = BASE_URL + "/fc/af.do?";
    //论坛1
    public static final String PLATE_REQUEST_URL = HUB_BASE + "/bbs/getForumList.do?";
    private static final String SUBJECT_REQUEST_BASE_URL = HUB_BASE + "/bbs/getSubjectListByFid.do?";
    private static final String POST_URL = HUB_BASE + "/bbs/wirteSubject.do?";
    public static final String HUB_HOT_URL = HUB_BASE + "/bbs/getHotPostList.do?";
    public static final String HUB_NEW_URL = HUB_BASE + "/bbs/getLastSubject.do?";
    private static final String HUB_MY_REPLIES = HUB_BASE + "/bbs/getPostListByUser.do?";
    private static final String HUB_MY_POST = HUB_BASE + "/bbs/getThreadListByUserName.do?";
    private static final String HUB_SYS_INFO = HUB_BASE + "/bbs/getNotifiListByUser.do?";
    private static final String HUB_POST_CONTENT = HUB_BASE + "/bbs/getPostListByTid.do?";
    private static final String HUB_POST_REPLY = HUB_BASE + "/bbs/wirtePost.do?";
    //知识平台（视频、标准规范、科研文献）
    private static final String KNOWLEDGE_ITEM_BASE = KNO_BASE + "/xxfKnow/find.do?type=";
    private static final String KNOWLEDGE_FILE_DOWNLOAD_URL = KNO_BASE + "/xxfKnow/down.do?";
    private static final String KNOWLEDGE_STATIC_FILE_URL = KNO_BASE + "/static/files/know/";
    //教育平台视频地址
    private static final String EDU_STATIC_FILE_URL = EDU_BASE + "/static/files/edu/";
    //培训视频列表
    private static final String EDU_VIDEO_TRAIN_LIST = EDU_BASE + "/xxf/xxfEduByColume.do?";
    //收藏列表[咨询]
    private static final String COLLECT_CON_LIST = BASE_URL + "/xxf/asConsultCollection.do?";
    //收藏列表[知识]
    private static final String COLLECT_KNO_LIST = BASE_URL + "/xxf/xxfXxfKnowCollection.do?";
    //收藏列表[教育]
    private static final String COLLECT_EDU_LIST = BASE_URL + "/xxf/xxfEduCollectionAndXxfEdu.do?";

    /**
     * 根据tid 获取帖子内容
     */
    public static String getPostContentUrl(int tid, int index) {
        return HUB_POST_CONTENT + "tid=" + String.valueOf(tid) + "&index=" + String.valueOf(index);
    }

    /**
     * 专家值班表
     */
    public static String getOnDutyExpert() {
        return EXPERT_BASE + "/ec/de.do?mark=current";
    }

    /**
     * 回复帖子
     */
    public static String getReplyPostUrl(int tid, String msg, String user, int reqType) {
        return HUB_POST_REPLY + "tid=" + String.valueOf(tid) + "&user=" + user + "&msg=" + msg + "&reqType=" + String.valueOf(reqType);
    }

    /**
     * 知识item详情
     */
    public static String getKnowlegeDetail(String knowId) {
        return KNOWLEDGE_DETAIL + knowId;
    }

    /**
     * 论坛系统消息
     */
    public static String getHubSysInfo(String userName) {
        return HUB_SYS_INFO + "user=" + userName;
    }

    /**
     * 我的回帖
     */
    public static String getHubMyReplies(String userName, int index) {
        return HUB_MY_REPLIES + "user=" + userName + "&index=" + String.valueOf(index);
    }

    /**
     * 我的发帖
     */
    public static String getHubMyPost(String userName, int index) {
        return HUB_MY_POST + "user=" + userName + "&index=" + String.valueOf(index);
    }

    /**
     * 发帖
     */
    public static String postHubSubject(String userName, int fid, String title, String content, int reqType) {
        return POST_URL + "user=" + userName + "&fid=" + String.valueOf(fid) + "&subject=" + title + "&msg=" + content + "&reqType=" + String.valueOf(reqType);
    }

    /**
     * 论坛主题
     */
    public static String getSubjectsById(int fId) {
        return SUBJECT_REQUEST_BASE_URL + "fid=" + fId;
    }

    /**
     * 建议
     */
    public static String doAdviceAction(String title, String content) {
        return ADVICE_BASE + "&title=" + title + "&content=" + content;
    }

    /**
     * 历史搜索
     */
    public static String getHistoryKW(int user_id, int start, int limit) {
        return HIS_KEY_WORDS + String.valueOf(user_id) + "&start=" + String.valueOf(start) + "&limit=" + String.valueOf(limit);
    }

    /**
     * 热门搜索
     */
    public static String getHotKW(int start, int limit) {
        return HOT_KEY_WORDS + String.valueOf(start) + "&limit=" + String.valueOf(limit);
    }

    /**
     * 收藏历年真题
     */
    public static String collectPastExam(String pastExamId, String sId) {
        return COLLECT_PAST_EXAM + "historyId=" + pastExamId + "&sId=" + sId;
    }

    /**
     * 收藏咨询
     */
    public static String collectConsult(String consultId, String sId) {
        return COLLECT_CONSULT + "consultId=" + consultId + "&sId=" + sId;
    }

    /**
     * 取消收藏咨询
     */
    public static String deCollectConsult(String consultId, String sId) {
        return DE_COLLECT_CONSULT + "consultId=" + consultId + "&sId=" + sId;
    }

    /**
     * 知识收藏
     */
    public static String collectOrDecollectKno(int type, String knowId, String sId) {
        return COLLECT_KNOWLEDGE + "type=" + String.valueOf(type) + "&knowId=" + knowId + "&sId=" + sId;
    }

    /**
     * 获取收藏的列表
     */
    public static String getCollectItemList(int type, String sId, int index, int pageNum) {
        String typeBase = null;
        switch (type) {
            case 1:
                typeBase = COLLECT_CON_LIST;
                break;
            case 2:
                typeBase = COLLECT_KNO_LIST;
                break;
            case 3:
                typeBase = COLLECT_EDU_LIST;
                break;
            default:
                break;

        }
        return typeBase + "sId=" + sId + "&index=" + String.valueOf(index) + "&pageNum=" + String.valueOf(pageNum);
    }

    /**
     * 教育平台视频地址
     */
    public static String getEduTrainVideoUrl(String urlInfo) {
        return EDU_STATIC_FILE_URL + urlInfo;
    }

    /**
     * 我的考试
     */
    public static String getMyExams(String sId) {
        return MY_EXAM + "sId=" + sId;
    }

    /**
     * 模拟试题列表（考试类型、时间）
     */
    public static String getMockExamList(String level) {
        StringBuilder builder = new StringBuilder(MOCK_EXAMS_LIST + "level=");
        if (!TextUtils.isEmpty(level)) {
            switch (level) {
                case "1001":
                    builder.append(MockExamsListActivity.MOCK_EXAM_LEVEL_1);
                    break;
                case "1002":
                    builder.append(MockExamsListActivity.MOCK_EXAM_LEVEL_2);
                    break;
                case "1003":
                    builder.append(MockExamsListActivity.MOCK_EXAM_LEVEL_3);
                    break;
                case "1004":
                    builder.append(MockExamsListActivity.MOCK_EXAM_LEVEL_4);
                    break;
                default:
                    break;
            }
        }
        return builder.toString();
    }

    /**
     * 我的学习列表
     */
    public static String getMyMockExamList(String sId, int index, int pageNum) {
        return MOCK_MY_EXAMS_LIST + "sId=" + sId + "&index=" + String.valueOf(index) + "&pageNum=" + String.valueOf(pageNum);
    }

    /**
     * 易错题列表
     */
    public static String getEasyWrongList(int index, int pageNum, String level) {
        return EASY_WRONG_LIST + "index=" + String.valueOf(index) + "&pageNum=" + String.valueOf(pageNum) + "&level=" + URLEncoder.encode(level);
    }

    /**
     * 易错题详情
     */
    public static String getEasyWrongDetail(int bankId) {
        return EASY_WRONG_DETAIL + String.valueOf(bankId);
    }

    /**
     * 培训视频列表【按考试类型分类】
     */
    public static String getVideoTrainList(String level) {
        return EDU_VIDEO_TRAIN_LIST + "level=" + URLEncoder.encode(level);
    }

    /**
     * 生成模拟试题
     */
    public static String getMockTTTInfo(String exaId, String sId, String exaName) {
        return MOCK_EXAM_TTT_INFO + "exaId=" + exaId + "&sId=" + sId + "&exaName=" + exaName;
    }

    /**
     * 一条试题题目
     */
    public static String getMockTTT(String examId, String userMockId, String sId, int index, int pageSize) {
        return MOCK_EXAM_TTT_CONTENT + "exaId=" + examId + "&id=" + userMockId + "&sId=" + sId + "&index=" + String.valueOf(index) + "&pageNum=" + String.valueOf(pageSize) + "&t=app";
    }

    /**
     * 提交一条考试题目
     */
    public static String submitMockTTT(String ans, int topicNo, int cordId) {
        return MOCK_SUBMIT_TTT + "cont=" + URLEncoder.encode(ans) + "&topicNo=" + String.valueOf(topicNo) + "&cordId=" + String.valueOf(cordId);
    }

    /**
     * 提交试卷
     */
    public static String submitMockPaper(String exaId, String userMockId, String sId) {
        return MOCK_SUBMIT_PAPER + "exaId=" + exaId + "&id=" + userMockId + "&sId=" + sId;
    }

    /**
     * 历年真题列表(按level)
     */
    public static String getPastExamListByLevel(int index, int pageNum, String level, String opTime, String searchWhat) {
        StringBuilder builder = new StringBuilder(PAST_EXAMS_LIST + "index=" + String.valueOf(index) + "&pageNum=" + String.valueOf(pageNum));
        if (!TextUtils.isEmpty(level)) {
            switch (level) {
                case "1001":
                    builder.append("&level=").append("一级注册消防工程师");
                    break;
                case "1002":
                    builder.append("&level=").append("二级注册消防工程师");
                    break;
                case "1003":
                    builder.append("&level=").append("初级建(构)筑物消防员");
                    break;
                case "1004":
                    builder.append("&level=").append("中级建(构)筑物消防员");
                    break;
                default:
                    break;

            }
        }
        if (!TextUtils.isEmpty(opTime)) {
            builder.append("&opTime=").append(opTime);
        }
        if (!TextUtils.isEmpty(searchWhat)) {
            builder.append("&key=").append(searchWhat);
        }
        return builder.toString();
    }

    /**
     * 历年真题详情
     */
    public static String getPastExamDetailById(String pstExamId) {
        return PAST_EXAM_DETAIL + pstExamId;
    }

    /**
     * 教育平台pdf地址
     */
    public static String getEduPdf(String pdfUrl) {
        return EDU_BASE + "/" + pdfUrl;
    }

    /**
     * 学习资料
     */
    public static String getStudyDatum(int index, int pageNum) {
        return STUDY_DATUM + "index=" + String.valueOf(index) + "&pageNum=" + String.valueOf(pageNum);
    }

    /**
     * 关注某专家
     */
    public static String followExpert(String sId, int expertId) {
        return FOLLOW_FOLLOW_EXPERT + "exId=" + String.valueOf(expertId) + "&sId=" + sId;
    }

    /**
     * 取消关注某专家
     */
    public static String unfollowExpert(String sId, int expertId) {
        return FOLLOW_UN_FOLLOW_EXPERT + "exId=" + String.valueOf(expertId) + "&sId=" + sId;
    }

    /**
     * 关注的专家列表
     */
    public static String getFollowedExpertList(String sId) {
        return BASE_URL + "/xxf/getXfExpertCare.do?sId=" + sId;
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
        return CHANGE_PSW + loginName + "&old_pwd=" + oldPsw + "&new_pwd=" + newPsw;
    }

    /**
     * 专家列表
     */
    public static String doGetExpertList(int pageSize, int start, String searchWho) {
        if (TextUtils.isEmpty(searchWho)) {
            return EXPERT_LIST + "pagesize=" + String.valueOf(pageSize) + "&index=" + String.valueOf(start);
        } else {
            return EXPERT_LIST + "pagesize=" + String.valueOf(pageSize) + "&index=" + String.valueOf(start) + "&key=" + URLEncoder.encode(searchWho);
        }
    }

    /**
     * 专家详情
     */
    public static String getExpertInfo(int expId) {
        return EXPERT_INFO + String.valueOf(expId);
    }

    /**
     * 最近、热点咨询[不分页]
     */
    public static String getRecentHotConsult(String type) {
        switch (type) {
            case "hot":
                return HOT_CONSULT + "type=" + type + "&ctype=1";
            case "new":
                return RECENT_CONSULT + "type=" + type + "&ctype=1";
            default:
                break;
        }
        return null;
    }

    /**
     * 最近、热点咨询[分页]
     */
    public static String getRecentHotConsultByPage(String type, int start, int pagesize) {
        return RECENT_CONSULT + "type=" + type + "&start=" + String.valueOf(start) + "&pagesize=" + String.valueOf(pagesize) + "&ctype=1";
    }

    /**
     * 咨询的具体内容cid
     */
    public static String getConsultContent(int cid) {
        return CONSULT_CONTENT + String.valueOf(cid);
    }

    /**
     * 知识平台（视频、标准规范、科研文献）
     */
    public static String doGetKnowledgeAction(int knowledgeType, int pageSize, int index, String searchWhat) {
        return KNOWLEDGE_ITEM_BASE + String.valueOf(knowledgeType) + "&index=" + String.valueOf(index) + "&pagesize=" + String.valueOf(pageSize) + "&findText=" + searchWhat;
    }

    /**
     * 咨询分类
     */
    public static String getConsultCato(int... type) {
        StringBuilder builder = new StringBuilder(CONSULT_CATO_BASE);
        for (int i = 0; i < type.length; i++) {
            if (i < type.length - 1) {
                builder.append(String.valueOf(type[i])).append(",");
            } else {
                builder.append(String.valueOf(type[i]));
            }
        }
        return builder.toString();
    }

    /**
     * 分类内容列表
     */
    public static String getTypedCatoList(int start, int... typeCode) {
        StringBuilder builder = new StringBuilder(TYPE_CONTENT_LIST_BASE + "index=" + start + "&keyList=");
        for (int i = 0; i < typeCode.length; i++) {
            if (i == typeCode.length - 1) {
                builder.append(String.valueOf(typeCode[i]));
            } else {
                builder.append(String.valueOf(typeCode[i])).append(",");
            }
        }
        return builder.toString();
    }

    /**
     * 登录
     */
    public static String doSignIn(String admin, String pwd) {
        return SIGN_IN_BASE + URLEncoder.encode(admin) + "&pwd=" + pwd + "&t=app";
    }

    /**
     * 注册
     */
    public static String doSignUp(String loginName, String pwd, String phone, String type) {
        return SIGN_UP_BASE + URLEncoder.encode(loginName) + "&pwd=" + pwd + "&phone=" + phone + "&t=" + type;
    }

    /**
     * 根据sessionID获得消息（所有消息）
     */
    public static String getMyQQMsg(String sessionID) {
        return MY_QQ_INFO + sessionID;
    }

    /**
     * 根据sessionID、qqID获得某QQ会话的未读消息
     */
    public static String getMyUnreadQQMsgUrl(String sessionID, int qq_id, int userId) {
        return QQ_MSG_BASE + sessionID + "&m=getQQUnreadMsg&qq_id=" + qq_id + "&userId=" + String.valueOf(userId);
    }

    /**
     * 根据sessionID、qqID获得某一条QQ的所有消息
     */

    public static String getMyAllQQMsgUrl(int cId) {
        return QQ_CHAT_MSGS + String.valueOf(cId);
    }

    /**
     * 根据sessionID发起咨询（创建一个QQ会话）
     */
    public static String startQQChatUrlGET(String title, String content, int type, int expertId, String sId) {
        return QQ_MSG_BASE + "title=" + title + "&content=" + content + "&type=" + String.valueOf(type) + "&expertId=" + String.valueOf(expertId) + "&sId=" + sId + "&t=app";
    }

    /**
     * 发送消息
     */
    public static String sendQQMsgUrl(String msg, int consultId, int userId) {
        return QQ_SEND_MSG + "msg=" + msg + "&cid=" + String.valueOf(consultId) + "&userId=" + String.valueOf(userId) + "&t=app";
    }

    /**
     * 获取新闻列表
     */
    public static String getNews(String state, int page, int pagesize, String type) {
        return NEWS_BASE + "state=" + state + "&page=" + String.valueOf(page) + "&pagesize=" + String.valueOf(pagesize) + "&type=" + type;
    }

    /**
     * 获取图片新闻
     */
    public static String getImgNews() {
        return BASE_URL + "/hc/fxnew.do?t=app";
    }

    /**
     * 获取公告列表
     */
    public static String getNotices(String state, int page, int pagesize) {
        return NOTICE_BASE + "state=" + state + "&page=" + String.valueOf(page) + "&pagesize=" + String.valueOf(pagesize);
    }

    /**
     * 下载知识平台pdf和视频
     */
    public static String getKnowledgeFileUrl(String knowId) {
        return KNOWLEDGE_FILE_DOWNLOAD_URL + "knowId=" + knowId;
    }

    /**
     * 获取知识平台视频封面\视频
     */
    public static String getKnowledgeStaticFile(String coverId) {
        return KNOWLEDGE_STATIC_FILE_URL + coverId;
    }

    /**
     * 获取一则新闻具体内容
     */
    public static String getNewsById(String newsId) {
        return NEWS_NOTICES_CONTENT_BASE + "type=news" + "&newsId=" + newsId;
    }

    /**
     * 获取一则公告具体内容
     */
    public static String getNoticeById(String noticeId) {
        return NEWS_NOTICES_CONTENT_BASE + "type=notice" + "&newsId=" + noticeId;
    }

    /**
     * 获取图片新闻
     */
    public static String getPicNewsUrl() {
        //// TODO: 2015/10/23
        return null;
    }
}
