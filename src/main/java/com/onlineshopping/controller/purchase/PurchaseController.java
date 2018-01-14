package com.onlineshopping.controller.purchase;

import com.onlineshopping.annotation.UserAccessRequired;
import com.onlineshopping.entity.Comment;
import com.onlineshopping.entity.Order;
import com.onlineshopping.entity.Result;
import com.onlineshopping.service.purchase.PurchaseService;
import com.onlineshopping.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jacky on 17-6-12.
 */
@RestController
@RequestMapping(value = "/user")
public class PurchaseController {

    private static final Logger log= LoggerFactory.getLogger(PurchaseController.class);

    @Resource
    private PurchaseService purchaseService;

    @UserAccessRequired
    @RequestMapping(value = "/goods/{goodsId}/comment", method = RequestMethod.POST)
    public Result submitComment(@PathVariable(value = "goodsId") String goodsId,
                                @RequestParam(value = "nickname") String nickname,
                                @RequestParam(value = "time") String timeStr,
                                @RequestParam(value = "content") String content,
                                @RequestParam(value = "score") int score) {
        log.info("访问：POST /goods/" + goodsId + "/comment");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date time;
        try {
            time = format.parse(timeStr);
        } catch (ParseException e) {
            Result result = new Result();
            result.setResultInfo("时间格式错误");
            result.setResultCode(-2);
            result.setContent(null);
            return result;
        }
        return purchaseService.saveComment(goodsId, nickname, time, content, score);
    }

    @UserAccessRequired
    @RequestMapping(value = "/orders",method = RequestMethod.POST)
    public Result submitOrder(@RequestBody String body,HttpSession session) throws IOException {
        String userId=(String)session.getAttribute("user_id");
        log.info("用户{}提交订单信息：{}",userId,body);
        Map<String ,Object> map=JsonUtil.jsonToMap(body);
        Result result=purchaseService.saveOrder(map,userId);
        return result;
    }

    /**
     * 确认收货
     * 订单状态更改为已收货，并用户增加相应的积分
     * @param order_id 订单id
     * @param session 会话信息
     * @return
     */
    @UserAccessRequired
    @PutMapping("/orders/{order_id}/receipt")
    public Result confirmReceipt(@PathVariable("order_id") Integer order_id, HttpSession session) {
        String user_id = (String) session.getAttribute("user_id");
        log.info("\n用户\"{}\"发送确认收货请求，订单号为 {}",user_id, order_id);
        return purchaseService.confirmReceipt(order_id, user_id);
    }

    /**
     * 取消订单
     *
     * @param order_id 订单id
     * @param session 会话信息
     * @return
     */
    @UserAccessRequired
    @PutMapping("/orders/{order_id}")
    public Result updateOrderStatus(@PathVariable Integer order_id,HttpSession session) {
        String user_id = (String) session.getAttribute("user_id");
        log.info("\n用户\"{}\"发送取消订单请求，订单号为 {}",user_id, order_id);
        return purchaseService.cancelOrder(user_id, order_id);
    }

    @UserAccessRequired
    @GetMapping(value = "/shoppingcart")
    public Result getGoodsFromShoppingcart(HttpSession session) throws IOException {
        log.info("用户获取购物车的商品");
        String userId=(String)session.getAttribute("user_id");
        Result result=purchaseService.getShoppingCart(userId);
        return result;
    }

    @UserAccessRequired
    @PutMapping(value = "/shoppingcart")
    public Result addGoodsToShoppingcart(@RequestParam String goodsId,HttpSession session) throws IOException {
        log.info("用户添加到购物车的商品id："+goodsId);
        String userId=(String)session.getAttribute("user_id");
        List<String> list= new ArrayList<>();
        list.add(goodsId);
        Result result=purchaseService.saveShoppingCart(list,userId);
        return result;
    }


    @UserAccessRequired
    @PostMapping(value = "/shoppingcart")
    public Result removeGoodsFromShoppingcart(@RequestParam String goodsId,HttpSession session) throws IOException {
        log.info("用户删除购物车的商品:"+goodsId);
        String userId=(String)session.getAttribute("user_id");
        List<String> list= new ArrayList<>();
        list.add(goodsId);
        Result result=purchaseService.deleteShoppingCart(list,userId);
        return result;
    }

    @UserAccessRequired
    @GetMapping(value = "/favorite")
    public Result getGoodsFromFavorite(HttpSession session) throws IOException {
        log.info("用户获取收藏夹的商品");
        String userId=(String)session.getAttribute("user_id");
        Result result=purchaseService.getFavorite(userId);
        return result;
    }

    @UserAccessRequired
    @PutMapping(value = "/favorite")
    public Result addGoodsToFavorite(@RequestParam String goodsId,HttpSession session) throws IOException {
        log.info("用户添加商品到收藏夹:"+goodsId);
        String userId=(String)session.getAttribute("user_id");
        List<String> list= new ArrayList<>();
        list.add(goodsId);
        Result result=purchaseService.saveFavorite(list,userId);
        return result;
    }

    @UserAccessRequired
    @PostMapping(value = "/favorite")
    public Result removeGoodsFromFavorite(@RequestParam String goodsId,HttpSession session) throws IOException {
        log.info("用户删除收藏夹的商品:"+goodsId);
        String userId=(String)session.getAttribute("user_id");
        List<String> list= new ArrayList<>();
        list.add(goodsId);
        Result result=purchaseService.deleteFavorite(list,userId);
        return result;
    }
}
