using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net.Sockets;

namespace Server
{
    class ClientThread
    {
        public Socket client = null;
        int i;
        public ClientThread(Socket k)
        {
            client = k;
        }
        public void ClientService()
        {
            string data = null;
            byte[] bytes = new byte[1024];
            Console.WriteLine("新用户的连接。。。");
            try
            {
                while ((i = client.Receive(bytes)) != 0)
                {
                    data = System.Text.Encoding.ASCII.GetString(bytes, 0, i);
                    Console.WriteLine("收到数据,长度：{0},内容：{1}",bytes.Length, data);
                    data = data.ToUpper();
                    bytes = System.Text.Encoding.ASCII.GetBytes("data from server time "+DateTime.Now.ToString());
                    client.Send(bytes);
                }
            }
            catch (System.Exception exp)
            {
                Console.WriteLine(exp.ToString());
            }
            client.Close();
            Console.WriteLine("用户断开连接。。。");
        }  
    }
}
