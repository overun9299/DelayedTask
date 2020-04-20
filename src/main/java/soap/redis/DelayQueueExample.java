package soap.redis.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.Instant;
import java.util.Set;


/**
 * 我们借助 zset 数据类型，把延迟任务存储在此数据集合中，
 * 然后在开启一个无线循环查询当前时间的所有任务进行消费，
 * 实现代码如下（需要借助 Jedis 框架）
 */
public class DelayQueueExample {
    // zset key
    private static final String _KEY = "myDelayQueue";
    
    public static void main(String[] args) throws InterruptedException {
        Jedis jedis = new JedisPool().getResource();
        // 延迟 30s 执行（30s 后的时间）
        long delayTime = Instant.now().plusSeconds(30).getEpochSecond();
        jedis.zadd(_KEY, delayTime, "order_1");
        // 继续添加测试数据
        jedis.zadd(_KEY, Instant.now().plusSeconds(2).getEpochSecond(), "order_2");
        jedis.zadd(_KEY, Instant.now().plusSeconds(2).getEpochSecond(), "order_3");
        jedis.zadd(_KEY, Instant.now().plusSeconds(7).getEpochSecond(), "order_4");
        jedis.zadd(_KEY, Instant.now().plusSeconds(10).getEpochSecond(), "order_5");
        // 开启延迟队列
        doDelayQueue(jedis);
    }

    /**
     * 延迟队列消费
     * @param jedis Redis 客户端
     */
    public static void doDelayQueue(Jedis jedis) throws InterruptedException {
        while (true) {
            // 当前时间
            Instant nowInstant = Instant.now();
            long lastSecond = nowInstant.plusSeconds(-1).getEpochSecond(); // 上一秒时间
            long nowSecond = nowInstant.getEpochSecond();
            // 查询当前时间的所有任务
            Set<String> data = jedis.zrangeByScore(_KEY, lastSecond, nowSecond);
            for (String item : data) {
                // 消费任务
                System.out.println("消费：" + item);
            }
            // 删除已经执行的任务
            jedis.zremrangeByScore(_KEY, lastSecond, nowSecond);
            Thread.sleep(1000); // 每秒轮询一次
        }
    }
}