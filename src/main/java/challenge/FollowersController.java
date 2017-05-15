package challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Class which implements REST API Endpoints for the Follower object
 */
@RestController
public class FollowersController {

    @Autowired
    private DatabaseRepository databaseRepository;

    /**
     * REST API which implements Feature 2
     * <p>
     * Returns the list of followers of the current user
     *
     * @param request : The HTTP Request
     * @return : The current user's followers
     */
    @RequestMapping(value = "/get-followers", method = RequestMethod.GET)
    public List<User> getUserFollowers(HttpServletRequest request) {
        Integer userId = databaseRepository.getUserId(request);
        return databaseRepository.getUserFollowersList(userId);
    }

    /**
     * REST API which implements Feature 2
     * <p>
     * Returns the list of users the current user is following
     *
     * @param request : The HTTP Request
     * @return : The users the current user is following
     */
    @RequestMapping(value = "/get-following", method = RequestMethod.GET)
    public List<User> getUserFollowing(HttpServletRequest request) {
        Integer userId = databaseRepository.getUserId(request);
        return databaseRepository.getUserFollowingList(userId);
    }

    /**
     * REST API which implements Feature 3
     * <p>
     * Method inserts a record indicating that the current user is
     * following the other user. The other user is identified by its ID in the Person Table
     * This ID is retrieved from the parameters with the name "person_id"
     * Add "person_id" Parameter to the GET Request with the value
     *
     * @param request : The HTTP Request
     */
    @RequestMapping(value = "/follow-user", method = RequestMethod.GET)
    public void addUserFollowing(HttpServletRequest request) {
        Integer userId = databaseRepository.getUserId(request);
        databaseRepository.insertUserFollowing(userId, Integer.parseInt(request.getParameter("person_id")));
    }

    /**
     * REST API which implements Feature 4
     * <p>
     * Method deletes a record indicating that the current user has unfollowed
     * the other user. The other user is identified by its ID in the Person Table
     * This ID is retrieved from the parameters with the name "person_id"
     * Add "person_id" Parameter to the GET Request with the value
     *
     * @param request : The HTTP Request
     */
    @RequestMapping(value = "/unfollow-user", method = RequestMethod.GET)

    public void removeUserFollowing(HttpServletRequest request) {
        Integer userId = databaseRepository.getUserId(request);
        databaseRepository.deleteUserFollowing(userId, Integer.parseInt(request.getParameter("person_id")));
    }

    /**
     * REST API which implements Extension Feature 1:
     * Find Shortest Distance
     * <p>
     * Computes the number of hops between the current user and the target user
     * The target user is identified by its ID in the Person Table
     * This ID is retrieved from the parameters with the name "person_id"
     * Add "person_id" Parameter to the GET Request with the value
     *
     * @param request : The HTTP Request
     * @return : The number of hops between the current user and target user
     * -1 if there is no valid path
     */
    @RequestMapping(value = "/shortest-distance", method = RequestMethod.GET)
    public Integer shortestDistance(HttpServletRequest request) {
        Integer userId = databaseRepository.getUserId(request);
        return databaseRepository.findShortestDistance(userId, Integer.parseInt(request.getParameter("person_id")));
    }

}
