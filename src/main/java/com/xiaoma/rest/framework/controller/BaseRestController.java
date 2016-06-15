package com.xiaoma.rest.framework.controller;

import com.xiaoma.rest.framework.page.PaginationParameter;
import com.xiaoma.rest.framework.query.GenericQuerySet;
import com.xiaoma.rest.framework.query.QuerySet;

/**
 * Created by vincent on 16/6/14.
 */
public class BaseRestController {

    // 定义controller的queryset类
    private Class<? extends QuerySet> querySetClass = GenericQuerySet.class;

    // 定义分页配置参数类
    private Class<? extends PaginationParameter> paginationParameterClass = PaginationParameter.class;

    private Class<?> modelClass = Object.class;

}
