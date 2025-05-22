package Server.network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import Common.models.StudyGroup;
import Common.models.User;
import Common.requests.AddRequest;
import Common.requests.AuthRequest;
import Common.requests.RemoveByIdRequest;
import Common.requests.RemoveLowerRequest;
import Common.requests.Request;
import Common.requests.UpdateIdRequest;
import Common.responses.AddResponse;
import Common.responses.AuthResponse;
import Common.responses.AverageOfTransferredStudentsResponse;
import Common.responses.ClearResponse;
import Common.responses.GroupCountingByFormOfEducationResponse;
import Common.responses.HeadResponse;
import Common.responses.HelpResponse;
import Common.responses.InfoResponse;
import Common.responses.PrintFieldAscendingGroupAdminResponse;
import Common.responses.RemoveByIdResponse;
import Common.responses.RemoveHeadResponse;
import Common.responses.RemoveLowerResponse;
import Common.responses.Response;
import Common.responses.ShowResponse;
import Common.responses.UpdateIdResponse;
import Server.collectionManagers.StudyGroupCollectionManager;
import Server.services.AuthService;

public class ServerRequestHandler implements RequestHandler {
    private final StudyGroupCollectionManager collectionManager;
    private final Map<String, Function<Request, Response>> requestHandlers;
    private final AuthService authService;
    private final ConcurrentHashMap<String, String> userUids;

    public ServerRequestHandler(StudyGroupCollectionManager collectionManager, AuthService authService) {
        this.collectionManager = collectionManager;
        this.authService = authService;
        this.requestHandlers = new HashMap<>();
        this.userUids = new ConcurrentHashMap<>();
        initializeRequestHandlers();
    }

    private void initializeRequestHandlers() {
        requestHandlers.put("auth", request -> {
            AuthRequest authRequest = (AuthRequest) request;
            String login = authRequest.getLogin();
            
            String oldUid = userUids.get(login);
            if (oldUid != null) {
                System.out.println("[Сервер] Пользователь " + login + " уже подключен. Отключаем старое соединение.");
            }

            User user = authService.authenticateOrRegister(
                    login,
                    authRequest.getPassword()
            );

            if (user != null) {
                String uid = UUID.randomUUID().toString();
                userUids.put(login, uid);
                
                String message = oldUid != null ? 
                    "Предыдущее подключение было отключено" : 
                    "Успешное подключение";
                System.out.println("[Сервер] " + message + " для пользователя " + login);
                
                return new AuthResponse(
                        AuthResponse.AuthStatus.AUTH_SUCCESS,
                        message,
                        user.getId(),
                        uid
                );
            }
            return new AuthResponse(
                    AuthResponse.AuthStatus.AUTH_FAILED,
                    "Неверные учетные данные",
                    null,
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
                StudyGroup targetGroup = collectionManager.getById(removeRequest.getId());
                
                if (targetGroup == null) {
                    throw new IllegalArgumentException("Элемент с ID " + removeRequest.getId() + " не найден");
                }

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
        if (!request.getName().equals("auth")) {
            String login = request.getLogin();
            String storedUid = userUids.get(login);
            
            if (storedUid == null) {
                System.out.println("[Сервер] Попытка выполнить команду без аутентификации от пользователя " + login);
                return new Response("error", "Требуется аутентификация") {};
            }
            
            if (!storedUid.equals(request.getUid())) {
                System.out.println("[Сервер] Попытка использовать недействительное соединение от пользователя " + login);
                return new Response("error", "Сессия была прервана новым подключением") {};
            }
        }
        
        collectionManager.setCurrentUser(request.getLogin());
        
        Function<Request, Response> handler = requestHandlers.get(request.getName());
        if (handler != null) {
            try {
                return handler.apply(request);
            } catch (Exception e) {
                System.err.println("[Сервер] Ошибка при обработке запроса " + request.getName() + " от пользователя " + request.getLogin() + ": " + e.getMessage());
                return new Response("error", "Внутренняя ошибка сервера: " + e.getMessage()) {};
            }
        }
        return new Response("error", "Неизвестный тип запроса: " + request.getName()) {};
    }
} 