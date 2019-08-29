# -*- coding: utf-8 -*-
import random
import string
from LoginPost import checkSchoolServer

#全局变量管理对象，维护从学校获取的登陆二维码
_global_dict=checkSchoolServer()
class gol:
    def _init():#初始化
        global _global_dict
        _global_dict=checkSchoolServer()
    
    def set_schoolCheck(schoolCheck):
        global _global_dict
        _global_dict=schoolCheck

    def get_schoolCheck():
        global _global_dict
        return _global_dict