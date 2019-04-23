package com.jin.web.common.constant;

/**
 * @author xun.wang
 * @email wangxun
 * @date 2019/4/23 11:48
 */
public class Consts {
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    public final static String TIME_PATTERN = "HH:mm:ss";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_TIME_PATTERNTWO = "yyyy/MM/dd HH:mm";

    /********************** Picture Format ***********************/
    public static final String PIC_FORMAT_JPG = "jpg";
    public static final String PIC_FORMAT_PNG = "png";

    /********************** Code Format & Response ***********************/

    public static final String SESSION_USER = "user";
    public static final String RESPONSE_STATUS = "status";
    public static final String RESPONSE_STATUS_RESULT = "result";
    public static final String RESPONSE_ERROR_MESSAGE = "errorMessage";
    public static final String RESPONSE_MESSAGE = "message";
    public static final String RESPONSE_STRING_SUCCESS = "success";
    public static final String RESPONSE_STRING_FAIL = "fail";
    public static final String RESPONSE_STRING_INVALID = "invalid";


    /************************** I18N **************************/

    /**
     * API Request语言参数名
     */
    public static final String LANGUAGE = "_language";

    /**
     * 默认语言
     */
    public static final String LANGUAGE_DEFAULT = "zh_CN";

    public static final String CODING_UTF8 = "UTF-8";

    public static final String CODING_GBK = "gbk";
}
