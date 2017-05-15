package challenge;

/**
 * Tweet table equivalent
 */
public class Tweet {

    private Integer id;
    private Integer person_id;
    private String content;

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

    protected String getContent() {
        return content;
    }

    protected void setContent(String content) {
        this.content = content;
    }
}

