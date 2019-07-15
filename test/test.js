function fun1(arr) {
	//within function
	var a, b, c, d;

	a = arr[0];
	b = arr[1];
	c = arr[2];
	d = arr[3];

	//expected result
	//[a, b, c, d] = arr;
}

function fun2(arr) {
	//unordered assignments (with gap)
	var a, b, c, d;

	b = arr[1];
	a = arr[0];
	d = arr[3];

	//expected result
	//[a, b, , d] = arr;
}

function fun3(arr) {
	//compressed declaration and declaration with initializer
	var b = arr[1], c = arr[2], a = arr[0];
	var d = arr[3];

	//expected result
	//var [a, b, c, d] = arr;
}

function fun4(arr) {
	//declarations of different types
	var b = arr[1];
	var c = arr[2];
	const a = arr[0];
	const d = arr[3];

	/* expected result
	var [, b, c] = arr;
	const [a, , , d] = arr;
	 */
}

function fun5(arr) {
	//taking appart single declaration statements with multiple variables
	var x = 3, a = arr[0];
	var b = arr[1];
	var c = arr[2];
	var d = arr[3], y = 5;

	/* expected result
	var x = 3, [a, b, c, d] = arr;
	var y = 5;
	 */
}

function fun6(arr) {
	//setting to more complex variable
	var obj = {a: 0, b: 1}, a, b;

	obj.a = arr[0];
	obj.b = arr[1];
	a = arr[2];
	b = arr[3];

	//expected result
	//[obj.a, obj.b, a, b] = arr;
}

function fun7(arr) {
	//assigning to value, which could produce side effects
	getArray().a = arr[0];
	getArray().b = arr[1];
	getArray().c = arr[2];

	//expected result
	//no change!
}

function fun8(arr) {
	//interrupted list
	var a = arr[0];
	var b = arr[1]
	funA(arr);
	var c = arr[2];
	var d = arr[3];

	/* expected result
	var [a, b] = arr;
	funA(arr);
	var [, , c, d] = arr;
	 */
}

function funA(obj) {
	//object assignment
	a = obj.a;
	b = obj.b;

	//expected result
	//({a, b} = obj);
}

function funB(obj) {
	//object assignment with declaration
	var a = obj.a;
	var b = obj.b;

	//expected result
	//var {a, b} = obj;
}

function funC(obj) {
	//variable mismatch
	var x = obj.a;
	var y = obj.b;

	//expected result
	//no change
}

function fun() {
	var arr = [1, 2, 3, 4, [5.1, 5.2], [[6.11, 6.12], 6.2], 7, 8];
	var test = ["a", "b", "c", "d"];
	var vector = {x: 10, y: 20};
	var point = {xPos: 5, yPos: 10};

	var unused, l = arr[9];
	var a = arr[0], j = arr[6];
	var b = arr[1];
	var c = arr[2], d = arr[3];
	var [e, f] = arr[4];
	var [[g, h], i] = arr[5];
	var k = arr[7], obj = {a: 0, b: 0, c: 0, d: 0, x:0, y: 0};
	const x = vector.x;
	const y = vector.y;
	obj.a = arr[0];
	obj.b = arr[2];
	obj.c = test[3];
	obj.d = test[4];
	var xPos = point.xPos;
	var yPos = point.yPos;
	xPos = point.xPos;
	yPos = point.yPos;

	/* expected output
	var arr = [1, 2, 3, 4, [5.1, 5.2], [[6.11, 6.12], 6.2], 7, 8];
	var test = ["a", "b", "c", "d"];
	var vector = {x: 10, y: 20};
	var point = {xPos: 5, yPos: 10};
	var unused, [a, b, c, d, [e, f], [[g, h], i], j, k, , l] = arr;
	var obj = {a: 0, b: 0, c: 0, d: 0, x: 0, y: 0};
	const {x, y} = vector;
	[obj.a, , obj.b] = arr;
	[, , , obj.c, obj.d] = test;
	var {xPos, yPos} = point;
	({xPos, yPos} = point);
	 */
}

var arr = [1, 2, 3, 4, 5, 6];
var obj = {};

//top level assignments (starting at element 1)
var a, b, c, d;
a = arr[1];
b = arr[2];
c = arr[3];
d = arr[4];

//expected outcome:
//[, a, b, c, d] = arr;


fun1(arr);
fun2(arr);
fun3(arr);
fun4(arr);
fun5(arr);
fun6(arr);
fun7(arr);
fun8(arr);

funA(obj);
funB(obj);
funC(obj);

fun();