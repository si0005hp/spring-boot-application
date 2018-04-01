package com.example.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class ApiController {

    @Autowired
    private HttpServletRequest requet;
    
    @ResponseBody
    @RequestMapping(value = "/heavy/{sec}", method = RequestMethod.GET)
    public String heavy(@PathVariable String sec) throws IOException {
        String client = requet.getRemoteAddr() + ":" + String.valueOf(requet.getRemotePort());
        System.out.println("Get request from " + client);
        
        long now = System.currentTimeMillis();
        long goal = now + 1000 * Integer.parseInt(sec);
        
        long cntr = 0;
        while (goal > System.currentTimeMillis()) {
            cntr +=1;
        }
        return String.valueOf(cntr);
    }

}
