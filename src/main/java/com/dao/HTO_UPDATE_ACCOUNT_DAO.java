package com.dao;

import com.po.HTO_UPDATE_ACCOUNT;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by zhangtuoyu on 2016-09-08.
 */
public interface HTO_UPDATE_ACCOUNT_DAO {
    List<HTO_UPDATE_ACCOUNT> queryAll();
}
