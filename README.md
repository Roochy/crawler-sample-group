# crawler-sample-group
A bundle project for crawler processing business.

[In Chinese](README_CN.md)

   

### Environment

- JDK 1.8

- zookeeper

- redis

- MySQL


### Usage

Packaging projects in application module and run it.
- #### article-crawler-application

A simple crawler based on [webmagic](http://webmagic.io)  for [importnew](http://importnew.com) . 

- #### article-persistent-application

A `spring boot` application to persists cache data into MySQL DB.

- #### article-web-application

Simple website based on `spring mvc` and `thymeleaf` to view collected data.

   

### Notice

You need to `install` modules below before run application.

- common-parent
- common-util
- article-module

