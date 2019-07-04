package com.cc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;


/**
 * @author:xiasc
 * @Date:2018/11/9
 * @Time:16:35
 **/
@Configuration
public class JmsListenerContainerConfiguration {

    @Bean
    public JmsListenerContainerFactory<?> topicListenerContainer(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory topicListenerContainer = new DefaultJmsListenerContainerFactory();
        topicListenerContainer.setPubSubDomain(true);
        topicListenerContainer.setConnectionFactory(connectionFactory);
        return topicListenerContainer;
    }

    @Bean
    public JmsListenerContainerFactory<?> queueListenerContainer(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory queueListenerContainer = new DefaultJmsListenerContainerFactory();
        queueListenerContainer.setPubSubDomain(false);
        queueListenerContainer.setConnectionFactory(connectionFactory);
        return queueListenerContainer;
    }


}
