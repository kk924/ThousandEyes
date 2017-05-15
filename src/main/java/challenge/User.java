package challenge;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import customserializers.UserSerializer;

import java.io.Serializable;

/**
 * Person table equivalent
 */
@JsonSerialize(using = UserSerializer.class)
public class User implements Serializable {

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}


