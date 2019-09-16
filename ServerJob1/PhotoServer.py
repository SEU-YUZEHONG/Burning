# coding=utf-8
from http.server import HTTPServer, BaseHTTPRequestHandler
import cgi

class PostHandler(BaseHTTPRequestHandler):
    def do_POST(self):
        form = cgi.FieldStorage(
            fp=self.rfile,
            headers=self.headers,
            environ={'REQUEST_METHOD': 'POST',
                    'CONTENT_TYPE': self.headers['Content-Type'],
                    }
        )
        self.send_response(200)
        self.end_headers()
        for field in form.keys():
            field_item = form[field]
            filename = field_item.filename
            filevalue = field_item.value
            filesize = len(filevalue)  # 文件大小(字节)
            with open('/var/www/html/Photos/%s'%(filename), 'wb') as f:
                f.write(filevalue)
        return


def StartServer():
    sever = HTTPServer(("", 9999), PostHandler)
    sever.serve_forever()


if __name__ == '__main__':
    StartServer()
