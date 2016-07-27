package com.javaclasses.webapp.command.impl;

import com.javaclasses.webapp.command.Controller;

import javax.servlet.http.HttpServletRequest;

public class HomeController implements Controller {

    @Override
    public String execute(HttpServletRequest request) {
        return "<html><body>" +
                "<h1>Main page</h1>" +
                "</body></html>";
    }
}
