package com.example.miaoshatest.miaosha.controller;

import com.example.miaoshatest.miaosha.service.SeckkillService;
import com.example.miaoshatest.miaosha.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/miaosha")
public class SeckkillController {


    @Autowired
    private SeckkillService seckkillService;
    @ResponseBody
    public String index(){
        return "HELLO";
    }

    @GetMapping("/uploadVideos")
    @ResponseBody
    public R uploadVideos(){
        seckkillService.uploadVideos();
        return R.ok("...");
    }

    @GetMapping("/killCAS")
    @ResponseBody
    public R killCAS(@RequestParam("uid") String uid,
                  @RequestParam("vid") String vid){
        //String result = seckkillService.Kill(uid, vid);
        String result = seckkillService.killCAS(uid, vid);
        return R.ok(result);
    }
    @GetMapping("/kill")
    @ResponseBody
    public R kill(@RequestParam("uid") String uid,
                  @RequestParam("vid") String vid){
        String result = seckkillService.Kill(uid, vid);
        //String result = seckkillService.killCAS(uid, vid);
        return R.ok(result);
    }

}
