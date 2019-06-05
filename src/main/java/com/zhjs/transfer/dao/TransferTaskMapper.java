package com.zhjs.transfer.dao;

import com.zhjs.transfer.entity.TransferTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferTaskMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TransferTask record);

    int insertSelective(TransferTask record);

    TransferTask selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TransferTask record);

    int updateByPrimaryKey(TransferTask record);

    int updateByTransactionIdAndStatus(@Param("newStatus") Integer newStatus, @Param("oldStatus") Integer oldStatus, @Param("requestId") String requestId);

    TransferTask queryByTransactionIdAndPaymentId(@Param("requestId") String requestId,@Param("payAccountId") String payAccountId);

    List<TransferTask> queryByStatus(@Param("status") Integer status,@Param("direction")Integer direction);
}