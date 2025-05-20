package Server.network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import Common.models.StudyGroup;
import Common.models.User;
import Server.services.AuthService;
import Server.collectionManagers.StudyGroupCollectionManager;
import Common.responses.*;
import Common.requests.*;

public class ServerRequestHandler implements RequestHandler {
    private final StudyGroupCollectionManager collectionManager;
    private final Map<String, Function<Request, Response>> requestHandlers;
    private final AuthService authService;

    public ServerRequestHandler(StudyGroupCollectionManager collectionManager, AuthService authService) {
        this.collectionManager = collectionManager;
        this.authService = authService;
        this.requestHandlers = new HashMap<>();
        initializeRequestHandlers();
    }

    private void initializeRequestHandlers() {

        requestHandlers.put("auth", request -> {
            AuthRequest authRequest = (AuthRequest) request;
            User user = authService.authenticateOrRegister(
                    authRequest.getLogin(),
                    authRequest.getPassword()
            );

            if (user != null) {
                return new AuthResponse(
                        AuthResponse.AuthStatus.AUTH_SUCCESS,
                        null,
                        user.getId()
                );
            }
            return new AuthResponse(
                    AuthResponse.AuthStatus.AUTH_FAILED,
                    "Неверные учетные данные",
                    null
            );
        });

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
                System.out.println("Обработка запроса head...");
                StudyGroup headGroup = collectionManager.getHead();
                System.out.println("Получен первый элемент коллекции: " + (headGroup != null ? headGroup.toString() : "null"));
                return new HeadResponse(headGroup, null);
            } catch (Exception e) {
                System.err.println("Ошибка при обработке запроса head: " + e.getMessage());
                e.printStackTrace();
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
                    collectionManager.getSize(),
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
                // Находим объект по ID из коллекции
                StudyGroup targetGroup = collectionManager.getById(removeRequest.getId());
                
                if (targetGroup == null) {
                    throw new IllegalArgumentException("Элемент с ID " + removeRequest.getId() + " не найден");
                }

                // Удаляем элементы, меньшие чем targetGroup
                int count = collectionManager.removeLower(targetGroup);
                return new RemoveLowerResponse(count, null);
            } catch (Exception e) {
                return new RemoveLowerResponse(0, e.getMessage());
            }
        });

        requestHandlers.put("show", request -> {
            try {
                List<StudyGroup> groups = collectionManager.show();
                if (groups.isEmpty()) {
                    return new ShowResponse(null, "Коллекция пуста");
                }
                return new ShowResponse(groups, null);
            } catch (Exception e) {
                return new ShowResponse(null, e.getMessage());
            }
        });

        requestHandlers.put("update_id", request -> {
            UpdateIdRequest updateRequest = (UpdateIdRequest) request;
            try {
                collectionManager.updateById(updateRequest.getId(), updateRequest.getUpdatedGroup());
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
        Function<Request, Response> handler = requestHandlers.get(request.getName());
        if (handler != null) {
            return handler.apply(request);
        }
        // Возвращаем базовый Response с ошибкой
        return new Response("error", "Unknown request type: " + request.getName()) {};
    }
} 