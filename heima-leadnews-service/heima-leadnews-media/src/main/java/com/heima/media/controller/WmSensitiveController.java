package com.heima.media.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.common.dto.ResponseResult;
import com.heima.media.entity.WmSensitive;
import com.heima.media.service.IWmSensitiveService;
import com.xiaoleilu.hutool.date.DateTime;
import io.swagger.annotations.Api;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 敏感词信息表 前端控制器
 * </p>
 *
 * @author syl
 * @since 2023-04-21
 */
@RestController
@RequestMapping("/api/v1/sensitive")
@Api(tags = "敏感词信息表接口")
@CrossOrigin
public class WmSensitiveController {

    @Autowired
    private IWmSensitiveService wmSensitiveService;


    //分页查询
    @PostMapping("list")
    public ResponseResult listByCondition(@RequestBody WmSensitive dto){

        if (StringUtil.isNotBlank(dto.getName())) {
            LambdaQueryWrapper<WmSensitive> lam = new LambdaQueryWrapper<>();
            LambdaQueryWrapper<WmSensitive> like = lam.like(WmSensitive::getSensitives, dto.getName());
            return ResponseResult.okResult(wmSensitiveService.list(like));
        }

        return ResponseResult.okResult(wmSensitiveService.list());
    }
    //保存
    @PostMapping
    public ResponseResult save(@RequestBody WmSensitive  ooo){
        ooo.setCreatedTime(DateTime.now());
        boolean save = wmSensitiveService.save(ooo);

        return ResponseResult.okResult(save);
    }
    //修改
    @PutMapping
    public ResponseResult xiugai(@RequestBody WmSensitive  iii){
        iii.setCreatedTime(DateTime.now());
        LambdaQueryWrapper<WmSensitive> wq = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<WmSensitive> ww = wq.eq(WmSensitive::getId, iii.getId());
        boolean update = wmSensitiveService.updateById(iii);
        return ResponseResult.okResult(update);
    }
    //删除敏感
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable("id") Integer  id){
//        LambdaQueryWrapper<WmSensitive> sc = new LambdaQueryWrapper<>();
//        LambdaQueryWrapper<WmSensitive> eq = sc.eq(WmSensitive::getId, id.getId());

        boolean delete = wmSensitiveService.removeById(id);
        return ResponseResult.okResult(delete);

    }

}
