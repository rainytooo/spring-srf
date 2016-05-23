package com.xiaoma.rest.framework.query;


import com.xiaoma.rest.framework.example.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class QuerySetTest {

    private static final Logger logger = LoggerFactory.getLogger(QuerySetTest.class);

    private QuerySet qs;

    private MultiValueMap<String, String[]> demoParams;


    @Before
    public void before() throws Exception {
        logger.debug("QuerySetTest start");
        LinkedHashMap<String, LinkedList<String>> hmap = new LinkedHashMap<String, LinkedList<String>>();
        LinkedList<String> key1list = new LinkedList<>();
        String v1 = "value1";
        key1list.add(v1);
        hmap.put("name", key1list);

        demoParams = new LinkedMultiValueMap(hmap);
    }


    @After
    public void after() throws Exception {
        logger.debug("QuerySetTest stop");
    }

    @Test
    public void indexWithLogin() throws Exception {
        qs = new QuerySet(this.demoParams, User.class);
    }
}
