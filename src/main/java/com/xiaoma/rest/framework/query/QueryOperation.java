package com.xiaoma.rest.framework.query;

/**
 * 查询参数的操作符
 * @author <a href="mailto:ergal@163.com">vincent.omg</a>
 * @version 1.0
 * @date 2016/6/6
 * @since 1.0
 */
public enum QueryOperation {
    /**
     *  NEQ 不等于
     *  GT 大于, GTE 大于等于, LT 小于, LTE 小于等于
     *  STARTWITH 开始于, ENDWITH 结束于
     */
    EQ, NEQ, GT, GTE, LT, LTE, STARTWITH, ENDWITH;
}
