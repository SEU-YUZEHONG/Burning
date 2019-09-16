import psutil   # cd C:\Python36-32\Scripts  pip install psutil

class My_system():
    # 获取本机磁盘使用率和剩余空间G信息
    def get_disk_info(self):
        # 循环磁盘分区
        content = ""
        num=0
        for disk in psutil.disk_partitions():
            # 读写方式 光盘 or 有效磁盘类型
            if 'cdrom' in disk.opts or disk.fstype == '':
                continue
            disk_name_arr = disk.device.split(':')
            disk_name = disk_name_arr[0]
            disk_info = psutil.disk_usage(disk.device)
            # 磁盘剩余空间，单位G
            free_disk_size = disk_info.free//1024//1024//1024
            # 当前磁盘使用率和剩余空间G信息
            info = '"C_Disk_Usage":"%s%%","free_disk_size":"%sG"' % (str(disk_info.percent), str(free_disk_size))
            # print(info)
            # 拼接多个磁盘的信息
            if num==0:
                content = content + info
            else:
                content=content+','+info
            num=num+1
            break
        print(content)
        return content

    # cpu信息
    def get_cpu_info(self):
        cpu_percent = psutil.cpu_percent(interval=1)
        cpu_info = '"cpu_percent":"%s%%"' % (str(cpu_percent))
        print(cpu_info)
        return cpu_info

    # 内存信息
    def get_memory_info(self):
        virtual_memory = psutil.virtual_memory()
        used_memory = virtual_memory.used/1024/1024/1024
        free_memory = virtual_memory.free/1024/1024/1024
        memory_percent = virtual_memory.percent
        memory_info = '"used_memory":"%0.2fG","memory_percent":"%0.1f%%","free_memory":"%0.2fG"' % (used_memory, memory_percent, free_memory)
        print(memory_info)
        return memory_info

if __name__ == "__main__":
    sys=My_system()
    sys.get_cpu_info()
    sys.get_disk_info()
    sys.get_memory_info()