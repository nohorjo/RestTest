var x = global("globalProperty");
var y = global();

assert(x=="unchanged", x);
assert(x==y.globalProperty, x);

global("globalProperty","changed");