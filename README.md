# A simple REST api testing utility

## Features
- Tests are written in JavaScript and is easily scriptable with typical logical operations: loops, conditionals, variables etc.
- Can read from and write to disk.
- Can use global variables, system properties and environment variables.
- Access to global functions.
- Supports multipart requests.

## Usage
```sh
$ java -jar RestTest.jar "_project-dir_"
$ # Returns 1 if any test had failed, otherwise 0. Results are written to the standard output.
```

Refer to the example project "test" for samples