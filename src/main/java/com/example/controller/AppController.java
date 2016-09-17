package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.service.JobMstManageService;
import com.example.struct.SimpleForm;

@Controller
@RequestMapping(value = "/app")
public class AppController {

	@Autowired
	private JobMstManageService service;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(SimpleForm form, Model model) {
		return "index";
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
