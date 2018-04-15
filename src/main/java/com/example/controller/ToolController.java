package com.example.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.prometheus.client.Counter;

@RestController
@RequestMapping(value = "/tool")
@Configuration
public class ToolController {

    @Autowired
    private HttpServletResponse response;

    @RequestMapping(value = "/plainString", method = RequestMethod.GET)
    public String plainString() {
        return "RESULT";
    }

    @RequestMapping(value = "/printWriter", method = RequestMethod.GET)
    public void printWriter() throws IOException {
        try (PrintWriter pw = response.getWriter()) {
            pw.write("RESULT");
        }
    }

    static final Counter requests = Counter.build().name("requests_total").help("Total requests.").register();
    @RequestMapping(value = "/inc", method = RequestMethod.GET)
    public void inc() {
        requests.inc();
    }
}
