package com.example.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.example.service.JobMstManageService;
import com.example.struct.SimpleForm;

@Controller
@RequestMapping(value = "/app")
public class AppController {

    private final List<String> list = new ArrayList<>();
    
    @Autowired
    private JobMstManageService service;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(SimpleForm form, Model model) {
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/io/{path}/{cnt}", method = RequestMethod.GET)
    public String io(@PathVariable String path, @PathVariable String cnt) throws IOException {
        write(path, Integer.parseInt(cnt));
        read(path);
        return "io";
    }
    
    @ResponseBody
    @RequestMapping(value = "/mem/{cnt}", method = RequestMethod.GET)
    public String mem(@PathVariable String cnt) throws IOException {
        List<String> tmp = IntStream.rangeClosed(1, Integer.parseInt(cnt)).boxed()
                .map(i -> UUID.randomUUID().toString()).collect(Collectors.toList());
        list.addAll(tmp);
        return list.toString();
    }
    
    @ResponseBody
    @RequestMapping(value = "/heavy/{sec}", method = RequestMethod.GET)
    public String heavy(@PathVariable String sec) throws IOException {
        long now = System.currentTimeMillis();
        long goal = now + 1000 * Integer.parseInt(sec);
        
        long cntr = 0;
        while (goal > System.currentTimeMillis()) {
            cntr +=1;
        }
        return String.valueOf(cntr);
    }
    
    @ResponseBody
    @RequestMapping(value = "/callHeavy/{sec}", method = RequestMethod.GET)
    public String callHeavy(@PathVariable String sec) throws IOException {
        final String uri = "http://node1:8080/api/heavy/" + sec;
        
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
         
        return result;
    }
    
    private void write(String path, int cnt) throws IOException {
        try (FileWriter fw = new FileWriter(new File(path));
                BufferedWriter bw = new BufferedWriter(fw)) {
            for (int i = 0; i<cnt; i++) {
                bw.write(UUID.randomUUID().toString());
                bw.newLine();
            }
            bw.flush();
        }
    }
    
    private void read(String path) throws IOException {
        try (FileReader fw = new FileReader(new File(path));
                BufferedReader br = new BufferedReader(fw)) {
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public String confirm(@Validated @ModelAttribute SimpleForm form, BindingResult result, Model model) {
        return "confirm";
    }

    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    public ModelAndView jobs() {
        ModelAndView view = new ModelAndView();
        view.setViewName("jobs");
        view.addObject("jobMsts", service.getAllJobMst());
        return view;
    }

    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    public String execute(@ModelAttribute SimpleForm form, Model model) {
        debugForm(form);
        return "redirect:/app/jobs";
    }

    private void debugForm(SimpleForm form) {
        System.out.println("***** DEBUG INFORMATION *****");
        System.out.println(form.getFtext());
        System.out.println("*****************************");
    }

}
