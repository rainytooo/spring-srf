package com.xiaoma.rest.framework.query;

import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;

/**
 * @author <a href="mailto:ergal@163.com">vincent.omg</a>
 * @version 1.0
 * @date 16/5/20
 * @since 1.0
 */
public class QuerySet {

    // 原始参数对象
    private final MultiValueMap<String, String[]> originParams;

    private Object queryObject;

    // model类
    private Class modelClass;

    // 非对象属性参数放这里
    private MultiValueMap<String, String[]> extraParams;

    public QuerySet(MultiValueMap<String, String[]> originParams, Class modelClass) {
        this.originParams = originParams;
        this.modelClass = modelClass;
        this.initParams();
    }

    /**
     * 重新拼装参数 设置对象参数和额外参数
     * 分页参数
     * 查询参数 设置为对象属性
     *
     */
    private void initParams(){
        try {
            this.queryObject = modelClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        // TODO: 把分页参数拿出来

        //MultiValueMap<String, String[]> originParamsCopy = new LinkedMultiValueMap<String, LinkedList<String>>(this.originParams);
        for (Map.Entry<String, List<String[]>> entry : this.originParams.entrySet()) {
            String name = entry.getKey();
            // 下划线转驼峰
            String camelName = LOWER_UNDERSCORE.to(LOWER_CAMEL, name);
            try {
                Field keyField = modelClass.getDeclaredField(camelName);
                keyField.setAccessible(true);
                // 属性的类
                Class<?> attr_class =  keyField.getType();
                if (attr_class.equals(String.class)){
                    System.out.println("asdasdasd");
                }
                //keyField.set(queryObject, entry.getValue().g);

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }
}
