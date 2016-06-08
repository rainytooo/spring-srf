package com.xiaoma.rest.framework.page;

import com.google.common.base.Splitter;
import com.xiaoma.rest.framework.exception.PaginatorNotInitializeException;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:ergal@163.com">vincent.omg</a>
 * @version 1.0
 * @date 2016/6/7
 * @since 1.0
 */
public class PaginatorTest {

    private static final Logger logger = LoggerFactory.getLogger(PaginatorTest.class);

    @Before
    public void before() throws Exception {
        logger.debug("PaginatorTest start");

    }

    @After
    public void after() throws Exception {
        logger.debug("PaginatorTest stop");
    }

    @Test(expected = PaginatorNotInitializeException.class)
    public void calculatePageUrlNotInit() throws PaginatorNotInitializeException {
        HttpServletRequest mockRequest = new MockHttpServletRequest("get", "");
        Paginator paginator = new Paginator(mockRequest);
        paginator.calculatePageUrl();
    }

    @Test
    public void calculatePageUrl() throws PaginatorNotInitializeException, MalformedURLException, URISyntaxException {
        int currentNum = 1;
        int totalNum = 1001;
        int pageSize = 20;
        // 请求
        String requestUrl = "http://localhost:8080/test/api/demo/";
        // 参数
        String queryUri = "?page="+currentNum+"&page_size=200&name=vincent&name=tom";

        String currentUrl = requestUrl + queryUri;
        // 模拟请求
        HttpServletRequest mockRequest = new MockHttpServletRequest("get", requestUrl + queryUri);


        // 创建Paginator对象
        Paginator paginator = new Paginator(mockRequest);

        paginator.setTotal(totalNum);
        // 设置完总数以后可以计算
        paginator.calculatePageUrl();


        // 开始验证测试

        String nextUrl = paginator.getNextUrl();

        // 比较计算完了以后的值,url是否符合, 页码
        // URL url = new URL(requestUrl + queryUri);
        //  String xxx = url.getQuery();
        List<NameValuePair> params = URLEncodedUtils.parse(new URI(nextUrl), "UTF-8");
        for(NameValuePair pair : params) {
            if (pair.getName() == "page"){
                assertEquals(2, Integer.parseInt(pair.getValue()));
            }
        }
        String nextQueryUri = "?page=" + currentNum + 1 + "&page_size=200&name=vincent&name=tom";
        assertEquals(requestUrl + queryUri, nextUrl);
        assertEquals("", paginator.getPreviousUrl());
        // google Guava
        // final Map<String, String> map = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(xxx);
    }
}
