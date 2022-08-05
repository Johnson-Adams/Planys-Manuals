package Model;

import java.util.ArrayList;
import java.util.Map;

public class listbasicinfo {
    private String active,created_at,key,list_id,name,type_id,updated_at,user_id;

    public listbasicinfo(String active, String created_at, String key, String list_id, String name, String type_id, String updated_at, String user_id) {
        this.active = active;
        this.created_at = created_at;
        this.key = key;
        this.list_id = list_id;
        this.name = name;
        this.type_id = type_id;
        this.updated_at = updated_at;
        this.user_id = user_id;
    }

    public listbasicinfo() {
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
