import com.google.inject.AbstractModule;

import java.time.Clock;

import com.google.inject.ProvidedBy;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.britto.config.SpringConfig;
import io.britto.persistence.TransactionPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.PropertySource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import services.ApplicationTimer;
import services.AtomicCounter;
import services.Counter;

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 * <p>
 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
public class Module extends AbstractModule {

    final static Logger logger = LoggerFactory.getLogger(Module.class);

    private ClassPathXmlApplicationContext ctx;

    public Module() {
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
            logger.error("Module can not start, halting with error", e);
            System.exit(1);
        }
    }

    @Override
    public void configure() {
        // Use the system clock as the default implementation of Clock
        bind(Clock.class).toInstance(Clock.systemDefaultZone());
        // Ask Guice to create an instance of ApplicationTimer when the
        // application starts.
        bind(ApplicationTimer.class).asEagerSingleton();
        // Set AtomicCounter as the implementation for Counter.
        bind(Counter.class).to(AtomicCounter.class);
    }

    @Provides
    public JedisConnectionFactory getJedisConnectionFactory() {
        return (JedisConnectionFactory) ctx.getBean("jedisConnectionFactory");
    }

    @Provides
    public StringRedisTemplate getRedisTemplate() {
        return (StringRedisTemplate) ctx.getBean("redisTemplate");
    }

    @Provides
    public TransactionPersistence getTransactionPersistence() {
        return (TransactionPersistence) ctx.getBean("transactionPersistence");
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
