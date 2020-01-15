package com.example.demo.service.impl;

import com.example.demo.entity.AttributeValue;
import com.example.demo.mapper.AttributeValueMapper;
import com.example.demo.service.AttributeValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AttributeValueServiceImpl extends ServiceImpl<AttributeValueMapper, AttributeValue> implements AttributeValueService {

}
