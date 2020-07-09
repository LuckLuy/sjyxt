package com.ly.sjyxt;

import com.ly.sjyxt.mapper.SysDataConnectMapper;
import com.ly.sjyxt.service.SysDataConnectService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class SjyxtApplicationTests {

	@Resource
	private SysDataConnectMapper dataConnectMapper;
	@Resource
	private SysDataConnectService iDataConnectService;
	@Test
	void contextLoads() {
		System.out.println(del_space("   123  2 23 3 "));
	}
	public String del_space(String str) {
		if (str == null) {
			return null;
		}
		char[] str_old = str.toCharArray();
		StringBuffer str_new = new StringBuffer();

		int i = 0;
		for (char a : str_old) {
			if (a != ' ') {
				str_new.append(a);
				i++;
			}
		}
		return str_new.toString();
	}
}
