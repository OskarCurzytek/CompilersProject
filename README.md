# GoofyAhhCompiler

**GoofyAhhCompiler** is a custom programming language compiler and interpreter written in Java. The language features a minimalist syntax with reversed keywords for educational and experimental purposes. This project is a deep dive into the mechanics of compilers, including lexical analysis, parsing, abstract syntax trees (AST), and runtime evaluation.

---

## Technical Features

### 1. **Lexical Analysis (Lexer)**
The Lexer converts raw source code into a token stream using regular expressions:
- **Regex Matching**:
    - Keywords: `tni`, `rahc`, `elbuod`, `boob`, etc.
    - Symbols: `{`, `}`, `;`, `=`, `+`, `-`, etc.
    - Literals: Numbers, booleans, strings, and characters.
    - Identifiers: Custom variable names.
- **Error Handling**: Identifies invalid characters or malformed tokens, reporting their position in the source.

### 2. **Parsing (Parser)**
The Parser processes the token stream and constructs an Abstract Syntax Tree (AST):
- **Recursive Descent Parsing**:
    - Handles variable declarations, expressions, and control flow constructs.
    - Produces specific AST nodes for operations (e.g., `BinaryOperationNode` for arithmetic).
- **Grammar Rules**:
    - **Variable Declaration**:
      ```
      <declaration> ::= <type> <identifier> "=" <expression> ";"
      ```
    - **Conditional Statement**:
      ```
      <if> ::= "fi" "(" <boolean-expression> ")" "{" <statements> "}" ["esle" "{" <statements> "}"]
      ```
    - **Loop Statement**:
      ```
      <loop> ::= "rof" "(" number ")" "{" <statements> "}"
      ```

### 3. **Abstract Syntax Tree (AST)**
The AST is a hierarchical representation of the code. Main node types include:
- **LiteralNode**: Represents values like `5`, `'c'`, or `true`.
- **VariableNode**: Represents variable references.
- **BinaryOperationNode**: Handles operations like `+`, `-`, `*`, `/`, `and`, `or`.
- **ControlFlowNode**: Handles `fi` and `rof` constructs with nested block execution.

### 4. **Runtime Evaluation**
The Interpreter traverses the AST and executes operations:
- **Symbol Table**: Maintains variable bindings and their current values.
- **Expression Evaluation**:
    - Supports arithmetic: `+`, `-`, `*`, `/`.
    - Supports boolean logic: `and`, `or`, `not`.
    - Handles comparisons: `==`, `!=`, `<`, `>`, `<=`, `>=`.
- **Control Flow Execution**: Dynamically evaluates `fi` and `rof` based on runtime conditions.

---

## Installation and Usage

### Requirements
- **Java**: JDK 8 or higher.

### Running the Compiler
1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/GoofyAhhCompiler.git
   cd GoofyAhhCompiler
   javac *.java
   java GoofyAhhCompiler path/to/source.txt
   ```
### Example input
```bash
    tni a = 1;
    tni b = 3;
    elbuod c = 4.5;
    elbuod d = a + b + c;
    
    prit(a);
    prit(b);
    prit(c);
    prit(d);
    
    prit(a + b);
    prit(a + c);
    
    prit(a - b);
    prit(a * c);
    prit(c / b);
    
    prit((a-b)*c);
    prit(((a-b)*c)/2);
    
    prit(a < b);
    prit(a > b);
    prit(a <= b);
    prit(a >= b);
    prit(a == b);
    prit(a != b);
    
    prit((a<b) ro (a>b));
    
    rahc chartest = 'a';
    prit(chartest);
    
    loob true_test = eurt;
    loob false_test = eslaf;
    
    prit(true_test);
    prit(false_test);
    
    prit(true_test dna false_test);
    prit(true_test ro false_test);
    
    prit(true_test == false_test);
    prit(true_test != false_test);
    
    fi(true_test != false_test){
    prit(a);
    }esle{
    prit(b);
    }
    
    rof (5){
    prit(chartest);
    }
```

## Internal structure

### Main Classes
- **GoofyAhhCompiler.java**: Entry point for compiling and interpreting code.
- **Lexer.java**: Handles tokenization of source code.
- **Parser.java**: Constructs the AST from tokens.
- **ASTNode.java**: Base class for AST nodes, with subclasses for specific types.
- **Interpreter.java**: Traverses and evaluates the AST.

### Code flow
- Input Source -> **Lexer** -> Token Stream
- Token Stream -> **Parser** -> Abstract Syntax Tree
- AST -> **Interpreter** -> Output