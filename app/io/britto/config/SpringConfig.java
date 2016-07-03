package io.britto.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.PropertySource;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by tfulton on 7/3/16.
 */
//@Singleton
public class SpringConfig {

    final static Logger logger = LoggerFactory.getLogger(SpringConfig.class);

    private ClassPathXmlApplicationContext ctx;

//    @Inject
    public SpringConfig() {

        System.out.println("*********************************************************************");
        System.out.println("*********************** LOADING SPRING CONFIG ***********************");
        System.out.println("*********************************************************************");
        try {
            final Config config = ConfigFactory.load("application.conf");
            ctx = new ClassPathXmlApplicationContext();
            ctx.setConfigLocation("classpath:britto-trans-context.xml");
            ctx.getEnvironment().getPropertySources().addLast(new TypesafeConfigPropertySource(config));
            ctx.refresh();
            ctx.start();

        } catch (Exception e) {
            logger.error("Can not start, halting with error", e);
            System.exit(1);
        }
    }

    public StringRedisTemplate getRedisTemplate() {
        return (StringRedisTemplate)ctx.getBean("redisTemplate");
    }

    private class TypesafeConfigPropertySource extends PropertySource<String> {

        final Config config;

        public TypesafeConfigPropertySource(Config config) {
            super("TypesafeConfigPropertySource");
            this.config = config;
        }

        @Override
        public Object getProperty(String name) {
            if (config.hasPath(name)) {
                final Object prop = config.getAnyRef(name);
                return prop;
            } else {
                return null;
            }
        }
    }
}
