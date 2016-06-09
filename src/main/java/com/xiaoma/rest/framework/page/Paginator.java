package com.xiaoma.rest.framework.page;

import com.xiaoma.rest.framework.exception.PaginatorNotInitializeException;

import javax.servlet.http.HttpServletRequest;

/**
 * 分页包装类
 * @author <a href="mailto:ergal@163.com">vincent.omg</a>
 * @version 1.0
 * @date 16/6/6
 * @since 1.0
 */
public class Paginator {

    //分页参数配置器
    private PaginationParameter paginationParameter;

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
     * 初始化,设置好 current, pageSize, currentUrl
     * @param request
     */
    public Paginator(HttpServletRequest request, PaginationParameter paginationParameter) {
        this.paginationParameter = paginationParameter;
        // page和pageSize参数拿出来
        this.initPage(request);
        this.initPageSize(request);
        String page = request.getParameter("page");
        String requestUrl = request.getRequestURL().toString();
        String queryUri = request.getQueryString();

        this.setCurrentUrl(requestUrl + "?" + queryUri);
        // ServletUriComponentsBuilder ucb = ServletUriComponentsBuilder.fromRequest(request);

    }

    public void initPage(HttpServletRequest request){
        String pageStr = request.getParameter(this.paginationParameter.getPageParamName());
        if(pageStr != null){
            this.current = Integer.parseInt(pageStr);
        } else {
            this.current = 1;
        }

    }

    public void initPageSize(HttpServletRequest request){
        String pageSizeStr = request.getParameter(this.paginationParameter.getPageSizeParamName());
        if(pageSizeStr != null){
            int pageSize = Integer.parseInt(pageSizeStr);
            this.pageSize = pageSize > this.paginationParameter.getMaxPageSize() ? this.paginationParameter.getMaxPageSize() : pageSize;
        } else {
            this.pageSize = this.paginationParameter.getMaxPageSize();
        }

    }


    /**
     * 如果设置了totalCount,直接build next
     */
    public void build(){

    }

    /**
     * 完成查询处理以后重新build分页对象
     * @param totalCount
     */
    public void build(int totalCount) throws PaginatorNotInitializeException {
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
