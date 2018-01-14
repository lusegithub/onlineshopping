package com.onlineshopping.entity;

/**
 * Created by hehe on 17-6-11.
 * The enum class is used for the 'status' attribute of the order entity
 */
public enum StatusType {
      /**
       * 已取消：用户取消
       * 审核未通过：后台取消
       */
      待审核, 审核通过, 已收货, 已取消, 审核未通过
}
