import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BCM)

GPIO.setup(23, GPIO.OUT)

while True:
	GPIO.output(23, GPIO.HIGH)
	time.sleep(1)
	print "HIGH"
	GPIO.output(23, GPIO.HIGH)
	time.sleep(1)
	GPIO.output(23, GPIO.LOW)
	time.sleep(1)
	print "LOW"
	GPIO.output(23, GPIO.LOW)
	time.sleep(1)
