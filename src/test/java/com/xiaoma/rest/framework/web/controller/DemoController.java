package com.xiaoma.rest.framework.web.controller;

import com.xiaoma.rest.framework.annotation.RestQuery;
import com.xiaoma.rest.framework.annotation.RestQuerySet;
import com.xiaoma.rest.framework.controller.BaseRestController;
import com.xiaoma.rest.framework.example.model.User;
import com.xiaoma.rest.framework.page.PaginationParameter;
import com.xiaoma.rest.framework.query.GenericQuerySet;
import com.xiaoma.rest.framework.query.QueryType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 测试用的controller
 * 测试自动注入queryset
 * Created by vincent on 16/6/16.
 */
@Controller
@RequestMapping("/test/users/")
public class DemoController extends BaseRestController{

    private static final Class<? extends PaginationParameter> paginationParameterClass = PaginationParameter.class;

    // 定义模型类
    private static final Class<User> modelClass = User.class;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @RestQuery(type = QueryType.LIST)
    public Collection list(
            @RestQuerySet GenericQuerySet querySet
            ) {
        List<User> users  = new LinkedList<>();
        User user = new User();
        user.setName("vincent");
        user.setDescription("test user vincent");
        users.add(user);
        return users;
    }
}
