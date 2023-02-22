package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Autowired
    private ShopTypeMapper shopTypeMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result selectAllTypeList() {
        //1.从redis中查询商铺类型缓存
        String typeShopJson = stringRedisTemplate.opsForValue().get("cache:typeshop");
        //2.判断是否存在
        if (StrUtil.isNotBlank(typeShopJson)) {
            //3.存在，直接返回
            List<ShopType> shopTypeList = JSONUtil.toList(typeShopJson, ShopType.class);
            return Result.ok(shopTypeList);
        }
        //4.不存在，查询数据库
        List<ShopType> shopTypeList = shopTypeMapper.selectList(null);
        //5.写入redis
        stringRedisTemplate.opsForValue().set("cache:typeshop",JSONUtil.toJsonStr(shopTypeList));
        stringRedisTemplate.expire("cache:typeshop",2, TimeUnit.HOURS);
        //6.返回数据
        return Result.ok(shopTypeList);
    }
}
