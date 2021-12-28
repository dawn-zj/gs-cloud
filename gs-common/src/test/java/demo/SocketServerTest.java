package demo;

import com.gs.common.socket.BasicCommunicator;
import com.gs.common.util.StringUtil;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Administator
 * @date 2021-10-19 13:52
 * @Description
 */
public class SocketServerTest {

    public static void main(String[] args) throws Exception {
        // 1. 创建套接字，绑定端口并监听
        ServerSocket server = new ServerSocket(9000);
        // 2. 阻塞，直到有客户端连接
        System.out.println("你不来，我不走，风里雨里一直等你...");
        Socket socket = server.accept();
        // 3. 接收客户端消息，并回复
        BasicCommunicator communicator = new BasicCommunicator(socket);

        // todo 整合实际业务，编写一个处理器，将请求转到实际业务层，并拿到业务层处理结果，响应给客户端
        // 参考NetSeal AppServer 的 SocketListener和SocketConnectionHandler
        byte[] recv = communicator.recv();
        System.out.println("服务端接收到的信息：" + StringUtil.getString(recv));

        String message = "Bye";
        communicator.send(StringUtil.getBytes(message));

        // 4. 关闭连接
        socket.close();

    }

}
