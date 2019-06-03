package com.zhjs.transfer.controller;

import com.zhjs.transfer.entity.AccountInfo;
import com.zhjs.transfer.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: AccountController
 * @author: zhjs
 * @createDate: 2019/5/30 下午3:11
 * @JDK: 1.8
 * @Desc: controller层
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @RequestMapping("/query/byId")
    public AccountInfo queryById(Long id){
        return accountService.queryById(id);
    }

    @RequestMapping("/add")
    public String add(@RequestBody AccountInfo accountInfo){
        accountService.add(accountInfo);
        return "success";
    }
}
