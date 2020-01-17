package com.example.demo.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzz
 * @since 2020-01-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Sku implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 属性
     */
    private String ids;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 货号
     */
    private String sku;


}
