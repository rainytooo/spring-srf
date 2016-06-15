package com.xiaoma.rest.framework.spring;

import com.xiaoma.rest.framework.annotation.RestQuerySet;
import com.xiaoma.rest.framework.controller.BaseRestController;
import com.xiaoma.rest.framework.page.PaginationParameter;
import com.xiaoma.rest.framework.query.QuerySet;
import org.springframework.core.MethodParameter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

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
     *
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

        // 组装spring的LinkedMultiValueMap
        Map<String, String[]> parameterMap = nativeWebRequest.getParameterMap();

        MultiValueMap<String, String> listMap = new LinkedMultiValueMap<>(parameterMap.size());
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            for (String value : entry.getValue()) {
                listMap.add(entry.getKey(), value);
            }
        }


        // 先获取到QuerySet的类
        Class<?> restControllerClass = methodParameter.getContainingClass();
        if (BaseRestController.class.isAssignableFrom(restControllerClass)) {
            // 拿出QuerySet的类对象
            Field querySetField = restControllerClass.getDeclaredField("querySetClass");
            Class<? extends QuerySet> querySetClass =
                    (Class<? extends QuerySet>) querySetField.getDeclaringClass();
            // 从类里面拿出分页器
            Field pageParamField = restControllerClass.getDeclaredField("paginationParameterClass");
            Class<? extends PaginationParameter> pageParamClass =
                    (Class<? extends PaginationParameter>) pageParamField.getDeclaringClass();
            // 拿出model Class
            Field modelClassField = restControllerClass.getDeclaredField("modelClass");
            Class<?> modelClass = modelClassField.getDeclaringClass();
            // 实例化一个分页器
            HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
            QuerySet querySet = querySetClass.getConstructor(
                    MultiValueMap.class,
                    Class.class,
                    PaginationParameter.class,
                    HttpServletRequest.class).newInstance(listMap, modelClass, pageParamField, request);
            return querySet;
            //nativeWebRequest.getParameterMap()

        }


        return null;
    }

    private boolean isNotSet(String value) {
        return value == null;
    }
}