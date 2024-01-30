package world.grendel.userlogindemo.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import world.grendel.userlogindemo.annotation.AuthenticatedRoute;
import world.grendel.userlogindemo.models.User;
import world.grendel.userlogindemo.services.UserService;

/**
 * AuthenticatedRouteAspect
 */
@Aspect
@Component
public class AuthenticatedRouteHandler {
    @Autowired
    private UserService userService;

    private static AuthenticatedRouteHandler instance = new AuthenticatedRouteHandler();

	@Around(value = "@annotation(world.grendel.userlogindemo.annotation.AuthenticatedRoute)")
    public String authenticateRoute(
        ProceedingJoinPoint joinPoint
    ) throws Throwable {
        HttpSession session = null;
        Model model = null;
        for (Object arg : joinPoint.getArgs()) {
            if (session == null && arg instanceof HttpSession) {
                session = (HttpSession) arg;
            }
            else if (model == null && arg instanceof Model) {
                model = (Model) arg;
            }
            else {
                break;
            }
        }
        if (session == null) {
            throw new Exception("Method does not use HttpSession");
        }
        if (model == null) {
            throw new Exception("Method does not use Model");
        }

        AuthenticatedRoute authenticatedRoute = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(AuthenticatedRoute.class);

        Long currentUserId = (Long) session.getAttribute("currentUserId");
        if (currentUserId == null) {
            return authenticatedRoute.redirectPath();
        }
        User currentUser = userService.getById(currentUserId);
        if (currentUser == null) {
            return authenticatedRoute.redirectPath();
        }

        model.addAttribute("currentUserId", currentUser.getId());

        return (String) joinPoint.proceed();
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
