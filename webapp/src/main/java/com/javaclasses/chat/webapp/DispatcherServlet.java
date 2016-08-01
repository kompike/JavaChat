package com.javaclasses.chat.webapp;

import com.javaclasses.chat.webapp.handler.Handler;
import com.javaclasses.chat.webapp.handler.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Dispatcher servlet which handles all user's requests
 */
public class DispatcherServlet extends HttpServlet {

    private final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerRegistry registry = HandlerRegistry.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        execute(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        execute(request, response);
    }

    private void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (log.isInfoEnabled()) {
            log.info("Start executing user's request...");
        }

        final String uri = request.getRequestURI();
        final String method = request.getMethod().toLowerCase();
        final RequestContext requestContext = new RequestContext(uri, method);

        final Handler handler = registry.getHandler(requestContext);
        final JsonObject jsonObject = handler.process(request, response);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        final PrintWriter printWriter = response.getWriter();

        printWriter.write(jsonObject.generateJson());
        response.setStatus(jsonObject.getResponseStatusCode());

        if (log.isInfoEnabled()) {
            log.info("Response successfully created.");
        }
    }
}
