## 框架简介
利用空闲时间，结合自己经历的系统模块，整合出一些好用的后台组件，部分功能尚仍需不断优化，目前已经可以满足大部分需求。

## 组件描述
com-dependencies 主要管理 Java 的 jar 包版本、 Spring 的 jar 包、以及三方的 jar 包；  
com-framework   管理框架组件等；  
#### 组件介绍
com-spring-boot-starter-base：包含工具类、统一异常处理、统一返回类型、统一请求日志等；
com-spring-boot-starter-auth：统一授权处理，当前版本为简版，如需使用，可根据业务自行修改补充；  
com-spring-boot-starter-sequence：序列管理，需要在数据库添加sequence_config表，执行resources下sql脚本即可；  
com-spring-boot-starter-mybatis：集成 mybatis-plus 组件；
com-spring-boot-starter-pay： 对接了微信支付、支付宝支付，如需添加其他支付方式，可自行维护；
com-spring-boot-starter-encryptable：对 yaml 中密码进行加解密处理，以防密码泄露；  
com-spring-boot-starter-tenant： 租户系统，暂未使用；  
com-spring-boot-starter-mq： 集成 redisson 队列；  
com-spring-boot-starter-rabbitmq： 集成 rabbitmq 队列；  