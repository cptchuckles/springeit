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

	@Around(value = "@annotation(world.grendel.cringeit.annotation.AuthenticatedRoute) && args(session, model)")
    public String authenticateRoute(
        ProceedingJoinPoint joinPoint,
        HttpSession session,
        Model model
    ) throws Throwable {
        AuthenticatedRoute authenticatedRoute = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(AuthenticatedRoute.class);

        Long currentUserId = (Long) session.getAttribute(User.sessionKey);
        if (currentUserId == null) {
            return authenticatedRoute.redirectPath();
        }
        User currentUser = userService.getById(currentUserId);
        if (currentUser == null) {
            return authenticatedRoute.redirectPath();
        }

        model.addAttribute(User.sessionKey, currentUser.getId());

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
