This is a Kotlin Multiplatform project targeting the Desktop (JVM) and Android.

### Summary
This is a quote management application, where you can CRUD quotes. It also supports associating multiple tags with quotes, CRUD for tags, and filtering quotes by tags.

I built it for me because I wanted a place to collect and look at all the memorable quotes and excerpts I came across. It was also a good chance to try out KMP for desktop

I used hexagonal architecture to help me understand the concept better, and it's used to easily swap between using a SqlLite and InMemory repository.
That alone makes it pretty useful, but I can see how if I had more complicated business logic than just CRUD, having a separate core would also be a big plus

The app is feature complete, I wanted to just finish everything I wanted in a personal quotes app for me first. I have too many projects I abandoned early because I got hung up on polishing and perfecting instead of finishing.

Update: A lot of cleanup and refactoring, both of code and of UI display is also finished

#### Showcase
Main screen
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/bfe57906-7ea5-4a4c-92ce-0a4df0a6da24" />
Note that each quote row has two buttons, one to copy the quote and one to delete it(asks for confirmation first)

Searching to find a quote (searches quote text and source)
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/77e3a841-56b7-43b7-9ab3-ce0482418a31" />


Filtering by tags

Select tags to filter by
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/60aeff10-f892-4313-a6f1-b5c6a1fe3054" />

Then click apply filter. Filtered quotes look like this
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/0fba8d5a-2219-4cbd-9a83-9b45ad98db45" />

All tag filters can be reset in the tag filter modal


Add/edit/delete tags
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/67ee50cd-b0f1-4068-8eb2-dd2b55500d0d" />

Add a new quote
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/9df349cb-ecc0-4584-9377-cea5b0714a2a" />

Edit an existing quote by clicking on the quote row
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/4b40ba09-fa14-4147-8a02-76b0cc97c889" />


### Next steps
Import/export from/to JSON or CSV.

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
