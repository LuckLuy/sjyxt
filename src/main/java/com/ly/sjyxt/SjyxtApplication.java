package com.ly.sjyxt;

import com.ly.sjyxt.config.MyEncryptablePropertyDetector;
import com.ly.sjyxt.config.MyEncryptablePropertyResolver;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyDetector;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
@Slf4j
@SpringBootApplication
@MapperScan(basePackages= {"com.ly.sjyxt.mapper"})
@EnableEncryptableProperties
public class SjyxtApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext application =SpringApplication.run(SjyxtApplication.class, args);
		Environment env = application.getEnvironment();
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String port = env.getProperty("server.port");
		String path = env.getProperty("server.servlet.context-path");
		log.info("\n----------------------------------------------------------\n\t" +
				"Application HYsoft-Boot is running! Access URLs:\n\t" +
				"Local: \t\thttp://localhost:" + port + path + "/\n\t" +
				"External: \thttp://" + ip + ":" + port + path + "/\n\t" +
				"swagger-ui: \thttp://" + ip + ":" + port + path + "/swagger-ui.html\n\t" +
				"----------------------------------------------------------");
	}
	//注册bean

	@Bean(name = "encryptablePropertyDetector")
	public EncryptablePropertyDetector encryptablePropertyDetector() {

		return new MyEncryptablePropertyDetector();
	}

	@Bean(name = "encryptablePropertyResolver")
	public EncryptablePropertyResolver encryptablePropertyResolver()
	{
		return new MyEncryptablePropertyResolver();
	}


}
