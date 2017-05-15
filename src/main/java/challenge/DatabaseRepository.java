package challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * Class which serves as the Database Repository
 * Accesses the database and executes queries as required by the Rest API Endpoints
 */
@Repository
public class DatabaseRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * Util function which returns the current user's ID as per the authorization credentials
     *
     * @param request : The HTTP Request
     * @return : The user ID in the Authorization Header value
     */
    Integer getUserId(HttpServletRequest request) {
        byte[] decodedBytes = Base64.getDecoder().decode(request.getHeader("Authorization").split(" ")[1]);
        String authorizationContents = new String(decodedBytes);
        return Integer.parseInt(authorizationContents.split(":")[0]);
    }

    /**
     * Method which implements Feature 1
     * <p>
     * This method returns the tweet messages from the database which are
     * 1) Associated with the current user's ID as per the authentication credentials.
     * 2) Associated with other users the current user is following
     * 3) Which contain messages which contain the search key as described in the
     * "Search" Header in the HTTP GET Request
     *
     * @param userId          : The current user's ID as per the authentication credentials
     * @param searchParameter : The search key as described in the "Search" Header in the HTTP GET Request
     * @return : The list of Tweet objects which contains messages which satisfies the constraints
     */
    @Transactional
    List<Tweet> getUserMessages(Integer userId, String searchParameter) {

        String query;
        Map parametersMap = new HashMap();
        parametersMap.put("userId", userId);

        // Search for tweets which belong to current user or current user's followers
        // And contains the searchParameter if it not null
        if (searchParameter == null || searchParameter.trim().isEmpty()) {
            query = "select distinct t.* from tweet t inner join followers f on t.person_id = f.person_id where (t.person_id = :userId or (t.person_id = f.person_id and f.follower_person_id = :userId))";
        } else {
            query = "select distinct t.* from tweet t inner join followers f on t.person_id = f.person_id where (t.person_id = :userId or (t.person_id = f.person_id and f.follower_person_id = :userId)) and t.content like :searchParameter";
            parametersMap.put("searchParameter", "%" + searchParameter + "%");
        }
        SqlParameterSource namedParameters = new MapSqlParameterSource(parametersMap);


        return this.namedParameterJdbcTemplate.query(query, namedParameters, new TweetRowMapper());
    }

    /**
     * Method which implements Feature 2
     * <p>
     * This method returns the current user's followers from the database
     *
     * @param userId : The current user's ID as per the authentication credentials
     * @return : The list of Person objects which contains the current user's followers
     */
    @Transactional
    List<User> getUserFollowersList(Integer userId) {
        String query = "select p.* from person p, followers f where p.id = f.follower_person_id and f.person_id = :userId";
        Map parametersMap = new HashMap();
        parametersMap.put("userId", userId);
        SqlParameterSource namedParameters = new MapSqlParameterSource(parametersMap);
        return this.namedParameterJdbcTemplate.query(query, namedParameters, new UserRowMapper());
    }

    /**
     * Method which implements Feature 2
     * <p>
     * This method returns the people the current user is following from the database
     *
     * @param userId : The current user's ID as per the authentication credentials
     * @return : The list of Person objects which contains people the current user is following
     */
    @Transactional
    List<User> getUserFollowingList(Integer userId) {
        String query = "select p.* from person p, followers f where p.id = f.person_id and f.follower_person_id = :userId";
        Map parametersMap = new HashMap();
        parametersMap.put("userId", userId);
        SqlParameterSource namedParameters = new MapSqlParameterSource(parametersMap);
        return this.namedParameterJdbcTemplate.query(query, namedParameters, new UserRowMapper());
    }

    /**
     * Method which implements Feature 3
     * <p>
     * This method inserts a new record into the database
     * It adds a new record of the current user following another person
     *
     * @param followerPersonId : The current user's ID as per the authentication credentials
     * @param personId         : The ID of the person the current user would like to follow
     */
    @Transactional
    void insertUserFollowing(Integer followerPersonId, Integer personId) {
        Map parametersMap = new HashMap();
        parametersMap.put("personId", personId);
        parametersMap.put("followerPersonId", followerPersonId);
        SqlParameterSource namedParameters = new MapSqlParameterSource(parametersMap);

        String precheck_query = "Select f.* from followers f where f.person_id = :personId and f.follower_person_id = :followerPersonId limit 1";
        List<Followers> existingRow = this.namedParameterJdbcTemplate.query(precheck_query, namedParameters, new FollowersRowMapper());
        if (!(existingRow != null && existingRow.size() > 0)) {
            String query = "Insert into followers (person_id,follower_person_id) values (:personId, :followerPersonId)";
            this.namedParameterJdbcTemplate.update(query, namedParameters);
        }
    }

    /**
     * Method which implements Feature 4
     * <p>
     * This method deletes a Follower record from the database
     * It deletes the record of the current user following another person
     *
     * @param followerPersonId : The current user's ID as per the authentication credentials
     * @param personId         : The ID of the person the current user would like to unfollow
     */
    @Transactional
    void deleteUserFollowing(Integer followerPersonId, Integer personId) {
        String query = "Delete from followers where person_id = :personId and follower_person_id = :followerPersonId";
        Map parametersMap = new HashMap();
        parametersMap.put("personId", personId);
        parametersMap.put("followerPersonId", followerPersonId);
        SqlParameterSource namedParameters = new MapSqlParameterSource(parametersMap);
        this.namedParameterJdbcTemplate.update(query, namedParameters);
    }

    /**
     * Method which implements Extension Feature 1:
     * Find Shortest Distance
     * <p>
     * This method computes the number of hops between the current user and the target user
     * It converts the rows in the Followers Table (which are essentially edges in
     * the directed graph) into a HashMap "graph" which represents the Directed Graph.
     * The edge starts at the node designated with the value in follower_person_id column
     * and ends at the node designated with the value in person_id column
     * <p>
     * After creating the graph data structure, it calls the BFS method to find the shortest path
     * from source to goal
     *
     * @param source : The current user's ID as per the authentication credentials
     * @param goal   : The other person's ID which is the destination
     * @return : The number of hops between the source current user and the goal user
     * Returns 0 if the source and destination is the same
     * Returns a positive number which indicates the number of hops if a path exists
     * Returns -1 if no path exists between source and goal
     */
    @Transactional
    Integer findShortestDistance(Integer source, Integer goal) {
        String query = "Select * from followers";
        Map<Integer, Set<Integer>> graph = new HashMap<Integer, Set<Integer>>();
        SqlParameterSource namedParameters = new MapSqlParameterSource();

        List<Map<String, Object>> followerRecords = this.namedParameterJdbcTemplate.queryForList(query, namedParameters);

        // For each row in Follower Table, insert into the Map graph. Convert the BigDecimal into an Integer
        for (Map followerRecord : followerRecords) {
            if (graph.containsKey(((BigDecimal) followerRecord.get("follower_person_id")).intValue())) {
                graph.get(((BigDecimal) followerRecord.get("follower_person_id")).intValue()).add(((BigDecimal) followerRecord.get("person_id")).intValue());
            } else {
                graph.put(((BigDecimal) (followerRecord.get("follower_person_id"))).intValue(), new HashSet<>());
                graph.get(((BigDecimal) followerRecord.get("follower_person_id")).intValue()).add(((BigDecimal) followerRecord.get("person_id")).intValue());
            }
        }
        return BFS(graph, source, goal);
    }

    /**
     * Method which implements Extension Feature 1:
     * Find Shortest Distance
     * <p>
     * Computes the distance between the source user and the goal user
     * Uses BFS since the edge weights are equal
     *
     * @param graph  : HashMap which represents the directed graph
     * @param source : The current user's ID as per the authentication credentials
     * @param goal   : The other person's ID which is the destination
     * @return : The number of hops between the source current user and the goal user
     * Returns 0 if the source and destination is the same
     * Returns a positive number which indicates the number of hops if a path exists
     * Returns -1 if no path exists between source and goal
     */
    Integer BFS(Map<Integer, Set<Integer>> graph, int source, int goal) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        Integer distance = -1;
        if (source == goal) {
            return 0;
        }
        if (!graph.containsKey(source)) {
            return -1;
        }
        queue.add(source);
        Queue<Integer> childQueue = new LinkedList<>();
        while (!queue.isEmpty()) {
            int currentNode = queue.remove();
            if (!visited.contains(currentNode)) {
                if (currentNode == goal) {
                    return distance + 1;
                }
                visited.add(currentNode);
                if (graph.get(currentNode) != null && !graph.get(currentNode).isEmpty()) {
                    childQueue.addAll(graph.get(currentNode));
                }
            }
            if (queue.isEmpty()) {
                if (childQueue.isEmpty()) {
                    break;
                } else {
                    System.out.println(childQueue.toString());
                    queue.addAll(childQueue);
                    childQueue.clear();
                    distance++;
                }
            }
        }
        return -1;
    }
}
