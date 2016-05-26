package com.xiaoma.rest.framework.query;

import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     */
    private void initParams() {
        try {
            this.queryObject = modelClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        // TODO: 把分页参数拿出来
        List<String[]> list = getPage();
        //MultiValueMap<String, String[]> originParamsCopy = new LinkedMultiValueMap<String, LinkedList<String>>(this.originParams);
        resetModelAttribute();
    }

    /**
     * 用参数设置model对象的属性
     */
    private void resetModelAttribute() {
        // 优化方法,从对象去查找
        Field[] keyFields = modelClass.getDeclaredFields();


        // final Stream<Field> fieldStream = Arrays.stream(keyFields);

        // String 类型字段
        List<Field> stringFields = Arrays.stream(keyFields)
                .filter(field -> String.class.isAssignableFrom(field.getType()))
                .collect(Collectors.toList());

        // Boolean 类型字段
        List<Field> booleanFields = Arrays.stream(keyFields)
                .filter(field -> Boolean.class.isAssignableFrom(field.getType()) | field.getType() == boolean.class)
                .collect(Collectors.toList());

        // Int 类型字段
        List<Field> intFields = Arrays.stream(keyFields)
                .filter(field -> Integer.class.isAssignableFrom(field.getType()) | field.getType() == int.class)
                .collect(Collectors.toList());

        // Long 类型字段
        List<Field> longFields = Arrays.stream(keyFields)
                .filter(field -> Long.class.isAssignableFrom(field.getType()) | field.getType() == long.class)
                .collect(Collectors.toList());

        // Float 类型字段
        List<Field> floatFields = Arrays.stream(keyFields)
                .filter(field -> Float.class.isAssignableFrom(field.getType()) | field.getType() == float.class)
                .collect(Collectors.toList());

        // Double类型字段
        List<Field> doubleFields = Arrays.stream(keyFields)
                .filter(field -> Double.class.isAssignableFrom(field.getType()) | field.getType() == double.class)
                .collect(Collectors.toList());



        for (Map.Entry<String, List<String[]>> entry : this.originParams.entrySet()) {
            String name = entry.getKey();
            // 下划线转驼峰
            String camelName = LOWER_UNDERSCORE.to(LOWER_CAMEL, name);
            try {
                Field keyField = modelClass.getDeclaredField(camelName);
                keyField.setAccessible(true);
                // 属性的类
                Class<?> attr_class = keyField.getType();
                // 强制类型转化
                if (keyField.getType().equals(String.class)) {
                    String stringValue = String.valueOf(attr_class.cast(entry.getValue().get(0)));
                    keyField.set(queryObject, stringValue);
                } else if (keyField.getType().equals(Integer.class)) {
                    Integer integerValue = Integer.valueOf(entry.getValue().get(0)[0]);
                    keyField.set(queryObject, integerValue);
                } else {
                    Object attrTypeObj = attr_class.cast(entry.getValue().get(0));
                    keyField.set(queryObject, attrTypeObj);
                }

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    // 获取分页参数
    private List<String[]> getPage() {
        return this.originParams.get("page");
    }

    public Object getModelObject() {
        return this.queryObject;
    }
}
