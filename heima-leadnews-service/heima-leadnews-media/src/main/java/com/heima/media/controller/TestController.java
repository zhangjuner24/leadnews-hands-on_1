package com.heima.media.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.common.dto.ResponseResult;
import com.heima.media.entity.WmSensitive;
import com.heima.media.service.IAuditService;
import com.heima.media.service.IWmSensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private IAuditService auditService;

    @GetMapping("/audit/{id}")
    public ResponseResult audit(@PathVariable("id") Integer id) {
        auditService.audit(id);
        return ResponseResult.okResult();
    }

    @Autowired
    private IWmSensitiveService wmSensitiveService;

    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile file) throws Exception {

        EasyExcel.read(file.getInputStream(), WmSensitive.class, new PageReadListener<WmSensitive>(dataList -> {
            for (WmSensitive ws : dataList) {
                // 先查询，如果没有再新增
                LambdaQueryWrapper<WmSensitive> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(WmSensitive::getSensitives, ws.getSensitives());
                int count = wmSensitiveService.count(queryWrapper);
                if(count==0){
                    ws.setCreatedTime(new Date());
                    wmSensitiveService.save(ws);
                }
            }
        })).sheet().doRead();

        // auditService.audit(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response) throws Exception {

        List<WmSensitive> list = wmSensitiveService.list();

         // 文件下载
        // 一个流：OutputStream
        // 两个头：文件的打开方式（inline：浏览器直接打开 attachment：以附件方式下载）
        //        文件mime类型   xlsx----application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");//设置MIME类型
        response.setHeader("Content-Disposition", "attachment; filename=sensitive.xlsx");
        EasyExcel.write(response.getOutputStream(), WmSensitive.class)
                .sheet("敏感词")
                .doWrite(() -> {
                    return list;
                });
    }

}
