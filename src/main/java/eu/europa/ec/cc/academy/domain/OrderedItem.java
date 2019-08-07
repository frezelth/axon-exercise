package eu.europa.ec.cc.academy.domain;

import com.google.common.collect.Lists;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Data
public class OrderedItem {

    private int item;
    private String description;
    private boolean drink;
    private BigDecimal price;

    public static OrderedItem from(OrderedItemVO vo){
        OrderedItem item = new OrderedItem();
        item.setItem(vo.getItem());
        item.setDescription(vo.getDescription());
        item.setDrink(vo.isDrink());
        item.setPrice(vo.getPrice());
        return item;
    }

    public static List<OrderedItem> from(Collection<OrderedItemVO> vos){
        if (vos == null){
            return null;
        }

        List<OrderedItem> items = Lists.newArrayList();
        for (OrderedItemVO vo : vos){
            items.add(from(vo));
        }
        return items;
    }
}
