import io.britto.config.SpringConfig;
import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import scala.collection.Seq;

/**
 * Created by tfulton on 6/30/16.
 */
public class SpringModule
        extends play.api.inject.Module {

    @Override
    public Seq<Binding<?>> bindings(Environment environment, Configuration configuration) {

        System.out.println("*********************************************************************");
        System.out.println("*********************** LOADING SPRING MODULE ***********************");
        System.out.println("*********************************************************************");
        return seq(
            bind(SpringConfig.class).toSelf().eagerly()
        );
    }
}
