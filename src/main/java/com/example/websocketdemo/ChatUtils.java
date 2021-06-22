package com.example.websocketdemo;

/**
 * @author wang wei
 * @description
 * @create 2021/6/22 10:43
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天室功能实现的一个工具类
 */
public class ChatUtils {

   //定义日志对象
   private static final Logger logger= LoggerFactory.getLogger(ChatUtils.class);

   //定义map集合，确保数据共享和安全，这里使用ConcurrentHashMap
   //用户名为key，session信息为value
   public static final Map<String, Session> CLIENTS=new ConcurrentHashMap<>();

   /**
    * 使用连接发送消息
    * @param session 用户的session
    * @param message 发送的消息内容
    */
   public static void sendMessage(Session session,String message) {
      if (session == null) {
         return;
      }

      final RemoteEndpoint.Basic basic=session.getBasicRemote();
      if (basic == null) {
         return;
      }

      try {
         basic.sendText(message);
      } catch (IOException e) {
         e.printStackTrace();
         logger.error("sendMessage IOException",e);
      }
   }

   /**
    * 发送消息给所有人
    * @param message
    */
   public static void sendMessageAll(String message) {
      CLIENTS.forEach((sessionId,session) -> sendMessage(session,message));
   }

   /**
    * 获取所有的在线用户
    */
   public static String getOnlineInfo() {
      Set<String> userNames=CLIENTS.keySet();
      if (userNames.size() == 0) {
         return "当前无人在线......";
      }
      return userNames.toString() + "在线";
   }
}