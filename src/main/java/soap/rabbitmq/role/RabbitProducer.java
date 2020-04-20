package soap.rabbitmq.role;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class RabbitProducer {
	private final RabbitTemplate rabbitTemplate;
	
	@Autowired
	public RabbitProducer(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}


	/**
	 * 发送延时消息
	 * @param messages 消息体
	 * @param expiration 过期时间可以传递两种格式 1.传递数字-单位毫秒 2.传递yyyy-MM-dd HH:mm:ss
	 */
	public void sendDelayMessage(Map messages , String expiration) {
		//这里的消息可以是任意对象，无需额外配置，直接传即可 log.info("===============延时队列生产消息====================");
		log.info("发送时间:{},发送内容:{}", LocalDateTime.now(), JSONObject.toJSON(messages));
		this.rabbitTemplate.convertAndSend("delay_exchange", "delay_key", messages, message -> {
			//注意这里时间要是字符串形式
			message.getMessageProperties().setExpiration(expiration);
			return message;
		});
		log.info("{}ms后执行", expiration);
		
	}
}
