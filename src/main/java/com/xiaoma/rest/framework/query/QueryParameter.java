package com.xiaoma.rest.framework.query;

import java.util.LinkedList;

/**
 * 查询包装,包含操作符
 * 后期查询可以有多种查询,如大于 小于等
 * @author <a href="mailto:ergal@163.com">vincent.omg</a>
 * @version 1.0
 * @date 16/5/30
 * @since 1.0
 */
public class QueryParameter {

    /*
    参数名 已经转为驼峰
     */
    private String paramName;

    private QueryOperation operation;

    private LinkedList<String> params;


    public QueryParameter() {
    }

    public QueryParameter(String paramName) {
        this.paramName = paramName;
    }

    public QueryParameter(String paramName, QueryOperation operation, LinkedList<String> params) {
        this.paramName = paramName;
        this.operation = operation;
        this.params = params;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public QueryOperation getOperation() {
        return operation;
    }

    public void setOperation(QueryOperation operation) {
        this.operation = operation;
    }

    public LinkedList<String> getParams() {
        return params;
    }

    public void setParams(LinkedList<String> params) {
        this.params = params;
    }
}
