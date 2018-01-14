package com.onlineshopping.service.impl.backmanage;

import com.onlineshopping.dao.*;
import com.onlineshopping.entity.*;
import com.onlineshopping.service.backmanage.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by lsy
 */
@Service
public class OrderServiceImpl implements OrderService{
    private Result result = new Result();
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Resource
    private CatalogDao CatalogDao;
    @Resource
    private OrderDao OrderDao;
    @Resource
    private OrderDetailDao OrderDetailDao;
    @Resource
    private UserDao UserDao;
    @Resource
    private GoodsDao goodsDao;

    /**
     * 查找订单
     * @param order_id      订单ID
     * @param user_id       用户ID
     * @param begin_time    下单时间下限
     * @param end_time      下单时间上限
     * @return
     */
    @Override
    public Result find(Integer order_id, String user_id, Date begin_time, Date end_time) {
        result.setContent(null);
        List<Order> orders;

        // 查找订单，保存到orders中
        if (order_id == null) {
            // 订单ID为空，通过其他条件查找
            result.setResultCode(0);
            orders = findOrders(user_id, begin_time, end_time);
            if (result.getResultCode() != 0) {
                logger.error(result.getResultInfo());
                return result;
            }
        } else {
            // 订单ID不为空，直接通过订单ID查找
            Order order = OrderDao.get(Order.class, order_id);
            if (order == null) {
                result.setResultCode(-1);
                result.setResultInfo("订单" + order_id + "不存在！");
                logger.error(result.getResultInfo());
                return result;
            } else {
                //将查询结果添加到orders中
                orders = new ArrayList<>();
                orders.add(order);
            }
        }

        if (orders == null) {
            result.setResultCode(-4);
            result.setResultInfo("没有满足条件的订单！");
            logger.error(result.getResultInfo());
        } else {
            for (Order order : orders) {
                order.setOrderDetailList(null);
            }
            result.setResultCode(0);
            result.setResultInfo("查询订单成功！");
            logger.error(result.getResultInfo());
            result.setContent(orders);
        }
        return result;
    }

    /**
     * 查找订单
     * @param user_id       用户ID
     * @param begin_time    下单时间下限
     * @param end_time      下单时间上限
     * @return 订单列表
     */
    private List<Order> findOrders(String user_id, Date begin_time, Date end_time) {
        if (user_id == null || user_id.equals("")) {
            if (begin_time == null) {
                if (end_time == null) {
                    // 全为空，查找全部订单
                    return OrderDao.findAll(Order.class);
                }else {
                    // 只有结束时间不为空
                    return OrderDao.getOrdersByEnd(end_time);
                }
            } else {
                // 起始时间不为空
                if (end_time == null) {
                    // 只有起始时间不为空
                    return OrderDao.getOrdersByBegin(begin_time);
                } else {
                    // 起始时间和结束时间非空
                    if (begin_time.after(end_time)) {
                        result.setResultCode(-3);
                        result.setResultInfo("起始时间晚于结束时间！");
                        return null;
                    }
                    return OrderDao.getOrdersByBeginEnd(begin_time, end_time);
                }
            }
        } else {
            // 用户ID不为空
            User user = UserDao.get(User.class, user_id);
            if(user == null){
                result.setResultCode(-2);
                result.setResultInfo("用户" + user_id + "不存在！");
                return null;
            }

            // 用户存在
            if (begin_time == null) {
                if (end_time == null) {
                    // 只有用户非空
                    return OrderDao.getOrdersByUser(user);
                }else {
                    // 只有起始时间为空
                    return OrderDao.getOrdersByUserEnd(user, end_time);
                }
            } else {
                // 起始时间非空
                if (end_time == null) {
                    // 只有结束时间为空
                    return OrderDao.getOrdersByUserBegin(user, begin_time);
                } else {
                    // 结束时间非空
                    if (begin_time.after(end_time)) {
                        result.setResultCode(-3);
                        result.setResultInfo("起始时间晚于结束时间！");
                        return null;
                    }
                    // 全部不为空
                    return OrderDao.getOrdersByUserBeginEnd(user, begin_time, end_time);
                }
            }
        }
    }

    /**
     * 查询订单详细信息
     * @param order_id 订单ID
     * @return 返回包含OrderDetailList的order，包装在result中
     */
    @Override
    public Result getDetail(int order_id) {
        Order order = OrderDao.get(Order.class, order_id);
        if (order == null) {
            result.setResultCode(-1);
            result.setResultInfo("订单" + order_id + "不存在！");
            logger.error(result.getResultInfo());
            result.setContent(null);
            return result;
        }

        // 通过订单ID查找订单详情列表，并添加到order中
        order.setOrderDetailList(OrderDetailDao.getOrderDetailByOrder(order));
        for (OrderDetail orderDetail : order.getOrderDetailList()) {
            orderDetail.setOrder(null);
        }

        result.setResultCode(0);
        result.setResultInfo("查询订单详情成功");
        logger.info(result.getResultInfo());
        result.setContent(order);
        return result;
    }

    /**
     * 更新订单状态
     * @param order_id  订单ID
     * @param status    状态
     * @return
     */
    @Override
    public Result updateStatus(int order_id, String status) {
        result.setContent(null);

        StatusType newStatus;
        try {
            newStatus = StatusType.valueOf(status);
        } catch (Exception e) {
            result.setResultCode(-2);
            result.setResultInfo("状态" + status + "不合法！");
            logger.error(result.getResultInfo());
            return result;
        }

        Order order = OrderDao.get(Order.class, order_id);
        if (order == null) {
            result.setResultCode(-1);
            result.setResultInfo("订单" + order_id + "不存在！");
            logger.error(result.getResultInfo());
            return result;
        }

        // 更新的订单状态
        order.setStatus(newStatus);
        OrderDao.update(order);

//      吴家驹 17/8/20 改动，把修改库存改到提交订单时
//        if (newStatus == StatusType.审核通过){
//            List<OrderDetail> list = OrderDetailDao.getOrderDetailByOrder(order);
//            Goods goods;
//            for (OrderDetail orderDetail : list) {
//                goods = orderDetail.getGoods();
//                goods.setStock(goods.getStock() - orderDetail.getGoodsNumber());
//                goodsDao.update(goods);
//            }
//        }

        //      吴家驹 17/8/20 改动，审核不通过时增加库存
        if (newStatus == StatusType.审核未通过){
            List<OrderDetail> list = OrderDetailDao.getOrderDetailByOrder(order);
            Goods goods;
            for (OrderDetail orderDetail : list) {
                goods = orderDetail.getGoods();
                goods.setStock(goods.getStock() + orderDetail.getGoodsNumber());
                goodsDao.update(goods);
            }
        }

        result.setResultCode(0);
        result.setResultInfo("更新订单状态成功！");
        logger.info(result.getResultInfo());
        return result;
    }

    /**
     * 统计销售量
     * @param begin_time    下单时间下限
     * @param end_time      下单时间上限
     * @param catalog_name  目录名
     * @return
     */
    @Override
    public Result getRevenue(Date begin_time, Date end_time, String catalog_name) {
        result.setResultCode(0);
        result.setContent(null);
        List<Integer> orderIdList = null;

        // 如果起始时间和结束时间均为空，订单ID将不成为查找订单详情的限制
        boolean findAll = (begin_time == null) && (end_time == null);

        if (!findAll) {
            // 查找从起始时间到结束时间内的所有订单的ID，并保存到orderIdList中
            orderIdList = findOrderId(begin_time, end_time);
            // 时间不合法
            if (result.getResultCode() != 0) {
                return result;
            }
            // 查找不到满足条件的订单
            if (orderIdList.isEmpty()) {
                result.setResultCode(-2);
                result.setResultInfo("没有满足条件的销售信息！");
                logger.error(result.getResultInfo());
                return result;
            } else {
                logger.info(orderIdList.toString());
            }
        }

        // 根据目录名和订单ID列表查找所有满足条件的订单详情，并保存到orderDetails列表中
        List<OrderDetail> orderDetails = findOrderDetail(catalog_name, orderIdList, findAll);
        // 目录不存在
        if (result.getResultCode() != 0) {
            logger.error(result.getResultInfo());
            return result;
        }
        // 查找不到满足条件的订单详情
        if (orderDetails == null) {
            result.setResultCode(-2);
            result.setResultInfo("没有满足条件的销售信息！");
            logger.error(result.getResultInfo());
            result.setContent(orderIdList);
            return result;
        }

        // 构建以goodsId为key，revenue为value的map，以对销售量进行统计
        Map<String, Revenue> revenueMap = new HashMap<>();
        String goodsId;
        Revenue revenue;
        // 遍历查询到的订单详情列表
        for (OrderDetail orderDetail : orderDetails) {
            goodsId = orderDetail.getGoods().getGoodsId();
            if (revenueMap.containsKey(goodsId)) {
                // goodsId已存在，则获取revenue，并累加商品销售数量和金额
                revenue = revenueMap.get(goodsId);
                revenue.addGoodsNumber(orderDetail.getGoodsNumber());
                // 商品销售金额 = 折扣 * 单价 * 数量
                revenue.addRevenue(orderDetail.getOrder().getDiscount() * orderDetail.getGoodsPrice() * orderDetail.getGoodsNumber());
            } else {
                // goodsId不存在则将其添加到map中
                revenue = new Revenue();
                revenue.setGoodsId(goodsId);
                revenue.setGoodsName(orderDetail.getGoodsName());
                revenue.setGoodsPrice(orderDetail.getGoodsPrice());
                revenue.setGoodsNumber(orderDetail.getGoodsNumber());
                revenue.setRevenue(orderDetail.getOrder().getDiscount() * orderDetail.getGoodsPrice() * orderDetail.getGoodsNumber());
                revenueMap.put(goodsId, revenue);
            }
        }

        result.setResultCode(0);
        result.setResultInfo("查询统计销量信息成功！");
        logger.info(result.getResultInfo());
        // 将所有的revenue保存到结果中
        result.setContent(revenueMap.values());
        return result;
    }

    /**
     * 查找从起始时间到结束时间内的订单的ID
     * @param begin_time    下单时间下限
     * @param end_time      下单时间上限
     * @return 订单ID列表
     */
    private List<Integer> findOrderId(Date begin_time, Date end_time) {
        if (begin_time == null) {
            if (end_time == null) {
                return null;
            } else {
                // 只有结束时间不为空
                return OrderDao.getOrderIdByEnd(end_time);
            }
        } else {
            // 起始时间不为空
            if (end_time == null) {
                // 只有起始时间不为空
                return OrderDao.getOrderIdByBegin(begin_time);
            } else {
                // 都不为空
                if (begin_time.after(end_time)) {
                    result.setResultCode(-1);
                    result.setResultInfo("起始时间晚于结束时间！");
                    logger.info(result.getResultInfo());
                    return null;
                }
                logger.info(begin_time.toString());
                logger.info(end_time.toString());
                return OrderDao.getOrderIdByBeginEnd(begin_time, end_time);
            }
        }
    }

    /**
     * 根据目录和订单号列表查找订单详情
     * @param catalog_name  目录名
     * @param orderIdList   订单号列表
     * @param findAll       是否查找所有订单
     * @return 订单详情列表
     */
    private List<OrderDetail> findOrderDetail(String catalog_name, List<Integer> orderIdList, boolean findAll) {
        if (catalog_name == null || catalog_name.equals("")) {
            if (findAll) {
                // 所有查询条件为空
                return OrderDetailDao.getAll();
            } else {
                // 目录为空，根据上面查询到的订单号列表进行查询
                return OrderDetailDao.getOrderDetailByOrderIdList(orderIdList);
            }
        } else {
            Catalog catalog = CatalogDao.getByName(catalog_name);
            if (catalog == null) {
                result.setResultCode(-3);
                result.setResultInfo("目录不存在！");
                return null;
            }
            if (findAll) {
                //只有目录名不为空，根据目录进行查询
                return OrderDetailDao.getOrderDetailByCatalog(catalog);
            } else {
                //都不为空，根据目录和订单号列表进行查询
                return OrderDetailDao.getOrderDetailByCatalogOrderIdList(catalog, orderIdList);
            }
        }
    }
}
