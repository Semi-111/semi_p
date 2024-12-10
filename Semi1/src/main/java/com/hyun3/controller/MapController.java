package com.hyun3.controller;


import com.hyun3.mvc.annotation.Controller;
import com.hyun3.mvc.annotation.RequestMapping;
import com.hyun3.mvc.annotation.RequestMethod;
import com.hyun3.mvc.view.ModelAndView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.hyun3.mvc.annotation.RequestMethod.GET;

@Controller
public class MapController {

    @RequestMapping(value = "/map",method = GET)
    public ModelAndView mapMain(HttpServletRequest req , HttpServletResponse resp) throws ServletException, IOException {
        ModelAndView mav = new ModelAndView("map/map");

        return mav;
    }




}
