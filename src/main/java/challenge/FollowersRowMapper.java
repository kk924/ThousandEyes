package challenge;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FollowersRowMapper implements RowMapper<Followers>{

    /**
     * Method which is used to Map the result in the Database ResultSet into a Follower Object
     * @param rs            : ResultSet
     * @param rowNum        : Row Number
     * @return              : A Follower Object
     * @throws SQLException
     */
    @Override
    public Followers mapRow(ResultSet rs, int rowNum) throws SQLException{
        Followers followers = new Followers();
        followers.setId(rs.getInt("id"));
        followers.setPerson_id(rs.getInt("person_id"));
        followers.setFollower_person_id(rs.getInt("follower_person_id"));

        return followers;
    }

}
