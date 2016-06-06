package com.xiaoma.rest.framework.query;

import com.xiaoma.rest.framework.page.Pagination;
import com.xiaoma.rest.framework.serializer.Serializer;

import java.util.HashMap;

/**
 *
 * @author <a href="mailto:ergal@163.com">vincent.omg</a>
 * @version 1.0
 * @date 16/5/27
 * @since 1.0
 */
public interface QuerySet {
    /**
     * 获取分页对象
     * @return
     */
    Pagination getPagination();

    /**
     *
     * @param paramName
     * @return
     */
    QueryParameter getQueryParameter(String paramName);

    /**
     * 获取所有的查询
     * @return
     */
    HashMap<String, QueryParameter> getAllQueryParameter();


    /**
     * 拿出查询的对象,设置完参数
     * @return
     */
    Object getQueryModel();


    /**
     * 获取排序参数
     * @return
     */
    HashMap<String, String> getOrderFilter();

    /**
     * 获取序列化参数
     * @return
     */
    Serializer getSerializer();

}
