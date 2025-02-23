# Brightside - A Strategic Board Game Simulation

Brightside is a software simulation of a strategic board game where players use hand cards to navigate their game pieces across an obstacle-filled board while attempting to hinder their opponents. The objective is to be the first to reach the finish line. The game is set in a maritime theme: game pieces are represented as ships, the board as a body of water, and obstacles as rocks and mines.

## Features
- Turn-based strategy gameplay
- Dynamic obstacle interactions
- Hand card mechanics for movement and strategic actions
- Graphical User Interface (GUI) using Java Swing
- Saving and loading game files
- Supports multiple design patterns for didactic purposes

## Educational Focus
Brightside is designed with a strong educational focus, aiming to introduce and demonstrate various software design patterns in a playful manner. The following design patterns are implemented:
- **Factory Pattern** - Managing object creation
- **Command Pattern** - Encapsulating actions as objects
- **Strategy Pattern** - Allowing interchangeable game behaviour strategies
- **Dependency Injection** - Enhancing modularity and testability
- **Memento Pattern** - Enabling undo functionality
- **Builder Pattern** - Simplifying complex object creation
- **Decorator Pattern** - Dynamically extending functionalities

## Technologies Used
- **Java** – Core language for game logic and mechanics
- **Java Swing** – GUI implementation for an interactive experience
- **Gradle** – Build automation and dependency management

## Installation & Setup
### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Gradle (installed or used via the wrapper)

### Clone the Repository
```bash
git clone https://github.com/SantanaQ/Brightside.git
cd Brightside
```

### Build and Run
```bash
./gradlew build
./gradlew run
```

## Future Enhancements
- Multiplayer support
- AI-controlled opponents
- Additional card types and game mechanics
- Improved animations and UI enhancements
