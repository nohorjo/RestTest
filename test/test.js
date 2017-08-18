global("string", "s")
print(global("string"))

global("number", 1)
print(global("number"))

global("boolean", true)
print(global("boolean"))

global("list", [ 9, 8, 7 ])
print(global("list")[0])

global("object", {
	a : {
		b : "c"
	}
})
print(global("object").a.b)
