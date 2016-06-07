package com.xiaoma.rest.framework.page;

import com.xiaoma.rest.framework.exception.PaginatorNotInitializeException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

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
        Paginator paginator = new Paginator();
        paginator.calculatePageUrl();
    }

    @Test
    public void calculatePageUrl() throws PaginatorNotInitializeException {
        String requestUrl = "http://localhost:8080/test/api/demo/";
        String queryUri = "?page=1&page_size=200&name=vincent";
        HttpServletRequest request = new MockHttpServletRequest("get", requestUrl + queryUri);
        String sss = request.getQueryString();
//        String requestUrl = request.getRequestURL().toString();
//        String queryUrl = request.getQueryString();
        ServletUriComponentsBuilder ucb = ServletUriComponentsBuilder.fromRequest(request);

        Paginator paginator = new Paginator();
        paginator.setCurrentUrl(ucb.build().toUriString());
        paginator.setPageSize(20);
        paginator.setCurrent(1);
        paginator.setTotal(1001);
        paginator.calculatePageUrl();
        assertEquals("http://localhost:8080/test/api/demo/?page=2&page_size=200&name=vincent", paginator.getNextUrl());
        assertEquals("", paginator.getPreviousUrl());
    }
}
