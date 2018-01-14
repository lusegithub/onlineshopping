package com.onlineshopping.service.impl.purchase;

import com.onlineshopping.dao.*;
import com.onlineshopping.entity.*;
import com.onlineshopping.service.purchase.PurchaseService;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by jacky on 17-6-12.
 */
@Service
public class PurchaseServiceImpl implements PurchaseService{

    private static final Logger log= LoggerFactory.getLogger(PurchaseServiceImpl.class);

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private OrderDao orderDao;

    @Resource
    private ShoppingCartDao shoppingCartDao;

    @Resource
    private FavoriteDao favoriteDao;

    @Resource
    private UserDao userDao;

    @Resource
    private VipLevelDao vipLevelDao;

    @Resource
    private AddressDao addressDao;

    @Resource
    private CommentDao commentDao;

    @Resource
    private OrderDetailDao orderDetailDao;

    @Override
    public Result saveOrder(Map<String ,Object> map,String userId) {
        Result result = new Result();
        Order order = new Order();
        List<OrderDetail> orderDetails = new ArrayList<>();
        double total_price = 0;
        //设置用户
        User currentUser = userDao.get(User.class, userId);
        order.setUser(currentUser);
        // 获取会员等级
        VipLevel vipLevel = getVipLevelByPoints(currentUser.getPoints());
        //时间
        order.setOrderTime(new Date());
        //获取并设置地址
        Address address = addressDao.get(Address.class, (Integer) map.get("addressId"));
        order.setConsignee(address.getName());
        order.setAddress(address.getAddress());
        order.setPhone(address.getPhoneNumber());
        //订单详细信息
        ShoppingCart shoppingCart = shoppingCartDao.getShoppingCartByUserId(userId);
        Map<String, Integer> goodsIdMap = shoppingCart.getGoodsIdMap();

        // 吴家驹 17/08/20 ↓
        Set<String> notDeleteGoodsIds = new HashSet<>();
        // ↑

        for (String goodsId : goodsIdMap.keySet()) {
            Goods goods = goodsDao.get(Goods.class, goodsId);
            OrderDetail orderDetail = new OrderDetail();
            if (map.get(goods.getGoodsId()) != null) {
                // 吴家驹 17/08/20 ↓
                int goodsNum = (Integer) map.get(goods.getGoodsId());
                if(goodsNum == 0) {//当商品数量为0时，
                    notDeleteGoodsIds.add(goods.getGoodsId());
                    continue;
                }
                // ↑
                total_price += goods.getPrice() * (Integer) map.get(goods.getGoodsId());
                orderDetail.setGoodsNumber((Integer) map.get(goods.getGoodsId()));
            } else {
                // 吴家驹 17/08/20 ↓
                int goodsNum = goodsIdMap.get(goodsId);
                if(goodsNum == 0) {//当商品数量为0时，
                    notDeleteGoodsIds.add(goods.getGoodsId());
                    continue;
                }
                // ↑
                total_price += goods.getPrice() * goodsIdMap.get(goodsId);
                orderDetail.setGoodsNumber(goodsIdMap.get(goodsId));
            }
            orderDetail.setGoods(goods);
            orderDetail.setGoodsName(goods.getName());
            orderDetail.setGoodsPrice(goods.getPrice());
            orderDetails.add(orderDetail);
        }
        order.setOrderDetailList(orderDetails);
        //价格
        order.setPrice(total_price);
        //折扣
        order.setDiscount(vipLevel.getDiscount());
        //订单积分
        order.setPoints((int) (order.getPrice() * vipLevel.getRateCredit()));


        // 吴家驹 17/08/20添加，提交订单时修改库存 ↓
        for (OrderDetail orderDetail : orderDetails) {
            Goods goods;
            goods = orderDetail.getGoods();
            int newStock = goods.getStock() - orderDetail.getGoodsNumber();
            if (newStock < 0) {
                result.setResultCode(-1);
                result.setResultInfo("商品超出库存");
                return result;
            }
            goods.setStock(newStock);
        }
        //所有商品库存都足够，提交订单继续进行，更新商品库存
        for (OrderDetail orderDetail : orderDetails) {
            Goods goods;
            goods = orderDetail.getGoods();
            goodsDao.update(goods);
        }
        // ↑

        orderDao.save(order);
        result.setResultCode(0);
        result.setResultInfo("订单保存成功");
        log.info("用户{}提交订单成功",userId);
        shoppingCartDao.deleteShoppingCartByUserId(userId);

        // 吴家驹 17/08/20 ↓
        if (notDeleteGoodsIds.isEmpty()) {
            //如果购物车所有商品都买了，则直接返回
            return result;
        }
        //以下代码用于保存新购物车
        for (String goodsId:notDeleteGoodsIds){
            //判断商品是否存在
            Goods goods=goodsDao.get(Goods.class,goodsId);
            if (goods==null) {
                log.error("商品 "+goodsId+" 不存在");
            }
            //判断该商品是否已经在购物车里
            if (shoppingCartDao.isGoodsExistInShoppingCart(goodsId,userId)){
                log.error("用户"+userId+"已经添加商品"+goodsId+"在购物车里");
            }
        }
        ShoppingCart newShoppingCart=new ShoppingCart();
        newShoppingCart.setUserId(userId);
        Map<String,Integer> newGoodsIdMap =new HashMap<>();
        for (String goodsId:notDeleteGoodsIds){
            newGoodsIdMap.put(goodsId,1);
        }
        newShoppingCart.setGoodsIdMap(newGoodsIdMap);
        shoppingCartDao.save(newShoppingCart);
        // ↑

        return result;
    }


    @Override
    public Result getShoppingCart(String userId) {
        Result result=new Result();
        ShoppingCart shoppingCart=shoppingCartDao.getShoppingCartByUserId(userId);
        if (shoppingCart==null){
            result.setResultCode(-1);
            result.setResultInfo("没有查找到对应的记录");
            return result;
        }
        result.setResultCode(0);
        //获取商品
        List<Goods> list=new ArrayList<>();
        for (String goodId : shoppingCart.getGoodsIdMap().keySet()) {
            Goods goods = goodsDao.get(Goods.class, goodId);
            list.add(goods);
        }
        result.setContent(list);
        log.info("从用户{}获取购物车的情况为：{}",userId,result);
        return result;
    }

    @Override
    public Result saveShoppingCart(List<String> goodsIdList,String userId) {
        Result result=new Result();
        for (String  goodsId:goodsIdList){
            //判断商品是否存在
            Goods goods=goodsDao.get(Goods.class,goodsId);
            if (goods==null){
                log.error("商品"+goodsId+"不存在");
                result.setResultCode(-1);
                result.setContent("商品不存在");
                return result;
            }
            //判断该商品是否已经在购物车里
            if (shoppingCartDao.isGoodsExistInShoppingCart(goodsId,userId)){
                log.error("用户"+userId+"已经添加商品"+goodsId+"在购物车里");
                result.setResultCode(-2);
                result.setContent("商品已经在购物车里");
                return result;
            }
        }
        ShoppingCart shoppingCart=new ShoppingCart();
        shoppingCart.setUserId(userId);
        Map<String,Integer> goodsIdMap=new HashMap<>();
        for (String goodsId:goodsIdList){
            goodsIdMap.put(goodsId,1);
        }
        shoppingCart.setGoodsIdMap(goodsIdMap);
        shoppingCartDao.save(shoppingCart);
        result.setResultCode(0);
        return result;
    }

    @Override
    public Result deleteShoppingCart(List<String> goodsIdList,String userId) {
        Result result=new Result();
        ShoppingCart shoppingCart=new ShoppingCart();
        shoppingCart.setUserId(userId);
        Map<String,Integer> goodsIdMap=new HashMap<>();
        for (String goodsId:goodsIdList){
            goodsIdMap.put(goodsId,1);
        }
        shoppingCart.setGoodsIdMap(goodsIdMap);
        shoppingCartDao.delete(shoppingCart);
        result.setResultCode(0);
        return result;
    }

    @Override
    public Result saveFavorite(List<String> goodsIdList,String userId) {
        Result result=new Result();
        for (String goodsId:goodsIdList) {
            //判断商品是否存在
            Goods goods = goodsDao.get(Goods.class, goodsId);
            if (goods == null) {
                log.error("商品" + goodsId + "不存在");
                result.setResultCode(-1);
                result.setContent("商品不存在");
                return result;
            }
            //判断商品是否已经在收藏夹里
            if (favoriteDao.isGoodsExistInFavorite(goodsId,userId)){
                log.error("用户"+userId+"已经收藏商品"+goodsId);
                result.setResultCode(-2);
                result.setContent("商品已经在收藏列表里");
                return result;
            }
        }
        Set<String> set=new HashSet<>();
        set.addAll(goodsIdList);
        Favorite favorite=new Favorite();
        favorite.setGoodsIdSet(set);
        favorite.setUserId(userId);
        favoriteDao.save(favorite);
        result.setResultCode(0);
        return result;
    }

    @Override
    public Result deleteFavorite(List<String> goodsIdList,String userId) {
        Result result=new Result();
        Set<String> set=new HashSet<>();
        set.addAll(goodsIdList);
        Favorite favorite=new Favorite();
        favorite.setUserId(userId);
        favorite.setGoodsIdSet(set);
        favoriteDao.delete(favorite);
        result.setResultCode(0);
        return result;
    }

    @Override
    public Result getFavorite(String userId) {
        Result result=new Result();
        Favorite favorite=favoriteDao.getFavoriteByUserId(userId);
        if (favorite==null){
            result.setResultCode(-1);
            result.setResultInfo("没有查找到对应的记录");
            return result;
        }
        result.setResultCode(0);
        //获取商品
        List<Goods> list=new ArrayList<>();
        for (String goodId : favorite.getGoodsIdSet()) {
            Goods goods = goodsDao.get(Goods.class, goodId);
            list.add(goods);
        }
        result.setContent(list);
        log.info("从用户"+userId+"获取购物车的情况为："+result);
        return result;
    }

    @Override
    public Result cancelOrder(String user_id, Integer order_id) {
        Result result = new Result();
        Order order = orderDao.get(Order.class, order_id);
        if (order==null||!order.getUser().getUserId().equals(user_id)) {
            result.setResultCode(-1);
            result.setResultInfo("商品ID不合法："+order_id);
            return result;
        }
        if (order.getStatus()!=StatusType.待审核) {
            result.setResultCode(-2);
            result.setResultInfo("订单 "+order_id+" 不处于未审核状态");
            return result;
        }
        order.setStatus(StatusType.已取消);
        orderDao.update(order);

        // 吴家驹 17/08/20添加，取消订单时修改库存 ↓
        List<OrderDetail> list = orderDetailDao.getOrderDetailByOrder(order);
        Goods goods;
        for (OrderDetail orderDetail : list) {
            goods = orderDetail.getGoods();
            goods.setStock(goods.getStock() + orderDetail.getGoodsNumber());
            goodsDao.update(goods);
        }
        // ↑

        result.setResultCode(0);
        result.setResultInfo("取消订单成功");
        return  result;
    }

    @Override
    public Result saveComment(String goodsId, String nickname, Date time, String content, int score) {
        Comment comment = new Comment();
        comment.setContent(content);
        Goods goods = goodsDao.get(Goods.class, goodsId);
        comment.setGoods(goods);
        comment.setNickname(nickname);
        comment.setScore(score);
        comment.setTime(time);
        commentDao.save(comment);
        Result result = new Result();
        result.setContent(null);
        result.setResultInfo("添加成功");
        result.setResultCode(0);
        return result;
    }

    @Override
    public Result confirmReceipt(Integer order_id, String user_id) {
        Result result = new Result();
        Order order = orderDao.get(Order.class, order_id);
        if (order==null||!order.getUser().getUserId().equals(user_id)) {
            result.setResultCode(-1);
            result.setContent("order_id \""+order_id+"\" 错误");
            return result;
        }
        if (order.getStatus()!=StatusType.审核通过) {
            result.setResultCode(-2);
            result.setContent("操作不合法，当前订单状态为："+order.getStatus());
            return result;
        }
        // 设置订单状态为：已收货
        order.setStatus(StatusType.已收货);
        orderDao.update(order);
        // 增加积分
        User user = order.getUser();
        user.setPoints(user.getPoints()+order.getPoints());
        userDao.update(user);
        result.setResultCode(0);
        result.setResultInfo("确认收货成功，当前用户积分为："+user.getPoints());
        return result;
    }

    /**
     * 根据积分获取会员等级
     *
     * @param points 积分
     * @return 会员等级
     */
    private VipLevel getVipLevelByPoints(int points) {
        log.info("获取等级");
        List<VipLevel> levels = vipLevelDao.getAll();
        if (levels == null) {
            log.warn("等级列表为空");
            return null;
        }
        levels.sort(Comparator.comparingInt(VipLevel::getLowerLimitCredit));
        VipLevel currentLevel = new VipLevel();
        for (VipLevel level : levels) {
            if (points > level.getLowerLimitCredit())
                currentLevel = level;
            else
                break;
        }
        return currentLevel;
    }
}
