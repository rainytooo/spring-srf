package com.xiaoma.rest.framework.query;

import com.xiaoma.rest.framework.page.Pagination;
import com.xiaoma.rest.framework.serializer.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;

/**
 * QuerySet的一般实现
 * @author <a href="mailto:ergal@163.com">vincent.omg</a>
 * @version 1.0
 * @date 16/5/20
 * @since 1.0
 */
public class GenericQuerySet implements QuerySet{

    private final Logger logger = LoggerFactory.getLogger(GenericQuerySet.class);

    // 原始参数对象
    private final MultiValueMap<String, String[]> originParams;

    // Model对象实体,如果用orm和简单查询,这个属性可以直接用来orm查询
    private Object queryObject;

    // model的类
    private Class modelClass;

    // 非对象属性参数放这里
    private MultiValueMap<String, String[]> extraParams;

    public GenericQuerySet(MultiValueMap<String, String[]> originParams, Class modelClass) {
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
     * 由于参数全部是小写,且有下划线符号,需要重新转化,而不使用spring自带的model对象注入
     */
    private void resetModelAttribute() {
        // 优化方法,从对象去查找
        Field[] keyFields = modelClass.getDeclaredFields();

        // 循环字段,根据参数设置,这里不涉及复杂查询(大于小于,以及数组等等),只简单设置对象
        for(Field field: keyFields){
            String fieldName = field.getName();
            // 转驼峰为下划线形式
            String fieldNameCamel = LOWER_CAMEL.to(LOWER_UNDERSCORE, fieldName);
            if(this.originParams.containsKey(fieldNameCamel)){
                // 取出值,转换相应的类型,并设置对象
                List<String[]> paramList = this.originParams.get(fieldNameCamel);
                // 只有一个参数的时候才可以

                if(paramList.size() == 1){
                    //LinkedList<String> paramLinkedList = (LinkedList<String>)paramList;
                    String paramValue = String.valueOf(paramList.get(0));
                    try {
                        field.setAccessible(true);
                        field.set(this.queryObject, convertModelAttrValue(paramValue, field));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                logger.debug("asdasd");
            }
        }


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

    /**
     * 参数转化
     * @param paramValue
     * @param field
     * @return
     */
    private Object convertModelAttrValue(String paramValue, Field field) {
        if(String.class.isAssignableFrom(field.getType())) {
            return paramValue;
        }else if(Integer.class.isAssignableFrom(field.getType()) | field.getType() == int.class){
            return Integer.parseInt(paramValue);
        }else if(Long.class.isAssignableFrom(field.getType()) | field.getType() == long.class){
            return Long.parseLong(paramValue);
        }
        return null;
    }

    // 获取分页参数
    private List<String[]> getPage() {
        return this.originParams.get("page");
    }

    public Object getModelObject() {
        return this.queryObject;
    }

    @Override
    public Pagination getPagination() {
        return null;
    }

    @Override
    public QueryParameter getQueryParameter(String paramName) {
        return null;
    }

    @Override
    public Object getQueryModel() {
        return null;
    }

    @Override
    public HashMap<String, String> getOrderFilter() {
        return null;
    }

    @Override
    public Serializer getSerializer() {
        return null;
    }
}
