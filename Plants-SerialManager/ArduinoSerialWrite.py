import serial
import time
import sys


conn = serial.Serial("/dev/ttyACM0")
time.sleep(3)
if sys.argv[1] == '0':
	conn.write(bytes(b'0'))
else:
	conn.write(bytes(b'1'))

conn.flush()
conn.close()