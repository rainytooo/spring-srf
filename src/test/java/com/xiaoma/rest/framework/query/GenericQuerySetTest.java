package com.xiaoma.rest.framework.query;


import com.xiaoma.rest.framework.example.model.Role;
import com.xiaoma.rest.framework.example.model.User;
import org.joda.time.LocalDateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import static org.junit.Assert.*;

public class GenericQuerySetTest {

    private static final Logger logger = LoggerFactory.getLogger(GenericQuerySetTest.class);

    // 模拟参数对象
    private MultiValueMap<String, String[]> demoParams;

    private User demoUser = new User();


    @Before
    public void before() throws Exception {


        logger.debug("GenericQuerySetTest start");
        LinkedHashMap<String, LinkedList<String>> hmap = new LinkedHashMap<>();
        // add name value
        LinkedList<String> nameList = new LinkedList<>();
        String nameValue = "foo";
        nameList.add(nameValue);
        // 再加一个值
        // nameList.add("bar");
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

        // add height value
        LinkedList<String> heightList = new LinkedList<>();
        String heightValue = "1.74";
        heightList.add(heightValue);
        hmap.put("height", heightList);

        // add isMale value
        LinkedList<String> isMaleList = new LinkedList<>();
        String isMaleValue = "true";
        isMaleList.add(isMaleValue);
        hmap.put("is_male", isMaleList);

        // add distance value
        LinkedList<String> distanceList = new LinkedList<>();
        String distanceValue = "181729371273123";
        distanceList.add(distanceValue);
        hmap.put("distance", distanceList);

        // add createDate value
        LinkedList<String> createDateList = new LinkedList<>();
        String createDateValue = "1465180995";
        createDateList.add(createDateValue);
        hmap.put("create_date", createDateList);


        // add Role value
        LinkedList<String> roleList = new LinkedList<>();
        String roleValue = "2";
        roleList.add(roleValue);
        hmap.put("role", roleList);

        this.demoParams = new LinkedMultiValueMap(hmap);

        Role role = new Role();
        role.setId(2);
        // role.setName("admin");
        // role.setDescription("Administrator");

        this.demoUser.setName(nameValue);
        this.demoUser.setStatus(Integer.parseInt(statusValue));
        this.demoUser.setDescription(descValue);
        this.demoUser.setHeight(Float.parseFloat(heightValue));
        this.demoUser.setDistance(Long.parseLong(distanceValue));
        this.demoUser.setCreateDate(new LocalDateTime(Long.parseLong(createDateValue) * 1000));
        this.demoUser.setMale(true);
        this.demoUser.setRole(role);


    }


    @After
    public void after() throws Exception {
        logger.debug("GenericQuerySetTest stop");
    }

    @Test
    public void resetModelAttribute() throws Exception {
        HttpServletRequest request = new MockHttpServletRequest();
        GenericQuerySet qs = new GenericQuerySet(this.demoParams, User.class, request);
        User queryObject = (User)qs.getQueryModel();
        // assertTrue( this.demoUser.equals(qs.getModelObject()) );
        // assertEquals(this.demoUser, qs.getModelObject());
        assertNotNull(queryObject);
        assertEquals(this.demoUser.getName(), queryObject.getName());
        assertEquals(this.demoUser.getDescription(), queryObject.getDescription());
        assertEquals(this.demoUser.getDistance(), queryObject.getDistance());
        assertEquals(this.demoUser.getStatus(), queryObject.getStatus());
        assertEquals(this.demoUser.getHeight(), queryObject.getHeight(), 0.01);
        assertEquals(this.demoUser.isMale(), queryObject.isMale());
        assertEquals(this.demoUser.getCreateDate(), queryObject.getCreateDate());
        assertEquals(this.demoUser.getRole().getId(), queryObject.getRole().getId());
    }


    @Test
    public void restParam(){
        logger.debug("restParam test start");
        LinkedHashMap<String, LinkedList<String>> hmap = new LinkedHashMap<>();


        LinkedList<String> nameList = new LinkedList<>();
        String nameValue = "foo";
        nameList.add(nameValue);
        hmap.put("name_stw", nameList);

        // add createDate value
        LinkedList<String> createDateList = new LinkedList<>();
        String createDateValue = "1465180995";
        createDateList.add(createDateValue);
        hmap.put("create_date_gt", createDateList);


        // 查询参数
        MultiValueMap<String, String[]> params = new LinkedMultiValueMap(hmap);

        HttpServletRequest request = new MockHttpServletRequest();

        GenericQuerySet qs = new GenericQuerySet(params, User.class, request);

        QueryParameter qp = qs.getQueryParameter("name");
        assertEquals(qp.getOperation(), QueryOperation.STARTWITH);
        assertEquals(qp.getParams().size(), 1);
        assertEquals(qp.getParams().get(0), nameValue);

        QueryParameter qpCreateDate = qs.getQueryParameter("createDate");
        assertEquals(qpCreateDate.getOperation(), QueryOperation.GT);
        assertEquals(qpCreateDate.getParams().size(), 1);
        assertEquals(qpCreateDate.getParams().get(0), createDateValue);

        logger.debug("restParam test end");
    }

    @Test
    public void pageSizeAndPage(){
        logger.debug("pageSize test start");
        LinkedHashMap<String, LinkedList<String>> hmap = new LinkedHashMap<>();


        LinkedList<String> pageSizeList = new LinkedList<>();
        String pageSizeValue = "100";
        pageSizeList.add(pageSizeValue);
        hmap.put("page_size", pageSizeList);

        LinkedList<String> pageList = new LinkedList<>();
        String pageValue = "2";
        pageList.add(pageValue);
        hmap.put("page", pageList);

        HttpServletRequest request = new MockHttpServletRequest();
        // 查询参数
        MultiValueMap<String, String[]> params = new LinkedMultiValueMap(hmap);
        GenericQuerySet qs = new GenericQuerySet(params, User.class, request);


        logger.debug("pageSize test end");
    }
}
