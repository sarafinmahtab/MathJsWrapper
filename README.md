# MathJs Wrapper
A wrapper of the mathjs.org (https://mathjs.org/) JavaScript library for Android to evaluate math expressions.

This project is based on *MathJs-Android*(https://github.com/niltonvasques/mathjs-android). 
But it is up-to-date with additional features such as `math.eval(expression, scope)` and 
`math.traverse()` which returns the list of specified node.


## Dependency
Choose your dependency to use this library.

#### Gradle

Include the library in your ``build.gradle``

```
dependencies {
    implementation 'com.sarafinmahtab:mathjswrapper:1.0'
}
```

#### maven

```
<dependency>
	<groupId>com.sarafinmahtab</groupId>
	<artifactId>mathjswrapper</artifactId>
	<version>1.0</version>
	<type>pom</type>
</dependency>
```

#### ivy

```
<dependency org="com.sarafinmahtab" name="mathjswrapper" rev="1.0">
	<artifact name="mathjswrapper" ext="pom"></artifact>
</dependency>
```



## Implementation

#### Initialization

Initialize MathJsWapper object with context.
```
var mathJsWrapper = MathJsWrapper(this)
```

#### Evaluate Expressions

1. Various supported expressions

```
val expression = "12 / (2.3 + 0.7)" // 2.718
val expression = "12.7 cm to inch" // 5 inch
val expression = "sin(45 deg) ^ 2" // 0.5
val expression = "9 / 3 + 2i" // 3 + 2i
val expression = "det([-1, 2; 3, 1])" // -7
val expression = "111111111111111111111111111111111111111111111111+2222222222222222222222222222222222222222222222222" // 2.3333333e+48


val output: String = mathJsWrapper.eval(expression)
```

2. Various supported expressions with variables

```
val expression = "a / (b + c)" // 2.718
val map = HashMap<String, Any>()
map["a"] = 12
map["b"] = 2.3
map["c"] = 0.7

val output: String = mathJsWrapper.eval(expression, map)
```

#### Traverse specified node of Expressions

You can get all the nodes from expressions such as 'SymbolNode', 'OperatorNode', 'ConstantNode', 'ParenthesisNode'
```
val expression = "a / (b + c)" // 2.718
val list = mathJsWrapper.getExpressionNodes(expression, NodeType.SymbolNode) // ["a","b","c"]
```

#### Remove MathJsWapper after being used

Deallocate instances after being used.
```
mathJsWrapper.onDestroy()
```
