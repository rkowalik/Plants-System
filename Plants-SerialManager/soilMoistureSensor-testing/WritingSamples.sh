#!/bin/bash

interval=10
duration=1800

cd ../
for ((i=0; i<=$duration; i+=$interval)) {
	./Plants-SerialManager.py write
	log="`date` [$i second]: writed to database"
	echo $log >> samples/logs.txt
	echo $log
	sleep $interval
	i=$((i+6))	# duration of gathering data
}

notify-send "Zbieranie próbek" "Zakończono zbieranie."