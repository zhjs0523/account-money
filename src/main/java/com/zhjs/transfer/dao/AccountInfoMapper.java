package com.zhjs.transfer.dao;

import com.zhjs.transfer.entity.AccountInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AccountInfo record);

    int insertSelective(AccountInfo record);

    AccountInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccountInfo record);

    int updateByPrimaryKey(AccountInfo record);

    AccountInfo queryBalance(String payAccountId);
}