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
    private final Integer userId;
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000;

    public StudyGroupService(TCPClient client, String login, String password, String uid, Integer userId) {
        this.client = client;
        this.login = login;
        this.password = password;
        this.uid = uid;
        this.userId = userId;
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
                throw e;
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
            // Create a new group without an ID for adding
            StudyGroup newGroup = new StudyGroup();
            newGroup.setName(group.getName());
            newGroup.setCoordinates(group.getCoordinates());
            newGroup.setStudentsCount(group.getStudentsCount());
            newGroup.setExpelledStudents(group.getExpelledStudents());
            newGroup.setTransferredStudents(group.getTransferredStudents());
            newGroup.setFormOfEducation(group.getFormOfEducation());
            newGroup.setGroupAdmin(group.getGroupAdmin());
            newGroup.setUserId(userId); // Set the current user's ID
            
            AddRequest request = new AddRequest(newGroup, login, password);
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
            // First get the current group to preserve its user ID
            ShowRequest showRequest = new ShowRequest(login, password);
            showRequest.setUid(uid);
            client.sendRequest(showRequest);
            ShowResponse showResponse = (ShowResponse) client.receiveResponse();
            if (showResponse.getError() != null) {
                throw new Exception("Server error: " + showResponse.getError());
            }
            
            // Find the original group and preserve its user ID
            showResponse.getCollection().stream()
                .filter(g -> g.getId().equals(group.getId()))
                .findFirst()
                .ifPresent(originalGroup -> group.setUserId(originalGroup.getUserId()));
            
            // Now send the update request
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

    public boolean isGroupOwner(Long id) throws Exception {
        return executeWithRetry(() -> {
            ShowRequest request = new ShowRequest(login, password);
            request.setUid(uid);
            client.sendRequest(request);
            ShowResponse response = (ShowResponse) client.receiveResponse();
            if (response.getError() != null) {
                throw new Exception("Server error: " + response.getError());
            }
            
            // Find the group and check if it belongs to the current user
            return response.getCollection().stream()
                    .filter(group -> group.getId().equals(id))
                    .findFirst()
                    .map(group -> {
                        // Debug output
      //                  System.out.println("Group ID: " + group.getId());
      //                  System.out.println("Group User ID: " + group.getUserId());
      //                  System.out.println("Current User ID: " + userId);
                        return group.getUserId() != null && group.getUserId().equals(userId);
                    })
                    .orElse(false);
        });
    }

    public void close() {
        if (client != null) {
            client.close();
        }
    }

    public Integer getUserId() {
        return userId;
    }
} 