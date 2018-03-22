package com.geekzhang.demo;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void ss(){
		File file = new File("/Users/zhangpengzhi/test/");
		searchFile(file);
	}
	public void searchFile(File file){
        File[] files = file.listFiles();
        for (File f: files) {
            if (!f.isDirectory()) {
                System.out.println(file.getName());
            } else {
                System.out.println(file.getName());
                searchFile(f);
            }
        }
	}
}
