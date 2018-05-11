#!/usr/bin/python3

import serial
import time
import sqlite3
import sys

class Database:
	def __init__(self, path):
		self.conn = sqlite3.connect(path)
		print ('Opened database successfilly.')

	def addToDatabase(self, oldSensor, newSensor):
		self.conn.execute("INSERT INTO data (time, oldSensor, newSensor) VALUES (datetime('now', 'localtime'),  " + str(oldSensor) + ", " + str(newSensor) + ")")
		
		self.conn.commit()

		print ("Records created successfully.")

	def close(self):
		self.conn.close()



class SerialConnection:
	def __init__(self, devicePath):
		self.connection = serial.Serial(devicePath, 9600, timeout=5)
		time.sleep(3)
		print("Initialized.")
		self.connection.flush()

	def openDatabase(self, path):
		self.database = Database(path)

	def formatLine(self, line):
		line = str(line).replace("b'", '')
		line = line.replace("\\r\\n'", '')
		return line.split(":")

	def requestForMeasurments(self):
		self.connection.write(bytes(b'r'))

	def getResults(self):
		self.requestForMeasurments()
		while True:
			received = self.connection.readline()
			if (received):
				return self.formatLine(received)

	def writeToDatabase(self):
		values = self.getResults()
		self.database.addToDatabase(values[0], values[1])

	def print(self, values):
		print("old sensor value: " + values[0] + "/1023\t new sensor value: " + values[1] + "/1023")

	def monitor(self):
		while True:
			try:
				values = self.getResults()
				self.print(values)
				time.sleep(1)
			except KeyboardInterrupt:
				break

	def closeDatabase(self):
		self.database.close()
		print("database closed.")

	def close(self):
		self.connection.close()
		print("connection closed.")

		

databasePath = "soilMoisture-samples.db"
arduinoDevice = "/dev/ttyACM0"

arduino = SerialConnection(arduinoDevice)

if (sys.argv[1] == "write"):
	arduino.openDatabase(databasePath)
	arduino.writeToDatabase()
	arduino.closeDatabase()
elif (sys.argv[1] == "monitor"):
	arduino.monitor()
else:
	print("Use 'monitor' or 'write' argument")

arduino.close()