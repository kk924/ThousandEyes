package challenge;

/**
 * Followers Table equivalent
 */
public class Followers {

    private Integer id;
    private Integer person_id;
    private Integer follower_person_id;

    protected Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    protected Integer getPerson_id() {
        return person_id;
    }

    protected void setPerson_id(Integer person_id) {
        this.person_id = person_id;
    }

    protected Integer getFollower_person_id() {
        return follower_person_id;
    }

    protected void setFollower_person_id(Integer follower_person_id) {
        this.follower_person_id = follower_person_id;
    }
}

