package com.xrzx.commonlibrary.utils;

import com.xrzx.commonlibrary.database.dao.base.BaseDao;

import java.util.UUID;

/**
 * @Description
 * @Author ks
 * @Date 2020/10/31 0:45
 */
public class UUIDUtils {
    public static String random(BaseDao baseDao) {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
