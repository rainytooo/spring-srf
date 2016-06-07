package com.xiaoma.rest.framework.page;

import com.xiaoma.rest.framework.exception.PaginatorNotInitializeException;

/**
 * 分页包装类
 * @author <a href="mailto:ergal@163.com">vincent.omg</a>
 * @version 1.0
 * @date 16/6/6
 * @since 1.0
 */
public class Paginator {

    // 总数
    private Integer total;

    // 每页大小
    private Integer pageSize;

    // 当前页
    private Integer current;

    // 总页数
    private Integer totalPage;

    // 查询url 不包含页码
    private String queryUrl;

    // 当前页url
    private String currentUrl;

    // 上一页url
    private String previousUrl;

    // 下一页url
    private String nextUrl;


    /**
     * 计算所有分页的url
     */
    public void calculatePageUrl() throws PaginatorNotInitializeException {
        if(this.total == null || current == null || pageSize == null) {
            throw new PaginatorNotInitializeException();
        }

    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public String getQueryUrl() {
        return queryUrl;
    }

    public void setQueryUrl(String queryUrl) {
        this.queryUrl = queryUrl;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    public String getPreviousUrl() {
        return previousUrl;
    }

    public void setPreviousUrl(String previousUrl) {
        this.previousUrl = previousUrl;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }


}
