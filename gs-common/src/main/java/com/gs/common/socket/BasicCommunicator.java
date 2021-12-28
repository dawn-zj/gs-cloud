package com.gs.common.socket;

import com.gs.common.define.Constants;
import com.gs.common.util.HexUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Administator
 * @date 2021-10-19 14:06
 * @Description
 */
public class BasicCommunicator {
    protected long maxRequestLength = Constants.LENGTH_5MB;
    protected long maxResponseLength = Constants.LENGTH_5MB;

    protected int READ_BLOCK_LENGTH = 1024; // 按块读

    public Socket socket;
    protected DataOutputStream out;
    protected DataInputStream input;

    /**
     * 构造函数
     *
     * @param s
     *            服务器与客户端的Socket连接
     */
    public BasicCommunicator(Socket s) {
        socket = s;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
        } catch (Exception e) {
        }
    }

    /**
     * 发送二进制数据
     */
    public void send(byte[] data) throws Exception {
        byte[] bs = HexUtil.int2Byte(data.length);
        byte[] sData = new byte[bs.length + data.length];

        System.arraycopy(bs, 0, sData, 0, bs.length);
        System.arraycopy(data, 0, sData, bs.length, data.length);
        out.write(sData);
        out.flush();
    }

    /**
     * 读取数据开头4个字节,取得接受数据的长度.
     *
     * @return 数据长度
     * @throws IOException
     */
    public int recvLength() throws Exception {
        int len = input.readInt();
        if ((len <= 0) || (len > maxRequestLength))
            throw new Exception("recv length is over limit, len is " + len);

        return len;
    }

    public byte[] recv() throws Exception {
        // 收到的报文长度
        int len = recvLength();

        byte[] allData = new byte[len];
        byte[] data = null;
        int rlen = len;

        while (rlen > 0) {
            if (rlen > READ_BLOCK_LENGTH)
                data = new byte[READ_BLOCK_LENGTH];
            else
                data = new byte[rlen];

            int alen = input.read(data);
            System.arraycopy(data, 0, allData, len - rlen, alen);
            rlen -= alen;
        }

        return allData;
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
        }
    }

}
