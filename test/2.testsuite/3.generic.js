var time = currentTimestamp();
var sleepTime = 1000;

sleep(sleepTime);
assert(currentTimestamp() > time + sleepTime);