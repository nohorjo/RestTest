var filename = "testFile.txt";
var data = "some data\n";
var contents = null;

assert(!fileExists(filename));

writeToFile(filename, data);

assert(fileExists(filename));

contents = readFromFile(filename);
assert(contents == data);

writeToFile(filename, data);
contents = readFromFile(filename);
assert(contents == data);

writeToFile(filename, data, true);
contents = readFromFile(filename);
assert(contents == (data + data));

deleteFile(filename);
assert(!fileExists(filename));
