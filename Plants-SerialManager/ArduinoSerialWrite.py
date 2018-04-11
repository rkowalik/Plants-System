#!/usr/bin/env python3

import serial
import time
import sys

print("Opening connection..")
conn = serial.Serial("/dev/ttyACM0")
time.sleep(3)
if sys.argv[1] == '0':
	conn.write(bytes(b'0'))
else:
	conn.write(bytes(b'1'))

print("Signal sent.")
conn.flush()
conn.close()
