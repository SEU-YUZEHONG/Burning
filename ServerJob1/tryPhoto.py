#coding=utf-8
import requests
import json
headers = {
 "Accept":"*/*"
 }
url = "http://49.235.241.216:9999"
path = "IMG_0433.jpg"
files = {'file': open(path, 'rb')}
r = requests.post(url, files=files)
print (r.url)
print (r.text)