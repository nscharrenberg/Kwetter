package controllers.viewModels;

import java.io.Serializable;

public class RoleViewModel implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
