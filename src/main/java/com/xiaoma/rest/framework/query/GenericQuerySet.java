package com.xiaoma.rest.framework.query;

import com.xiaoma.rest.framework.page.PaginationParameter;
import com.xiaoma.rest.framework.page.Paginator;
import com.xiaoma.rest.framework.serializer.Serializer;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Field;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;

/**
 * QuerySet的一般实现
 *
 * @author <a href="mailto:ergal@163.com">vincent.omg</a>
 * @version 1.0
 * @date 16/5/20
 * @since 1.0
 */
public class GenericQuerySet implements QuerySet {

    private final Logger logger = LoggerFactory.getLogger(GenericQuerySet.class);

    // 查询类型
    private QueryType queryType;

    // 原始参数对象
    private MultiValueMap<String, String[]> originParams;

    private HttpServletRequest request;

    // 分页标准对象
    private PaginationParameter paginationParameter;

    // 分页器
    private Paginator paginator;

    // Model对象实体,如果用orm和简单查询,这个属性可以直接用来orm查询
    private Object queryObject;

    // model的类
    private Class modelClass;

    // 所有查询参数经过处理放在这里
    private HashMap<String, QueryParameter> queryParamWithOp;

    public GenericQuerySet() {
        this.originParams = null;
    }

    public GenericQuerySet(MultiValueMap<String, String[]> originParams,
                           Class modelClass,
                           HttpServletRequest request) {
        this.originParams = originParams;
        this.modelClass = modelClass;
        this.paginationParameter = new PaginationParameter();
        this.queryType = QueryType.LIST;
        this.request = request;
        this.initParams(request);
    }

    public GenericQuerySet(MultiValueMap<String, String[]> originParams,
                           Class modelClass,
                           PaginationParameter paginationParameter,
                           HttpServletRequest request) {
        this.originParams = originParams;
        this.modelClass = modelClass;
        this.paginationParameter = paginationParameter;
        this.queryType = QueryType.LIST;
        this.request = request;
        this.initParams(request);
    }

    public GenericQuerySet(MultiValueMap<String, String[]> originParams,
                           Class modelClass,
                           PaginationParameter paginationParameter,
                           QueryType queryType,
                           HttpServletRequest request) {
        this.originParams = originParams;
        this.modelClass = modelClass;
        this.paginationParameter = paginationParameter;
        this.queryType = queryType;
        this.request = request;
        this.initParams(request);
    }

    /**
     * 重新拼装参数 设置对象参数和额外参数
     * 分页参数
     * 查询参数 设置为对象属性
     * @param request
     */
    private void initParams(HttpServletRequest request) {
        try {
            this.queryObject = modelClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        resetModelAttribute();
        resetParam();
        if (this.queryType == QueryType.LIST) {
            resetPaginator(request);
        }

    }


    /**
     * 设置分页
     * @param request
     */
    private void resetPaginator(HttpServletRequest request) {
        this.paginator = new Paginator(request, this.paginationParameter);
        // 拿出当前页参数 如果有直接设置,如果没有设置为1
        this.initPageParams(this.paginator);

        // 将原始的请求参数拿出来
        String requestUrl = request.getRequestURL().toString();
        String queryUrl = request.getQueryString();

        ServletUriComponentsBuilder ucb = ServletUriComponentsBuilder.fromRequest(request);
        // 设置现在的url
        this.paginator.setCurrentUrl(ucb.build().toUriString());
    }


    /**
     * 把所有参数做一次包装
     */
    private void resetParam() {


        queryParamWithOp = new HashMap<>();

        // 循环所有参数
        for (Map.Entry<String, List<String[]>> entry : this.originParams.entrySet()) {
            // 取出参数名
            String paramNameOrigin = entry.getKey();
            // 转化名称和操作符
            QueryParameter queryParameter = this.parseQueryNameAndOperation(paramNameOrigin);
            Object paramValueObj = entry.getValue();
            queryParameter.setParams((LinkedList<String>) paramValueObj);
            queryParamWithOp.put(queryParameter.getParamName(), queryParameter);
        }
    }

    /**
     * 初始化分页的参数值
     */
    private void initPageParams(Paginator paginator) {
        // 根据参数名称 获取分页大小参数,数字和默认最大值比较
        Object pageSizeParamObj = this.originParams.get(this.paginationParameter.getPageSizeParamName());
        if (pageSizeParamObj != null) {
            LinkedList<String> pageSizeValueList = (LinkedList<String>) pageSizeParamObj;
            int pageSize = Integer.parseInt(pageSizeValueList.get(0));
            pageSize = pageSize > this.paginationParameter.getMaxPageSize() ? this.paginationParameter.getMaxPageSize() : pageSize;
            paginator.setPageSize(pageSize);
            this.originParams.remove(this.paginationParameter.getPageSizeParamName());
        } else {
            int pageSize = this.paginationParameter.getPageSize();
            paginator.setPageSize(pageSize);
        }

        Object pageParamObj = this.originParams.get(this.paginationParameter.getPageParamName());
        if (pageParamObj != null) {
            LinkedList<String> pageValueList = (LinkedList<String>) pageParamObj;
            int pageNum = Integer.parseInt(pageValueList.get(0));
            paginator.setCurrentPage(pageNum);
            this.originParams.remove(this.paginationParameter.getPageParamName());
        } else {
            int pageNum = 1;
            paginator.setCurrentPage(pageNum);
        }
    }

    /**
     * 将参数名做一次重组装
     *
     * @param paramNameOrigin
     * @return
     */
    private QueryParameter parseQueryNameAndOperation(String paramNameOrigin) {
        QueryParameter queryParameter = new QueryParameter();
        // 如果包含下划线,如果下划线的后面属于操作符,那么就截断
        if (paramNameOrigin.indexOf('_') != -1) {
            // 取出最后参数
            String afterLastUnderline = paramNameOrigin.substring(paramNameOrigin.lastIndexOf('_') + 1, paramNameOrigin.length());
            QueryOperation operation = this.getOpreation(afterLastUnderline);
            String queryParamName = paramNameOrigin;
            if (operation != QueryOperation.EQ) {
                // 有附加查询操作符,去掉最后一个操作符和下划线
                queryParamName = paramNameOrigin.substring(0, paramNameOrigin.lastIndexOf('_'));
            }
            queryParameter.setParamName(LOWER_UNDERSCORE.to(LOWER_CAMEL, queryParamName));
            queryParameter.setOperation(operation);
        } else {
            queryParameter.setParamName(LOWER_UNDERSCORE.to(LOWER_CAMEL, paramNameOrigin));
            queryParameter.setOperation(QueryOperation.EQ);
        }
        return queryParameter;
    }

    private QueryOperation getOpreation(String opName) {
        switch (opName) {
            case "neq":
                return QueryOperation.NEQ;
            case "gt":
                return QueryOperation.GT;
            case "gte":
                return QueryOperation.GTE;
            case "lt":
                return QueryOperation.LT;
            case "lte":
                return QueryOperation.LTE;
            case "stw":
                return QueryOperation.STARTWITH;
            case "edw":
                return QueryOperation.ENDWITH;
            default:
                return QueryOperation.EQ;
        }
    }

    /**
     * 用参数设置model对象的属性
     * 由于参数全部是小写,且有下划线符号,需要重新转化,而不使用spring自带的model对象注入
     * 如果一个属性有多个值,则不会作为model属性去设置
     * 只能用来查询等于这种情况下的
     */
    private void resetModelAttribute() {
        // 优化方法,从对象去查找
        Field[] keyFields = modelClass.getDeclaredFields();

        // 循环字段,根据参数设置,这里不涉及复杂查询(大于小于,以及数组等等),只简单设置对象
        for (Field field : keyFields) {
            String fieldName = field.getName();
            // 转驼峰为下划线形式
            String fieldNameCamel = LOWER_CAMEL.to(LOWER_UNDERSCORE, fieldName);
            if (this.originParams.containsKey(fieldNameCamel)) {
                // 取出值,转换相应的类型,并设置对象
                List<String[]> paramList = this.originParams.get(fieldNameCamel);
                // 只有一个参数的时候才可以

                if (paramList.size() == 1) {
                    //LinkedList<String> paramLinkedList = (LinkedList<String>)paramList;
                    String paramValue = String.valueOf(paramList.get(0));
                    try {
                        field.setAccessible(true);
                        Object primitiveAttr = convertPrimitiveField(paramValue, field);
                        if (primitiveAttr != null) {
                            field.set(this.queryObject, primitiveAttr);
                        } else {
                            // 如果此时还是空,尝试转成对象,设置id
                            convertNotPrimitiveField(field, paramValue);
                        }

                    } catch (IllegalAccessException e) {
                        logger.info(field.getName() + " class set attr error!");
                    } catch (InstantiationException e) {
                        logger.info(field.getName() + " create instance error!");
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        /**
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
         */


        /**
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
         */
    }

    private void convertNotPrimitiveField(Field field, String paramValue) throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        // 创建一个对象
        Object fieldObject = field.getType().newInstance();
        // 设置对象的id属性
        Field fieldClassIdField = field.getType().getDeclaredField("Id");
        if (fieldClassIdField != null) {
            fieldClassIdField.setAccessible(true);
            fieldClassIdField.set(fieldObject, Integer.parseInt(paramValue));
            // 将这个非基本类型的对象设置给model的对象
            field.setAccessible(true);
            field.set(this.queryObject, fieldObject);
        }
    }

    /**
     * 参数转化,主要是对象用
     *
     * @param paramValue
     * @param field
     * @return
     */
    private Object convertPrimitiveField(String paramValue, Field field) {
        if (String.class.isAssignableFrom(field.getType())) {
            return paramValue;
        } else if (Integer.class.isAssignableFrom(field.getType()) || field.getType() == int.class) {
            return Integer.parseInt(paramValue);
        } else if (Long.class.isAssignableFrom(field.getType()) || field.getType() == long.class) {
            return Long.parseLong(paramValue);
        } else if (Double.class.isAssignableFrom(field.getType()) || field.getType() == double.class) {
            return Double.parseDouble(paramValue);
        } else if (Float.class.isAssignableFrom(field.getType()) || field.getType() == float.class) {
            return Float.parseFloat(paramValue);
        } else if (Short.class.isAssignableFrom(field.getType()) || field.getType() == short.class) {
            return Short.parseShort(paramValue);
        } else if (Boolean.class.isAssignableFrom(field.getType()) || field.getType() == boolean.class) {
            return Boolean.parseBoolean(paramValue);
        } else if (LocalDateTime.class.isAssignableFrom(field.getType())) {
            // 时间日期转化
            LocalDateTime ldt = new LocalDateTime(Long.parseLong(paramValue) * 1000);
            return ldt;
        }
        return null;
    }

    // 获取分页参数
    private List<String[]> getPage() {
        return this.originParams.get("page");
    }


    public PaginationParameter getPaginationParameter() {
        return this.paginationParameter;
    }

    /**
     * 过去参数值
     * @param paramName
     * @return
     */
    @Override
    public QueryParameter getQueryParameter(String paramName) {
        return this.queryParamWithOp.get(paramName);
    }

    /**
     * 获取所有参数, 参数包装对象
     * @return
     */
    @Override
    public HashMap<String, QueryParameter> getAllQueryParameter() {
        return this.queryParamWithOp;
    }

    /**
     * 获取设置完值的 查询对象
     * @return
     */
    @Override
    public Object getQueryModel() {
        return this.queryObject;
    }

    /**
     * 获取排序器
     * @return
     */
    @Override
    public HashMap<String, String> getOrderFilter() {
        return null;
    }

    /**
     * 获取序列化工具对象
     * @return
     */
    @Override
    public Serializer getSerializer() {
        return null;
    }

    /**
     * 获取分页器
     * @return
     */
    @Override
    public Paginator getPaginator() {
        return this.paginator;
    }
}
