package Common.requests;

import java.io.Serializable;
import java.util.Objects;

public abstract class Request implements Serializable {
    private final String name;
    private final String login;
    private final String password;
    private String uid;

    public Request(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.uid = null;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request response = (Request) o;
        return Objects.equals(name, response.name) &&
               Objects.equals(login, response.login) &&
               Objects.equals(password, response.password) &&
               Objects.equals(uid, response.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, login, password, uid);
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
