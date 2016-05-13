package commands

import org.crsh.cli.Command
import org.crsh.cli.Usage
import org.crsh.command.InvocationContext
import org.springframework.boot.actuate.endpoint.BeansEndpoint

/**********************
@TODO - If additional commands can communicate with runtime source
 **********************/
class hello {

    @Usage("Display beans in ApplicationContext")
    @Command
    def message(InvocationContext context) {
//        def result = [:]
//        context.attributes['spring.beanfactory'].getBeansOfType(BeansEndpoint.class).each { name, endpoint ->
//            result.put(name+"NAMAMAMAAMAM", endpoint.invoke())
//        }
//        result.size() == 1 ? result.values()[0] : result
        return "TODO - Groovy communicate with system's features"
    }

}