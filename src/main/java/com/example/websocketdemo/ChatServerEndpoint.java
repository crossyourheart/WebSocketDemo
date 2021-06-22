package com.example.websocketdemo;

/**
 * @author wang wei
 * @description
 * @create 2021/6/22 10:44
 */


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @ServerEndpoint注解中指定WebSocket协议的地址
 * @OnOpen、@OnMessage、@OnClose、@OnError注解与WebSocket中监听事件对应
 */
@Slf4j //生成一些日志代码
@Component
@ServerEndpoint("/websocket/{username}")
public class ChatServerEndpoint {

   /**
    * 连接建立时触发
    */
   @OnOpen
   public void onOpen(@PathParam("username") String username, Session session) {
      log.info("用户{}登录",username);
      String message= "用户[" + username + "]已进入聊天室！";

      //将该用户登录的消息发送给其他人
      ChatUtils.sendMessageAll(message);

      //将自己的信息添加到map集合中
      ChatUtils.CLIENTS.put(username,session);

      //获取当前的在线人数，发给自己查看
      String onlineInfo=ChatUtils.getOnlineInfo();
      ChatUtils.sendMessage(session,onlineInfo);
   }

   /**
    * 客户端接收服务端发来的数据时触发
    */
   @OnMessage
   public void onMessage(@PathParam("username") String username,String message) {
      log.info("发送消息：{}, {}",username,message);
      //广播，把消息同步给其他客户端
      ChatUtils.sendMessageAll("[" + username + "]: " + message);
   }

   /**
    * 连接关闭时触发
    */
   @OnClose
   public void onClose(@PathParam("username") String username,Session session) {
      //从当前的map集合中移除该用户
      ChatUtils.CLIENTS.remove(username);

      //将该用户离线的消息通知给其他人
      ChatUtils.sendMessageAll("[" + username + "]已离线！");

      try {
         //关闭WebSocket下的该Seesion会话
         session.close();
         log.info("{} 已离线......",username);
      } catch (IOException e) {
         e.printStackTrace();
         log.error("onClose error",e);
      }
   }

   /**
    * 聊天通信发生错误时触发
    */
   @OnError
   public void onError(Session session,Throwable throwable) {
      try {
         //关闭WebSocket下的该Seesion会话
         session.close();
      } catch (IOException e) {
         e.printStackTrace();
         log.error("onError Exception",e);
      }
      log.info("Throwable msg " + throwable.getMessage());
   }
}