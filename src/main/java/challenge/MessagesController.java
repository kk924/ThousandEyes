package challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Class which implements REST API Endpoints for the Tweet object
 */
@RestController
public class MessagesController {


    @Autowired
    private DatabaseRepository databaseRepository;

    /**
     * REST API which implements Feature 1
     * <p>
     * Method which returns the current user's tweets, current user following's tweets
     * and filter them by a search value.
     * <p>
     * Add "Search" Header to the GET Request with the search token value
     *
     * @param request : The HTTP Request
     * @return : List of Tweets
     */
    @RequestMapping(value = "/get-tweets", method = RequestMethod.GET)
    public List<Tweet> getTweets(HttpServletRequest request) {
        Integer userId = databaseRepository.getUserId(request);
        return databaseRepository.getUserMessages(userId, request.getHeader("Search"));
    }
}
