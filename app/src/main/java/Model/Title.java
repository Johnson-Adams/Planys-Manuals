package Model;

public class Title {

    private String active,created_at,key,name,numberarticles,updated_at,userid;

    public Title() {
    }

    public Title(String active,String created_at,String key, String name, String numberarticles,String updated_at, String userid) {
        this.active = active;
        this.created_at = created_at;
        this.key = key;
        this.name = name;
        this.numberarticles = numberarticles;
        this.updated_at = updated_at;
        this.userid = userid;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberarticles() {
        return numberarticles;
    }

    public void setNumberarticles(String numberarticles) {
        this.numberarticles = numberarticles;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
