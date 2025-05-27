package GUI;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Client.network.TCPClient;
import Common.models.StudyGroup;
import Common.requests.AddRequest;
import Common.requests.ClearRequest;
import Common.requests.RemoveByIdRequest;
import Common.requests.ShowRequest;
import Common.requests.UpdateIdRequest;
import Common.responses.AddResponse;
import Common.responses.Response;
import Common.responses.ShowResponse;

public class StudyGroupService {
    private TCPClient client;
    private final String login;
    private final String password;
    private String uid;
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000;

    public StudyGroupService(TCPClient client, String login, String password, String uid) {
        this.client = client;
        this.login = login;
        this.password = password;
        this.uid = uid;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void ensureConnected() throws IOException {
        if (!client.isConnected()) {
            // Try to reconnect
            client = new TCPClient(InetAddress.getByName("localhost"), 55555);
        }
    }

    private <T> T executeWithRetry(Operation<T> operation) throws Exception {
        Exception lastException = null;
        for (int i = 0; i < MAX_RETRIES; i++) {
            try {
                ensureConnected();
                return operation.execute();
            } catch (IOException e) {
                lastException = e;
                if (i < MAX_RETRIES - 1) {
                    sleep(RETRY_DELAY_MS);
                }
            } catch (Exception e) {
                lastException = e;
                throw e; // Don't retry for non-IO exceptions
            }
        }
        throw lastException;
    }

    @FunctionalInterface
    private interface Operation<T> {
        T execute() throws Exception;
    }

    public List<StudyGroup> getAllGroups() throws Exception {
        return executeWithRetry(() -> {
            ShowRequest request = new ShowRequest(login, password);
            request.setUid(uid);
            client.sendRequest(request);
            ShowResponse response = (ShowResponse) client.receiveResponse();
            if (response.getError() != null) {
                throw new Exception("Server error: " + response.getError());
            }
            return new ArrayList<>(response.getCollection());
        });
    }

    public void addGroup(StudyGroup group) throws Exception {
        executeWithRetry(() -> {
            AddRequest request = new AddRequest(group, login, password);
            request.setUid(uid);
            client.sendRequest(request);
            AddResponse response = (AddResponse) client.receiveResponse();
            if (response.getError() != null) {
                throw new Exception("Server error: " + response.getError());
            }
            return null;
        });
    }

    public void updateGroup(StudyGroup group) throws Exception {
        executeWithRetry(() -> {
            UpdateIdRequest request = new UpdateIdRequest(group.getId().longValue(), group, login, password);
            request.setUid(uid);
            client.sendRequest(request);
            Response response = client.receiveResponse();
            if (response.getError() != null) {
                throw new Exception("Server error: " + response.getError());
            }
            return null;
        });
    }

    public void removeGroup(Long id) throws Exception {
        executeWithRetry(() -> {
            RemoveByIdRequest request = new RemoveByIdRequest(id, login, password);
            request.setUid(uid);
            client.sendRequest(request);
            Response response = client.receiveResponse();
            if (response.getError() != null) {
                throw new Exception("Server error: " + response.getError());
            }
            return null;
        });
    }

    public void clearGroups() throws Exception {
        executeWithRetry(() -> {
            ClearRequest request = new ClearRequest(login, password);
            request.setUid(uid);
            client.sendRequest(request);
            Response response = client.receiveResponse();
            if (response.getError() != null) {
                throw new Exception("Server error: " + response.getError());
            }
            return null;
        });
    }

    public List<StudyGroup> filterGroups(String nameFilter) throws Exception {
        List<StudyGroup> allGroups = getAllGroups();
        if (nameFilter == null || nameFilter.trim().isEmpty()) {
            return allGroups;
        }
        String lowerCaseFilter = nameFilter.toLowerCase();
        return allGroups.stream()
                .filter(group -> group.getName().toLowerCase().contains(lowerCaseFilter))
                .collect(Collectors.toList());
    }

    public void close() {
        if (client != null) {
            client.close();
        }
    }
} 