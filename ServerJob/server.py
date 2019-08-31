# -*- coding: utf-8 -*-
from globalManager import gol
from LoginPost import checkSchoolServer
from sqlUtil import MysqlUtil
from http.server import HTTPServer, BaseHTTPRequestHandler
from collections import defaultdict
import json
import string
import socketserver
import pymysql
import threading
import sys
import random
sys.path.append('./*')

# 服务端

class MyServer(BaseHTTPRequestHandler):
    # 从a-zA-Z0-9生成指定数量的随机字符做为cookie
    ran_str_cookie = ''.join(random.sample(
        string.ascii_letters + string.digits, 24))
    # 处理get请求

    def do_GET(self):
        print("收到来自:"+self.client_address[0]+"的连接")
        schookCheck = checkSchoolServer()
        gol.set_schoolCheck(schookCheck)
        self.send_response(200)
        # 发送响应头部并附带cookie
        self.send_header('cookie: '+self.ran_str_cookie +
                         '\n'+'Content-type', 'application/json')
        print('cookie: '+self.ran_str_cookie+'\n' +
              'Content-type', 'application/json')
        self.end_headers()
        # 发送维护的验证码
        self.wfile.write(gol.get_schoolCheck().valcode.content)

    # 处理post请求
    def do_POST(self):
        print("收到来自:"+self.client_address[0]+"的连接")
        head = str(self.headers)
        print(head)
        # 寻找头部cookie并与本地匹配，验证成功才响应
        print(head[head.find('Cookie')+8:head.find('Cookie')+32])
        if head.find('Cookie') == -1:
            return "No cookie"
        elif head[head.find('Cookie')+8:head.find('Cookie')+32] != self.ran_str_cookie:
            return "Wrong cookie"
        # 回复响应头部
        self.send_response(301)
        self.send_header('Content-type', 'application/json')
        self.end_headers()
        try:
            # 获取客户端传输数据
            length = int(self.headers['Content-Length'])
            data = self.rfile.read(length).decode('utf-8')
            print(data)
            Js = json.loads(data)
            print(Js)
            # 建立sql数据库对象
            global mysqlutil
            mysqlutil = MysqlUtil()
            # 获取处理结果
            respond = mysqlutil.mysqlAll(Js)
            print(respond)
            # 反馈结果
            self.wfile.write(str(respond).encode('utf-8'))
        except Exception as e:
            mysqlutil = MysqlUtil()
            self.wfile.write("json error ".encode('utf-8'))
            print("json error "+str(e))


if __name__ == '__main__':
    mysqlutil = MysqlUtil()
    gol._init()
    # 多线程的TCP服务端，可以同时开启多个任务等着客户端来连，来一个请求就处理一个
    s = HTTPServer(("", 8089), MyServer)
    s.serve_forever()
