var caught = false;

try {
	assert(false);
} catch (e) {
	caught = true;
}

assert(caught, "Not caught");