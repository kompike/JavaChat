package com.javaclasses.chat.webapp.listener;

import com.javaclasses.chat.webapp.controller.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Context listener for available controllers initialization
 */
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        UserController.init();
        ChatController.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
