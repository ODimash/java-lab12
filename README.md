# Multi-threaded Call Center Application

**Author:** Dinmukhamed Osmanov\
**Group:** VTIPO-35\
**Institution:** Eurasian National University (ENU)

----

![image](https://github.com/user-attachments/assets/6acf2142-adb0-4f41-a786-832004cdd5e1)

----

## Project Overview

The objective of this project was to develop a multi-threaded call center application using the `java.util.concurrent` package without using the `synchronized` keyword. The application simulates a call center with multiple operators handling clients. Each operator can serve only one client at a time, and clients may hang up and call again later if all operators are busy.

## Project Structure

The project follows a Maven structure with the following files and directories:

```
call-center
├── pom.xml
└── src
    └── main
        └── java
            └── com
                └── example
                    └── callcenter
                        ├── CallCenter.java
                        ├── Client.java
                        ├── Operator.java
                        └── MainApp.java
```

### pom.xml

The `pom.xml` file defines the project dependencies and build configuration:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>call-center</artifactId>
    <version>1.0-SNAPSHOT</version>
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>
</project>
```

### CallCenter.java

This class manages a pool of operators using a `BlockingQueue`. It allows clients to acquire and release operators:

```java
package com.example.callcenter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CallCenter {
    private final BlockingQueue<Operator> operators;

    public CallCenter(int numberOfOperators) {
        operators = new LinkedBlockingQueue<>(numberOfOperators);
        for (int i = 0; i < numberOfOperators; i++) {
            operators.add(new Operator("Operator-" + (i + 1)));
        }
    }

    public Operator getAvailableOperator() throws InterruptedException {
        return operators.take();
    }

    public void releaseOperator(Operator operator) {
        try {
            operators.put(operator);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

### Client.java

This class represents a client calling the call center. Each client tries to get an available operator, gets served, and then may call again after a random delay:

```java
package com.example.callcenter;

import java.util.concurrent.ThreadLocalRandom;

public class Client implements Runnable {
    private final CallCenter callCenter;
    private final String name;

    public Client(CallCenter callCenter, String name) {
        this.callCenter = callCenter;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println(name + " is calling...");
                Operator operator = callCenter.getAvailableOperator();
                System.out.println(name + " is being served by " + operator.getName());
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
                System.out.println(name + " has finished the call.");
                callCenter.releaseOperator(operator);
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));  // Client waits before calling again
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

### Operator.java

This class represents an operator in the call center:

```java
package com.example.callcenter;

public class Operator {
    private final String name;

    public Operator(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

### MainApp.java

This is the entry point of the application. It initializes the call center and starts multiple client threads using an `ExecutorService`:

```java
package com.example.callcenter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainApp {
    public static void main(String[] args) {
        CallCenter callCenter = new CallCenter(3);  // 3 operators

        ExecutorService executorService = Executors.newFixedThreadPool(10);  // 10 clients
        for (int i = 0; i < 10; i++) {
            Client client = new Client(callCenter, "Client-" + (i + 1));
            executorService.execute(client);
        }

        executorService.shutdown();
    }
}
```

## Running the Application

To run the application, use the following Maven commands:

### Compile the Project

```sh
mvn clean compile
```

### Run the Application

```sh
mvn exec:java -Dexec.mainClass=com.example.callcenter.MainApp
```

Alternatively, you can import the project into an IDE like IntelliJ IDEA or Eclipse:

1. **Import Project**: Open the IDE and import the project by selecting the `pom.xml` file.
2. **Build the Project**: Let the IDE build the project. Maven dependencies should be automatically resolved.
3. **Run MainApp**: Locate the `MainApp` class in the project explorer, right-click, and select "Run MainApp".

## Conclusion

This project successfully demonstrates a multi-threaded call center application using the `java.util.concurrent` package. It efficiently manages multiple client calls with a pool of operators, ensuring that only one client is served by an operator at a time. The implementation avoids using the `synchronized` keyword, instead relying on concurrency utilities provided by Java.
