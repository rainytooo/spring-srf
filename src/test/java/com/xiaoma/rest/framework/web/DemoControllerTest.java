package com.xiaoma.rest.framework.web;

/**
 * Created by vincent on 16/6/16.
 */

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import com.xiaoma.rest.framework.spring.RestQuerySetHandlerMethodArgumentResolver;
import com.xiaoma.rest.framework.web.controller.DemoController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by vincent on 2014/6/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:spring/mvc-core-config.xml"})
//@ActiveProfiles("test")
public class DemoControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(DemoControllerTest.class);

    @Autowired
    private DemoController demoController;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    //    @Before
//    public void before(){
//        logger.debug("test setup");
//        this.mockMvc =
//                webAppContextSetup(this.webApplicationContext)
//                .build();
//    }
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                        .standaloneSetup(demoController)
                        .setCustomArgumentResolvers(new RestQuerySetHandlerMethodArgumentResolver())
                        .build();
    }

    @Test
    public void testQuerySet() throws Exception {
        ResultActions actions = mockMvc.perform(
                get("/test/users/?page=2&name=vincent&status=1&height=1.88&is_male=true")
                        .accept(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$", hasSize(1)));
        actions.andExpect(content().contentType("application/json;charset=UTF-8"));
    }
}
