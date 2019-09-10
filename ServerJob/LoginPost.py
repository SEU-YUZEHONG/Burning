# coding:utf-8
import requests
import os
import re
# import json
import itertools
import urllib.request as request
import sys


data = {
    "vercode": 1,
    "password": "Mly200211",
    "userName": 213173625
}

checkUrl = 'http://xk.urp.seu.edu.cn/studentService/system/login.action'
codeurl = 'http://xk.urp.seu.edu.cn/studentService/getCheckCode'


class checkSchoolServer(object):
   
    def __init__(self):
        try:
            self.sess = requests.session()
            self.sess.get(checkUrl)
            self.valcode = self.sess.get(codeurl) # 获取valcode并维护session
            #f = open('vercode.jpg', 'wb')
            # 将response的二进制内容写入到文件中
            #f.write(self.valcode.content)
            # 关闭文件流对象
            #f.close()
        except Exception as e:
            print(e)

    def checkLogin(self, password, userName, vercode):
        try:
            data["vercode"] = str(vercode)
            data["password"] = str(password)
            data["userName"] = str(userName)
            # 传输用户名，密码，验证码并得到学校返回
            r = self.sess.post(checkUrl, data=data)
            # 从返回中找到alertError错误字段，识别错误类型
            where = r.text.find('<body onload="alertError')
            if where == -1:
                return True
            tex = r.text[where:where+50]
            print(tex)
            # 判断错误情况
            if tex.find("验证码") != -1:
                return "vercodeError"
            elif tex.find("用户名") != -1:
                return "passwordError"
            return False
        except Exception as e:
            print("Login Post error"+e)
            return False


# if __name__ == "__main__":
#    school = checkSchoolServer()
#    school.checkLogin()
