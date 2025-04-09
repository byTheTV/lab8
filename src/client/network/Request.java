package client.network;

import java.io.Serializable;
import java.util.Objects;
import client.models.*;



public class Request implements Serializable {
    private final String name;
    private final StudyGroup studyGroup;

    public Request(String name) {
        this.name = name;
        this.studyGroup = null;
    }

    public Request(String name, StudyGroup studyGroup) {
        this.name = name;
        this.studyGroup = studyGroup;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request response = (Request) o;
        return Objects.equals(name, response.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                '}';
    }
}
