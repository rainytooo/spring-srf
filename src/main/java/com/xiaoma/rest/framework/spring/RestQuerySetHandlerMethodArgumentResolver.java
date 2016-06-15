package com.xiaoma.rest.framework.spring;

import com.xiaoma.rest.framework.annotation.RestQuerySet;
import com.xiaoma.rest.framework.controller.BaseRestController;
import com.xiaoma.rest.framework.query.QuerySet;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 用于在controller里直接注入一个标准的查询对象QuerySet
 * Created by vincent on 16/6/15.
 */
public class RestQuerySetHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> klass = parameter.getParameterType();
        if (klass.isAssignableFrom(QuerySet.class)) {
            Annotation[] as = parameter.getParameterAnnotations();
            for (Annotation a : as) {
                if (a.annotationType() == RestQuerySet.class) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 在这里产生QuerySet的实例,给客户代码使用
     * methodParameter 里拿出controller的类,并得知基本的几个参数,分页器,过滤参数,序列化器,排序参数等等
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        // 先获取到QuerySet的类
        Class<? extends BaseRestController> t = (Class<? extends BaseRestController>) methodParameter.getContainingClass();
        Class s = methodParameter.getDeclaringClass();


        return null;
    }

    private boolean isNotSet(String value) {
        return value == null;
    }
}