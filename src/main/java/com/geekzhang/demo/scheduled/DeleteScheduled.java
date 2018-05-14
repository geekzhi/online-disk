package com.geekzhang.demo.scheduled;

import com.geekzhang.demo.mapper.UserFileMapper;
import com.geekzhang.demo.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @author: zhangpengzhi<geekzhang @ 1 6 3 . com>
 * @date: 2018/4/2 下午1:54
 * @version: V1.0
 */
@Slf4j
@Component
public class DeleteScheduled {

    @Autowired
    private UserFileMapper userFileMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 每天两点执行更新vip 和 清空回收站文件
     */
    //    @Scheduled(cron = "0/10 * * * * ?")
//    @Scheduled(cron = "0 0 2 * * ? ")
    public void scheduled(){
        userMapper.updateVip();
        userFileMapper.deleteScheduled();
    }
}
