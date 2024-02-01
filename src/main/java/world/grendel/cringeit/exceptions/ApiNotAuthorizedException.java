package world.grendel.cringeit.exceptions;

import world.grendel.cringeit.models.User;

/**
 * ApiNotAuthorizedException
 */
public class ApiNotAuthorizedException extends RuntimeException {
    public ApiNotAuthorizedException(User user) {
        super(user == null ?
            "Anonymous users are not permitted to perform this operation" :
            String.format("%s (%d) is not authorized for this operation", user.getUsername(), user.getId())
        );
    }
}
