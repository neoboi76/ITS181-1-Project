# 🎵 Java Music Player Application

This project is a **desktop-based Music Player** built in **Java** that integrates a **graphical user interface (GUI)**, **database persistence**, and **audio playback**. It follows the **Model-View-Controller (MVC)** architecture, ensuring modularity, scalability, and maintainability. The application uses **Java Persistence API (JPA)** to manage songs stored in a **MySQL database**, and relies on **custom event handling** and **multithreading** for responsive user interaction.

---

## 🚀 Features

* 🎶 **Music Playback:** Play, pause, and stop audio tracks with smooth controls.
* 📂 **Song Library Management:** Add, display, and remove songs in a table-based interface.
* 🗄 **Database Integration:** Songs are persisted in a **MySQL database** using **JPA**.
* 🖥 **MVC Architecture:** Separation of logic (Controller), data (Model), and UI (View).
* ⚡ **Event Handling:** Implements custom listeners (`SongEvent`, `SongListener`) for UI responsiveness.
* 🧵 **Multithreading:** Background audio playback handled by `PlayerThread`.
* 🪟 **Swing-based GUI:** Clean and interactive interface with multiple panels (`ControlPanel`, `SongPanel`, `TablePanel`).

---

## 📂 Project Structure

```text
src/
 ├── Controller/
 │   ├── MusicController.java        # Handles user actions and coordinates between Model and View
 │   └── PlayerThread.java           # Runs music playback in a separate thread
 │
 ├── Model/
 │   ├── Database.java               # Handles database connection and persistence logic
 │   └── SongEntity.java             # JPA entity representing a song in the database
 │
 ├── View/
 │   ├── App.java                    # Main entry point for launching the application
 │   ├── MusicPlayer.java            # Core UI for music playback
 │   ├── ControlPanel.java           # Buttons for controlling playback
 │   ├── SongPanel.java              # Panel for song details
 │   ├── TablePanel.java             # Table for listing stored songs
 │   ├── SongEntityTableModel.java   # Table model for JPA entities
 │   ├── SongEvent.java              # Custom event fired on song actions
 │   ├── SongListener.java           # Interface for handling song events
 │   ├── SongFileFilter.java         # Restricts selectable files to audio formats
 │   └── Utils.java                  # Utility functions used across the app
 │
 └── META-INF/
     └── persistence.xml             # JPA persistence configuration
```

---

## 🛠️ Technologies Used

* **Java SE** – Core programming language.
* **Java Swing** – Graphical User Interface framework.
* **Java Persistence API (JPA)** – Object-relational mapping for database integration.
* **MySQL** – Relational database to store and manage songs.
* **Multithreading (Java Threads)** – Ensures smooth playback without freezing the UI.
* **MVC Design Pattern** – Provides separation of concerns and maintainability.

---

## ⚙️ Requirements

* Java 8 or later
* MySQL Database Server
* JPA Provider (configured via `META-INF/persistence.xml`)
* (Optional) IDE like IntelliJ IDEA or Eclipse for easier development

---

## ▶️ Setup & Run

1. **Clone the repository**

   ```bash
   git clone https://github.com/neoboi76/ITS181-1-Project.git
   cd ITS181-1-Project
   ```
2. **Configure the database**

   * Create a MySQL database.
   * Update connection settings in `META-INF/persistence.xml` (JDBC URL, username, password).
3. **Compile and Run**

   ```bash
   javac -d bin src/java/**/*.java
   java -cp bin View.App
   ```
4. **Use the Application**

   * Add songs through the UI.
   * Play, pause, and stop music.
   * Songs will be stored and managed in the MySQL database.

---

## 🧭 Notes & Tips

* Supported file types depend on Java’s built-in audio libraries (e.g., WAV, AU). Additional codecs may be needed for MP3.
* Ensure your MySQL server is running and accessible.
* For deployment, consider packaging as an executable JAR with dependencies.

---

## 👨‍💻 Authors

* Kenji Mark Alan Arceo
* **Ryonan Owen Ferrer (Lead front-end developer)**
* **Dino Alfred Timbol (Lead back-end developer)**
* Mike Emil Vocal

---

## 🎥 Demo

[Link to the Video Demonstration of the Application](https://drive.google.com/file/d/1LudnPLUX-jcsMl2ityxmUof7OnKPjlVp/view?usp=sharing)
