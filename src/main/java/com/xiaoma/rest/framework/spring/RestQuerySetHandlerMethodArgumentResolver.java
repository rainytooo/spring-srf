package com.xiaoma.rest.framework.spring;

import com.google.common.collect.Lists;
import com.sun.tools.javac.jvm.Gen;
import com.xiaoma.rest.framework.annotation.RestQuery;
import com.xiaoma.rest.framework.annotation.RestQuerySet;
import com.xiaoma.rest.framework.controller.BaseRestController;
import com.xiaoma.rest.framework.page.PaginationParameter;
import com.xiaoma.rest.framework.query.GenericQuerySet;
import com.xiaoma.rest.framework.query.QuerySet;
import com.xiaoma.rest.framework.query.QueryType;
import org.springframework.core.MethodParameter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 用于在controller里直接注入一个标准的查询对象QuerySet
 * Created by vincent on 16/6/15.
 */
public class RestQuerySetHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> klass = parameter.getParameterType();
        if (QuerySet.class.isAssignableFrom(klass)) {
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

        // 获取rest的annotation
        QueryType queryType;
        RestQuery rqAnnotation = methodParameter.getMethod().getAnnotation(RestQuery.class);
        QueryType[] queryTypes = rqAnnotation.type();
        if(queryTypes.length > 0){
            queryType = queryTypes[0];
        } else {
            queryType = QueryType.LIST;
        }

        // 先获取到QuerySet的类
        Class<?> restControllerClass = methodParameter.getDeclaringClass(); // methodParameter.getContainingClass();
        if (BaseRestController.class.isAssignableFrom(restControllerClass)) {
            // 拿出QuerySet的类对象
            Class<? extends QuerySet> querySetClass =
                    (Class<? extends QuerySet>) methodParameter.getParameterType();
            // 拿出model Class
            List<Field> currentClassFields = Lists.newArrayList(restControllerClass.getDeclaredFields());
            Field modelClassField = restControllerClass.getDeclaredField("modelClass");
            modelClassField.setAccessible(true);
            Class<?> modelClass = (Class<?>) modelClassField.get(null);
            // 从类里面拿出分页器 调用方法获取
            Field pageParamField = restControllerClass.getDeclaredField("paginationParameterClass");
            pageParamField.setAccessible(true);
            Class<? extends PaginationParameter> pageParamClass =
                    (Class<? extends PaginationParameter>) pageParamField.get(null);

            // 实例化一个分页器
            HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
            Constructor<? extends QuerySet> querySetConstructor = querySetClass.getConstructor(
                    MultiValueMap.class,
                    Class.class,
                    PaginationParameter.class,
                    QueryType.class,
                    HttpServletRequest.class);

            QuerySet querySet =
                    querySetConstructor.newInstance(
                            listMap,
                            modelClass,
                            pageParamClass.newInstance(),
                            queryType,
                            request);
            return querySet;

        }

        return null;
    }
}