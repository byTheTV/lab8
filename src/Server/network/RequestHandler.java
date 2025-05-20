package Server.network;

import Common.requests.Request;
import Common.responses.Response;

public interface RequestHandler {
    Response handleRequest(Request request);
} 