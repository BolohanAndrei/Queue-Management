Queues Management Application
<br>

📌 Problem Statement
In many real-world scenarios, such as supermarkets or call centers, improper queue management can lead to high client waiting times and inefficient resource usage. This project addresses this problem by designing and implementing a simulation that minimizes client waiting time in a queue-based system.

<br>

🎯 Main Objective
The main objective of this application is to analyze queuing-based systems by:

Simulating a series of N clients arriving for service, entering Q queues, waiting, being served, and leaving the queues.

Computing and displaying key performance metrics like average waiting time, average service time, and peak hour for the simulation.

<br>

🔍 Sub-Objectives
Analyze the problem and identify both functional and non-functional requirements.

Design a structured, multi-threaded simulation application.

Implement the application using Java, incorporating a graphical user interface.

Test the application with various data sets to ensure its reliability and performance.

<br>

🚀 Features
Multithreaded Simulation: The application uses multiple threads—one for the simulation manager and one for each queue—to process clients in parallel.

Dynamic Client Generation: Clients are randomly generated with a unique ID, arrival time, and service time based on user-defined parameters.

Intelligent Scheduling: A scheduler is responsible for distributing clients to the queue with the shortest waiting time, aiming to optimize the overall process.

Graphical User Interface (GUI): A user-friendly interface allows for simulation setup and displays the real-time evolution of the queues.

Comprehensive Logging: A log of events is generated, showing the status of waiting clients and queues throughout the simulation.

Results Display: The final simulation results, including average waiting time and peak hour, are displayed in the GUI and/or a log file.

<br>

🛠️ Technologies Used
Java: The core programming language for the application logic.

Java Swing: Used for creating the graphical user interface.

Multithreading: Utilizes Java's threading and synchronization mechanisms (BlockingQueue, AtomicInteger, synchronized blocks) to ensure thread safety and parallel processing.

Design Patterns: The project implements the Strategy pattern for selecting the client dispatch policy (e.g., shortest queue, shortest time).

<br>

📖 Getting Started
Clone the repository:

git clone https://github.com/your-username/your-repository-name.git

Compile and run the application: You will need a Java Development Kit (JDK) installed to build and run the project.

<br>

📌 Contribution
Contributions are welcome! Feel free to fork this repository and submit pull requests.
