package com.ly.sjyxt.mapper;

import com.ly.sjyxt.entity.DataConnect;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
@SpringBootTest
public class MapperTest {

    @Resource
    private DataConnect dataConnect;


    @Test
    public void getDatabase(){

    }
}
