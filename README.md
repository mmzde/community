## community

## 资料

[Spring 文档](https://spring.io/guides)
[Spring web](https://spring.io/guides/gs/serving-web-content/)
[Bootstrap 文档](https://v3.bootcss.com/)
[OkHttp](https://square.github.io/okhttp/)
[Flyway 数据库版本控制器](https://flywaydb.org/getstarted/firststeps/maven)
[MVN 仓库](https://mvnrepository.com/)
[MyBatis 文档](https://mybatis.org/mybatis-3/configuration.html)
## 工具
idea git 菜鸟教程
[Flyway 安装教程](https://blog.csdn.net/qq_38571521/article/details/89401584?utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~all~first_rank_v2~rank_v25-2-89401584.nonecase)
[Git 命令大全](https://www.jianshu.com/p/46ffff059092)


## 脚本
```sql
CREATE TABLE user (
  id int(11) AUTO_INCREMENT,
  account_id varchar(100) NULL,
  name varchar(50) NULL,
  token char(36) NULL,
  gmt_create bigint(20) NULL,
  gmt_modified bigint(20) NULL,
  constraint user_pk
  PRIMARY KEY (`id`)
);
```
```bash
mvn flyway:migrate

mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```