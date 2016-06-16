package com.xiaoma.rest.framework.annotation;

import com.xiaoma.rest.framework.query.QueryType;
import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 *
 * 指定rest请求类型
 * Created by vincent on 16/6/16.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface RestQuery {

    QueryType[] type() default {};
}
