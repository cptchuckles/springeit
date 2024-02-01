package world.grendel.cringeit.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import world.grendel.cringeit.annotation.AuthenticatedRoute;
import world.grendel.cringeit.models.User;
import world.grendel.cringeit.services.UserService;

/**
 * AuthenticatedRouteAspect
 */
@Aspect
@Component
public class AuthenticatedRouteHandler {
    @Autowired
    private UserService userService;

    private static AuthenticatedRouteHandler instance = new AuthenticatedRouteHandler();

    @Around(value = "@annotation(world.grendel.cringeit.annotation.AuthenticatedRoute)")
    public String authenticateRoute(ProceedingJoinPoint joinPoint) throws Throwable {
        AuthenticatedRoute authenticatedRoute = ((MethodSignature) joinPoint.getSignature()).getMethod()
                .getAnnotation(AuthenticatedRoute.class);

        HttpSession session = null;
        Model model = null;
        int userArg = -1;

        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof HttpSession) {
                session = (HttpSession) args[i];
            }
            else if (args[i] instanceof Model) {
                model = (Model) args[i];
            }
            else if (args[i] instanceof User) {
                userArg = i;
            }
        }
        if (session == null) {
            throw new Exception("Session was not provided");
        }

        Long currentUserId = (Long) session.getAttribute(User.sessionKey);
        if (currentUserId == null) {
            return authenticatedRoute.redirectPath();
        }

        User currentUser = userService.getById(currentUserId);
        if (currentUser == null) {
            return authenticatedRoute.redirectPath();
        }

        if (model != null) {
            model.addAttribute(User.modelKey, currentUser);
        }

        if (userArg >= 0) {
            args[userArg] = currentUser;
        }

        return (String) joinPoint.proceed(args);
    }

    public static AuthenticatedRouteHandler aspectOf() {
        return instance;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
