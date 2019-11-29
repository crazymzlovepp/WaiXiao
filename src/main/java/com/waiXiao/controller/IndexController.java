package com.waiXiao.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @param :
 * @author :     mym
 * @version :    V1.0
 * @date :       2019/11/29 17:37
 * @describe :   TODO
 * @return :
 */
@Controller
@RequestMapping("/index")
public class IndexController {
    private static final Log LOG = LogFactory.getLog(IndexController.class);
    @RequestMapping("/welcome")
    public ModelAndView welcome(ModelAndView model){
        try {
            model.setViewName("actionPage/welcome");
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return model;
    }
}
