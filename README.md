This is a Kotlin Multiplatform project targeting only the Desktop (JVM).

### Summary
This is a quote management application, where you can CRUD quotes. It also supports associating multiple tags with quotes, CRUD for tags, and filtering quotes by tags.

I built it for me because I wanted a place to collect and look at all the memorable quotes and excerpts I came across. It was also a good chance to try out KMP for desktop

I used hexagonal architecture to help me understand the concept better, and it's used to easily swap between using a SqlLite and InMemory repository.
That alone makes it pretty useful, but I can see how if I had more complicated business logic than just CRUD, having a separate core would also be a big plus

The app is feature complete, I wanted to just finish everything I wanted in a personal quotes app for me first. I have too many projects I abandoned early because I got hung up on polishing and perfecting instead of finishing.

Update: A lot of cleanup and refactoring, both of code, and of UI display is also finished

### Next steps
Add tests. I haven't tried out tests in Jetpack Compose before, so this is where I learn. I'm sure refactoring would've been 10x easier with those in place

---
### Build and Run Desktop (JVM) Application

To build and run the development version of the desktop app, use the run configuration from the run widget
in your IDE’s toolbar or run it directly from the terminal:

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:run
  ```

---