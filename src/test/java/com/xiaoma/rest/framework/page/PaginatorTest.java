package com.xiaoma.rest.framework.page;

import com.xiaoma.rest.framework.exception.PaginatorNotInitializeException;
import com.xiaoma.rest.framework.exception.PaginatorllegalITotalCountException;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:ergal@163.com">vincent.omg</a>
 * @version 1.0
 * @date 2016/6/7
 * @since 1.0
 */
public class PaginatorTest {

    private static final Logger logger = LoggerFactory.getLogger(PaginatorTest.class);

    private String requestUrl;

    private String queryUrl;

    private MockHttpServletRequest mockRequest;

    private PaginationParameter paginationParameter;


    @Before
    public void before() throws Exception {
        logger.debug("PaginatorTest start");
        this.paginationParameter = new PaginationParameter();
        int currentNum = 1;
        int totalNum = 1001;
        int pageSize = 20;
        // 请求
        this.requestUrl = "/test/api/demo/";
        // 参数
        this.queryUrl = "page=" + currentNum + "&page_size=200&name=vincent&name=tom";
        // this.mockQueryUrl = requestUrl + queryUri;
        // 模拟请求
        this.mockRequest = new MockHttpServletRequest("get", this.requestUrl);
        this.mockRequest.setQueryString(this.queryUrl);
        this.mockRequest.setServerName("localhost");
        this.mockRequest.setServerPort(8080);
        // this.mockRequest.setRequestURI(queryUri);
        this.mockRequest.setParameter("page", "1");
        this.mockRequest.setParameter("name", new String[]{"vincent", "tom"});
        this.mockRequest.setParameter("page_size", "20");

    }

    @After
    public void after() throws Exception {
        logger.debug("PaginatorTest stop");
    }

    @Test(expected = PaginatorNotInitializeException.class)
    public void buildInit() throws PaginatorNotInitializeException, PaginatorllegalITotalCountException, MalformedURLException, URISyntaxException {
        HttpServletRequest nullMockRequest = new MockHttpServletRequest("get", "");
        Paginator paginator = new Paginator(nullMockRequest, this.paginationParameter);
        paginator.build(200);
    }

    /**
     * 测试通过构造函数初始化
     * 初始化完成以后,应该已经设置好基本参数,如页码,分页大小,链接等等
     */
    @Test
    public void newPaginator() {
        String xxx = this.mockRequest.getParameter("page");

        Paginator paginator = new Paginator(this.mockRequest, this.paginationParameter);
        assertEquals(Integer.valueOf(1), paginator.getCurrentPage());
        assertEquals(Integer.valueOf(20), paginator.getPageSize());
        logger.debug(this.mockRequest.getRequestURL().toString());
        logger.debug(this.mockRequest.getQueryString());
        assertEquals("http://localhost:8080/test/api/demo/", this.mockRequest.getRequestURL().toString());
        assertEquals(this.queryUrl, this.mockRequest.getQueryString());
        assertEquals("http://localhost:8080/test/api/demo/" + "?" + this.queryUrl,
                paginator.getCurrentUrl());

        // 不设置page_size, 默认参数
        // 模拟请求
        this.mockRequest = new MockHttpServletRequest("get", this.requestUrl);
        this.mockRequest.setQueryString(this.queryUrl);
        this.mockRequest.setServerName("localhost");
        this.mockRequest.setServerPort(8080);
        this.mockRequest.setParameter("page", "1");
        this.mockRequest.setParameter("name", new String[]{"vincent", "tom"});

        paginator = new Paginator(this.mockRequest, this.paginationParameter);

        assertEquals(Integer.valueOf(1), paginator.getCurrentPage());
        assertEquals(Integer.valueOf(200), paginator.getPageSize());

        // 设置page_size大于默认参数
        // 模拟请求
        this.mockRequest = new MockHttpServletRequest("get", this.requestUrl);
        this.mockRequest.setQueryString(this.queryUrl);
        this.mockRequest.setServerName("localhost");
        this.mockRequest.setServerPort(8080);
        this.mockRequest.setParameter("page", "1");
        this.mockRequest.setParameter("page_size", "201");
        this.mockRequest.setParameter("name", new String[]{"vincent", "tom"});

        paginator = new Paginator(this.mockRequest, this.paginationParameter);

        assertEquals(Integer.valueOf(1), paginator.getCurrentPage());
        assertEquals(Integer.valueOf(200), paginator.getPageSize());

        // 设置page_size等于默认参数
        // 模拟请求
        this.mockRequest = new MockHttpServletRequest("get", this.requestUrl);
        this.mockRequest.setQueryString(this.queryUrl);
        this.mockRequest.setServerName("localhost");
        this.mockRequest.setServerPort(8080);
        this.mockRequest.setParameter("page", "1");
        this.mockRequest.setParameter("page_size", "200");
        this.mockRequest.setParameter("name", new String[]{"vincent", "tom"});

        paginator = new Paginator(this.mockRequest, this.paginationParameter);

        assertEquals(Integer.valueOf(1), paginator.getCurrentPage());
        assertEquals(Integer.valueOf(200), paginator.getPageSize());

        // 设置page_size小于默认参数
        // 模拟请求
        this.mockRequest = new MockHttpServletRequest("get", this.requestUrl);
        this.mockRequest.setQueryString(this.queryUrl);
        this.mockRequest.setServerName("localhost");
        this.mockRequest.setServerPort(8080);
        this.mockRequest.setParameter("page", "1");
        this.mockRequest.setParameter("page_size", "199");
        this.mockRequest.setParameter("name", new String[]{"vincent", "tom"});

        paginator = new Paginator(this.mockRequest, this.paginationParameter);

        assertEquals(Integer.valueOf(1), paginator.getCurrentPage());
        assertEquals(Integer.valueOf(199), paginator.getPageSize());

        // 不设置page
        // 模拟请求
        this.mockRequest = new MockHttpServletRequest("get", this.requestUrl);
        this.mockRequest.setQueryString(this.queryUrl);
        this.mockRequest.setServerName("localhost");
        this.mockRequest.setServerPort(8080);
        // this.mockRequest.setParameter("page", "1");
        this.mockRequest.setParameter("page_size", "199");
        this.mockRequest.setParameter("name", new String[]{"vincent", "tom"});

        paginator = new Paginator(this.mockRequest, this.paginationParameter);

        assertEquals(Integer.valueOf(1), paginator.getCurrentPage());
        assertEquals(Integer.valueOf(199), paginator.getPageSize());



    }


    @Test
    public void build() throws PaginatorNotInitializeException, MalformedURLException, URISyntaxException, PaginatorllegalITotalCountException {
        // 模拟请求
        this.mockRequest = new MockHttpServletRequest("get", this.requestUrl);
        this.mockRequest.setQueryString(this.queryUrl);
        this.mockRequest.setServerName("localhost");
        this.mockRequest.setServerPort(8080);
        this.mockRequest.setParameter("page", "1");
        this.mockRequest.setParameter("page_size", "200");
        this.mockRequest.setParameter("name", new String[]{"vincent", "tom"});

        Paginator paginator = new Paginator(this.mockRequest, this.paginationParameter);
        paginator.build(300);
        assertEquals(Integer.valueOf(1), paginator.getCurrentPage());
        assertEquals(Integer.valueOf(200), paginator.getPageSize());

        assertEquals(null, paginator.getPreviousUrl());
        assertEquals(Integer.valueOf(200), paginator.getPageSize());

        // 获取下一页的url, 得到的参数不变,页码变成2
        String nextUrl = paginator.getNextUrl();

        URIBuilder urlb = new URIBuilder(nextUrl);
        List<NameValuePair> params = urlb.getQueryParams();

        // urlb.setParameter("page", "2");
        // List<NameValuePair> params = URLEncodedUtils.parse(new URI(nextUrl), "UTF-8");
        for(NameValuePair pair : params) {
            logger.debug(pair.getValue());
            if (pair.getName() == "page"){
                assertEquals(2, Integer.parseInt(pair.getValue()));
            }
        }
        // 转换键值对
        // Map<String, String> result2 = params.stream().collect(
        //                              Collectors.toMap(x -> x.getName(), x -> x.getValue()));


    }
}
