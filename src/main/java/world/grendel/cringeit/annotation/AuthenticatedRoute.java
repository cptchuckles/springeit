package world.grendel.userlogindemo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AuthenticatedRoute
 *
 * HTTP Request is redirected unless the session cookie identifies a valid user
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticatedRoute {

    public String redirectPath() default "redirect:/logout";

}
