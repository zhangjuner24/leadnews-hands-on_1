package com.heima.behavior.controller;

import com.heima.behavior.service.IApBehaviorEntryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * APP行为实体表,一个行为实体可能是用户或者设备，或者其它 前端控制器
 * </p>
 *
 * @author syl
 * @since 2023-04-27
 */
@RestController
@RequestMapping("/api/v1/behavior_entry")
@Api(tags = "APP行为实体表,一个行为实体可能是用户或者设备，或者其它接口")
@CrossOrigin
public class ApBehaviorEntryController{

    @Autowired
    private IApBehaviorEntryService apBehaviorEntryService;



}
