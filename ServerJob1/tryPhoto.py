#coding=utf-8
import requests
import json
headers = {
 "Accept":"*/*"
 }
url = "http://49.235.241.216:8089"
files = {"method":'getUserInfo'}
r = requests.post(url, files=files)
print (r.url)
print (r.text)