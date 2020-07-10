# crawler-sample-group
一个简单的爬虫业务链路的练习项目。

[In English](README.md)

   

### 环境

- JDK 1.8

- zookeeper

- redis

- MySQL

   

### 使用

编译，运行 `application` 模块下的项目即可。
- #### article-crawler-application

一个基于 [webmagic](http://webmagic.io) 实现的 [importnew](http://importnew.com)  网站的爬虫。

- #### article-persistent-application

将缓存中的数据持久化到数据库中的 `spring boot` 应用。

- #### article-web-application

一个简单的用于展示爬取到的数据的网站，基于 `spring mvc` 和 `thymeleaf`。

   

### 提示

在运行程序前，你需要 `install` 以下模块。

- common-parent
- common-util
- article-module

