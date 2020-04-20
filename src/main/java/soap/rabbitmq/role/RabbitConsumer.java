package soap.rabbitmq.role;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@Component
@Slf4j
public class RabbitConsumer {

	/**
	 * 默认情况下,如果没有配置手动ACK,
	 * 那么Spring Data AMQP 会在消息消费完毕后自动帮我们去ACK * 存在问题：
	 * 如果报错了,消息不会丢失,但是会无限循环消费,一直报错,
	 * 如果开启了错误日志很容易就吧磁盘空间耗完 *
	 * 解决方案：手动ACK,或者try-catch
	 * 然后在 catch 里面将错误的消息转移到其它的系列中去 *
	 * spring.rabbitmq.listener.simple.acknowledge-mode = manual * @param list 监听的内容
	 */
	@RabbitListener(queues = "receive_queue")
	@RabbitHandler
	public void cfgUserReceiveDealy(List<Integer> list, Message message, Channel channel) throws IOException {
		log.info("===============接收队列接收消息====================");
		log.info("接收时间:{},接受内容:{}", LocalDateTime.now(), list.toString());
		//通知 MQ 消息已被接收,可以ACK(从队列中删除)了
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		try {
			System.out.println("a");
		} catch (Exception e) {
			log.error("============消费失败,尝试消息补发再次消费!==============");
			log.error(e.getMessage());
			/** * basicRecover方法是进行补发操作，
			 * * 其中的参数如果为true是把消息退回到queue但是有可能被其它的consumer(集群)接收到，
			 * * 设置为false是只补发给当前的consumer */

			channel.basicRecover(false);
		}
	}
	
	
}
