import pyrebase
import time
import RPi.GPIO as GPIO
import pigpio
import _thread

config = {
    "apiKey": "AIzaSyC8N96YrS-ardGy6OIef6hcVYeXS7ml7mc",
    "authDomain": "sonarrobot-152dc.firebaseapp.com",
    "databaseURL": "https://sonarrobot-152dc.firebaseio.com",
    "storageBucket": "sonarrobot-152dc.appspot.com"

}

GPIO.setmode(GPIO.BCM)

servoPin = 17
pi = pigpio.pi()
motorABackward = 5
motorAForward = 6
motorBForward = 26
motorBBackward = 19

TRIG = 21
ECHO = 20




firebase = pyrebase.initialize_app(config)
db = firebase.database()

GPIO.setup(TRIG,GPIO.OUT)
GPIO.setup(ECHO,GPIO.IN)
GPIO.setup(motorABackward,GPIO.OUT)
GPIO.setup(motorAForward,GPIO.OUT)
GPIO.setup(motorBForward,GPIO.OUT)
GPIO.setup(motorBBackward,GPIO.OUT)
GPIO.output(TRIG,0)
time.sleep(0.1)

def stream_handler(message):
    # GPIO sync
    if message['path'] == '/joystickAngle':
        joystickAngle = message['data']
        print("Angle is " + str(message['data']))
        
        #Stop
        if(joystickAngle== 0 ):
            resetMotorPins()
        #Right
        elif(0<joystickAngle<45):
            print("Moving Right..")
            moveMotorAForward()
            moveMotorBBackward()    
        #Straight
        elif(45<=joystickAngle<135):
            print("Moving Straight..")
            moveMotorAForward()
            moveMotorBForward()
        #Left
        elif(135<=joystickAngle<225):
            print("Moving Left..")
            moveMotorABackward()
            moveMotorBForward()     
        #Backward
        elif(225<=joystickAngle<315):
            print("Moving Backward..")
            moveMotorABackward()
            moveMotorBBackward()
        #Right as well
        elif(315<=joystickAngle<360 ):
            print("Moving Right..")
            moveMotorAForward()
            moveMotorBBackward()    
            
    elif message['path'] == '/joystickStrength':
        joystickStrength = message['data']
        print("Strength is " + str(message['data']))
        
my_stream = db.stream(stream_handler)

def write_to_db(key, value):
    db.child(key).set(value)
    
def moveMotorAForward():
    GPIO.output(motorAForward,GPIO.HIGH)
    GPIO.output(motorABackward,GPIO.LOW)
def moveMotorABackward():
    GPIO.output(motorABackward,GPIO.HIGH)
    GPIO.output(motorAForward,GPIO.LOW)
def moveMotorBForward():
    GPIO.output(motorBForward,GPIO.HIGH)
    GPIO.output(motorBBackward,GPIO.LOW)
def moveMotorBBackward():
    GPIO.output(motorBBackward,GPIO.HIGH)
    GPIO.output(motorBForward,GPIO.LOW)
    
    

def resetMotorPins():
    GPIO.output(motorAForward,GPIO.LOW)
    GPIO.output(motorABackward,GPIO.LOW)
    GPIO.output(motorBForward,GPIO.LOW)
    GPIO.output(motorBBackward,GPIO.LOW)

def measureDistance():
    #print("Starting Measurement")

    GPIO.output(TRIG,1)
    time.sleep(0.00001)
    GPIO.output(TRIG,0)


    while GPIO.input(ECHO) == 0:
        pass
    start = time.time()
    while GPIO.input(ECHO) == 1:
        pass
    stop = time.time()
    distance = int((stop-start) * 17000)
    #print(distance)
    _thread.start_new_thread(write_to_db, ("distance", 2*distance))
    
    
try:
    while True:
        for i in range(0,180,5):
            
            dutyCycle = (79/9)*i + 500
            pi.set_servo_pulsewidth(servoPin,dutyCycle)
            _thread.start_new_thread(write_to_db, ("angle", i))
            measureDistance()
            time.sleep(0.04)
        for i in range(180,0,-5):
            
            dutyCycle = (79/9)*i + 500
            pi.set_servo_pulsewidth(servoPin,dutyCycle)
            _thread.start_new_thread(write_to_db, ("angle", i))
            measureDistance()
            time.sleep(0.04)
            
       # GPIO.output(motorAOne,GPIO.LOW)
       # GPIO.output(motorATwo,GPIO.HIGH)

        
       # GPIO.output(motorBOne,GPIO.HIGH)
       # GPIO.output(motorBTwo,GPIO.LOW)
       # time.sleep(2000)
        
except KeyboardInterrupt:
    pwm.stop()
    GPIO.cleanup()
    pi.set_servo_pulsewidth(servoPin,0)
pi.stop()
