### 项目场景描述
​      实现两个账户之间的转账，编写所涉及到的接口代码。

要求如下：

1. 两个账户不在同一个数据库下，所以要保证事物的一致性。
2. 考虑代码规范，幂等性处理，分布式事物，重试，安全，并发,考虑重复消费问题
3. 不要写伪代码，一定要是一个可运行的程序

### 技术栈
1. 代码规范：严格按照阿里巴巴的代码规范
2. 安全：使用MD5进行加密，加密验签。
3. 幂等性处理：通过建立交易单号的数据库唯一索引
4. 失败重试： RocketMQ重试机制
5. 分库分表：使用sharding-jdbc，分库分表策略是使用支付账户编号进行哈希取模
6. 项目架构：springboot
7. 日志：Log4j2异步日志输出
8. 任务：生产方定时扫描消息表中的失败消息，重新发送一次

### 实现思路
 

### 项目包划分
  - config 配置类的统一包
    - rocketmq  rocketmq生产者和消费者的配置
    - shardingjdbc  shardingjdbc双数据源配置
  - constants 系统全局变量的定义
  - controller 控制层
  - dao  dao层
    - xml  mapper文件
  - dto 数据传输对象
  - entity 实体包
  - exception  自定义异常
  - schedule  定时任务
  - service service层
  - utils  工具类包