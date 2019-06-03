package com.zhjs.transfer;

import com.zhjs.transfer.dao.AccountInfoMapper;
import com.zhjs.transfer.entity.AccountInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountMoneyApplicationTests {

	@Resource
	private AccountInfoMapper accountInfoMapper;


	@Test
	public void testInsert() {
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

}
