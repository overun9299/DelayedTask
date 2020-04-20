package soap.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitUserConfig {

	
	/**
	 * 死信接收队列 * @return
	 */
	@Bean
	public Queue receiveQueue() {
		return new Queue("receive_queue");
	}
	
	/**
	 * 死信队列 * @return
	 */
	@Bean
	public Queue delayQueue() {
		Map<String, Object> map = new HashMap<>(16);
		map.put("x-dead-letter-exchange", "receive_exchange");
		map.put("x-dead-letter-routing-key", "receive_key");
		return new Queue("delay_queue", true, false, false, map);
	}
	
	/**
	 * 死信接收交换机 * @return
	 */
	@Bean
	public DirectExchange receiveExchange() {
		return new DirectExchange("receive_exchange",true,false);
	}
	
	/**
	 * 死信交换机
	 */
	@Bean
	public DirectExchange delayExchange() {
		return new DirectExchange("delay_exchange",true,false);
	}
	
	/**
	 * 死信交换机绑定消费队列 * @return
	 */
	@Bean
	public Binding receiveBinding(Queue receiveQueue, DirectExchange receiveExchange) {
		return BindingBuilder.bind(receiveQueue).to(receiveExchange).with("receive_key");
	}
	
	/**
	 * 给死信队列绑定交换机 * @return
	 */
	@Bean
	public Binding delayBinding(Queue delayQueue, DirectExchange delayExchange) {
		return BindingBuilder.bind(delayQueue).to(delayExchange).with("delay_key");
	}


	

}