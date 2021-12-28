package demo;

import com.gs.common.socket.BasicCommunicator;
import com.gs.common.util.StringUtil;

import java.net.Socket;

/**
 * @author Administator
 * @date 2021-10-19 13:52
 * @Description
 */
public class SocketClientTest {

    public static void main(String[] args) throws Exception {
        // 1. 创建套接字，指定服务端Ip、Port
        Socket socket = new Socket("127.0.0.1", 9000);
        // 2. 向服务端发送消息，并等待回复
        BasicCommunicator communicator = new BasicCommunicator(socket);
        String message = "我是client";
        communicator.send(StringUtil.getBytes(message));

        byte[] recv = communicator.recv();
        System.out.println(StringUtil.getString(recv));

        socket.close();
    }
}
