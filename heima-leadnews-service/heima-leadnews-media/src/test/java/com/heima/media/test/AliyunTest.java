package com.heima.media.test;

import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.media.MediaApp;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = MediaApp.class)
public class AliyunTest {

    @Autowired
    private GreenTextScan greenTextScan;
    @Autowired
    private GreenImageScan greenImageScan;

    @Test
    public void testText() throws Exception{
        // Map map = greenTextScan.greenTextScan("今天突然下雨了");
        Map map = greenTextScan.greenTextScan("你那还有冰毒吗");
        System.out.println("testText返回的结果"+map.get("suggestion") +":"+map.get("label"));

        Map map1 = greenTextScan.greenTextScan("你妈逼你相亲了吗");
        System.out.println("testText返回的结果"+map1.get("suggestion")+":"+map.get("label"));
    }

    @Test
    public void testImage() throws Exception{

        // blood.jpg
        FileInputStream file1 = new FileInputStream("d://blood.jpg");
        byte[] bytes1 = IOUtils.toByteArray(file1);

        // blood.jpg
        FileInputStream file2 = new FileInputStream("d://vs.jpg");
        byte[] bytes2 = IOUtils.toByteArray(file2);

        List<byte[]> imageList = new ArrayList<>();
        imageList.add(bytes1);
        imageList.add(bytes2);
        Map map = greenImageScan.imageScan(imageList);
        System.out.println("testImage 返回的结果"+map.get("suggestion"));
    }

}
