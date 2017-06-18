package edu.fjnu.cse.lostandfound.common;

/**
 * @Author:  HMH
 * @Date: 2017/6/15
 * @Description: 错误码列表
 */
public class AppExCode {

    public static final String testCode = "001";

    //用户码
    public static final String U_COMMON_ERROR = "U001";//用户通用错误码
    public static final String U_NOT_EXISTS = "U002";//用户不存在
    public static final String U_ERROR_PWD = "U003";//密码错误
    public static final String U_IS_EXISTS = "U004";//用户已经存在
    public static final String U_NOT_AVAILABLE = "U005";//用户已禁用
    public static final String U_NOT_FIND_MONITORED="U006";//无法找到被监护人，和推送成败相关
    public static final String U_NOT_FIND_MONITOR="U007";//无法找到被监护人，和推送成败相关

    //accesstoken错误码
    public static final String A_CREATE_ERROR = "A001";//accesstoken创建错误
    public static final String A_UPDATE_ERROR = "A002";//accesstoken更新错误
    public static final String A_GET_UNO_ERROR = "A001";//accesstoken获取用户号错误
    public static final String ACCESSTOKEN_TIMEOUT = "A999";//accesstoken过期

    //客户端系统配置错误码
    public static final String C_COMMON_ERROR = "C001";
    public static final String C_NOT_CONFIG = "C002";

    //位置信息
    public static final String P_MONITORED_NOT_EXISTS = "P001";

    //活动
    public static final String AC_PARA_NULL = "AC001";
    public static final String AC_NOT_FOUND = "AC002";//找不到对应的活动
    public static final String AC_DELETE_ERROR = "AC003";//找不到对应的活动

    //联系人
    public static final String CON_PARA_NULL = "CON001";
    public static final String CON_CONTACTS_NOT_EXISTS = "CON002";
    public static final String NO_BELONG_U = "CON003";

    //推送失败
    public static final String PUSH_ERROR = "PUSHERROR001";


    public static final String MONITORED_AND_MONITOR_ERROR = "mm001";

    public static final String NOT_EXIST_POSITION = "p001";

}
