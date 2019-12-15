package com.chengm.pirate.base.impl;

import com.chengm.pirate.base.BaseService;
import com.chengm.pirate.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * program: CmPirate
 * description:
 * author: ChengMo
 * create: 2019-12-15 10:12
 **/
public class BaseBizService implements BaseService {

    @Autowired
    protected RedisUtil mRedisUtil;

}
