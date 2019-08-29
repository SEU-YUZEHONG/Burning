# -*- coding: utf-8 -*-
from LoginPost import checkSchoolServer

_global_dict=checkSchoolServer()
class gol:
    def _init():#初始化
        global _global_dict
        _global_dict=checkSchoolServer()
    
    def set_schoolCheck(schoolCheck):
        global _global_dict
        _global_dict=schoolCheck

    def get_schoolCheck():
        return _global_dict
    