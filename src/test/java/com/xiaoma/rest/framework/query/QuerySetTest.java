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
import static org.junit.Assert.*;

public class QuerySetTest {

    private static final Logger logger = LoggerFactory.getLogger(QuerySetTest.class);

    private QuerySet qs;

    private MultiValueMap<String, String[]> demoParams;

    private User demoUser = new User();


    @Before
    public void before() throws Exception {
        logger.debug("QuerySetTest start");
        LinkedHashMap<String, LinkedList<String>> hmap = new LinkedHashMap<>();
        // add name value
        LinkedList<String> nameList = new LinkedList<>();
        String nameValue = "foo";
        nameList.add(nameValue);
        hmap.put("name", nameList);
        // add description value
        LinkedList<String> descList = new LinkedList<>();
        String descValue = "foo bar";
        descList.add(descValue);
        hmap.put("description", descList);
        // add status value
        LinkedList<String> statusList = new LinkedList<>();
        String statusValue = "1";
        statusList.add(statusValue);
        hmap.put("status", statusList);

        // add page value
        LinkedList<String> pageList = new LinkedList<>();
        String pageValue = "1";
        pageList.add(pageValue);
        hmap.put("page", pageList);

        // add page value
        LinkedList<String> heightList = new LinkedList<>();
        String heightValue = "1.74";
        pageList.add(heightValue);
        hmap.put("height", heightList);

        // add page value
        LinkedList<String> distanceList = new LinkedList<>();
        String distanceValue = "181729371273123";
        pageList.add(distanceValue);
        hmap.put("distance", distanceList);
        demoParams = new LinkedMultiValueMap(hmap);


        this.demoUser.setName(nameValue);
        this.demoUser.setStatus(Integer.parseInt(statusValue));
        this.demoUser.setDescription(descValue);
        this.demoUser.setHeight(Float.parseFloat(heightValue));
        this.demoUser.setDistance(Long.parseLong(distanceValue));


    }


    @After
    public void after() throws Exception {
        logger.debug("QuerySetTest stop");
    }

    @Test
    public void indexWithLogin() throws Exception {
        qs = new QuerySet(this.demoParams, User.class);
        assertEquals(this.demoUser, qs.getModelObject());
    }
}
