package com.javaclasses.webapp;

import com.javaclasses.webapp.command.Controller;
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

        final Controller controller = registry.getController(uri, method);
        final JsonEntity jsonEntity = controller.execute(request);

        final PrintWriter printWriter = response.getWriter();
        printWriter.write(jsonEntity.generateJson());
        response.setStatus(jsonEntity.getResponseStatus());

        if (log.isInfoEnabled()) {
            log.info("Response successfully created.");
        }
    }
}
