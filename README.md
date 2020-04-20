----
# 延迟任务
* 自己手写一个“死循环”一直判断当前时间节点有没有要执行的任务
* 借助 JDK 或者第三方提供的工具类来实现延迟任务。

----
#### infiniteLloop: 无限循环实现延迟任务

#### javaapi: java api实现的延迟队列

#### redis:使用redis的zset的数据类型实现延迟队列

#### rabbitmq:使用rabbitmq的死信队列来实现延迟队列
