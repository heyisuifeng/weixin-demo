package com.leaf.controller;

import com.leaf.entity.wxResult.JSConfig;
import com.leaf.service.WXFunctionService;
import com.leaf.utils.WXJSSDKUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/******************************************************
 *Copyrights @ 2017，xiaowo  Co., Ltd.
 *
 *Author:
 *		 yecanyi
 *Finished：
 *		2017/10/13
 ********************************************************/
@Controller
@RequestMapping("weixin")
public class WXFunctionController {

    @Resource
    private WXFunctionService wxFunctionService;

    @RequestMapping(value = "/getJSConfig")
    public ModelAndView getJSConfig(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        JSConfig jsConfig = wxFunctionService.getJSConfig(request);
        mv.addObject("jsConfig",jsConfig);
        mv.setViewName("");
        return mv;
    }
}
