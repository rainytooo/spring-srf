### 介绍

rest framework简称ssrf,目的用于对spring mvc框架下的restful api开发的简化.

主要实现以下功能

* 对象参数和非对象参数的统一封装和格式化
* 无缝对接任何dao层实现
* rest请求参数可实现大于,小于,like等查询
* 返回值符合restful的标准
* 自动实现分页,分页对象可以自定义
* 实现基本的rest方法:get(list), get, post, put, patch, delete
* controller只需指定基本几个参数就可以得到完整的rest api接口
* 返回对象的序列化可以简单定义属性,不同方法只需指定serializer就可以返回具有不同属性数量的序列化对象



# 使用介绍

### 约定和规范

使用一下类型

String, int, Integer, Long, long, float, Float, double, Double, LocalDateTime(Joda)

尽量用int来表示boolean, 用包装类型,不用基本类型

参数传递

客户端全部小写, 服务端用驼峰比如

```
private boolean isMale;
// 这个在客户端传递参数的时候用
is_male
```


# 其它

### 版本说明

* 2016-06-16 v1.0.1
    - 完成queryset部分,查询封装完毕
* 2016-05-20 v1.0
    - 项目初始化,定义结构


### TODO

* [X] 完成属性值转换,设置给model对象
* [X] Date类型
* [X] 查询操作符, 等号不得和其他方式共存
* [X] 分页的标准配置


