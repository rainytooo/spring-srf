package com.xiaoma.rest.framework.page;

/**
 * 分页参数的标准类
 * @author <a href="mailto:ergal@163.com">vincent.omg</a>
 * @version 1.0
 * @date 16/5/30
 * @since 1.0
 */
public class Pagination {
    private final static int PAGE_SIZE = 20;

    private final static String PAGE_SIZE_PARAM_NAME = "page_size";

    private final static int MAX_PAGE_SIZE = 1000;

    public static int getPageSize(){
        return PAGE_SIZE;
    }

    public static int getMaxPageSize(){
        return MAX_PAGE_SIZE;
    }

    public static String getPageSizeParamName(){
        return PAGE_SIZE_PARAM_NAME;
    }
}
