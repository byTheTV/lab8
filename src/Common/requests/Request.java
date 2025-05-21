package Common.requests;

import java.io.Serializable;
import java.util.Objects;

public abstract class Request implements Serializable {
    private final String name;
    private final String login;
    private final String password;

    public Request(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request response = (Request) o;
        return Objects.equals(name, response.name) &&
               Objects.equals(login, response.login) &&
               Objects.equals(password, response.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, login, password);
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
