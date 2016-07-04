package controllers;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Service
public class HomeController extends Controller {

    @Inject
    private JedisConnectionFactory jedisConnectionFactory;

    @Inject
    private StringRedisTemplate redisTemplate;

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result status() {

        String systemTime = System.currentTimeMillis() + "";

        String key = "myMessage";
        String value = "Hello World " + systemTime + "!\n";
        redisTemplate.opsForValue().set(key, value);

        assert jedisConnectionFactory.getConnection().exists(key.getBytes());

        return ok(redisTemplate.opsForValue().get(key));
    }

}
