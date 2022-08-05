package Model;

import android.widget.TextView;

public class Users {

    private String active,admin,created_at,email,empid,updated_at;

    public Users() {
    }

    public Users(String active, String admin, String created_at, String email, String empid, String updated_at) {
        this.active = active;
        this.admin = admin;
        this.created_at = created_at;
        this.email = email;
        this.empid = empid;
        this.updated_at = updated_at;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
