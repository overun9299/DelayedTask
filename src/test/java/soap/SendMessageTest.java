package soap;

import javafx.application.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import soap.rabbitmq.role.RabbitProducer;

import java.util.HashMap;
import java.util.Map;


/**
 * 测试消息发送
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SendMessageTest {

    @Autowired
    private RabbitProducer rabbitProduct;

    @Test
    public void sendTest() {
        Map<String , Object> message = new HashMap<>();
        message.put("msg","发送延迟消息");
        message.put("exp",5000);
        rabbitProduct.sendDelayMessage(message , "5000");
    }
}
