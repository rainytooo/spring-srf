package com.xiaoma.rest.framework.page;

/**
 * 分页包装类
 * @author <a href="mailto:ergal@163.com">vincent.omg</a>
 * @version 1.0
 * @date 16/6/6
 * @since 1.0
 */
public class Paginator {

    // 总数
    private int total;

    // 每页大小
    private int pageSize;

    // 当前页
    private int current;

    // 总页数
    private int totalPage;

    // 查询url 不包含页码
    private String queryUrl;

    // 当前页url
    private String currentUrl;

    // 上一页url
    private String previousUrl;

    // 下一页url
    private String nextUrl;
}
