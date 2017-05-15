package challenge;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

class TweetRowMapper implements RowMapper<Tweet> {
    /**
     * Method which is used to Map the result in the Database ResultSet into a Tweet Object
     *
     * @param rs     : ResultSet
     * @param rowNum : Row Number
     * @return : Tweet Object
     * @throws SQLException
     */
    @Override
    public Tweet mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tweet tweet = new Tweet();
        tweet.setId(rs.getInt("id"));
        tweet.setPerson_id(rs.getInt("person_id"));
        tweet.setContent(rs.getString("content"));

        return tweet;

    }
}
