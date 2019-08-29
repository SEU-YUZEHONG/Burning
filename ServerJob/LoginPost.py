# coding:utf-8
import requests
import os
import re
# import json
import itertools
import urllib.request as request
import sys


data={
 "vercode":1,
 "password":"Mly200211",
 "userName":213173625
}

sess = requests.session()
checkUrl='http://xk.urp.seu.edu.cn/studentService/system/login.action'
codeurl = 'http://xk.urp.seu.edu.cn/studentService/getCheckCode'

class checkSchoolServer(object):
    def __init__(self):
        try:
            sess.get(checkUrl)
            self.valcode = sess.get(codeurl)
            f = open('vercode.jpg', 'wb')
            # 将response的二进制内容写入到文件中
            f.write(self.valcode.content)
            # 关闭文件流对象
            f.close()
        except Exception as e:
            print(e)

    def checkLogin(self,password,userName,vercode):
        try:
            data["vercode"]=str(vercode)
            data["password"]=str(password)
            data["userName"]=str(userName)
            r=sess.post(checkUrl,data=data)
            if r.text.find("验证码错误")!=-1:
                return False
            return True
        except Exception as e:
            print(e)
            return False
