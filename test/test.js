x = evalXPath("<html><head lang=\"1\"/><head lang=\"2\"/></html>", "/html//head[2]@lang")
print(x)