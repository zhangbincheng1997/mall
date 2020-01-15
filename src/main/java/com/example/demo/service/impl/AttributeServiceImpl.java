package com.example.demo.service.impl;

import com.example.demo.entity.Attribute;
import com.example.demo.mapper.AttributeMapper;
import com.example.demo.service.AttributeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class AttributeServiceImpl extends ServiceImpl<AttributeMapper, Attribute> implements AttributeService {

}
