package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.dto.PageResponseResult;
import com.heima.common.dto.ResponseResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.user.client.ArticleFeign;
import com.heima.user.client.MediaFeign;
import com.heima.user.dto.ApAuthor;
import com.heima.user.dto.AuthDto;
import com.heima.user.dto.WmUser;
import com.heima.user.entity.ApUser;
import com.heima.user.entity.ApUserRealname;
import com.heima.user.mapper.ApUserRealnameMapper;
import com.heima.user.service.IApUserRealnameService;
import com.heima.user.service.IApUserService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * APP实名认证信息表 服务实现类
 * </p>
 *
 * @author syl
 * @since 2023-04-16
 */
@Service
public class ApUserRealnameServiceImpl extends ServiceImpl<ApUserRealnameMapper, ApUserRealname> implements IApUserRealnameService {

    @Autowired
    private ArticleFeign articleFeign;
    @Autowired
    private MediaFeign mediaFeign;
    @Autowired
    private IApUserService apUserService;

    @Override
    public ResponseResult listByStatus(AuthDto dto) {

        LambdaQueryWrapper<ApUserRealname> queryWrapper = new LambdaQueryWrapper<>();

        if (dto.getStatus() != null) {
            queryWrapper.eq(ApUserRealname::getStatus, dto.getStatus());
        }

        IPage<ApUserRealname> page = new Page<>(dto.getPage(), dto.getSize());
//        Mapper selectPage
        IPage<ApUserRealname> page1 = this.page(page, queryWrapper);
        return new PageResponseResult(dto.getPage(), dto.getSize(), page1.getTotal(), page1.getRecords());
    }

    @Override
    //@Transactional //TODO 任务1：改成分布式事务
    @GlobalTransactional
    public ResponseResult auth(AuthDto dto, int type) { // 1:通过  0：驳回

        Long userId = dto.getId();//apUser的id
        LambdaQueryWrapper<ApUserRealname> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApUserRealname::getUserId, userId);
        ApUserRealname one = this.getOne(queryWrapper);
        if (one == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        if (type == 0) {   //驳回
            one.setStatus(2);
            one.setReason(dto.getMsg());
            one.setUpdatedTime(new Date());
            this.updateById(one);
        } else {

            ApUser apUser = apUserService.getById(userId);
//            1、远程调用自媒体微服务。插入一条数据 自媒体人的id
            WmUser wmUser = createWmUser(apUser);
//            2、远程调用文章微服务。插入一条数据
           ApAuthor apAuthor = createApAuthor(apUser,wmUser.getId());
           // 3、给WmUser的aPAuthorId赋值
            wmUser.setApAuthorId(apAuthor.getId());
            mediaFeign.updateWmUser(wmUser);
//            4、修改ApUserRealname状态和ApUser的 是否身份认证、自媒体人的标记
            one.setStatus(9);
            one.setUpdatedTime(new Date());
            this.updateById(one);


            apUser.setIsIdentityAuthentication(true);
            apUser.setFlag(1);  //0 普通用户            1 自媒体人            2 大V
            apUserService.updateById(apUser);

        }

        return ResponseResult.okResult();
    }

    private ApAuthor createApAuthor(ApUser apUser,Integer wmUserId) {
        ApAuthor apAuthor = new ApAuthor();
        // apAuthor.setId(0); //主键自增
        apAuthor.setName(apUser.getName());
        apAuthor.setType(2);  //0 爬取数据            1 签约合作商            2 平台自媒体人
        apAuthor.setUserId(apUser.getId());
        apAuthor.setWmUserId(wmUserId);
        ResponseResult<ApAuthor> responseResult = articleFeign.saveApAuthor(apAuthor);
        if (responseResult.getCode()!=0) {
            throw new RuntimeException("远程调用失败");
        }
        return responseResult.getData();
    }

    private WmUser createWmUser(ApUser apUser) {
        WmUser wmUser = new WmUser();
        // wmUser.setId(0); //主键自增
        wmUser.setApUserId(apUser.getId());
        // wmUser.setApAuthorId(0); //文章作者ID 暂时无法赋值 新增文章作者后再赋值
        wmUser.setName(apUser.getName());
        wmUser.setPassword(apUser.getPassword());
        wmUser.setSalt(apUser.getSalt());
        // wmUser.setNickname(); //无法赋值
        wmUser.setImage(apUser.getImage());
        // wmUser.setLocation(); //无法赋值
        wmUser.setPhone(apUser.getPhone());
        wmUser.setStatus(apUser.getStatus());
        // wmUser.setEmail(); //无法赋值
        wmUser.setType(0);  //账号类型            0 个人             1 企业            2 子账号
        wmUser.setScore(0);
        // wmUser.setLoginTime(); //无法赋值
        ResponseResult<WmUser> responseResult = mediaFeign.saveWmUser(wmUser);
        if (responseResult.getCode()!=0) {
            throw new RuntimeException("远程调用失败");
        }

        return responseResult.getData();
    }

}
