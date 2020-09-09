package com.zhjs.transfer.controller;

import com.alibaba.fastjson.JSON;
import com.zhjs.transfer.dto.TransferDTO;
import com.zhjs.transfer.dto.TransferDocDTO;
import com.zhjs.transfer.entity.AccountInfo;
import com.zhjs.transfer.service.AccountService;
import com.zhjs.transfer.service.TransferService;
import com.zhjs.transfer.utils.RSAUtil;
import com.zhjs.transfer.utils.SnowFlake;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
@Api(value = "AccountController", description = "账户相关api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransferService transferService;


    @RequestMapping("/query/byId")
    @ApiOperation(value = "查找账户", notes = "查找账户", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "账户id",dataType = "Long")
    })
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
        transferDTO.setRequestId(String.valueOf(SnowFlake.getSnowFlakeId()));
        transferDTO.setPayerAccount("zhjs@pay.com");
        transferDTO.setPayeeAccount("zxh@pay.com");
        transferDTO.setAmount(200L);
        transferDTO.setRemark("转账200元");
        String transferInfo = JSON.toJSONString(transferDTO);
        transferDocDTO.setAppId("001");
        transferDocDTO.setTransferInfo(transferInfo);
        transferDocDTO.setSignInfo(RSAUtil.sign(transferInfo));
        transferService.transferMoney(transferDocDTO);

        return "success";
    }
}
