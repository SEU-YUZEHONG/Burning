# -*- coding: utf-8 -*-
import pymysql
import json
from LoginPost import checkSchoolServer 
from globalManager import gol

class MysqlUtil(object):
    name = 'MysqlUtil'
    """
    use for connect Mysql ,need sql words
    """

    def __init__(self):
        try:
            # 创建链接
            self.conn = pymysql.connect(
                host='49.235.241.216',  # 连接你要取出数据库的ip，如果是本机可以不用写
                port=3306,  # 端口
                user='root',  # 你的数据库用户名
                passwd='Mly200211',  # 你的数据库密码
                db='basicdb',  # 数据库
                charset='utf8'
            )
            # 创建一个游标对象
            self.cur = self.conn.cursor(pymysql.cursors.DictCursor)
            #服务器开启进行数据库维护
            sql_TableCreate = "create table if not exists Users(userNumber int, userType int, userAccount char(20), currentlngtion int" +\
                ", userAllTime double, userAim text)"
            sql_UserCheck = "select * from Users"
            cursor = self.conn.cursor()
            cursor.execute(sql_TableCreate)
            cursor.execute(sql_UserCheck)
            worldList = []
            for Users in cursor:
                worldList.append([Users])
            print(worldList)
            self.conn.commit()
            
        except Exception as e:
            print("mysql error ", str(e))
            mysqlutil = MysqlUtil()
            self.conn.rollback()

    def mysqlAll(self, js):
        try:
            global mysqlutil
            Js = json.loads(js)
            #解析客户端请求并执行相应操作
            if Js["method"] == "userLogin":
                return str(self.Login(Js["data"][0]))
            elif Js["method"] == "userCreate":
                return str(self.Create(Js["data"][0]))
            elif Js["method"] == "userJobFinish":
                return str(self.JobFinish(Js))
            elif Js["method"] == "userToDoUpdate":
                return str(self.ToDoUpdata(Js))
            elif Js["method"] == "userTolngitionLeaderBoard":
                return str(self.TolngitionLeardBoard(Js))
            elif Js["method"] == "userAllTimeLeaderBoard":
                return str(self.AllTimeLeaderBoard(Js))

        except Exception as e:
            print("mysql error ", e)
            mysqlutil = MysqlUtil()
            self.conn.rollback()
            return str(e)
        finally:
            # 关闭游标和连接
            self.cur.close()
            self.conn.close()

    def Login(self,Js):
        try:
            sqlQ = "select * from Users where userAccount='" + \
                Js["userAccount"]+"'"
            print(sqlQ)
            cursor = self.conn.cursor()
            cursor.execute(sqlQ)
            worldList = []
            for Users in cursor:
                worldList.append([Users])
            schoolCheck=gol.get_schoolCheck()
            if worldList == [] and schoolCheck.checkLogin(Js["userPassword"],Js["userAccount"],Js["verCode"])==True:
                self.Create(Js) #用户未登陆过，创建用户表格
                return True
            return schoolCheck.checkLogin(Js["userPassword"],Js["userAccount"],Js["verCode"])
        except Exception as e:
            print("Login error ", str(e))
            mysqlutil = MysqlUtil()
            self.conn.rollback()
            return str(e)

    def Create(self, Js):
        try:
            #查询用户是否存在
            sqlQ = "select * from Users where userAccount='" + \
                Js["userAccount"]+"'"
            print(sqlQ)
            cursor = self.conn.cursor()
            cursor.execute(sqlQ)
            worldList = []
            for Users in cursor:
                worldList.append([Users])
            #用户存在不允许再创建
            if worldList != []:
                return "User exist"
            #用户表格中插入用户信息
            sql = "insert into Users(userType,userAccount) values("+str(Js["userType"]) +\
                ","+"'"+Js["userAccount"]+"')"
            print(sql)
            cursor.execute(sql)
            #创建用户Job表格
            sql_UserJobTable = "create table if not exists "+Js["userAccount"]+"Job(jobName char(20), " +\
                "jobSetDuration double, jobCreateTime char(20), jobStartTime char(20), jobEndTime char(20), jobSusbendTime char(20), "+\
                "jobType int, jobScene char(20), jobAlreadyTime double, Concentration int, Circularity int)"
            print(sql_UserJobTable)
            cursor.execute(sql_UserJobTable)
            #创建用户Todo表格
            sql_UserToDoTable = "create table if not exists "+Js["userAccount"]+"Todo(jobName char(20), " +\
                "jobSetDuration double, jobCreateTime char(20), jobStartTime char(20), jobEndTime char(20), jobSusbendTime char(20), "+\
                "jobType int, jobScene char(20), jobAlreadyTime double, currentTime char(20), currentlngtion double, currentSlience double, tolgnition double)"
            print(sql_UserToDoTable)
            cursor.execute(sql_UserToDoTable)
            self.conn.commit()
            return "True"
        except Exception as e:
            print("Create error ", str(e))
            mysqlutil = MysqlUtil()
            self.conn.rollback()
            return str(e)

    def JobFinish(self, Js):
        try:
            cursor = self.conn.cursor()
            AllTime=0.0
            #任务完成，更新用户AllTime
            if Js["jobSetDuration"] == Js["jobAlreadyTime"]:
                sql_FindAllTime = "select userAllTime for Users where userAccount='" + \
                    str(Js["userAccount"])+"'"
                print(sql_FindAllTime)
                cursor.execute(sql_FindAllTime)
                AllTime = cursor.fetchone()[0]
                sql_AddAllTime = "update User set userAllTime=" + \
                    str(AllTime+Js["jobSetDuration"])
                print(sql_AddAllTime)
            #增加用户记录
            sql_AddRecord = "insert into "+Js["userAccount"]+"Job values('"+str(Js["jobName"])+"'"+","+"'"\
                + str(Js["jobSetDuration"])+"'"+","+"'"+str(Js["jobCreateTime"])+"'"+","+"'"+str(Js["jobStartTime"])+"'"+","+"'"\
                + str(Js["jobEndTime"])+"'"+","+"'"+str(Js["jobSusbendTime"])+"'"+","+"'"+str(Js["jobType"])+"'"+","+"'"\
                + str(Js["jobScene"])+"'"+","+"'"+str(Js["jobAlreadyTime"])+"'"+","+"'"+str(Js["Concentration"])+"'"+","+"'"\
                + str(Js["Circularity"])+"')"
            cursor.execute(sql_FindAllTime)
            print(sql_AddRecord)
            cursor.execute(sql_AddRecord)
            self.conn.commit()
            return "Job Recode Add Success"
        except Exception as e:
            print("JobFinish error ", str(e))
            mysqlutil = MysqlUtil()
            self.conn.rollback()
            return str(e)
    
    def getToDoList(self,userAccount):
        try:
            #获取表中存储的ToDo任务
            sql_TodoList = "select * from "
            cursor = self.conn.cursor()
            cursor.execute(sql_TodoList)
            TodoList = []
            for job in cursor:
                TodoList.append([job])
            print(TodoList)
            return TodoList
        except Exception as e:
            print("getToDoList error ", str(e))
            mysqlutil = MysqlUtil()
            self.conn.rollback()
            return str(e)
    
    def ToDoUpdata(self,Js):
        try:
            #ToDo表格与客户端动态同步
            sql_CoverUserToDo = "delete from "+Js["userAccount"]+"Todo"
            print(sql_CoverUserToDo)
            cursor = self.conn.cursor()
            cursor.execute(sql_CoverUserToDo)
            for ToDo in Js["data"]:
                sql_ToDoUpdata = "insert into "+Js["userAccount"]+"Todo values('"+str(ToDo["jobName"])+"'"+","+"'"\
                    + str(ToDo["jobSetDuration"])+"'"+","+"'"+str(ToDo["jobCreateTime"])+"'"+","+"'"+str(ToDo["jobStartTime"])+"'"+","+"'"\
                    + str(ToDo["jobEndTime"])+"'"+","+"'"+str(ToDo["jobSusbendTime"])+"'"+","+str(ToDo["jobType"])+","+"'"\
                    + str(ToDo["jobScene"])+"'"+","+"'"+str(ToDo["jobAlreadyTime"])+"'"+","+"'"+str(ToDo["currentTime"])+"'"+","\
                    + str(ToDo["currentlngtion"])+str(ToDo["currentSlience"]
                                                        )+","+str(ToDo["tolgnition"])+")"
                print(sql_ToDoUpdata)
                cursor.execute(sql_ToDoUpdata)
            self.conn.commit()
            return "True"
        except Exception as e:
            print("ToDoUpdata error ", str(e))
            mysqlutil = MysqlUtil()
            self.conn.rollback()
            return "ToDoUpdata error "+str(e)

    def TolngitionLeardBoard(self,Js):
        try:
            #将燃度值比用户大的都挑选出来，计算得出用户排名
            sql_Select = "select userAccount from Users where currentlngtion>" + \
                str(Js["currentlngtion"])
            print(sql_Select)
            cursor = self.conn.cursor()
            cursor.execute(sql_Select)
            num = 1
            for Users in cursor:
                num = num+1
            self.conn.commit()
            return str(num)
        except Exception as e:
            print("TolngitionLeaderBoard error ", str(e))
            mysqlutil = MysqlUtil()
            self.conn.rollback()
            return "TolngitionLeaderBoard error "+str(e)

    def AllTimeLeaderBoard(self,Js):
        try:
            #由用户名找到用户累计时间
            sql_SelectTime = "select userAllTime from Users where userAccount='" + \
            str(Js["userAccount"])+"'"
            cursor = self.conn.cursor()
            cursor.execute(sql_SelectTime)
            Time = cursor.fetchone()[0]
            #挑选累计时间大于用户累计时间的，得出用户排名
            sql_Select = "select userAccount from Users where userAllTime>" + \
                str(Time)
            print(sql_Select)
            cursor.execute(sql_Select)
            num = 0
            for Users in cursor:
                num = num+1
            self.conn.commit()
            return str(num)
        except Exception as e:
            print("AllTimeLeaderBoard error ", str(e))
            mysqlutil = MysqlUtil()
            self.conn.rollback()
            return "AllTimeLeaderBoard error "+str(e)


if __name__ == '__main__':
    mysqlutil = MysqlUtil()
