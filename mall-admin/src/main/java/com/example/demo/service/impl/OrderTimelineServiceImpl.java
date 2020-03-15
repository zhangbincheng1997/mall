package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.OrderTimeline;
import com.example.demo.mapper.OrderTimelineMapper;
import com.example.demo.service.OrderTimelineService;
import org.springframework.stereotype.Service;

@Service
public class OrderTimelineServiceImpl extends ServiceImpl<OrderTimelineMapper, OrderTimeline> implements OrderTimelineService {

}
