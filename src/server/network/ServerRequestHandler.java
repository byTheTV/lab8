package Server.network;

import Common.requests.*;
import Common.responses.*;
import Server.collectionManagers.StudyGroupCollectionManager;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

public class ServerRequestHandler implements RequestHandler {
    private final StudyGroupCollectionManager collectionManager;
    private final Map<String, Function<Request, Response>> requestHandlers;

    public ServerRequestHandler(StudyGroupCollectionManager collectionManager) {
        this.collectionManager = collectionManager;
        this.requestHandlers = new HashMap<>();
        initializeRequestHandlers();
    }

    private void initializeRequestHandlers() {
        requestHandlers.put("add", request -> {
            AddRequest addRequest = (AddRequest) request;
            try {
                collectionManager.add(addRequest.getStudyGroup());
                return new AddResponse(null);
            } catch (Exception e) {
                return new AddResponse(e.getMessage());
            }
        });

        requestHandlers.put("clear", request -> {
            try {
                collectionManager.clear();
                return new ClearResponse(null);
            } catch (Exception e) {
                return new ClearResponse(e.getMessage());
            }
        });

/*
        requestHandlers.put("execute_script", request -> {
            ExecuteScriptRequest scriptRequest = (ExecuteScriptRequest) request;
            try {
                List<String> results = collectionManager.executeScript(scriptRequest.getScriptPath());
                return new ExecuteScriptResponse(results, null);
            } catch (Exception e) {
                return new ExecuteScriptResponse(null, e.getMessage());
            }
        });
*/
        requestHandlers.put("group_counting_by_form_of_education", request -> {
            try {
                Map<String, Integer> counts = collectionManager.groupCountingByFormOfEducation();
                return new GroupCountingByFormOfEducationResponse(counts, null);
            } catch (Exception e) {
                return new GroupCountingByFormOfEducationResponse(null, e.getMessage());
            }
        });

        requestHandlers.put("head", request -> {
            try {
                return new HeadResponse(collectionManager.getHead(), null);
            } catch (Exception e) {
                return new HeadResponse(null, e.getMessage());
            }
        });

        requestHandlers.put("help", request -> {
            try {
                Map<String, String> descriptions = collectionManager.getCommandDescriptions();
                return new HelpResponse(descriptions, null);
            } catch (Exception e) {
                return new HelpResponse(null, e.getMessage());
            }
        });

        requestHandlers.put("info", request -> {
            try {
                return new InfoResponse(
                    collectionManager.getCollectionType(),
                    collectionManager.getCreationDate(),
                    collectionManager.getCollectionSize(),
                    null
                );
            } catch (Exception e) {
                return new InfoResponse(null, null, 0, e.getMessage());
            }
        });

        requestHandlers.put("print_field_ascending_group_admin", request -> {
            try {
                List<String> adminNames = collectionManager.printFieldAscendingGroupAdmin();
                return new PrintFieldAscendingGroupAdminResponse(adminNames, null);
            } catch (Exception e) {
                return new PrintFieldAscendingGroupAdminResponse(null, e.getMessage());
            }
        });

        requestHandlers.put("remove_by_id", request -> {
            RemoveByIdRequest removeRequest = (RemoveByIdRequest) request;
            try {
                collectionManager.removeById(removeRequest.getId());
                return new RemoveByIdResponse(null);
            } catch (Exception e) {
                return new RemoveByIdResponse(e.getMessage());
            }
        });

        requestHandlers.put("remove_head", request -> {
            try {
                return new RemoveHeadResponse(collectionManager.removeHead(), null);
            } catch (Exception e) {
                return new RemoveHeadResponse(null, e.getMessage());
            }
        });

        requestHandlers.put("remove_lower", request -> {
            RemoveLowerRequest removeRequest = (RemoveLowerRequest) request;
            try {
                int count = collectionManager.removeLower(removeRequest.getStudyGroup());
                return new RemoveLowerResponse(count, null);
            } catch (Exception e) {
                return new RemoveLowerResponse(0, e.getMessage());
            }
        });

        requestHandlers.put("show", request -> {
            try {
                return new ShowResponse(collectionManager.show(), null);
            } catch (Exception e) {
                return new ShowResponse(null, e.getMessage());
            }
        });

        requestHandlers.put("update_id", request -> {
            UpdateIdRequest updateRequest = (UpdateIdRequest) request;
            try {
                collectionManager.updateId(updateRequest.getId(), updateRequest.getStudyGroup());
                return new UpdateIdResponse(null);
            } catch (Exception e) {
                return new UpdateIdResponse(e.getMessage());
            }
        });

        requestHandlers.put("average_of_transferred_students", request -> {
            try {
                double average = collectionManager.averageOfTransferredStudents();
                return new AverageOfTransferredStudentsResponse(average, null);
            } catch (Exception e) {
                return new AverageOfTransferredStudentsResponse(0, e.getMessage());
            }
        });
    }

    @Override
    public Response handleRequest(Request request) {
        try {
            Function<Request, Response> handler = requestHandlers.get(request.getName());
            if (handler != null) {
                return handler.apply(request);
            }
            return new ErrorResponse("Unknown request type", "Unknown request type");
        } catch (Exception e) {
            return new ErrorResponse("Error processing request", e.getMessage());
        }
    }
} 