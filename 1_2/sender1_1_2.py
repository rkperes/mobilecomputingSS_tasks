import RPi.GPIO as GPIO
import time
import binascii as ba

def strtobin(strg):
	strbin = bin(int(ba.hexlify(strg), 16))[2:]
	if len(strbin) == 6:
                strbin = "0"+strbin
	return strbin

def send(strg, setupcode, sleeptime):
	GPIO.setmode(GPIO.BCM)
	GPIO.setup(setupcode, GPIO.OUT)
        
 	#syncronize
 	while True:
                #cnt = 0
		if int(time.time()) % int(20 * sleeptime) == 0:
 			#while cnt < len(strg):
                        for c in strg:
				#set time for sender 1
				while not (int(time.time()) % int(20 * sleeptime) < (10 * sleeptime)):
                                #the former if ....>0 is redundant, so we remove it
                                #And we change the if to while
                                #if the time for sending next char doesn't meet the condition
                                #the program will be stuck at this line and wait for the right time
                                
				#starting code 01
				GPIO.output(setupcode, GPIO.HIGH)
				time.sleep(sleeptime)
				GPIO.output(setupcode, GPIO.LOW)
				time.sleep(sleeptime)
				#c = strg[cnt]
				asc = strtobin(c)
				for b in asc:
					print b
					if b == '1':
						GPIO.output(setupcode, GPIO.LOW)
					elif b == '0':
						GPIO.output(setupcode, GPIO.HIGH)
					time.sleep(sleeptime)
				print asc
				print c
				time.sleep(sleeptime) # additional waiting time
				GPIO.output(setupcode, GPIO.LOW)
				#cnt += 1
setupcode = 23
sleeptime = 0.3 # sleeptime
send("hello from sender 1", setupcode, sleeptime)
