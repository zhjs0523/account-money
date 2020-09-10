package com.zhjs.transfer;

import com.alibaba.fastjson.JSON;
import com.zhjs.transfer.dao.AccountInfoMapper;
import com.zhjs.transfer.dto.TransferDTO;
import com.zhjs.transfer.dto.TransferDocDTO;
import com.zhjs.transfer.entity.AccountInfo;
import com.zhjs.transfer.service.TransferService;
import com.zhjs.transfer.utils.RSAUtil;
import com.zhjs.transfer.utils.SnowFlake;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountMoneyApplicationTests {

	@Resource
	private AccountInfoMapper accountInfoMapper;

	@Autowired
	private TransferService transferService;

	@Test
	public void testInsertAccount() {
		AccountInfo accountDO  = new AccountInfo();
		accountDO.setId(2L);
		accountDO.setPayAccountId("zxh@pay.com");
		accountDO.setAmount(500L);
		accountDO.setVersionId(1L);
		accountDO.setRemark("init");
		accountDO.setCreated(new Date());
		accountDO.setModified(new Date());
		accountInfoMapper.insert(accountDO);
	}

	@Test
	public void testTransfer(){
		TransferDocDTO transferDocDTO = new TransferDocDTO();
		TransferDTO transferDTO = new TransferDTO();
		transferDTO.setRequestId(String.valueOf(SnowFlake.getSnowFlakeId()));
		transferDTO.setPayerAccount("fhy@pay.com");
		transferDTO.setPayeeAccount("风险账户");
		transferDTO.setAmount(200L);
		String transferInfo = JSON.toJSONString(transferDTO);
		transferDocDTO.setAppId("001");
		transferDocDTO.setTransferInfo(transferInfo);
		transferDocDTO.setSignInfo(RSAUtil.sign(transferInfo));
		transferService.transferMoney(transferDocDTO);
	}
}
