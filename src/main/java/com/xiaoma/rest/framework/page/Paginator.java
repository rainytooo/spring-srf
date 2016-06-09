package com.xiaoma.rest.framework.page;

import com.xiaoma.rest.framework.exception.PaginatorNotInitializeException;
import com.xiaoma.rest.framework.exception.PaginatorllegalITotalCountException;
import org.apache.http.client.utils.URIBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * 分页包装类
 * @author <a href="mailto:ergal@163.com">vincent.omg</a>
 * @version 1.0
 * @date 16/6/6
 * @since 1.0
 */
public class Paginator {

    // 请求
    private HttpServletRequest request;

    //分页参数配置器
    private PaginationParameter paginationParameter;

    // 总数
    private Integer total;

    // 每页大小
    private Integer pageSize;

    // 当前页
    private Integer currentPage;

    // 下一页
    private Integer nextPage;

    // 上一页
    private Integer previousPage;

    // 最后页
    private Integer lastPage;

    // 第一页
    private Integer firstPage;

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
     * 初始化,设置好 currentPage, pageSize, currentUrl
     * @param request
     */
    public Paginator(HttpServletRequest request, PaginationParameter paginationParameter) {
        this.request = request;
        this.paginationParameter = paginationParameter;
        // page和pageSize参数拿出来
        this.initPage(request);
        this.initPageSize(request);
        String requestUrl = request.getRequestURL().toString();
        String queryUri = request.getQueryString();

        this.setCurrentUrl(requestUrl + "?" + queryUri);
        // ServletUriComponentsBuilder ucb = ServletUriComponentsBuilder.fromRequest(request);

    }

    public void initPage(HttpServletRequest request){
        String pageStr = request.getParameter(this.paginationParameter.getPageParamName());
        if(pageStr != null){
            this.currentPage = Integer.parseInt(pageStr);
        } else {
            this.currentPage = 1;
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
    public void build() throws PaginatorNotInitializeException, URISyntaxException, MalformedURLException {
        if(this.total == null || currentPage == null || pageSize == null) {
            throw new PaginatorNotInitializeException();
        }
        // 计算总页数
        this.setTotalPage(this.total / this.pageSize + 1);
        // 设置第一页
        this.setFirstPage(1);

        // 计算下一页 上一页
        Integer nextPageNum = this.currentPage.equals(this.totalPage) ? null : this.currentPage + 1;
        Integer previousPageNum = this.currentPage.equals(new Integer(1)) ? null : this.currentPage - 1;

        String pageValue = this.request.getParameter(this.paginationParameter.getPageParamName());
        this.buildNextPage(nextPageNum, pageValue);
        this.buildPreviousPage(previousPageNum, pageValue);

        // 设置上一页
//        if(pageValue != null){
//            urlb.setParameter(this.paginationParameter.getPageParamName(), String.valueOf(previousPageNum));
//        } else {
//            urlb.addParameter(this.paginationParameter.getPageParamName(), String.valueOf(previousPageNum));
//        }
//        this.setPreviousPage(previousPageNum);
//        this.setPreviousUrl(urlb.build().toURL().toString());

    }

    private void buildNextPage(Integer nextPageNum, String pageValue) throws URISyntaxException, MalformedURLException {
        // 构建一个url
        URIBuilder urlb = new URIBuilder(this.getCurrentUrl());
        if(nextPageNum != null) {
            // 设置下一页
            if (pageValue != null) {
                urlb.setParameter(this.paginationParameter.getPageParamName(), String.valueOf(nextPageNum));
            } else {
                urlb.addParameter(this.paginationParameter.getPageParamName(), String.valueOf(nextPageNum));
            }
            this.setNextPage(nextPageNum);
            this.setNextUrl(urlb.build().toURL().toString());
        }
    }

    private void buildPreviousPage(Integer previousPageNum, String pageValue) throws URISyntaxException, MalformedURLException {
        // 构建一个url
        URIBuilder urlb = new URIBuilder(this.getCurrentUrl());
        if(previousPageNum != null) {
            // 设置下一页
            if (pageValue != null) {
                urlb.setParameter(this.paginationParameter.getPageParamName(), String.valueOf(previousPageNum));
            } else {
                urlb.addParameter(this.paginationParameter.getPageParamName(), String.valueOf(previousPageNum));
            }
            this.setPreviousPage(previousPageNum);
            this.setPreviousUrl(urlb.build().toURL().toString());
        }
    }

    /**
     * 完成查询处理以后重新build分页对象
     * 包含下一页和上一页的url
     * @param totalCount
     */
    public void build(int totalCount) throws PaginatorNotInitializeException, PaginatorllegalITotalCountException, URISyntaxException, MalformedURLException {
        if (totalCount < 0) {
            throw new PaginatorllegalITotalCountException();
        }
        this.setTotal(totalCount);
        this.build();
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

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
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

    public PaginationParameter getPaginationParameter() {
        return paginationParameter;
    }

    public void setPaginationParameter(PaginationParameter paginationParameter) {
        this.paginationParameter = paginationParameter;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public Integer getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(Integer previousPage) {
        this.previousPage = previousPage;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

    public Integer getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(Integer firstPage) {
        this.firstPage = firstPage;
    }
}
