package com.zhjs.transfer.controller;

import com.alibaba.fastjson.JSON;
import com.zhjs.transfer.dto.TransferDTO;
import com.zhjs.transfer.dto.TransferDocDTO;
import com.zhjs.transfer.entity.AccountInfo;
import com.zhjs.transfer.service.AccountService;
import com.zhjs.transfer.service.TransferService;
import com.zhjs.transfer.utils.RSAUtil;
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

    @Autowired
    private TransferService transferService;


    @RequestMapping("/query/byId")
    public AccountInfo queryById(Long id){
        return accountService.queryById(id);
    }

    @RequestMapping("/add")
    public String add(@RequestBody AccountInfo accountInfo){
        accountService.add(accountInfo);
        return "success";
    }

    /**
     * 转账
     * @return
     */
    @RequestMapping("/transferAccount")
    public String transferAccount(){
        TransferDocDTO transferDocDTO = new TransferDocDTO();
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setRequestId(String.valueOf(System.currentTimeMillis()));
        transferDTO.setPayerAccount("zhjs@pay.com");
        transferDTO.setPayeeAccount("zxh@pay.com");
        transferDTO.setAmount(200L);
        String transferInfo = JSON.toJSONString(transferDTO);
        transferDocDTO.setAppId("001");
        transferDocDTO.setTransferInfo(transferInfo);
        transferDocDTO.setSignInfo(RSAUtil.sign(transferInfo));
        transferService.transferMoney(transferDocDTO);

        return "success";
    }
}
