README FILE
A JavaFX application built as part of the JavaFX ITEC627 Lab.  
It demonstrates layouts, graphics with property binding, image/font usage, and a reusable clock widget.

 Features
 Layout Playground — HBox, VBox, FlowPane demos, theme switching (light/dark), wrap toggle.
 Graphics & Binding — Circle with rotation, radius slider, color picker, pulse animation, uni and bi-directional bindings.
 Clock Widget — Reusable analog clock with start/stop, style switching, and resizing.

 Requirements
 Java: 17 or higher
 JavaFX: 21+ (via Maven dependencies)
 Maven: 3.6 or higher

 Project Structure

src/main/java/org/example/
  Main.java
  LayoutPlayground.java
  GraphicsBinding.java
  ClockTab.java
  ClockPane.java
src/main/resources/
  images/javaimage.png
  fonts/Quicksand-SemiBold.ttf
  light.css
  dark.css


 Build & Run
1. Clone or extract the project folder.
2. Open it in IntelliJ IDEA (or any Java IDE with Maven support).
3. Make sure Maven downloads JavaFX dependencies:
    Open the Maven tool window.
    Click "Reload All Maven Projects".
4. Run:
    From IntelliJ: Rightclick `Main.java` → `Run`.
    Or via terminal:
     bash
     mvn clean javafx:run
     
# Notes
 Images and fonts are loaded from the `resources` folder.
 External CSS themes (`light.css`, `dark.css`) change the appearance.
 The clock is designed as a reusable component for other JavaFX apps.
