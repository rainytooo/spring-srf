package com.xiaoma.rest.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by vincent on 16/6/15.
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RestQuerySet {
}
